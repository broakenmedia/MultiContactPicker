package com.wafflecopter.multicontactpicker.RxContacts;

public class ContactAddress {
  private int mType;
  private String mLabel;
  private String mStreet;
  private String mPobox;
  private String mNeighborhood;
  private String mCity;
  private String mRegion;
  private String mPostcode;
  private String mCountry;

  public ContactAddress() {}

  public ContactAddress(int type, String label, String street, String pobox, String neighborhood, String city, String region, String postcode, String country) {
    mType = type;
    mLabel = label;
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
}
