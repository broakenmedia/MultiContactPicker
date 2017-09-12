package com.wafflecopter.multicontactpicker.RxContacts;

public class ContactAddress {
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

  public ContactAddress() {}

  public ContactAddress(int type, String label, String formattedAddress, String street, String pobox, String neighborhood, String city, String region, String postcode, String country) {
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

  public void setFormattedAddress(String mFormattedAddress) {
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

    ContactAddress that = (ContactAddress) o;

    if (mType != that.mType) {
      return false;
    }
    if (mLabel != null ? !mLabel.equals(that.mLabel) : that.mLabel != null) {
      return false;
    }
    return mFormattedAddress != null ? mFormattedAddress.equals(that.mFormattedAddress)
        : that.mFormattedAddress == null;
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
    return "ContactAddress{" +
        "type=" + getType() +
        ", label='" + getLabel() + '\'' +
        ", street='" + getStreet() + '\'' +
        ", pobox='" + getPobox() + '\'' +
        ", neighborhood='" + getNeighborhood() + '\'' +
        ", city='" + getCity() + '\'' +
        ", region='" + getRegion() + '\'' +
        ", postcode='" + getPostcode() + '\'' +
        ", country='" + getCountry() + '\'' +
        ", formattedAddress='" + getFormattedAddress() + '\'' +
        '}';
  }
}
