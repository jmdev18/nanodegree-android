package com.typingeek.xadahiya.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by xadahiya on 8/16/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.typingeek.xadahiya.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);

    public static final String PATH_FMOVIE = "fmovies";

    /* Inner class that defines the table contents of the location table */
    public static final class FavouriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FMOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FMOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FMOVIE;

        // Table name
        public static final String TABLE_NAME = "fmovies";


        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_ISADULT = "isAdult";
        public static final String COLUMN_BACKDROP_URL = "backdrop_url";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_BACKDROP_IMAGE = "backdrop_image";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
