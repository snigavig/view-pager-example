package com.goodcodeforfun.viewpagerexample;

/**
 * Created by snigavig on 03.03.16.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public TextView mTextView;
    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mTextView = (TextView) rootView.findViewById(R.id.section_label);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        DownloadJSON task = new DownloadJSON();
        task.execute(DownloadJSON.BASE_URL + getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private class DownloadJSON extends AsyncTask<String, Void, String> {
        public static final String BASE_URL =
                "http://jsonplaceholder.typicode.com/posts/";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (null != mTextView) {
                mTextView.setText(getString(R.string.loading_message));
            }
        }

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            for (String url : urls) {
                URL obj;
                HttpURLConnection con;
                try {
                    obj = new URL(url);
                    con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");

                    int responseCode = con.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) { //success
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        result = response.toString();
                    } else {
                        Log.i(LOG_TAG, "request did not work.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return getDataFromJson(result);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (null != mTextView) {
                mTextView.setText(s);
            }
        }

        private String getDataFromJson(String jsonStr) {

            final String TITLE = "title";
            String title = "";
            try {
                JSONObject json = new JSONObject(jsonStr);
                title = json.getString(TITLE);

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return title;

        }
    }
}
