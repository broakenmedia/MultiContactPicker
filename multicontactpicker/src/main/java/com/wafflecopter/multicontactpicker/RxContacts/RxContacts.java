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
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.LongSparseArray;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


public class RxContacts {
    private static final String[] PROJECTION = {
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Data.DISPLAY_NAME_PRIMARY,
            ContactsContract.Data.STARRED,
            ContactsContract.Data.PHOTO_URI,
            ContactsContract.Data.PHOTO_THUMBNAIL_URI,
            ContactsContract.Data.DATA1,
            ContactsContract.Data.MIMETYPE,
            ContactsContract.Data.IN_VISIBLE_GROUP
    };

    private ContentResolver mResolver;

    public static Observable<Contact> fetch (@NonNull final Context context) {
        return Observable.create(new ObservableOnSubscribe<Contact>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Contact> e) throws Exception {
                new RxContacts(context).fetch(e);
            }
        });
    }

    private RxContacts(@NonNull Context context) {
        mResolver = context.getContentResolver();
    }


    private void fetch (ObservableEmitter emitter) {
        LongSparseArray<Contact> contacts = new LongSparseArray<>();
        Cursor cursor = createCursor();
        cursor.moveToFirst();
        int idColumnIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID);
        int inVisibleGroupColumnIndex = cursor.getColumnIndex(ContactsContract.Data.IN_VISIBLE_GROUP);
        int displayNamePrimaryColumnIndex = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME_PRIMARY);
        int starredColumnIndex = cursor.getColumnIndex(ContactsContract.Data.STARRED);
        int photoColumnIndex = cursor.getColumnIndex(ContactsContract.Data.PHOTO_URI);
        int thumbnailColumnIndex = cursor.getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI);
        int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int mimetypeColumnIndex = cursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
        int dataColumnIndex = cursor.getColumnIndex(ContactsContract.Data.DATA1);
        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(idColumnIndex);
            Contact contact = contacts.get(id, null);
            if (contact == null) {
                contact = new Contact(id);
                ColumnMapper.mapInVisibleGroup(cursor, contact, inVisibleGroupColumnIndex);
                ColumnMapper.mapDisplayName(cursor, contact, displayNamePrimaryColumnIndex);
                ColumnMapper.mapStarred(cursor, contact, starredColumnIndex);
                ColumnMapper.mapPhoto(cursor, contact, photoColumnIndex);
                ColumnMapper.mapThumbnail(cursor, contact, thumbnailColumnIndex);
                ColumnMapper.mapPhoneNumber(cursor, contact, phoneNumberIndex);
                contacts.put(id, contact);
            } else {
                String mimetype = cursor.getString(mimetypeColumnIndex);
                switch (mimetype) {
                    case ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE: {
                        ColumnMapper.mapEmail(cursor, contact, dataColumnIndex);
                        break;
                    }
                }
            }
            cursor.moveToNext();
        }
        cursor.close();
        for (int i = 0; i < contacts.size(); i++)
            emitter.onNext(contacts.valueAt(i));
        emitter.onComplete();
    }

    private Cursor createCursor () {
        return mResolver.query(
                ContactsContract.Data.CONTENT_URI,
                PROJECTION,
                null,
                null,
                ContactsContract.Data.CONTACT_ID
        );
    }
}
