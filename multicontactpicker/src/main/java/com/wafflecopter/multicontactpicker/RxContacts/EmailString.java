package com.wafflecopter.multicontactpicker.RxContacts;

import android.content.res.Resources;
import android.os.Parcel;
import android.provider.ContactsContract.CommonDataKinds.Email;


@SuppressWarnings({"unused", "WeakerAccess"})
public class EmailString extends TypeLabeledString implements android.os.Parcelable {

  public EmailString(int type, String label, String value) {
    super(type,label,value);
  }

  public CharSequence getTypeLabel(Resources resource) {
    return Email.getTypeLabel(resource, getType(), getLabel() );
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

  public EmailString() {
    super();
  }

  protected EmailString(Parcel in) {
    this.mType = in.readInt();
    this.mLabel = in.readString();
    this.mValue = in.readString();
  }

  public static final Creator<EmailString> CREATOR = new Creator<EmailString>() {
    @Override
    public EmailString createFromParcel(Parcel source) {
      return new EmailString(source);
    }

    @Override
    public EmailString[] newArray(int size) {
      return new EmailString[size];
    }
  };
}
