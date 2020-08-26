package com.wafflecopter.contactspicker.contactsmodel

import android.graphics.Color
import android.net.Uri
import com.wafflecopter.contactspicker.utils.ColorUtils.randomMaterialColor
import java.util.*

class Contact internal constructor(val id: Long) : Comparable<Contact> {
    var inVisibleGroup = 0
    var displayName: String? = null
    var isStarred = false
    var photo: Uri? = null
    var thumbnail: Uri? = null
    var emails: MutableList<String> = ArrayList()
    var phoneNumbers: MutableList<PhoneNumber> = ArrayList()
    var isSelected = false
    var backgroundColor = Color.BLUE
    override fun compareTo(other: Contact): Int {
        return if (displayName != null && other.displayName != null) displayName!!.compareTo(
            other.displayName!!
        ) else -1
    }

    override fun hashCode(): Int {
        return (id xor (id ushr 32)).toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val contact = other as Contact
        return id == contact.id
    }

    init {
        backgroundColor = randomMaterialColor
    }
}