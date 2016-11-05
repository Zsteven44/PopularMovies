package com.stevenzafrani.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.stevenzafrani.android.popularmovies.R.layout.fragment_details;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {
    private static final String LOG_TAG = DetailsActivityFragment.class.getSimpleName();


    public DetailsActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(fragment_details, container, false);

        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra("movie")) {
            Movie thisMovie = intent.getParcelableExtra("movie");

            TextView title = (TextView) rootView.findViewById(R.id.item_title);
            title.setText(thisMovie.movieName);

            TextView synopsis = (TextView) rootView.findViewById(R.id.item_synopsis);
            synopsis.setText(thisMovie.movieSynopsis);

            TextView rating = (TextView) rootView.findViewById(R.id.item_rating);
            rating.setText(String.valueOf(thisMovie.movieRating));

            TextView release = (TextView) rootView.findViewById(R.id.item_release);
            release.setText(thisMovie.movieReleaseDate);

            ImageView image = (ImageView) rootView.findViewById(R.id.item_thumbnail);
            Picasso.with(getContext()).load(thisMovie.moviePoster).into(image);

        }

        return rootView;
    }
}
