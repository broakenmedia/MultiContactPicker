package com.wafflecopter.multicontactpicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.l4digital.fastscroll.FastScroller;
import com.wafflecopter.multicontactpicker.RxContacts.Contact;
import com.wafflecopter.multicontactpicker.Views.RoundLetterView;

import java.util.ArrayList;
import java.util.List;

public class MultiContactPickerAdapter extends RecyclerView.Adapter<MultiContactPickerAdapter.ContactViewHolder> implements FastScroller.SectionIndexer {

    private List<Contact> contactItemList;
    private ContactSelectListener listener;

    interface ContactSelectListener{
        void onContactSelected(Contact contact, int totalSelectedContacts);
    }

    MultiContactPickerAdapter(List<Contact> contactItemList, ContactSelectListener listener) {
        this.contactItemList = contactItemList;
        this.listener = listener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_contact_pick_item, viewGroup, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int i) {
        final Contact contactItem = contactItemList.get(i);
        holder.tvContactName.setText(contactItem.getDisplayName());
        holder.vRoundLetterView.setTitleText(String.valueOf(contactItem.getDisplayName().charAt(0)));
        holder.vRoundLetterView.setBackgroundColor(contactItem.getBackgroundColor());

        if(contactItem.getPhoneNumbers().size() > 0) {
            holder.tvNumber.setVisibility(View.VISIBLE);
            holder.tvNumber.setText(String.valueOf(contactItem.getPhoneNumbers().get(0)));
        }else{
            holder.tvNumber.setVisibility(View.GONE);
        }

        if(contactItem.isSelected()){
            holder.ivSelectedState.setVisibility(View.VISIBLE);
        }else{
            holder.ivSelectedState.setVisibility(View.INVISIBLE);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactItemList.get(i).setSelected(!contactItem.isSelected());
                notifyDataSetChanged();
                if(listener != null){
                    listener.onContactSelected(contactItemList.get(i), getSelectedContactsCount());
                }
            }
        });
    }

    private int getSelectedContactsCount(){
        return getSelectedContacts().size();
    }

    List<Contact> getSelectedContacts(){
        List<Contact> selectedContacts = new ArrayList<>();
        for(Contact contact : contactItemList){
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

    @Override
    public String getSectionText(int position) {
        return String.valueOf(contactItemList.get(position).getDisplayName().charAt(0));
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
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
}