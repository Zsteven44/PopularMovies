package com.stevenzafrani.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MovieAdapter movieAdapter;


    public MainActivityFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
           // movieArrayList = new ArrayList<Movie>();
        }
        else {
           // movieArrayList = savedInstanceState.getParcelableArrayList("movies");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
      //  outState.putParcelableArrayList("movies", movieArrayList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(movieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movieClick = movieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("movie", movieClick);

                startActivity(intent);
            }
        });

        return rootView;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void updateMovies() {
        FetchMovieTask fetchMovies = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
        fetchMovies.execute(sort_by);
    }

    @Override
    public void onStart() {
        super.onStart();
        movieAdapter.clear();
        updateMovies();
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();



        private Movie[] getMovieDataFromJSON(String movieJsonStr) throws JSONException {
            String OWM_RESULTS = "results";
            String OWM_POSTER_PATH = "poster_path";
            String OWM_OVERVIEW = "overview";
            String OWM_RELEASE = "release_date";
            String OWM_TITLE = "title";
            String OWM_RATING = "vote_average";
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);
            Movie[] movieList = new Movie[movieArray.length()];
            for (int i =0; i<movieArray.length(); i++){
                String movie_title;
                String movie_overview;
                String movie_release;
                float movie_rating;
                String movie_thumbnail_path;
                String movie_poster_path;
                String thumbnail_size = "w500/";
                String poster_size = "w500/";

                JSONObject movieItem = movieArray.getJSONObject(i);

                movie_title = movieItem.getString(OWM_TITLE);
                movie_overview = movieItem.getString(OWM_OVERVIEW);
                movie_release = movieItem.getString(OWM_RELEASE);
                movie_rating = (float) movieItem.getDouble(OWM_RATING);
                movie_thumbnail_path = "https://image.tmdb.org/t/p/"+ thumbnail_size + movieItem.getString(OWM_POSTER_PATH);
                movie_poster_path = "https://image.tmdb.org/t/p/"+poster_size + movieItem.getString(OWM_POSTER_PATH);


                movieList[i] = new Movie(movie_title,movie_release,movie_rating,movie_overview,movie_thumbnail_path, movie_poster_path);
            }
            for (Movie m: movieList) {
                Log.v(LOG_TAG, "Movie entry: " + m);
            }
            return movieList;
        }


        @Override
        protected Movie[] doInBackground(String... params) {

        // declared outside the try/catch block below so that they can be closed
        // in the finally block at the end.
            if (params.length == 0) {
                return null;
            }
            Log.v(LOG_TAG,"The params for sorting: " + params[0]);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJSONStr = null;
            String sortBy = params[0];


            try {
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String SORT_BY_PARAM = "sort_by";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath((params[0]))
                        .appendQueryParameter(APPID_PARAM, BuildConfig.MOVIESDB_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0){
                    return null;
                }
                movieJSONStr = buffer.toString();
                Log.v(LOG_TAG, "Movie String: " + movieJSONStr);


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJSON(movieJSONStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }




        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Movie[] result) {
            if (result != null) {
                movieAdapter.clear();
                for(Movie movie : result) {
                    movieAdapter.add(movie);

                }
                movieAdapter.notifyDataSetChanged();
            }
        }
    }
}
