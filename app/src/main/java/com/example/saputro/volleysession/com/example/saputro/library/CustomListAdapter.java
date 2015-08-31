package com.example.saputro.volleysession.com.example.saputro.library;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.saputro.volleysession.R;

import java.util.List;

/**
 * Created by saputro on 24/08/2015.
 */
public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Movie> movieItem;
    ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Movie> movieItem) {
        this.activity = activity;
        this.movieItem = movieItem;
    }

    @Override
    public int getCount() {
        return movieItem.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItem.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        NetworkImageView thumbnail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.titleMovie);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

        //getting movie data for the row
        Movie m = movieItem.get(position);

        //thumbnails image
        thumbnail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        //
        title.setText(m.getTitle());

        //rating
        rating.setText("Rating : " + String.valueOf(m.getRating()));

        //genre
        String strGenre = "";
        for (String str : m.getGenre()) {
            strGenre += str + ", ";
        }

        strGenre = strGenre.length() > 0 ? strGenre.substring(0,
                strGenre.length() - 2) : strGenre;
        genre.setText(strGenre);

        // release year
        year.setText(String.valueOf(m.getYear()));


        return convertView;

    }
}
