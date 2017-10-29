package com.example.android.bookListing;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kanav on 27/09/17.
 */

public class Book implements Parcelable {

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    private String mTitle;
    private String mAuthors;

    public Book(String title, String authors) {
        mTitle = title;
        mAuthors = authors;

    }

    private Book(Parcel in) {
        mTitle = in.readString();
        mAuthors = in.readString();

    }

    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return mTitle;
    }

    public String toString() {

        return " " + mTitle;

    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeString(mAuthors);
    }

    public String getAuthors() {
        return mAuthors;

    }

}
