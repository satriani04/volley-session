package com.example.saputro.volleysession;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.saputro.volleysession.com.example.saputro.library.CustomListAdapter;
import com.example.saputro.volleysession.com.example.saputro.library.Movie;
import com.example.saputro.volleysession.com.example.saputro.library.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class VolleyListActivity extends ActionBarActivity {


    private static final String url = "http://api.androidhive.info/json/movies.json";
    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_list);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("loading..");
        pDialog.show();

        //changing action bar color
        //getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b1b1b")));
//        getSupportActionBar().setBackgroundDrawable(
//                new ColorDrawable(Color.parseColor("#1b1b1b")));

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));

        JsonArrayRequest jreq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("respon", response.toString());
                hidePDialog();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jObj = response.getJSONObject(i);
                        Movie m = new Movie();
                        m.setTitle(jObj.getString("title"));
                        m.setThumbnailUrl(jObj.getString("image"));
                        m.setRating(((Number) jObj.get("rating")).doubleValue());
                        m.setYear(jObj.getInt("releaseYear"));

                        //genre is json array
                        JSONArray genreArr = jObj.getJSONArray("genre");
                        ArrayList<String> genre = new ArrayList<String>();
                        for (int j = 0; j < genreArr.length(); j++) {
                            genre.add((String) genreArr.get(j));
                        }

                        m.setGenre(genre);
                        movieList.add(m);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("errornya ", "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        MyApplication.getInstance().addToReqQueue(jreq);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


}
