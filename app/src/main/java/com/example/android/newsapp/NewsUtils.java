package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class NewsUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = NewsUtils.class.getSimpleName();

    /**
     * Tags for nodes
     */
    private static final String KEY_RESPONSE = "response";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_FIELDS = "fields";

    /**
     * Tags for target keys
     */
    private static final String KEY_SECTION = "sectionName";
    private static final String KEY_DATE = "webPublicationDate";
    private static final String KEY_TITLE = "webTitle";
    private static final String KEY_URL = "webUrl";
    private static final String KEY_TRAIL_TEXT = "trailText";
    private static final String KEY_THUMBNAIL = "thumbnail";

    private NewsUtils() {
    }

    /**
     * Query the Guardian API and return a list of {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {

            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {

            Log.e(LOG_TAG, "InputStream closing problem", e);

        }

        // Extract relevant fields from the JSON response and return the list of {@link News}s
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {

            url = new URL(stringUrl);

        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "URL building problem", e);

        }

        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {

                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());

            }

        } catch (IOException e) {

            Log.e(LOG_TAG, "JSON results retrieving problem", e);

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {

                output.append(line);
                line = reader.readLine();

            }
        }

        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String articleJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articleJSON)) {
            return Collections.emptyList();
        }

        // Create an empty ArrayList that we can start adding articles to
        List<News> articles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject rootJsonObject = new JSONObject(articleJSON);

            JSONObject responseJsonObject = rootJsonObject.getJSONObject(KEY_RESPONSE);

            // Extract the JSONArray associated with the key called "results"
            JSONArray resultsArray = responseJsonObject.getJSONArray(KEY_RESULTS);

            String author = null;
            String trailText = null;
            String thumbnailUrl = null;

            // Iterating through resultsArray, creating an (@link News) object
            for (int i = 0; i < resultsArray.length(); i++) {

                // Get a single article at position i within the list of articles
                JSONObject currentArticle = resultsArray.getJSONObject(i);

                // Extract the value for the key called "sectionName"
                String category = currentArticle.optString(KEY_SECTION);

                // Extract the value for the key called "webPublicationDate"
                String date = currentArticle.optString(KEY_DATE);

                // Extract the value for the key called "webTitle"
                String title = currentArticle.optString(KEY_TITLE);

                // Extract the value for the key called "webUrl"
                String url = currentArticle.optString(KEY_URL);

                if (currentArticle.has(KEY_TAGS)) {

                    // Extract the next JSONArray associated with the key called "tags"
                    JSONArray tagsArray = currentArticle.getJSONArray(KEY_TAGS);

                    // Extract the JSONObject associated with first position of the tagsArray
                    JSONObject tagsObject = tagsArray.getJSONObject(0);

                    if (tagsObject.has(KEY_TITLE)) {

                        // Extract the value for the key called "webTitle"
                        author = tagsObject.getString(KEY_TITLE);

                    }
                }

                if (currentArticle.has(KEY_FIELDS)) {

                    // Extract the JSONObject associated with the key called "fields"
                    JSONObject fieldsObject = currentArticle.getJSONObject(KEY_FIELDS);

                    if (fieldsObject.has(KEY_TRAIL_TEXT)) {

                        // Extract the value for the key called "trailText"
                        trailText = fieldsObject.getString(KEY_TRAIL_TEXT);

                        // Extract the value for the key called "thumbnail"
                        thumbnailUrl = fieldsObject.getString(KEY_THUMBNAIL);

                    }
                }

                // Create a new {@link News} object with the category, date, title,
                // url, author, trail text and thumbnail url from the JSON response.

                News article = new News(category, date, title, url, author, trailText, thumbnailUrl);

                articles.add(article);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "JSON results parsing problem", e);
        }

        // Return the list of articles
        return articles;
    }
}
