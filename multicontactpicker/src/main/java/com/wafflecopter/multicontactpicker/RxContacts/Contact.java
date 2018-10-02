package com.wafflecopter.multicontactpicker.RxContacts;

import android.graphics.Color;
import android.net.Uri;

import com.wafflecopter.multicontactpicker.ColorUtils;

import java.util.ArrayList;
import java.util.List;

public class Contact implements Comparable<Contact> {
    private final long mId;
    private int mInVisibleGroup;
    private String mDisplayName;
    private boolean mStarred;
    private Uri mPhoto;
    private Uri mThumbnail;
    private List<String> mEmails = new ArrayList<>();
    private List<PhoneNumber> mPhoneNumbers = new ArrayList<>();
    private boolean isSelected;
    private int backgroundColor = Color.BLUE;

    Contact(long id) {
        this.mId = id;
        this.backgroundColor = ColorUtils.getRandomMaterialColor();
    }

    public long getId() {
        return mId;
    }

    public int getInVisibleGroup() {
        return mInVisibleGroup;
    }

    public void setInVisibleGroup(int inVisibleGroup) {
        mInVisibleGroup = inVisibleGroup;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public boolean isStarred() {
        return mStarred;
    }

    public void setStarred(boolean starred) {
        mStarred = starred;
    }

    public Uri getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Uri photo) {
        mPhoto = photo;
    }

    public Uri getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Uri thumbnail) {
        mThumbnail = thumbnail;
    }

    public List<String> getEmails() {
        return mEmails;
    }

    public void setEmails(List<String> emails) {
        mEmails = emails;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return mPhoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        mPhoneNumbers = phoneNumbers;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public int compareTo(Contact other) {
        if(mDisplayName != null && other.mDisplayName != null)
            return mDisplayName.compareTo(other.mDisplayName);
        else return -1;
    }

    @Override
    public int hashCode () {
        return (int) (mId ^ (mId >>> 32));
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contact contact = (Contact) o;
        return mId == contact.mId;
    }
}