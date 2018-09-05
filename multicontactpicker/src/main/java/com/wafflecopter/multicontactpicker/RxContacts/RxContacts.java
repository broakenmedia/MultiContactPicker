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
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.LongSparseArray;

import com.wafflecopter.multicontactpicker.LimitColumn;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


public class RxContacts {

    private static final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

    private static final Uri PHONE_CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private static final Uri EMAIL_CONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;

    private static final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.IN_VISIBLE_GROUP,
            DISPLAY_NAME,
            ContactsContract.Contacts.STARRED,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };

    private static final String[] EMAIL_PROJECTION = {
            ContactsContract.CommonDataKinds.Email.CONTACT_ID,
            ContactsContract.CommonDataKinds.Email.DATA
    };

    private static final String[] NUMBER_PROJECTION = {
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };




    private ContentResolver mResolver;

    public static Observable<Contact> fetch (@NonNull final LimitColumn columnLimitChoice, @NonNull final Context context) {
        return Observable.create(new ObservableOnSubscribe<Contact>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Contact> e) throws Exception {
                new RxContacts(context).fetch(columnLimitChoice, e);
            }
        });
    }

    private RxContacts(@NonNull Context context) {
        mResolver = context.getContentResolver();
    }

    private void fetch (LimitColumn columnLimitChoice, ObservableEmitter emitter) {
        LongSparseArray<Contact> contacts = new LongSparseArray<>();
        Cursor cursor = createCursor(getFilter(columnLimitChoice));
        cursor.moveToFirst();
        int idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int inVisibleGroupColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.IN_VISIBLE_GROUP);
        int displayNamePrimaryColumnIndex = cursor.getColumnIndex(DISPLAY_NAME);
        int starredColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.STARRED);
        int photoColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);
        int thumbnailColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI);
        int hasPhoneNumberColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);


        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(idColumnIndex);
            Contact contact = contacts.get(id, null);
            if (contact == null) {
                contact = new Contact(id);
            }
            ColumnMapper.mapInVisibleGroup(cursor, contact, inVisibleGroupColumnIndex);
            ColumnMapper.mapDisplayName(cursor, contact, displayNamePrimaryColumnIndex);
            ColumnMapper.mapStarred(cursor, contact, starredColumnIndex);
            ColumnMapper.mapPhoto(cursor, contact, photoColumnIndex);
            ColumnMapper.mapThumbnail(cursor, contact, thumbnailColumnIndex);

            switch (columnLimitChoice){
                case EMAIL:
                    getEmail(id, contact);
                    break;
                case PHONE:
                    getPhoneNumber(id, cursor, contact, hasPhoneNumberColumnIndex);
                    break;
                case NONE:
                    getEmail(id, contact);
                    getPhoneNumber(id, cursor, contact, hasPhoneNumberColumnIndex);
                    break;
            }

            if(columnLimitChoice == LimitColumn.EMAIL){
                if(contact.getEmails().size() > 0){
                    contacts.put(id, contact);
                    //noinspection unchecked
                    emitter.onNext(contact);
                }
            }else{
                contacts.put(id, contact);
                //noinspection unchecked
                emitter.onNext(contact);
            }
            cursor.moveToNext();
        }
        cursor.close();
        emitter.onComplete();
    }

    private void getEmail(long id, Contact contact){
        Cursor emailCursor = mResolver.query(EMAIL_CONTENT_URI, EMAIL_PROJECTION,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{String.valueOf(id)}, null);

        if(emailCursor != null) {
            int emailDataColumnIndex = emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            if(emailCursor.moveToFirst()){
                ColumnMapper.mapEmail(emailCursor, contact, emailDataColumnIndex);
            }
            emailCursor.close();
        }
    }

    private void getPhoneNumber(long id, Cursor cursor, Contact contact, int hasPhoneNumberColumnIndex){
        int hasPhoneNumber = Integer.parseInt(cursor.getString(hasPhoneNumberColumnIndex));
        if (hasPhoneNumber > 0) {
            Cursor phoneCursor = mResolver.query(PHONE_CONTENT_URI, NUMBER_PROJECTION,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{String.valueOf(id)}, null);
            if (phoneCursor != null) {
                phoneCursor.moveToFirst();
                int phoneNumberColumnIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                while (!phoneCursor.isAfterLast()) {
                    ColumnMapper.mapPhoneNumber(phoneCursor, contact, phoneNumberColumnIndex);
                    phoneCursor.moveToNext();
                }
                phoneCursor.close();
            }
        }
    }

    private String getFilter(LimitColumn limitColumn){
        switch (limitColumn){
            case PHONE:
                return ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0";
        }
        return null;
    }

    private Cursor createCursor (String filter) {
        return mResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                filter,
                null,
                ContactsContract.Contacts._ID
        );
    }

}