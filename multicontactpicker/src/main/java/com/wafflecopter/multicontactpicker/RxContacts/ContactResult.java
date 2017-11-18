package com.wafflecopter.multicontactpicker.RxContacts;

import android.graphics.Color;
import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.wafflecopter.multicontactpicker.ColorUtils;

import java.util.ArrayList;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ContactResult implements Comparable<ContactResult>, android.os.Parcelable {
    private final long mId;
    private int mInVisibleGroup;
    private String mDisplayName;
    private boolean mStarred;
    private Uri mPhoto;
    private Uri mThumbnail;

    private ArrayList<EmailString> mEmails = new ArrayList<>();
    private ArrayList<PhoneNumberString> mPhoneNumbers = new ArrayList<>();
    private ArrayList<PostalAddress> mAddresses = new ArrayList<>();

    private boolean isSelected;
    private int backgroundColor = Color.BLUE;

    ContactResult(long id) {
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

    public ArrayList<EmailString> getEmails() {
        return mEmails;
    }

    public ArrayList<PhoneNumberString> getPhoneNumbers() {
        return mPhoneNumbers;
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

    public ArrayList<PostalAddress> getAddresses() {
        return mAddresses;
    }

    @Override
    public int compareTo(@NonNull ContactResult other) {
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
        ContactResult contact = (ContactResult) o;
        return mId == contact.mId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeInt(this.mInVisibleGroup);
        dest.writeString(this.mDisplayName);
        dest.writeByte(this.mStarred ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.mPhoto, flags);
        dest.writeParcelable(this.mThumbnail, flags);
        dest.writeTypedList(this.mEmails);
        dest.writeTypedList(this.mPhoneNumbers);
        dest.writeTypedList(this.mAddresses);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.backgroundColor);
    }

    protected ContactResult(Parcel in) {
        this.mId = in.readLong();
        this.mInVisibleGroup = in.readInt();
        this.mDisplayName = in.readString();
        this.mStarred = in.readByte() != 0;
        this.mPhoto = in.readParcelable(Uri.class.getClassLoader());
        this.mThumbnail = in.readParcelable(Uri.class.getClassLoader());
        this.mEmails = in.createTypedArrayList(EmailString.CREATOR);
        this.mPhoneNumbers = in.createTypedArrayList(PhoneNumberString.CREATOR);
        this.mAddresses = in.createTypedArrayList(PostalAddress.CREATOR);
        this.isSelected = in.readByte() != 0;
        this.backgroundColor = in.readInt();
    }

    public static final Creator<ContactResult> CREATOR = new Creator<ContactResult>() {
        @Override
        public ContactResult createFromParcel(Parcel source) {
            return new ContactResult(source);
        }

        @Override
        public ContactResult[] newArray(int size) {
            return new ContactResult[size];
        }
    };
}