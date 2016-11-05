package com.stevenzafrani.android.popularmovies;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Zsteven44 on 10/30/16.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        if (convertView ==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.grid_image);
        Picasso.with(getContext()).load(movie.movieThumbnail).into(image);
        // TextView title = (TextView) convertView.findViewById(R.id.grid_title);
        // title.setText(movie.movieName);

        return convertView;

    }

}
