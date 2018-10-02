package com.wafflecopter.multicontactpicker.RxContacts;

import android.os.Parcel;
import android.os.Parcelable;

public class PhoneNumber implements Parcelable {

    private String typeLabel;
    private String number;

    PhoneNumber(String typeLabel, String number) {
        this.typeLabel = typeLabel;
        this.number = number;
    }

    private PhoneNumber(Parcel in) {
        this.typeLabel = in.readString();
        this.number = in.readString();
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.typeLabel);
        dest.writeString(this.number);
    }

    public static final Creator<PhoneNumber> CREATOR = new Creator<PhoneNumber>() {
        @Override
        public PhoneNumber createFromParcel(Parcel in) {
            return new PhoneNumber(in);
        }

        @Override
        public PhoneNumber[] newArray(int size) {
            return new PhoneNumber[size];
        }
    };
}
