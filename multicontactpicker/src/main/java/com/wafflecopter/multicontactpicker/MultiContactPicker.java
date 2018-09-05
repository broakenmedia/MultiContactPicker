package com.wafflecopter.multicontactpicker;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;

import com.wafflecopter.multicontactpicker.RxContacts.Contact;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultiContactPicker {

    public static final int CHOICE_MODE_MULTIPLE = 0;
    public static final int CHOICE_MODE_SINGLE = 1;

    public static final int LOAD_ASYNC = 0;
    public static final int LOAD_SYNC = 1;

    public static class Builder implements Serializable {

        protected transient Activity acc;
        protected transient Fragment frag;
        @StyleRes
        protected int theme = R.style.MultiContactPicker_Azure;
        protected int bubbleColor;
        protected int bubbleTextColor;
        protected int handleColor;
        protected int trackColor;
        protected LimitColumn columnLimit = LimitColumn.NONE;
        protected Integer searchIconColor;
        protected boolean hideScrollbar;
        protected boolean showTrack = true;
        protected int selectionMode = CHOICE_MODE_MULTIPLE;
        protected int loadingMode = LOAD_ASYNC;
        protected ArrayList<Long> selectedItems = new ArrayList<>();
        protected String titleText;

        public Builder(@NonNull Activity act) {
            this.acc = act;
        }

        public Builder(@NonNull Fragment frag) {
            this.frag = frag;
        }

        public Builder theme(@StyleRes int theme) {
            this.theme = theme;
            return this;
        }

        public Builder bubbleColor(@ColorInt int bubbleColor) {
            this.bubbleColor = bubbleColor;
            return this;
        }

        public Builder bubbleTextColor(@ColorInt int textColor) {
            this.bubbleTextColor = textColor;
            return this;
        }

        public Builder handleColor(@ColorInt int handleColor) {
            this.handleColor = handleColor;
            return this;
        }

        public Builder trackColor(@ColorInt int trackColor) {
            this.trackColor = trackColor;
            return this;
        }

        public Builder searchIconColor(@ColorInt Integer searchIconColor) {
            this.searchIconColor = searchIconColor;
            return this;
        }

        public Builder hideScrollbar(boolean hideScrollbar) {
            this.hideScrollbar = hideScrollbar;
            return this;
        }

        public Builder showTrack(boolean showTrack) {
            this.showTrack = showTrack;
            return this;
        }

        public Builder setChoiceMode(int selectionMode){
            this.selectionMode = selectionMode;
            return this;
        }

        public Builder setLoadingType(int loadingMode){
            this.loadingMode = loadingMode;
            return this;
        }

        public Builder setTitleText(String titleText){
            this.titleText = titleText;
            return this;
        }

        public Builder limitToColumn(LimitColumn limitedColumn){
            this.columnLimit = limitedColumn;
            return this;
        }

        public Builder setSelectedContacts(String... selectedContactIDs){
            this.selectedItems.clear();
            for(String id : selectedContactIDs){
                this.selectedItems.add(Long.parseLong(id));
            }
            return this;
        }

        public Builder setSelectedContacts(ArrayList<ContactResult> selectedContacts){
            this.selectedItems.clear();
            for(ContactResult result : selectedContacts){
                this.selectedItems.add(Long.parseLong(result.getContactID()));
            }
            return this;
        }

        
        public void showPickerForResult(int requestCode) {
            if (acc != null) {
                Intent intent = new Intent(acc, MultiContactPickerActivity.class);
                intent.putExtra("builder", this);
                acc.startActivityForResult(intent, requestCode);
            }else if(frag != null){
                if(frag.getActivity() != null) {
                    Intent intent = new Intent(frag.getActivity(), MultiContactPickerActivity.class);
                    intent.putExtra("builder", this);
                    frag.startActivityForResult(intent, requestCode);
                }
            }else{
                throw new RuntimeException("Unable to find a context for intent. Is there a valid activity or fragment passed in the builder?");
            }
        }
    }

    static ArrayList<ContactResult> buildResult(List<Contact> selectedContacts){
        ArrayList<ContactResult> contactResults = new ArrayList<>();
        for(Contact contact : selectedContacts){
            contactResults.add(new ContactResult(contact));
        }
        return contactResults;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<ContactResult> obtainResult(Intent data){
        return data.getParcelableArrayListExtra(MultiContactPickerActivity.EXTRA_RESULT_SELECTION);
    }
}
