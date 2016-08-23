package app.com.example.samitabh.movieapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by SAmitabh on 29-06-2016.
 */
public class MovieContract {

    private final String LOG_TAG=MovieContract.class.getSimpleName();


    public static  final String CONTENT_AUTHORITY = "app.com.example.samitabh.movieapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_FAVOURITES = "favourites";

    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_TYPE =
                        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;



        public static final String TABLE_NAME = "PoularMovies";

        public static final String MOVIE_ID = "movie_id";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String OVERVIEW = "overview";
        public static final String POPULARITY = "popularity";
        public static final String RATING = "rating";
        public static final String POSTER_PATH = "poster_path";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String RELEASE_DATE ="release_date";
        public static final String GENRE ="genre";

        public static  Uri buildmovieUri(){
            return CONTENT_URI;
        }

        public static  Uri buildUriWithMovieIDUri(int movie_id){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(movie_id)).build();
        }
        public static int getMovieIDFromUri( Uri uri ){
            return ( Integer.parseInt( uri.getPathSegments().get(1) ) );
        }
        public static Uri buildMovieUriwith_ID (long id){
             return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }
    public static final class FavouriteEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;



        public static final String TABLE_NAME = "FavouriteTable";

        public static final String MOVIE_ID = "movie_id";
        public static final String ORIGINAL_TITLE = "original_title";
        public static final String OVERVIEW = "overview";
        public static final String POPULARITY = "popularity";
        public static final String RATING = "rating";
        public static final String POSTER_PATH = "poster_path";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String RELEASE_DATE ="release_date";
        public static final String GENRE ="genre";

        public static  Uri buildFavouriteUri(){
            return CONTENT_URI;
        }

        public static  Uri buildFavouriteWithIDUri(){
            return CONTENT_URI.buildUpon().appendPath(MOVIE_ID).build();
        }
        public static int getFavouriteIDFromUri( Uri uri ){
            return ( Integer.parseInt( uri.getPathSegments().get(1) ) );
        }
        public static Uri buildFavouriteUriwith_ID (long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

    }


}
