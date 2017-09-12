package com.wafflecopter.multicontactpicker;

import android.os.Parcel;

import com.wafflecopter.multicontactpicker.RxContacts.ContactAddress;

@SuppressWarnings({"WeakerAccess","unused"})
public class PostalAddress implements android.os.Parcelable {

    private int mType;
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
    }

    public PostalAddress(ContactAddress contactAddress) {
        mType = contactAddress.getType();
        mLabel = contactAddress.getLabel();
        mFormattedAddress = contactAddress.getFormattedAddress();
        mStreet = contactAddress.getStreet();
        mPobox = contactAddress.getPobox();
        mNeighborhood = contactAddress.getNeighborhood();
        mCity = contactAddress.getCity();
        mRegion = contactAddress.getRegion();
        mPostcode = contactAddress.getPostcode();
        mCountry = contactAddress.getCountry();
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
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
        this.mFormattedAddress = mFormattedAddress;
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

        if (mType != address.mType) {
            return false;
        }
        if (mLabel != null ? !mLabel.equals(address.mLabel) : address.mLabel != null) {
            return false;
        }
        if (mFormattedAddress != null ? !mFormattedAddress.equals(address.mFormattedAddress)
            : address.mFormattedAddress != null) {
            return false;
        }
        if (mStreet != null ? !mStreet.equals(address.mStreet) : address.mStreet != null) {
            return false;
        }
        if (mPobox != null ? !mPobox.equals(address.mPobox) : address.mPobox != null) {
            return false;
        }
        if (mNeighborhood != null ? !mNeighborhood.equals(address.mNeighborhood)
            : address.mNeighborhood != null) {
            return false;
        }
        if (mCity != null ? !mCity.equals(address.mCity) : address.mCity != null) {
            return false;
        }
        if (mRegion != null ? !mRegion.equals(address.mRegion) : address.mRegion != null) {
            return false;
        }
        if (mPostcode != null ? !mPostcode.equals(address.mPostcode) : address.mPostcode != null) {
            return false;
        }
        return mCountry != null ? mCountry.equals(address.mCountry) : address.mCountry == null;

    }

    @Override
    public int hashCode() {
        int result = mType;
        result = 31 * result + (mLabel != null ? mLabel.hashCode() : 0);
        result = 31 * result + (mFormattedAddress != null ? mFormattedAddress.hashCode() : 0);
        result = 31 * result + (mStreet != null ? mStreet.hashCode() : 0);
        result = 31 * result + (mPobox != null ? mPobox.hashCode() : 0);
        result = 31 * result + (mNeighborhood != null ? mNeighborhood.hashCode() : 0);
        result = 31 * result + (mCity != null ? mCity.hashCode() : 0);
        result = 31 * result + (mRegion != null ? mRegion.hashCode() : 0);
        result = 31 * result + (mPostcode != null ? mPostcode.hashCode() : 0);
        result = 31 * result + (mCountry != null ? mCountry.hashCode() : 0);
        return result;
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