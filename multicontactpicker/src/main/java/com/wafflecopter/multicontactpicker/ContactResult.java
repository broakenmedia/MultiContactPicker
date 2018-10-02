package com.wafflecopter.multicontactpicker;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.wafflecopter.multicontactpicker.RxContacts.Contact;
import com.wafflecopter.multicontactpicker.RxContacts.PhoneNumber;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ContactResult implements Parcelable {

    private String mContactID;
    private String mDisplayName;
    private boolean mStarred;
    private Uri mPhoto;
    private Uri mThumbnail;
    private List<String> mEmails = new ArrayList<>();
    private List<PhoneNumber> mPhoneNumbers = new ArrayList<>();

    public String getContactID() {
        return mContactID;
    }

    public void setContactID(String mContactID) {
        this.mContactID = mContactID;
    }

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

    public List<PhoneNumber> getPhoneNumbers() {
        return mPhoneNumbers;
    }

    public ContactResult(Contact contact){
        this.mContactID = String.valueOf(contact.getId());
        this.mDisplayName = contact.getDisplayName();
        this.mStarred = contact.isStarred();
        this.mPhoto = contact.getPhoto();
        this.mThumbnail = contact.getThumbnail();
        this.mEmails.clear(); this.mEmails.addAll(contact.getEmails());
        this.mPhoneNumbers.clear(); this.mPhoneNumbers.addAll(contact.getPhoneNumbers());
    }

    protected ContactResult(Parcel in) {
        this.mContactID = in.readString();
        this.mDisplayName = in.readString();
        this.mStarred = in.readByte() != 0;
        this.mPhoto = in.readParcelable(Uri.class.getClassLoader());
        this.mThumbnail = in.readParcelable(Uri.class.getClassLoader());
        this.mEmails = in.createStringArrayList();
        in.readTypedList(this.mPhoneNumbers, PhoneNumber.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mContactID);
        dest.writeString(this.mDisplayName);
        dest.writeByte(this.mStarred ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.mPhoto, flags);
        dest.writeParcelable(this.mThumbnail, flags);
        dest.writeStringList(this.mEmails);
        dest.writeTypedList(this.mPhoneNumbers);
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
