package com.wafflecopter.contactspicker

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wafflecopter.contactspicker.contactsmodel.ContactResult

class ContactsPickerSharedViewModel @ViewModelInject constructor(): ViewModel() {
    val selectedContacts = MutableLiveData<List<ContactResult>>()

    fun setContacts( list : List<ContactResult>) {
        selectedContacts.value = list
    }
}