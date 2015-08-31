package com.example.saputro.volleysession;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.saputro.volleysession.com.example.saputro.library.MyApplication;
import com.example.saputro.volleysession.com.example.saputro.library.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ListBlogActivity extends ListActivity {

    ArrayList<HashMap<String, String>> blog_list;
    ProgressDialog pDialog;
    ListAdapter adapter;
    UserSessionManager session;

    //json node
    private static final String ITEM_ID = "id";
    private static final String ITEM_NAME = "judul";
    private static final String ITEM_KONTEN = "konten";
    private static final String ITEM_TANGGAL = "tanggal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent i = getIntent();
//        String id_user = i.getStringExtra("id_user");
//        Log.d("id_user", id_user);

        blog_list = new ArrayList<HashMap<String, String>>();

        session = new UserSessionManager(getApplicationContext());
        HashMap<String, String> detail = session.getUserDetail();

        String uId = detail.get(UserSessionManager.KEY_ID);

        Log.d("detail", detail.toString());

        pDialog = new ProgressDialog(ListBlogActivity.this);
        pDialog.setMessage("loading all your blog");
        pDialog.setCancelable(false);


        getListView().setOnItemClickListener(new ListitemClickListener());
        registerForContextMenu(getListView());
        allBlog(uId);


    }

    public void allBlog(String id_user) {
        pDialog.show();
        String url = "http://10.0.3.2/apigw/blog.php?user=" + id_user;
        JsonObjectRequest jReq = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        JSONArray blog = response.getJSONArray("blog");
                        for (int i = 0; i < blog.length(); i++) {
                            JSONObject jObj = blog.getJSONObject(i);
                            HashMap<String, String> item = new HashMap<String, String>();
                            item.put(ITEM_ID, jObj.getString(ITEM_ID));
                            item.put(ITEM_NAME, jObj.getString(ITEM_NAME));
                            item.put(ITEM_KONTEN,jObj.getString(ITEM_KONTEN));
                            item.put(ITEM_TANGGAL,jObj.getString(ITEM_TANGGAL));

                            blog_list.add(item);
                        }

                        String[] from = {ITEM_ID, ITEM_NAME};
                        int[] to = {R.id.blog_id, R.id.blog_title};

                        adapter = new SimpleAdapter(getApplicationContext(), blog_list, R.layout.list_blog_item, from, to);
                        setListAdapter(adapter);
                        pDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                } else if (error instanceof AuthFailureError) {
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                } else if (error instanceof NetworkError) {
                    //TODO
                } else if (error instanceof ParseError) {
                    //TODO
                }

            }
        });

        MyApplication.getInstance().addToReqQueue(jReq);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("select action");
        menu.add(0, v.getId(), 0, "update");
        menu.add(0, v.getId(), 0, "delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //return super.onContextItemSelected(item);

        if (item.getTitle() == "update") {
            Toast.makeText(getApplicationContext(), "you clicked at update " + item.getItemId(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "you clicked at delete", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    class ListitemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            Intent detail = new Intent(ListBlogActivity.this,
                    DetailActivity.class);

            detail.putExtra("id_blog", blog_list.get(position));

            startActivity(detail);

        }

    }


}
