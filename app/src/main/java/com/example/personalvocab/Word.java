package com.example.personalvocab;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class Word implements Parcelable{
    String soz;
    String kontent;

    Timestamp timestamp;

    public Word() {
    }

    public String getSoz() {
        return soz;
    }

    public void setSoz(String soz) {
        this.soz = soz;
    }

    public String getKontent() {
        return kontent;
    }

    public void setKontent(String kontent) {
        this.kontent = kontent;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    // Parcelable implementation
    protected Word(Parcel in) {
        soz = in.readString();
        kontent = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(soz);
        dest.writeString(kontent);
        dest.writeParcelable(timestamp, flags);
    }
}
