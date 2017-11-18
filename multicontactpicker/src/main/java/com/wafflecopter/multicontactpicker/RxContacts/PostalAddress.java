package com.wafflecopter.multicontactpicker.RxContacts;

import android.content.res.Resources;
import android.os.Parcel;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;

@SuppressWarnings({"WeakerAccess","unused"})
public class PostalAddress implements android.os.Parcelable,TypeLabelInterface {

    private final int mType;
    private String mLabel;
    private String mFormattedAddress;
    private String mStreet;
    private String mPobox;
    private String mNeighborhood;
    private String mCity;
    private String mRegion;
    private String mPostcode;
    private String mCountry;

    public PostalAddress() {
        mType = StructuredPostal.TYPE_OTHER;
    }

    public PostalAddress(int type, String label, String formattedAddress, String street, String pobox, String neighborhood, String city, String region, String postcode, String country) {
        mType = type;
        mLabel = label;
        mFormattedAddress = formattedAddress;
        mStreet = street;
        mPobox = pobox;
        mNeighborhood = neighborhood;
        mCity = city;
        mRegion = region;
        mPostcode = postcode;
        mCountry = country;
    }

    public int getType() {
        return mType;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        mStreet = street;
    }

    public String getPobox() {
       return mPobox;
    }

    public void setPobox(String pobox) {
        mPobox = pobox;
    }

    public String getNeighborhood() {
        return mNeighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        mNeighborhood = neighborhood;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getRegion() {
        return mRegion;
    }

    public void setRegion(String region) {
        mRegion = region;
    }

    public String getPostcode() {
        return mPostcode;
    }

    public void setPostcode(String postcode) {
        mPostcode = postcode;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getFormattedAddress() {
        return mFormattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        mFormattedAddress = formattedAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostalAddress address = (PostalAddress) o;

        return mType == address.mType && (mLabel != null ? mLabel.equals(address.mLabel)
            : address.mLabel == null && (mFormattedAddress != null ? mFormattedAddress
                .equals(address.mFormattedAddress) : address.mFormattedAddress == null));

    }

    @Override
    public int hashCode() {
        int result = mType;
        result = 31 * result + (mLabel != null ? mLabel.hashCode() : 0);
        result = 31 * result + (mFormattedAddress != null ? mFormattedAddress.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getFormattedAddress();
    }

    @Override
    public CharSequence getTypeLabel(Resources resources) {
        return StructuredPostal.getTypeLabel(resources, getType(), getLabel());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeString(this.mLabel);
        dest.writeString(this.mFormattedAddress);
        dest.writeString(this.mStreet);
        dest.writeString(this.mPobox);
        dest.writeString(this.mNeighborhood);
        dest.writeString(this.mCity);
        dest.writeString(this.mRegion);
        dest.writeString(this.mPostcode);
        dest.writeString(this.mCountry);
    }

    protected PostalAddress(Parcel in) {
        this.mType = in.readInt();
        this.mLabel = in.readString();
        this.mFormattedAddress = in.readString();
        this.mStreet = in.readString();
        this.mPobox = in.readString();
        this.mNeighborhood = in.readString();
        this.mCity = in.readString();
        this.mRegion = in.readString();
        this.mPostcode = in.readString();
        this.mCountry = in.readString();
    }

    public static final Creator<PostalAddress> CREATOR = new Creator<PostalAddress>() {
        @Override
        public PostalAddress createFromParcel(Parcel source) {
            return new PostalAddress(source);
        }

        @Override
        public PostalAddress[] newArray(int size) {
            return new PostalAddress[size];
        }
    };
}