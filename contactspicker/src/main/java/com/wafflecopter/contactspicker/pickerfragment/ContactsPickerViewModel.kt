package com.wafflecopter.contactspicker.pickerfragment

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.wafflecopter.contactspicker.contactsmodel.Contact
import java.util.*

class ContactsPickerViewModel @ViewModelInject constructor(): ViewModel() {
    var queryText = ""
    val contactList: MutableList<Contact> = ArrayList()
    val contactsLoaded = false
    var allSelected = false
}