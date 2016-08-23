package app.com.example.samitabh.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by SAmitabh on 05-07-2016.
 */
public class MovieProvider extends ContentProvider {
    private final String LOG_TAG=MovieProvider.class.getSimpleName();

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenDBHelper;
    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 200 ;
    static final int MOVIE_IF_FAVOURITE = 300;
    static final int FAVOURITE_MOVIES = 400;
    static final int FAVOURITE_WITH_ID = 500;



    static  final  UriMatcher buildUriMatcher(){
      final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
      final String AUTHORITY = MovieContract.CONTENT_AUTHORITY;
        sUriMatcher.addURI(AUTHORITY,MovieContract.PATH_MOVIES,MOVIE);
        sUriMatcher.addURI(AUTHORITY,MovieContract.PATH_FAVOURITES,FAVOURITE_MOVIES);
        sUriMatcher.addURI(AUTHORITY,MovieContract.PATH_MOVIES + "/#",MOVIE_WITH_ID);
        sUriMatcher.addURI(AUTHORITY,MovieContract.PATH_FAVOURITES + "/#",FAVOURITE_WITH_ID);
        sUriMatcher.addURI(AUTHORITY,MovieContract.PATH_MOVIES + "/*",MOVIE_IF_FAVOURITE);


      return sUriMatcher;
    }
    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            case (MOVIE) :
                Log.e(LOG_TAG,"Inside movie to query");
              retCursor = mOpenDBHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,projection,selection,
                      selectionArgs,null,null,sortOrder);
                break;
            case (FAVOURITE_MOVIES) :
                retCursor = mOpenDBHelper.getReadableDatabase().query(MovieContract.FavouriteEntry.TABLE_NAME,projection,selection,
                        selectionArgs,null,null,sortOrder);
                break;
            case (MOVIE_WITH_ID) :
                Log.e(LOG_TAG,"Inside movie_with_id to query");
                int id = MovieContract.MovieEntry.getMovieIDFromUri(uri);
                selection = MovieContract.MovieEntry.MOVIE_ID + " = ?";
                selectionArgs = new String[] {Integer.toString(id)};
                retCursor = mOpenDBHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,projection,selection,
                        selectionArgs,null,null,sortOrder);
                break;
            case (FAVOURITE_WITH_ID) :
                //Log.e(LOG_TAG,"Inside fav_with_id to query");
                Log.e(LOG_TAG,"uri fa: "+uri.toString());
                int fav_id = MovieContract.FavouriteEntry.getFavouriteIDFromUri(uri);

                selection = MovieContract.FavouriteEntry.MOVIE_ID + " = ?";
                selectionArgs = new String[] {Integer.toString(fav_id)};
                retCursor = mOpenDBHelper.getReadableDatabase().query(MovieContract.FavouriteEntry.TABLE_NAME,projection,selection,
                        selectionArgs,null,null,sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenDBHelper.getWritableDatabase();
        Uri retUri;
        long id;
        switch(sUriMatcher.match(uri)){
            case (MOVIE) :
                id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);
                if(id > 0){
                    retUri = MovieContract.MovieEntry.buildMovieUriwith_ID(id);
                }
                break;
            case (FAVOURITE_MOVIES) :
                id = db.insert(MovieContract.FavouriteEntry.TABLE_NAME,null,values);
                if(id > 0){
                    retUri = MovieContract.FavouriteEntry.buildFavouriteUriwith_ID(id);
                }
                break;
            case (MOVIE_WITH_ID) :

                id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);
                break;
            case (FAVOURITE_WITH_ID) :

                id = db.insert(MovieContract.FavouriteEntry.TABLE_NAME,null,values);
                break;

            case (MOVIE_IF_FAVOURITE) :
                id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }
        if(id > 0){
            retUri = MovieContract.MovieEntry.buildMovieUriwith_ID(id);
        }
        else throw new android.database.SQLException("Failed to insert row into " + uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenDBHelper.getWritableDatabase();
        int row_updated;
        switch(sUriMatcher.match(uri)){
            case (MOVIE) :
                row_updated = db.update(MovieContract.MovieEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case (FAVOURITE_MOVIES) :
                row_updated = db.update(MovieContract.FavouriteEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case (MOVIE_WITH_ID) :

                row_updated = db.update(MovieContract.MovieEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case (FAVOURITE_WITH_ID) :

                row_updated = db.update(MovieContract.FavouriteEntry.TABLE_NAME,values,selection,selectionArgs);
                break;

            case (MOVIE_IF_FAVOURITE) :
                row_updated = db.update(MovieContract.MovieEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }
        if(row_updated > 0) getContext().getContentResolver().notifyChange(uri, null);
        return row_updated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int insert_count = 0;
        final SQLiteDatabase db = mOpenDBHelper.getWritableDatabase();
        switch ((sUriMatcher.match(uri))){
            case MOVIE:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        insert_count++;
                    }
                    db.setTransactionSuccessful();
                }
                finally{
                    db.endTransaction();
                }
                if(insert_count > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                    }
                return insert_count;
            case FAVOURITE_MOVIES:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        db.insert(MovieContract.FavouriteEntry.TABLE_NAME, null, value);
                        insert_count++;
                    }
                    db.setTransactionSuccessful();
                }
                finally{
                    db.endTransaction();
                }
                if(insert_count > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return insert_count;

                    default:
                    return super.bulkInsert(uri, values);
        }




    }


    @Override
    public boolean onCreate() {
        mOpenDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenDBHelper.getWritableDatabase();
        int row_updated;
        switch(sUriMatcher.match(uri)){
            case (MOVIE) :
                row_updated = db.delete(MovieContract.MovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case (MOVIE_WITH_ID) :
                row_updated = db.delete(MovieContract.MovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case (FAVOURITE_WITH_ID) :
                row_updated = db.delete(MovieContract.FavouriteEntry.TABLE_NAME,selection,selectionArgs);
                break;

            case (MOVIE_IF_FAVOURITE) :
                row_updated = db.delete(MovieContract.MovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }
        if(row_updated > 0) getContext().getContentResolver().notifyChange(uri, null);
        return row_updated;
    }
}
