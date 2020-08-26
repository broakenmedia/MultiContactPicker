package com.wafflecopter.contactspicker.pickerfragment

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wafflecopter.contactspicker.ChoiceMode
import com.wafflecopter.contactspicker.ContactsPickerSharedViewModel
import com.wafflecopter.contactspicker.LoadMode
import com.wafflecopter.contactspicker.R
import com.wafflecopter.contactspicker.contactsmodel.Contact
import com.wafflecopter.contactspicker.contactsmodel.ContactResult
import com.wafflecopter.contactspicker.contactsmodel.RxContacts
import com.wafflecopter.contactspicker.databinding.FragmentContactsPickerBinding
import com.wafflecopter.contactspicker.utils.viewLifecycleLazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ContactsPickerFragment : Fragment(), SearchView.OnQueryTextListener {
    private val binding by viewLifecycleLazy { FragmentContactsPickerBinding.bind(requireView()) }
    private val viewModel: ContactsPickerViewModel by viewModels()
    private val sharedViewModel : ContactsPickerSharedViewModel by activityViewModels()
    private val args: ContactsPickerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_contacts_picker, container, false)
    }

    private lateinit var adapter: MultiContactPickerAdapter

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = (binding.topAppBar.menu.findItem(R.id.search).actionView as SearchView)
        searchView.setQuery(viewModel.queryText, false)
        searchView.setOnQueryTextListener(this)

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = MultiContactPickerAdapter(
            viewModel.contactList,
            object : MultiContactPickerAdapter.ContactSelectListener {
                override fun onContactSelected(contact: Contact?, totalSelectedContacts: Int) {
                    updateSelectBarContents(totalSelectedContacts)
                    if (args.selectionMode == ChoiceMode.CHOICE_MODE_SINGLE) {
                        finishPicking()
                    }
                }
            })

        binding.tvSelect.setOnClickListener{ finishPicking() }

        binding.tvSelectAll.setOnClickListener{
            viewModel.allSelected = !viewModel.allSelected
            adapter.setAllSelected(viewModel.allSelected)
            if (viewModel.allSelected) binding.tvSelectAll.text = getString(args.unselectAllText) else binding.tvSelectAll.text =
                getString(args.selectAllText)
        }

        if (!viewModel.contactsLoaded) {
            loadContacts()
        }

        binding.recyclerView.adapter = adapter

        initialiseUI()
    }

    private fun initialiseUI() {
        binding.recyclerView.setHideScrollbar(args.hideScrollbar)
        binding.recyclerView.setTrackVisible(args.showTrack)
        binding.controlPanel.isVisible = (args.selectionMode == ChoiceMode.CHOICE_MODE_MULTIPLE)
        if (args.selectionMode === ChoiceMode.CHOICE_MODE_SINGLE && args.selectedItems.isNotEmpty()) {
            throw RuntimeException("You must be using MultiContactPicker.CHOICE_MODE_MULTIPLE in order to use setSelectedContacts()")
        }

        binding.topAppBar.title = getString( args.title)
        binding.tvSelectAll.text = getString( args.selectAllText )
        binding.tvSelect.text = getString( args.selectTextDisabled )
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        adapter.filterOnText(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.queryText = newText.orEmpty()
        adapter.filterOnText(newText)
        return true
    }

    private fun updateSelectBarContents(totalSelectedContacts: Int) {
        binding.tvSelect.isEnabled = totalSelectedContacts > 0
        if (totalSelectedContacts > 0) {

            binding.tvSelect.text = getString(
                args.selectTextEnabled,
                totalSelectedContacts.toString()
            )
        } else {
            binding.tvSelect.text = getString(args.selectTextDisabled)
        }
    }

    @ExperimentalCoroutinesApi
    private val permissionRequester =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                binding.progressBar.isVisible = true
                binding.tvSelectAll.isEnabled = false

                GlobalScope.launch(Dispatchers.IO) {
                    RxContacts(requireContext()).fetch(args.columnLimit).onEach {
                        viewModel.contactList.add(it)

                        if (args.selectedItems.contains(it.id)) {
                            adapter.setContactSelected(it.id)
                        }
                        viewModel.contactList.sortWith { p0, p1 ->
                            p0.displayName!!.compareTo(
                                p1.displayName!!,
                                ignoreCase = true
                            )
                        }

                        if (args.loadingMode == LoadMode.LOAD_ASYNC) {
                            GlobalScope.launch(Dispatchers.Main) {
                                adapter.notifyDataSetChanged()
                                binding.progressBar.isVisible = false
                            }
                        }
                    }.onCompletion {
                        GlobalScope.launch(Dispatchers.Main) {
                            if (viewModel.contactList.isEmpty()) {
                                binding.tvNoContacts.isVisible = true
                            }

                            if (args.loadingMode == LoadMode.LOAD_SYNC) {
                                adapter.notifyDataSetChanged()
                            }

                            updateSelectBarContents(adapter.selectedContactsCount)

                            binding.progressBar.visibility = View.GONE
                            binding.tvSelectAll.isEnabled = true
                        }
                    }.collect()
                }
            } else {
                Snackbar.make(requireView(), args.permissionExplanation, Snackbar.LENGTH_LONG)
                    .setAction(android.R.string.ok) { loadContacts() }.show()
            }
        }

    @ExperimentalCoroutinesApi
    fun loadContacts() {
        permissionRequester.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun finishPicking() {
        val contactResults = mutableListOf<ContactResult>()

        for (contact in adapter.selectedContacts!!) {
            contactResults.add(ContactResult(contact))
        }

        sharedViewModel.setContacts( contactResults )

        //close and return to the launching fragment
        parentFragmentManager.popBackStack()
    }
}