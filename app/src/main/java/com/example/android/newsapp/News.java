package com.example.android.newsapp;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {

    //creator - used when un-parceling our parcle (creating the object)
    public static final Creator<News> CREATOR = new Creator<News>() {

        @Override
        public News createFromParcel(Parcel parcel) {
            return new News(parcel);
        }

        @Override
        public News[] newArray(int size) {
            return new News[0];
        }
    };

    private final String mSectionName;

    private final String mDatePublished;

    private final String mArticleTitle;

    private final String mArticleUrl;

    private final String mArticleAuthor;

    private final String mTrailText;

    private final String mThumbnailUrl;

    /**
     * Constructs a new {@link News} object.
     *
     * @param sectionName   is the name of the news category
     * @param datePublished is the article's publication date
     * @param articleTitle  is the article's title
     * @param articleUrl    is the web address of the article
     * @param articleAuthor is the name of the article's author
     * @param trailText     is the name of the sneak peak text
     * @param thumbnailUrl  is the web address of the thumbnail image
     */
    News(String sectionName, String datePublished, String articleTitle, String articleUrl, String articleAuthor, String trailText, String thumbnailUrl) {

        mSectionName = sectionName;
        mDatePublished = datePublished;
        mArticleTitle = articleTitle;
        mArticleUrl = articleUrl;
        mArticleAuthor = articleAuthor;
        mTrailText = trailText;
        mThumbnailUrl = thumbnailUrl;

    }

    //read and set saved values from parcel
    private News(Parcel parcel) {

        mSectionName = parcel.readString();
        mDatePublished = parcel.readString();
        mArticleTitle = parcel.readString();
        mArticleUrl = parcel.readString();
        mArticleAuthor = parcel.readString();
        mTrailText = parcel.readString();
        mThumbnailUrl = parcel.readString();

    }

    /**
     * Get the string resource ID for article's section name
     */
    public String getSectionName() {
        return mSectionName;
    }

    /**
     * Get the string resource ID for the article's date od publication
     */
    public String getDatePublished() {
        return mDatePublished;
    }

    /**
     * Get the string resource ID for the article's title
     */
    public String getArticleTitle() {
        return mArticleTitle;
    }

    /**
     * Get the string resource ID for the article's url
     */
    public String getArticleUrl() {
        return mArticleUrl;
    }

    /**
     * Get the string resource ID for the article's author
     */
    public String getArticleAuthor() {
        return mArticleAuthor;
    }

    /**
     * Get the string resource ID for the article's author
     */
    public String getTraiText() {
        return mTrailText;
    }

    /**
     * Get the string resource ID for the article's author
     */
    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    //write the values to parcel for storage
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mSectionName);
        dest.writeString(mDatePublished);
        dest.writeString(mArticleTitle);
        dest.writeString(mArticleUrl);
        dest.writeString(mArticleAuthor);
        dest.writeString(mTrailText);
        dest.writeString(mThumbnailUrl);

    }

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }
}