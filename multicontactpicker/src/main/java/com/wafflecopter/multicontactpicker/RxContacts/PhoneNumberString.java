package com.wafflecopter.multicontactpicker.RxContacts;

import android.content.res.Resources;
import android.os.Parcel;
import android.provider.ContactsContract.CommonDataKinds.Phone;


@SuppressWarnings({"unused", "WeakerAccess"})
public class PhoneNumberString extends TypeLabeledString implements android.os.Parcelable {

  public PhoneNumberString(int type, String label, String value) {
    super(type,label,value);
  }

  @Override
  public CharSequence getTypeLabel(Resources resources) {
    return Phone.getTypeLabel(resources, getType(), getLabel());
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.mType);
    dest.writeString(this.mLabel);
    dest.writeString(this.mValue);
  }

  public PhoneNumberString() {
    super();
  }

  protected PhoneNumberString(Parcel in) {
    this.mType = in.readInt();
    this.mLabel = in.readString();
    this.mValue = in.readString();
  }

  public static final Creator<PhoneNumberString> CREATOR = new Creator<PhoneNumberString>() {
    @Override
    public PhoneNumberString createFromParcel(Parcel source) {
      return new PhoneNumberString(source);
    }

    @Override
    public PhoneNumberString[] newArray(int size) {
      return new PhoneNumberString[size];
    }
  };
}
