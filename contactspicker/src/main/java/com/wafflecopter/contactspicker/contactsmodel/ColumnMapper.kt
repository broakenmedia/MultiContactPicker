package com.wafflecopter.contactspicker.contactsmodel

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract

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
 */internal object ColumnMapper {
    @JvmStatic
    fun mapInVisibleGroup(cursor: Cursor, contact: Contact, columnIndex: Int) {
        contact.inVisibleGroup = cursor.getInt(columnIndex)
    }

    @JvmStatic
    fun mapDisplayName(cursor: Cursor, contact: Contact, columnIndex: Int) {
        val displayName = cursor.getString(columnIndex)
        if (displayName != null && !displayName.isEmpty()) {
            contact.displayName = displayName
        }
    }

    @JvmStatic
    fun mapEmail(cursor: Cursor, contact: Contact, columnIndex: Int) {
        val email = cursor.getString(columnIndex)
        if (email != null && !email.trim { it <= ' ' }.isEmpty()) {
            contact.emails.add(email)
        }
    }

    @JvmStatic
    fun mapPhoneNumber(
        con: Context,
        cursor: Cursor,
        contact: Contact,
        noColumnIndex: Int,
        typeColIndex: Int,
        labelColIndex: Int
    ) {
        var phoneNumber = cursor.getString(noColumnIndex)
        val phonetype = cursor.getInt(typeColIndex)
        val customLabel = cursor.getString(labelColIndex)
        val phoneLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(
            con.resources,
            phonetype,
            customLabel
        ) as String
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Remove all whitespaces
            phoneNumber = phoneNumber.replace("\\s+".toRegex(), "")
            contact.phoneNumbers.add(PhoneNumber(phoneLabel, phoneNumber.trim { it <= ' ' }))
        }
    }

    @JvmStatic
    fun mapPhoto(cursor: Cursor, contact: Contact, columnIndex: Int) {
        val uri = cursor.getString(columnIndex)
        if (uri != null && !uri.isEmpty()) {
            contact.photo = Uri.parse(uri)
        }
    }

    @JvmStatic
    fun mapStarred(cursor: Cursor, contact: Contact, columnIndex: Int) {
        contact.isStarred = cursor.getInt(columnIndex) != 0
    }

    @JvmStatic
    fun mapThumbnail(cursor: Cursor, contact: Contact, columnIndex: Int) {
        val uri = cursor.getString(columnIndex)
        if (uri != null && !uri.isEmpty()) {
            contact.thumbnail = Uri.parse(uri)
        }
    }
}