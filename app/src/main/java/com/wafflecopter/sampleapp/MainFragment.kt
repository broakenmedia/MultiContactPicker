package com.wafflecopter.sampleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.google.android.material.snackbar.Snackbar
import com.wafflecopter.contactspicker.ChoiceMode
import com.wafflecopter.contactspicker.ContactsPickerSharedViewModel
import com.wafflecopter.contactspicker.LoadMode
import com.wafflecopter.contactspicker.pickerfragment.ContactsPickerFragment
import com.wafflecopter.contactspicker.pickerfragment.ContactsPickerFragmentArgs
import com.wafflecopter.contactspicker.utils.viewLifecycleLazy
import com.wafflecopter.sampleapp.databinding.FragmentMainBinding

class MainFragment : androidx.fragment.app.Fragment() {
    private val binding by viewLifecycleLazy { FragmentMainBinding.bind(requireView()) }
    private val sharedViewModel : ContactsPickerSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startSingle.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.host_fragment, ContactsPickerFragment::class.java, ContactsPickerFragmentArgs(
                    selectedItems = longArrayOf(),
                    selectionMode = ChoiceMode.CHOICE_MODE_SINGLE
                ).toBundle())
            }
        }

        binding.startMulti.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.host_fragment, ContactsPickerFragment::class.java, ContactsPickerFragmentArgs(
                    selectedItems = longArrayOf()
                ).toBundle())
            }
        }

        binding.startSyncLoad.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.host_fragment, ContactsPickerFragment::class.java, ContactsPickerFragmentArgs(
                    selectedItems = longArrayOf(),
                    loadingMode = LoadMode.LOAD_SYNC
                ).toBundle())
            }
        }

        binding.startNoScrollbar.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.host_fragment, ContactsPickerFragment::class.java, ContactsPickerFragmentArgs(
                    selectedItems = longArrayOf(),
                    hideScrollbar = true,
                    showTrack = false
                ).toBundle())
            }
        }

        binding.startCustomText.setOnClickListener {
            parentFragmentManager.commit {
                addToBackStack(null)
                replace(R.id.host_fragment, ContactsPickerFragment::class.java, ContactsPickerFragmentArgs(
                    selectedItems = longArrayOf(),
                    title = R.string.app_name,
                    selectAllText = R.string.pick_all,
                    unselectAllText = R.string.reset,
                    noContactsText = R.string.nothing_found,
                    selectTextEnabled = R.string.finish_nr,
                    selectTextDisabled = R.string.finish
                ).toBundle())
            }
        }

        sharedViewModel.selectedContacts.observe( viewLifecycleOwner, {
            it?.let{
                Snackbar.make(requireView(),
                    "Done, selected and returned " + it.size + " contact(s)",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })
    }
}