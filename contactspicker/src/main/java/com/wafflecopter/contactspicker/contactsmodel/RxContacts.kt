package com.wafflecopter.contactspicker.contactsmodel

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.LongSparseArray
import com.wafflecopter.contactspicker.LimitColumn
import com.wafflecopter.contactspicker.contactsmodel.ColumnMapper.mapDisplayName
import com.wafflecopter.contactspicker.contactsmodel.ColumnMapper.mapEmail
import com.wafflecopter.contactspicker.contactsmodel.ColumnMapper.mapInVisibleGroup
import com.wafflecopter.contactspicker.contactsmodel.ColumnMapper.mapPhoneNumber
import com.wafflecopter.contactspicker.contactsmodel.ColumnMapper.mapPhoto
import com.wafflecopter.contactspicker.contactsmodel.ColumnMapper.mapStarred
import com.wafflecopter.contactspicker.contactsmodel.ColumnMapper.mapThumbnail
import kotlinx.coroutines.flow.flow

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
class RxContacts constructor(private val mContext: Context) {

    private val mResolver: ContentResolver = mContext.contentResolver

    fun fetch(columnLimitChoice: LimitColumn) = flow{
        val contacts = LongSparseArray<Contact?>()
        val cursor = createCursor(getFilter(columnLimitChoice))
        cursor!!.moveToFirst()
        val idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
        val inVisibleGroupColumnIndex =
            cursor.getColumnIndex(ContactsContract.Contacts.IN_VISIBLE_GROUP)
        val displayNamePrimaryColumnIndex = cursor.getColumnIndex(DISPLAY_NAME)
        val starredColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.STARRED)
        val photoColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
        val thumbnailColumnIndex =
            cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI)
        val hasPhoneNumberColumnIndex =
            cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
        while (!cursor.isAfterLast) {
            val id = cursor.getLong(idColumnIndex)
            var contact = contacts[id, null]
            if (contact == null) {
                contact = Contact(id)
            }
            mapInVisibleGroup(cursor, contact, inVisibleGroupColumnIndex)
            mapDisplayName(cursor, contact, displayNamePrimaryColumnIndex)
            mapStarred(cursor, contact, starredColumnIndex)
            mapPhoto(cursor, contact, photoColumnIndex)
            mapThumbnail(cursor, contact, thumbnailColumnIndex)
            when (columnLimitChoice) {
                LimitColumn.EMAIL -> getEmail(id, contact)
                LimitColumn.PHONE -> getPhoneNumber(id, cursor, contact, hasPhoneNumberColumnIndex)
                LimitColumn.NONE -> {
                    getEmail(id, contact)
                    getPhoneNumber(id, cursor, contact, hasPhoneNumberColumnIndex)
                }
            }
            if (columnLimitChoice === LimitColumn.EMAIL) {
                if (contact.emails.size > 0) {
                    contacts.put(id, contact)
                    emit(contact)
                }
            } else {
                contacts.put(id, contact)
                emit( contact )
            }
            cursor.moveToNext()
        }
        cursor.close()

    }

    private fun getEmail(id: Long, contact: Contact) {
        val emailCursor = mResolver.query(
            EMAIL_CONTENT_URI, EMAIL_PROJECTION,
            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", arrayOf(id.toString()), null
        )
        if (emailCursor != null) {
            val emailDataColumnIndex =
                emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)
            if (emailCursor.moveToFirst()) {
                mapEmail(emailCursor, contact, emailDataColumnIndex)
            }
            emailCursor.close()
        }
    }

    private fun getPhoneNumber(
        id: Long,
        cursor: Cursor?,
        contact: Contact,
        hasPhoneNumberColumnIndex: Int
    ) {
        val hasPhoneNumber = cursor!!.getString(hasPhoneNumberColumnIndex).toInt()
        if (hasPhoneNumber > 0) {
            val phoneCursor = mResolver.query(
                PHONE_CONTENT_URI,
                NUMBER_PROJECTION,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                arrayOf(id.toString()),
                null
            )
            if (phoneCursor != null) {
                phoneCursor.moveToFirst()
                val phoneNumberColumnIndex =
                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val phoneNumberTypeIndex =
                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
                val labelColIndex =
                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL)
                while (!phoneCursor.isAfterLast) {
                    mapPhoneNumber(
                        mContext,
                        phoneCursor,
                        contact,
                        phoneNumberColumnIndex,
                        phoneNumberTypeIndex,
                        labelColIndex
                    )
                    phoneCursor.moveToNext()
                }
                phoneCursor.close()
            }
        }
    }

    private fun getFilter(limitColumn: LimitColumn): String? {
        @Suppress("NON_EXHAUSTIVE_WHEN")
        when (limitColumn) {
            LimitColumn.PHONE -> return ContactsContract.Contacts.HAS_PHONE_NUMBER + " > 0"
        }
        return null
    }

    private fun createCursor(filter: String?): Cursor? {
        return mResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            PROJECTION,
            filter,
            null,
            ContactsContract.Contacts._ID
        )
    }

    companion object {
        private const val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        private val PHONE_CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        private val EMAIL_CONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI
        private val PROJECTION = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.IN_VISIBLE_GROUP,
            DISPLAY_NAME,
            ContactsContract.Contacts.STARRED,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )
        private val EMAIL_PROJECTION = arrayOf(
            ContactsContract.CommonDataKinds.Email.CONTACT_ID,
            ContactsContract.CommonDataKinds.Email.DATA
        )
        private val NUMBER_PROJECTION = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.LABEL
        )
    }
}