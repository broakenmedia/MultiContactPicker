package com.wafflecopter.multicontactpicker;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.l4digital.fastscroll.FastScroller;
import com.wafflecopter.multicontactpicker.RxContacts.ContactResult;
import com.wafflecopter.multicontactpicker.Views.RoundLetterView;

import java.util.ArrayList;
import java.util.List;

class MultiContactPickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FastScroller.SectionIndexer, Filterable {

    private List<ContactResult> contactItemList;
    private List<ContactResult> contactItemListOriginal;
    private ContactSelectListener listener;


    interface ContactSelectListener{
        void onContactSelected(ContactResult contact, int totalSelectedContacts);
    }

    MultiContactPickerAdapter(List<ContactResult> contactItemList, ContactSelectListener listener) {
        this.contactItemList = contactItemList;
        this.contactItemListOriginal = contactItemList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_contact_pick_item, viewGroup, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int i) {
        if(holder instanceof ContactViewHolder) {
            ContactViewHolder contactViewHolder = (ContactViewHolder) holder;
            final ContactResult contactItem = getItem(i);
            contactViewHolder.tvContactName.setText(contactItem.getDisplayName());
            contactViewHolder.vRoundLetterView.setTitleText(String.valueOf(contactItem.getDisplayName().charAt(0)));
            contactViewHolder.vRoundLetterView.setBackgroundColor(contactItem.getBackgroundColor());

            if (contactItem.getPhoneNumbers().size() > 0) {
                String phoneNumber = contactItem.getPhoneNumbers().get(0).getValue().replaceAll("\\s+", "");
                String displayName = contactItem.getDisplayName().replaceAll("\\s+", "");
                if (!phoneNumber.equals(displayName)) {
                    contactViewHolder.tvNumber.setVisibility(View.VISIBLE);
                    contactViewHolder.tvNumber.setText(String.valueOf(contactItem.getPhoneNumbers().get(0)));
                } else {
                    contactViewHolder.tvNumber.setVisibility(View.GONE);
                }
            } else {
                if (contactItem.getEmails().size() > 0) {
                    String email = contactItem.getEmails().get(0).getValue().replaceAll("\\s+", "");
                    String displayName = contactItem.getDisplayName().replaceAll("\\s+", "");
                    if (!email.equals(displayName)) {
                        contactViewHolder.tvNumber.setVisibility(View.VISIBLE);
                        contactViewHolder.tvNumber.setText(String.valueOf(contactItem.getEmails().get(0)));
                    } else {
                        contactViewHolder.tvNumber.setVisibility(View.GONE);
                    }
                } else {
                    contactViewHolder.tvNumber.setVisibility(View.GONE);
                }
            }

            if (contactItem.isSelected()) {
                contactViewHolder.ivSelectedState.setVisibility(View.VISIBLE);
            } else {
                contactViewHolder.ivSelectedState.setVisibility(View.INVISIBLE);
            }

            contactViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setContactSelected(contactItem.getId());
                    notifyDataSetChanged();
                    if (listener != null) {
                        listener.onContactSelected(getItem(i), getSelectedContactsCount());
                    }
                }
            });
        }
    }

    private void setContactSelected(long id){
        int pos = getItemPosition(contactItemList, id);
        contactItemList.get(pos).setSelected(!contactItemList.get(pos).isSelected());
    }

    private int getItemPosition(List<ContactResult> list, long mid){
        int i = 0;
        for(ContactResult contact : list){
            if(contact.getId() == mid){
                return i;
            }
            i++;
        }
        return -1;
    }

    private int getSelectedContactsCount(){
        return getSelectedContacts().size();
    }

    ArrayList<ContactResult> getSelectedContacts(){
        ArrayList<ContactResult> selectedContacts = new ArrayList<>();
        for(ContactResult contact : contactItemListOriginal){
            if(contact.isSelected()){
                selectedContacts.add(contact);
            }
        }
        return selectedContacts;
    }

    @Override
    public int getItemCount() {
        return (null != contactItemList ? contactItemList.size() : 0);
    }

    private ContactResult getItem(int pos){
        return contactItemList.get(pos);
    }

    @Override
    public String getSectionText(int position) {
        try {
            return String.valueOf(contactItemList.get(position).getDisplayName().charAt(0));
        } catch (NullPointerException | IndexOutOfBoundsException ex){
            ex.printStackTrace();
            return "";
        }
    }

    private class ContactViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private TextView tvContactName;
        private TextView tvNumber;
        private RoundLetterView vRoundLetterView;
        private ImageView ivSelectedState;
        ContactViewHolder(View view) {
            super(view);
            this.mView = view;
            this.vRoundLetterView = (RoundLetterView) view.findViewById(R.id.vRoundLetterView);
            this.tvContactName = (TextView) view.findViewById(R.id.tvContactName);
            this.tvNumber = (TextView) view.findViewById(R.id.tvNumber);
            this.ivSelectedState = (ImageView) view.findViewById(R.id.ivSelectedState);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactItemList = (List<ContactResult>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                @SuppressWarnings("UnusedAssignment") List<ContactResult> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = contactItemListOriginal;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }
        };
    }

    private List<ContactResult> getFilteredResults(String constraint) {
        List<ContactResult> results = new ArrayList<>();
        for (ContactResult item : contactItemListOriginal) {
            if (item.getDisplayName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

}