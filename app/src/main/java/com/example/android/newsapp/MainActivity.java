package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * URL for news data from the the Guardian dataset
     */
    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search?from-date=2018-03-01&order-by=newest&use-date=published&show-tags=contributor&show-fields=all&page-size=20&q=news%20AND%20Poland&api-key=test";

    /**
     * Constant value for the article loader ID.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * Adapter for the list of articles
     */
    private NewsAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = findViewById(R.id.list);

        mEmptyList = findViewById(R.id.no_content);
        articleListView.setEmptyView(mEmptyList);

        // Create a new adapter that takes an empty list of articles as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected article.
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current article that was clicked on
                News currentArticle = mAdapter.getItem(position);

                if (currentArticle != null) {

                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri articleUri = Uri.parse(currentArticle.getArticleUrl());

                    // Create a new intent to view the article URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);

                }
            }
        });

        setLoader();
    }

    private void setLoader() {

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm !=null) {

            // Get details on the currently active default data network
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            // If there is a network connection, fetch data
            if (networkInfo != null && networkInfo.isConnected()) {
                // Get a reference to the LoaderManager, in order to interact with loaders.
                LoaderManager lm = getLoaderManager();

                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                lm.initLoader(NEWS_LOADER_ID, null, this);
            } else {
                // Otherwise, display error
                // First, hide loading indicator so error message will be visible
                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.GONE);

                // Update empty state with no connection error message
                mEmptyList.setText(getResources().getString(R.string.no_connection));
            }
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> articles) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No articles found."
        mEmptyList.setText(getResources().getString(R.string.no_articles));

        // Clear the adapter of previous article data
        mAdapter.clear();

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
