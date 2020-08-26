package com.wafflecopter.contactspicker.pickerfragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.l4digital.fastscroll.FastScroller
import com.wafflecopter.contactspicker.R
import com.wafflecopter.contactspicker.Views.RoundLetterView
import com.wafflecopter.contactspicker.contactsmodel.Contact
import java.util.*

internal class MultiContactPickerAdapter(contactItemList: List<Contact>, listener: ContactSelectListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), FastScroller.SectionIndexer, Filterable {
    private var contactItemList: List<Contact>
    private val contactItemListOriginal: List<Contact>
    private val listener: ContactSelectListener?
    private var currentFilterQuery: String? = null

    internal interface ContactSelectListener {
        fun onContactSelected(contact: Contact?, totalSelectedContacts: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_contact_row, viewGroup, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        if (holder is ContactViewHolder) {
            val contactItem = getItem(i)
            holder.tvContactName.text = contactItem.displayName
            holder.vRoundLetterView.titleText = contactItem.displayName!![0].toString()
            holder.vRoundLetterView.setBackgroundColor( contactItem.backgroundColor )
            if (contactItem.phoneNumbers.size > 0) {
                val phoneNumber = contactItem.phoneNumbers[0].number!!.replace("\\s+".toRegex(), "")
                val displayName = contactItem.displayName!!.replace("\\s+".toRegex(), "")
                if (phoneNumber != displayName) {
                    holder.tvNumber.visibility = View.VISIBLE
                    holder.tvNumber.text = phoneNumber
                } else {
                    holder.tvNumber.visibility = View.GONE
                }
            } else {
                if (contactItem.emails.size > 0) {
                    val email = contactItem.emails[0].replace("\\s+".toRegex(), "")
                    val displayName = contactItem.displayName!!.replace("\\s+".toRegex(), "")
                    if (email != displayName) {
                        holder.tvNumber.visibility = View.VISIBLE
                        holder.tvNumber.text = email
                    } else {
                        holder.tvNumber.visibility = View.GONE
                    }
                } else {
                    holder.tvNumber.visibility = View.GONE
                }
            }
            highlightTerm(
                holder.tvContactName,
                currentFilterQuery,
                holder.tvContactName.text.toString()
            )
            if (contactItem.isSelected) {
                holder.ivSelectedState.visibility = View.VISIBLE
            } else {
                holder.ivSelectedState.visibility = View.INVISIBLE
            }
            holder.mView.setOnClickListener {
                setContactSelected(contactItem.id)
                listener?.onContactSelected(getItem(i), selectedContactsCount)
                notifyDataSetChanged()
            }
        }
    }

    private fun highlightTerm(tv: TextView, query: String?, originalString: String) {
        if (query != null && query.isNotEmpty()) {
            val startPos = originalString.toLowerCase(Locale.getDefault()).indexOf(query.toLowerCase(
                Locale.getDefault()))
            val endPos = startPos + query.length
            if (startPos != -1) {
                val spannable: Spannable = SpannableString(originalString)
                val blackColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(Color.BLACK))
                val highlightSpan = TextAppearanceSpan(null, Typeface.BOLD, -1, blackColor, null)
                spannable.setSpan(
                    highlightSpan,
                    startPos,
                    endPos,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                tv.text = spannable
            } else {
                tv.text = originalString
            }
        } else {
            tv.text = originalString
        }
    }

    fun setAllSelected(isAll: Boolean) {
        for (c in contactItemList) {
            c.isSelected = isAll
            listener?.onContactSelected(c, selectedContactsCount)
        }
        notifyDataSetChanged()
    }

    fun setContactSelected(id: Long) {
        val pos = getItemPosition(contactItemList, id)
        contactItemList[pos].isSelected = !contactItemList[pos].isSelected
    }

    private fun getItemPosition(list: List<Contact>?, mid: Long): Int {
        for ((i, contact) in list!!.withIndex()) {
            if (contact.id == mid) {
                return i
            }
        }
        return -1
    }

    val selectedContactsCount: Int
        get() = if (selectedContacts != null) selectedContacts!!.size else 0
    val selectedContacts: List<Contact>?
        get() {
            val selectedContacts: MutableList<Contact> = ArrayList()
            for (contact in contactItemListOriginal) {
                if (contact.isSelected) {
                    selectedContacts.add(contact)
                }
            }
            return selectedContacts
        }

    override fun getItemCount(): Int {
        return contactItemList.size
    }

    private fun getItem(pos: Int): Contact {
        return contactItemList[pos]
    }

    override fun getSectionText(position: Int): String {
        return try {
            contactItemList[position].displayName!![0].toString()
        } catch (ex: NullPointerException) {
            ex.printStackTrace()
            ""
        } catch (ex: IndexOutOfBoundsException) {
            ex.printStackTrace()
            ""
        }
    }

    private class ContactViewHolder(val mView: View) :
        RecyclerView.ViewHolder( mView ) {
        val tvContactName: TextView = mView.findViewById<View>(R.id.tvContactName) as TextView
        val tvNumber: TextView = mView.findViewById<View>(R.id.tvNumber) as TextView
        val vRoundLetterView: RoundLetterView = mView.findViewById<View>(R.id.vRoundLetterView) as RoundLetterView
        val ivSelectedState: ImageView = mView.findViewById<View>(R.id.ivSelectedState) as ImageView
    }

    fun filterOnText(query: String?) {
        currentFilterQuery = query
        filter.filter(currentFilterQuery)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                @Suppress("UNCHECKED_CAST")
                contactItemList = results.values as List<Contact>
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredResults: List<Contact>?
                if (constraint.isEmpty()) {
                    filteredResults = contactItemListOriginal
                    currentFilterQuery = null
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase(Locale.getDefault()))
                }
                val results = FilterResults()
                results.values = filteredResults
                return results
            }
        }
    }

    private fun getFilteredResults(constraint: String): List<Contact> {
        val results: MutableList<Contact> = ArrayList()
        for (item in contactItemListOriginal) {
            if (item.displayName!!.toLowerCase(Locale.getDefault()).contains(constraint)) {
                results.add(item)
            }
        }
        return results
    }

    init {
        this.contactItemList = contactItemList
        contactItemListOriginal = contactItemList
        this.listener = listener
    }
}