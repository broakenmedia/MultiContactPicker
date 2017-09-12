package com.wafflecopter.multicontactpicker;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.wafflecopter.multicontactpicker.RxContacts.Contact;
import com.wafflecopter.multicontactpicker.RxContacts.ContactAddress;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ContactResult implements Parcelable {

    private String mDisplayName;
    private boolean mStarred;
    private Uri mPhoto;
    private Uri mThumbnail;
    private List<String> mEmails = new ArrayList<>();
    private List<String> mPhoneNumbers = new ArrayList<>();
    private List<PostalAddress> mPostalAddresses = new ArrayList<>();

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
        this.mPostalAddresses.clear();
        for (ContactAddress contactAddress : contact.getAddresses()) {
            this.mPostalAddresses.add(new PostalAddress(contactAddress));
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDisplayName);
        dest.writeByte(this.mStarred ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.mPhoto, flags);
        dest.writeParcelable(this.mThumbnail, flags);
        dest.writeStringList(this.mEmails);
        dest.writeStringList(this.mPhoneNumbers);
        dest.writeList(this.mPostalAddresses);
    }

    protected ContactResult(Parcel in) {
        this.mDisplayName = in.readString();
        this.mStarred = in.readByte() != 0;
        this.mPhoto = in.readParcelable(Uri.class.getClassLoader());
        this.mThumbnail = in.readParcelable(Uri.class.getClassLoader());
        this.mEmails = in.createStringArrayList();
        this.mPhoneNumbers = in.createStringArrayList();
        this.mPostalAddresses = new ArrayList<>();
        in.readList(this.mPostalAddresses, PostalAddress.class.getClassLoader());
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
