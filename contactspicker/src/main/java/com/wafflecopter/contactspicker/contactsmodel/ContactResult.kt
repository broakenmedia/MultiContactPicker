package com.wafflecopter.contactspicker.contactsmodel

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.util.*

class ContactResult : Parcelable {
    var contactID: String?
    var displayName: String?
        private set
    var isStarred: Boolean
        private set
    var photo: Uri?
        private set
    var thumbnail: Uri?
        private set
    private var mEmails: MutableList<String>? = ArrayList()
    private val mPhoneNumbers: MutableList<PhoneNumber> = ArrayList()
    val emails: List<String>?
        get() = mEmails
    val phoneNumbers: List<PhoneNumber>
        get() = mPhoneNumbers

    constructor(contact: Contact) {
        contactID = contact.id.toString()
        displayName = contact.displayName
        isStarred = contact.isStarred
        photo = contact.photo
        thumbnail = contact.thumbnail
        mEmails!!.clear()
        mEmails!!.addAll(contact.emails)
        mPhoneNumbers.clear()
        mPhoneNumbers.addAll(contact.phoneNumbers)
    }

    protected constructor(`in`: Parcel) {
        contactID = `in`.readString()
        displayName = `in`.readString()
        isStarred = `in`.readByte().toInt() != 0
        photo = `in`.readParcelable(Uri::class.java.classLoader)
        thumbnail = `in`.readParcelable(Uri::class.java.classLoader)
        mEmails = `in`.createStringArrayList()
        `in`.readTypedList(mPhoneNumbers, PhoneNumber.CREATOR)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(contactID)
        dest.writeString(displayName)
        dest.writeByte(if (isStarred) 1.toByte() else 0.toByte())
        dest.writeParcelable(photo, flags)
        dest.writeParcelable(thumbnail, flags)
        dest.writeStringList(mEmails)
        dest.writeTypedList(mPhoneNumbers)
    }

    companion object {
        val CREATOR: Parcelable.Creator<ContactResult> =
            object : Parcelable.Creator<ContactResult> {
                override fun createFromParcel(`in`: Parcel): ContactResult? {
                    return ContactResult(`in`)
                }

                override fun newArray(size: Int): Array<ContactResult?> {
                    return arrayOfNulls(size)
                }
            }
    }
}