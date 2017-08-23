package com.wafflecopter.multicontactpicker;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.wafflecopter.multicontactpicker.RxContacts.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactResult implements Parcelable {

    private String mDisplayName;
    private boolean mStarred;
    private Uri mPhoto;
    private Uri mThumbnail;
    private List<String> mEmails = new ArrayList<>();
    private List<String> mPhoneNumbers = new ArrayList<>();

    public String getDisplayName() {
        return mDisplayName;
    }

    public boolean isStarred() {
        return mStarred;
    }

    public Uri getPhoto() {
        return mPhoto;
    }

    public Uri getThumbnail() {
        return mThumbnail;
    }

    public List<String> getEmails() {
        return mEmails;
    }

    public List<String> getPhoneNumbers() {
        return mPhoneNumbers;
    }


    public ContactResult(Contact contact){
        this.mDisplayName = contact.getDisplayName();
        this.mStarred = contact.isStarred();
        this.mPhoto = contact.getPhoto();
        this.mThumbnail = contact.getThumbnail();
        this.mEmails.clear(); this.mEmails.addAll(contact.getEmails());
        this.mPhoneNumbers.clear(); this.mPhoneNumbers.addAll(contact.getPhoneNumbers());
    }

    protected ContactResult(Parcel in) {
        mDisplayName = in.readString();
        mStarred = in.readByte() != 0x00;
        mPhoto = (Uri) in.readValue(Uri.class.getClassLoader());
        mThumbnail = (Uri) in.readValue(Uri.class.getClassLoader());
        if (in.readByte() == 0x01) {
            mEmails = new ArrayList<>();
            in.readList(mEmails, String.class.getClassLoader());
        } else {
            mEmails = null;
        }
        if (in.readByte() == 0x01) {
            mPhoneNumbers = new ArrayList<>();
            in.readList(mPhoneNumbers, String.class.getClassLoader());
        } else {
            mPhoneNumbers = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDisplayName);
        dest.writeByte((byte) (mStarred ? 0x01 : 0x00));
        dest.writeValue(mPhoto);
        dest.writeValue(mThumbnail);
        if (mEmails == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mEmails);
        }
        if (mPhoneNumbers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mPhoneNumbers);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ContactResult> CREATOR = new Parcelable.Creator<ContactResult>() {
        @Override
        public ContactResult createFromParcel(Parcel in) {
            return new ContactResult(in);
        }

        @Override
        public ContactResult[] newArray(int size) {
            return new ContactResult[size];
        }
    };
}
