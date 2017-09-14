package com.wafflecopter.multicontactpicker.RxContacts;

/*
 * Copyright (C) 2016 Ulrich Raab.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.support.annotation.NonNull;
import android.util.LongSparseArray;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


public class RxContacts {

    private static final String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY;

    private static final Uri PHONE_CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private static final Uri EMAIL_CONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;

    private static final String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

    private static final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.IN_VISIBLE_GROUP,
            DISPLAY_NAME,
            ContactsContract.Contacts.STARRED,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            HAS_PHONE_NUMBER
    };

    private static final String[] EMAIL_PROJECTION = new String[] {
        Email.TYPE,
        Email.LABEL,
        Email.DATA
    };

    private static final String[] PHONE_PROJECTION = new String[] {
        Phone.TYPE,
        Phone.LABEL,
        Phone.NUMBER
    };

    private static final String[] ADDRESS_PROJECTION = new String[] {
        StructuredPostal.TYPE,
            StructuredPostal.LABEL,
            StructuredPostal.FORMATTED_ADDRESS,
            StructuredPostal.STREET,
            StructuredPostal.POBOX,
            StructuredPostal.NEIGHBORHOOD,
            StructuredPostal.CITY,
            StructuredPostal.REGION,
            StructuredPostal.POSTCODE,
            StructuredPostal.COUNTRY
    };

    private ContentResolver mResolver;

    public static Observable<ContactResult> fetch (@NonNull final Context context) {
        return Observable.create(new ObservableOnSubscribe<ContactResult>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ContactResult> e) throws Exception {
                new RxContacts(context).fetch(e);
            }
        });
    }

    private RxContacts(@NonNull Context context) {
        mResolver = context.getContentResolver();
    }

    private void fetch (ObservableEmitter emitter) {
        LongSparseArray<ContactResult> contacts = new LongSparseArray<>();
        Cursor cursor = createCursor();
        cursor.moveToFirst();
        int idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int inVisibleGroupColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.IN_VISIBLE_GROUP);
        int displayNamePrimaryColumnIndex = cursor.getColumnIndex(DISPLAY_NAME);
        int starredColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.STARRED);
        int photoColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);
        int thumbnailColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);

        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(idColumnIndex);
            ContactResult contact = contacts.get(id, null);
            if (contact == null) {
                contact = new ContactResult(id);
                ColumnMapper.mapInVisibleGroup(cursor, contact, inVisibleGroupColumnIndex);
                ColumnMapper.mapDisplayName(cursor, contact, displayNamePrimaryColumnIndex);
                ColumnMapper.mapStarred(cursor, contact, starredColumnIndex);
                ColumnMapper.mapPhoto(cursor, contact, photoColumnIndex);
                ColumnMapper.mapThumbnail(cursor, contact, thumbnailColumnIndex);

                Cursor emailCursor = mResolver.query(EMAIL_CONTENT_URI, EMAIL_PROJECTION,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{String.valueOf(id)}, null);
                if(emailCursor != null) {
                    emailCursor.moveToFirst();
                    int emailTypeIndex = emailCursor.getColumnIndex(Email.TYPE);
                    int emailLabelIndex = emailCursor.getColumnIndex(Email.LABEL);
                    int emailDataColumnIndex = emailCursor.getColumnIndex(Email.DATA);
                    while (!emailCursor.isAfterLast()) {
                        ColumnMapper.mapPhoneNumber(
                            cursor,
                            contact,
                            emailTypeIndex,
                            emailLabelIndex,
                            emailDataColumnIndex
                        );
                    }
                    emailCursor.close();
                }

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = mResolver.query(PHONE_CONTENT_URI, PHONE_PROJECTION,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{String.valueOf(id)}, null);
                    if(phoneCursor != null) {
                        phoneCursor.moveToFirst();
                        int phoneNumberColumnIndex = phoneCursor.getColumnIndex(Phone.NUMBER);
                        int phoneNumberTypeIndex = phoneCursor.getColumnIndex(Phone.TYPE);
                        int phoneNumberLabelIndex = phoneCursor.getColumnIndex(Phone.LABEL);

                        while (!phoneCursor.isAfterLast()) {
                            ColumnMapper.mapPhoneNumber(
                                cursor,
                                contact,
                                phoneNumberTypeIndex,
                                phoneNumberLabelIndex,
                                phoneNumberColumnIndex
                            );
                        }
                        phoneCursor.close();
                    }
                }

                Cursor addressCursor = mResolver.query(ContactsContract.Data.CONTENT_URI,
                    ADDRESS_PROJECTION,
                    ContactsContract.Data.CONTACT_ID + "=? AND " + StructuredPostal.MIMETYPE + "= ?",
                    new String[] {String.valueOf(id), StructuredPostal.CONTENT_ITEM_TYPE},
                    null
                );

                if (addressCursor != null) {
                    int typeIndex = addressCursor.getColumnIndex(StructuredPostal.TYPE);
                    int labelIndex = addressCursor.getColumnIndex(StructuredPostal.LABEL);
                    int formattedAddressIndex = addressCursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS);
                    int streetIndex = addressCursor.getColumnIndex(StructuredPostal.STREET);
                    int poboxIndex = addressCursor.getColumnIndex(StructuredPostal.POBOX);
                    int neighborhoodIndex = addressCursor.getColumnIndex(StructuredPostal.NEIGHBORHOOD);
                    int cityIndex = addressCursor.getColumnIndex(StructuredPostal.CITY);
                    int regionIndex = addressCursor.getColumnIndex(StructuredPostal.REGION);
                    int postCodeIndex = addressCursor.getColumnIndex(StructuredPostal.POSTCODE);
                    int countryIndex = addressCursor.getColumnIndex(StructuredPostal.COUNTRY);

                    while (addressCursor.moveToNext()) {
                        contact.getAddresses().add(new PostalAddress(
                            addressCursor.getInt(typeIndex),
                            addressCursor.getString(labelIndex),
                            addressCursor.getString(formattedAddressIndex),
                            addressCursor.getString(streetIndex),
                            addressCursor.getString(poboxIndex),
                            addressCursor.getString(neighborhoodIndex),
                            addressCursor.getString(cityIndex),
                            addressCursor.getString(regionIndex),
                            addressCursor.getString(postCodeIndex),
                            addressCursor.getString(countryIndex)
                        ));
                    }
                    addressCursor.close();
                }

                contacts.put(id, contact);
            }
            cursor.moveToNext();
        }
        cursor.close();
        for (int i = 0; i < contacts.size(); i++) {
            //noinspection unchecked
            emitter.onNext(contacts.valueAt(i));
        }
        emitter.onComplete();
    }

    private Cursor createCursor () {
        return mResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                null,
                null,
                ContactsContract.Contacts._ID
        );
    }

}