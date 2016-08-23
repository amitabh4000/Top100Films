package app.com.example.samitabh.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SAmitabh on 05-07-2016.
 */
public class MovieDBHelper extends SQLiteOpenHelper {
     final static String LOG_TAG = MovieDBHelper.class.getSimpleName();
     public static final int DATABASE_VERSION = 2;
     public static final String DATABASE_NAME = "Popular_movie.db";


    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
   final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "( " +
           MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
           MovieContract.MovieEntry.MOVIE_ID + " INTEGER NOT NULL, " +
           MovieContract.MovieEntry.ORIGINAL_TITLE + " TEXT NOT NULL, " +
           MovieContract.MovieEntry.OVERVIEW + " TEXT , " +
           MovieContract.MovieEntry.POSTER_PATH + " TEXT , " +
           MovieContract.MovieEntry.BACKDROP_PATH + " TEXT , " +
           MovieContract.MovieEntry.POPULARITY + " REAL , " +
           MovieContract.MovieEntry.RATING + " REAL , " +
           MovieContract.MovieEntry.RELEASE_DATE + " TEXT , "+
           MovieContract.MovieEntry.GENRE + " TEXT, "+
           "UNIQUE (" + MovieContract.MovieEntry.MOVIE_ID + " , " + MovieContract.MovieEntry.ORIGINAL_TITLE +
           ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);


        final String SQL_CREATE_FAVOURITE_TABLE = "CREATE TABLE " + MovieContract.FavouriteEntry.TABLE_NAME + "( " +
                MovieContract.FavouriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.FavouriteEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.FavouriteEntry.ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.FavouriteEntry.OVERVIEW + " TEXT , " +
                MovieContract.FavouriteEntry.POSTER_PATH + " TEXT , " +
                MovieContract.FavouriteEntry.BACKDROP_PATH + " TEXT , " +
                MovieContract.FavouriteEntry.POPULARITY + " REAL , " +
                MovieContract.FavouriteEntry.RATING + " REAL , " +
                MovieContract.FavouriteEntry.RELEASE_DATE + " TEXT , "+
                MovieContract.FavouriteEntry.GENRE + " TEXT, "+
                "UNIQUE (" + MovieContract.FavouriteEntry.MOVIE_ID + " , " + MovieContract.FavouriteEntry.ORIGINAL_TITLE +
                ") ON CONFLICT REPLACE);";


        db.execSQL(SQL_CREATE_FAVOURITE_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavouriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
