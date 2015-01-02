package me.attwoodthomas.myblogreader;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity {
    private ProgressDialog pDialog;

    // URL to get posts JSON
    private static String url = "http://attwoodthomas.me/post_json";

    // JSON Node names
    private static final String TAG_RESULTS = "results";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_BODY = "body";
    private static final String TAG = "MainActivity";


    // posts JSONArray
    JSONArray posts = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> postList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postList = new ArrayList<HashMap<String, String>>();
        // Calling async task to get json
        new GetContacts().execute();
    }

    private void logExeception(Exception e) {
        Log.e(TAG, "Exeception Caught", e);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        try {
            HashMap<String, String> post = postList.get(position);
            String[] postArray = {post.get(TAG_TITLE), post.get(TAG_BODY)};
            Intent intent = new Intent(this, Post.class);
            intent.putExtra("postArray", postArray);
            startActivity(intent);

        } catch (Exception e) {
            logExeception(e);
        }
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    posts = jsonObj.getJSONArray(TAG_RESULTS);

                    // looping through All Contacts
                    for (int i = 0; i < posts.length(); i++) {
                        JSONObject c = posts.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String title = c.getString(TAG_TITLE);
                        String body = c.getString(TAG_BODY);




                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_TITLE, title);
                        contact.put(TAG_BODY, body);

                        // adding contact to contact list
                        postList.add(contact);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */


            String[] keys = {TAG_TITLE};
            int[] ids = {android.R.id.text1};
            SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, postList, android.R.layout.simple_list_item_1, keys, ids);


            setListAdapter(adapter);
        }

    }
}

