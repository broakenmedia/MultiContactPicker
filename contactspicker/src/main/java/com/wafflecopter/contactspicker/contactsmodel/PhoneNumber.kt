package com.wafflecopter.contactspicker.contactsmodel

import android.os.Parcel
import android.os.Parcelable

class PhoneNumber : Parcelable {
    var typeLabel: String?
    var number: String?

    internal constructor(typeLabel: String?, number: String?) {
        this.typeLabel = typeLabel
        this.number = number
    }

    private constructor(`in`: Parcel) {
        typeLabel = `in`.readString()
        number = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(typeLabel)
        dest.writeString(number)
    }

    companion object {
        var CREATOR: Parcelable.Creator<PhoneNumber> = object : Parcelable.Creator<PhoneNumber> {
            override fun createFromParcel(`in`: Parcel): PhoneNumber? {
                return PhoneNumber(`in`)
            }

            override fun newArray(size: Int): Array<PhoneNumber?> {
                return arrayOfNulls(size)
            }
        }
    }
}