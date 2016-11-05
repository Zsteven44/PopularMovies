package com.stevenzafrani.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zsteven44 on 10/30/16.
 */

public class Movie implements Parcelable{
    String movieName;
    String movieSynopsis;
    float movieRating;
    String movieReleaseDate;
    String movieThumbnail;
    String moviePoster;


    public Movie(String name, String releaseDate, float rating, String synopsis,
                 String thumbnail, String poster) {
        this.movieName = name;
        this.movieSynopsis = synopsis;
        this.movieRating = rating;
        this.movieReleaseDate = releaseDate;
        this.movieThumbnail = thumbnail;
        this.moviePoster = poster;
    }

    private Movie(Parcel in) {
        movieName = in.readString();
        movieSynopsis = in.readString();
        movieRating = in.readFloat();
        movieReleaseDate = in.readString();
        movieThumbnail = in.readString();
        moviePoster = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return movieName + "-- Release: " + movieReleaseDate + " -- Rating: " + movieRating + " -- " +movieThumbnail;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieName);
        parcel.writeString(movieSynopsis);
        parcel.writeFloat(movieRating);
        parcel.writeString(movieReleaseDate);
        parcel.writeString(movieThumbnail);
        parcel.writeString(moviePoster);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }
        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };

}
