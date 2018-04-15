package com.example.android.firebaseupload.activities.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 21/03/2018.
 */

public class xprocure implements Parcelable {
    public xprocure(String pharmacyName, String practiceNo) {
        this.pharmacyName = pharmacyName;
        this.practiceNo = practiceNo;
    }

    public String getPharmacyName() {

        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getPracticeNo() {
        return practiceNo;
    }

    public void setPracticeNo(String practiceNo) {
        this.practiceNo = practiceNo;
    }

    private String pharmacyName;
    private String practiceNo;
    protected xprocure(Parcel in) {
        pharmacyName=in.readString();
        practiceNo=in.readString();
    }

    public static final Creator<xprocure> CREATOR = new Creator<xprocure>() {
        @Override
        public xprocure createFromParcel(Parcel in) {
            return new xprocure(in);
        }

        @Override
        public xprocure[] newArray(int size) {
            return new xprocure[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pharmacyName);
        dest.writeString(practiceNo);
    }
}
