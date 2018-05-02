package com.example.android.newsapp;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {

    //creator - used when un-parceling our parcel (creating the object)
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

    private final String sectionName;

    private final String datePublished;

    private final String articleTitle;

    private final String articleUrl;

    private final String articleAuthor;

    private final String trailText;

    private final String thumbnailUrl;

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

        this.sectionName = sectionName;
        this.datePublished = datePublished;
        this.articleTitle = articleTitle;
        this.articleUrl = articleUrl;
        this.articleAuthor = articleAuthor;
        this.trailText = trailText;
        this.thumbnailUrl = thumbnailUrl;

    }

    //read and set saved values from parcel
    private News(Parcel parcel) {

        sectionName = parcel.readString();
        datePublished = parcel.readString();
        articleTitle = parcel.readString();
        articleUrl = parcel.readString();
        articleAuthor = parcel.readString();
        trailText = parcel.readString();
        thumbnailUrl = parcel.readString();

    }

    /**
     * Get the string resource ID for article's section name
     */
    public String getSectionName() {
        return sectionName;
    }

    /**
     * Get the string resource ID for the article's date od publication
     */
    public String getDatePublished() {
        return datePublished;
    }

    /**
     * Get the string resource ID for the article's title
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     * Get the string resource ID for the article's url
     */
    public String getArticleUrl() {
        return articleUrl;
    }

    /**
     * Get the string resource ID for the article's author
     */
    public String getArticleAuthor() {
        return articleAuthor;
    }

    /**
     * Get the string resource ID for the article's author
     */
    public String getTrailText() {
        return trailText;
    }

    /**
     * Get the string resource ID for the article's author
     */
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    //write the values to parcel for storage
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(sectionName);
        dest.writeString(datePublished);
        dest.writeString(articleTitle);
        dest.writeString(articleUrl);
        dest.writeString(articleAuthor);
        dest.writeString(trailText);
        dest.writeString(thumbnailUrl);

    }

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }
}