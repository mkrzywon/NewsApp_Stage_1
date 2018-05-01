package com.example.android.newsapp;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.Collections;
import java.util.List;

/**
 * Loads a list of articles by using an AsyncTask to perform the
 * network request to the given URL.
 */
class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Query URL */
    private final String mArticleUrl;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    @SuppressWarnings("SameParameterValue")
    NewsLoader(Context context, String url) {
        super(context);
        mArticleUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground() {
        if (mArticleUrl == null) {
            return Collections.emptyList();
        }

        // Perform the network request, parse the response, and extract a list of articles.
        return NewsUtils.fetchNewsData(mArticleUrl);
    }
}
