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

import android.database.Cursor;
import android.net.Uri;

class ColumnMapper {

    // Utility class -> No instances allowed
    private ColumnMapper () {}

    static void mapInVisibleGroup (Cursor cursor, ContactResult contact, int columnIndex) {
        contact.setInVisibleGroup(cursor.getInt(columnIndex));
    }

    static void mapDisplayName (Cursor cursor, ContactResult contact, int columnIndex) {
        String displayName = cursor.getString(columnIndex);
        if (displayName != null && !displayName.isEmpty()) {
            contact.setDisplayName(displayName);
        }
    }

    static void mapEmail (Cursor cursor, ContactResult contact, int typeIndex, int labelIndex, int columnIndex) {
        int type = cursor.getInt(typeIndex);
        String label = cursor.getString(labelIndex);
        String email = cursor.getString(columnIndex);
        if (email != null && !email.trim().isEmpty()) {
            contact.getEmails().add(new EmailString(type,label,email));
        }
    }

    static void mapPhoneNumber (Cursor cursor, ContactResult contact, int typeIndex, int labelIndex, int columnIndex) {
        int type = cursor.getInt(typeIndex);
        String label = cursor.getString(labelIndex);
        String phoneNumber = cursor.getString(columnIndex);
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            phoneNumber = phoneNumber.replaceAll("\\s+","");
            contact.getPhoneNumbers().add(new PhoneNumberString(type,label, phoneNumber));
        }
    }

    static void mapPhoto (Cursor cursor, ContactResult contact, int columnIndex) {
        String uri = cursor.getString(columnIndex);
        if (uri != null && !uri.isEmpty()) {
            contact.setPhoto(Uri.parse(uri));
        }
    }

    static void mapStarred (Cursor cursor, ContactResult contact, int columnIndex) {
        contact.setStarred(cursor.getInt(columnIndex) != 0);
    }

    static void mapThumbnail (Cursor cursor, ContactResult contact, int columnIndex) {
        String uri = cursor.getString(columnIndex);
        if (uri != null && !uri.isEmpty()) {
            contact.setThumbnail( Uri.parse(uri));
        }
    }
}