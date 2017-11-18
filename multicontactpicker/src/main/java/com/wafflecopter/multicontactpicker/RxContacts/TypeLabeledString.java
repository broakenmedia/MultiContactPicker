package com.wafflecopter.multicontactpicker.RxContacts;


import android.content.res.Resources;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class TypeLabeledString implements TypeLabelInterface {
  int mType;
  String mLabel;
  String mValue;

  public TypeLabeledString() {

  }

  public TypeLabeledString(int type, String label, String value) {
    mType = type;
    mLabel = label;
    mValue = value;
  }

  public int getType() {
    return mType;
  }

  public void setType(int mType) {
    this.mType = mType;
  }

  public String getLabel() {
    return mLabel;
  }

  public void setLabel(String mLabel) {
    this.mLabel = mLabel;
  }

  public String getValue() {
    return mValue;
  }

  public void setValue(String mValue) {
    this.mValue = mValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TypeLabeledString that = (TypeLabeledString) o;

    return mType == that.mType && (mLabel != null ? mLabel.equals(that.mLabel)
        : that.mLabel == null && (mValue != null ? mValue.equals(that.mValue)
            : that.mValue == null));

  }

  @Override
  public int hashCode() {
    int result = mType;
    result = 31 * result + (mLabel != null ? mLabel.hashCode() : 0);
    result = 31 * result + (mValue != null ? mValue.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return getValue();
  }

  @Override
  public abstract CharSequence getTypeLabel(Resources resources);
}
