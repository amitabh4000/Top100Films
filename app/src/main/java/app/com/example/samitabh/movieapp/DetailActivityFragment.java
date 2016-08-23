package app.com.example.samitabh.movieapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.com.example.samitabh.movieapp.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    static final String LOG_TAG  = DetailActivityFragment.class.getSimpleName();
    final int LOADER_ID = 999;
    Intent intent;
    boolean is_favourite = false;
    public DetailActivityFragment() {
    }
    private final String[] MOVIE_COLUMNS ={
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.MOVIE_ID,
            MovieContract.MovieEntry.ORIGINAL_TITLE,
            MovieContract.MovieEntry.OVERVIEW,
            MovieContract.MovieEntry.POSTER_PATH,
            MovieContract.MovieEntry.BACKDROP_PATH,
            MovieContract.MovieEntry.POPULARITY,
            MovieContract.MovieEntry.RATING,
            MovieContract.MovieEntry.RELEASE_DATE,
            MovieContract.MovieEntry.GENRE
    };
    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_ORIGINAL_TITLE = 2;
    static final int COL_OVERVIEW = 3;
    static final int COL_POSTER_PATH = 4;
    static final int COL_BACKDROP_PATH = 5;
    static final int COL_POPULARITY = 6;
    static final int COL_RATING = 7;
    static final int COL_RELEASE_DATE =8;
    static final int COL_GENRE =9;


    ////// DECLARE AND INITIALIZE THE MOVIE PARAMETERS /////////
    int movie_id =0;
    String original_title = "";
    String overview ="";
    double popularity = 0.0;
    double rating = 0.0;
    String posterpath ="";
    String backdroppath ="";
    String release_date ="";
    String genre ="";



    /////  DECLARE THE VIEWS //////////
    TextView titleView;
    TextView descriptionView;
    TextView release_dateView;
    TextView ratingView;
    TextView popularityView;
    TextView genreView;
    ImageView backgroundView;
    TextView favouriteView;


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        intent = getActivity().getIntent();
        if(intent ==null || intent.getData() == null){
            return null;
        }
        //from_favourite_view = intent.getBooleanExtra("fav or not",true);
        return new CursorLoader(getActivity(),intent.getData(),MOVIE_COLUMNS,null,null,null);
    }

    @Override

    public void onLoadFinished(Loader loader, Cursor data) {

        //titleView.setText(intent.getData().toString());
        if (!data.moveToFirst()) {
            Log.e(LOG_TAG, "Empty Cursor");
            return;
        } else {
            movie_id = data.getInt(COL_MOVIE_ID);

            original_title = data.getString(COL_ORIGINAL_TITLE);
            titleView.setText(original_title);

            overview = data.getString(COL_OVERVIEW);
            descriptionView.setText(overview );

            release_date = data.getString(COL_RELEASE_DATE);
            release_dateView.setText(Utilities.getFriendlyReleaseDate( release_date ));

            rating = data.getDouble(COL_RATING);
            ratingView.setText(Utilities.getRating(rating, getActivity()));

            popularity = data.getDouble(COL_POPULARITY);
            popularityView.setText(Utilities.getPopularity(popularity, getActivity()));

            genre = data.getString(COL_GENRE);
            genreView.setText(Utilities.getGenre(genre, getActivity()));

            posterpath =data.getString(COL_POSTER_PATH);
            backdroppath = data.getString(COL_BACKDROP_PATH);
            Picasso.with(getActivity())
                    .load(posterpath)
                    .resize(600, 900)
                    .into(backgroundView);

            Uri uri = MovieContract.FavouriteEntry.buildFavouriteUriwith_ID(movie_id);
            String[] projection = new String[] {Integer.toString(movie_id)};
            String selection =  MovieContract.FavouriteEntry.MOVIE_ID + " =? ";
            String[] selectionargs = new String[] {Integer.toString(movie_id)};
            Cursor cursor = getActivity().getContentResolver()
                    .query(uri,projection,selection,selectionargs,null);
            if(cursor.moveToFirst()){
                is_favourite = true;
                favouriteView.setText("Added as favourite");
                favouriteView.setBackgroundColor(getResources().getColor(R.color.maroon));
            }
            else{
                is_favourite = false;
                favouriteView.setText("Add as favourite");
                favouriteView.setBackgroundColor(getResources().getColor(R.color.dark_green));
            }

        }
    }
    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID,null ,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //getActivity().getActionBar().hide();

        View rootView=inflater.inflate(R.layout.fragment_movie_detail, container, false);

         titleView = (TextView) rootView.findViewById(R.id.movie_name_textview);

         descriptionView = (TextView) rootView.findViewById(R.id.movie_description);

         release_dateView = (TextView) rootView.findViewById(R.id.movie_Release_Date);

         ratingView = (TextView) rootView.findViewById(R.id.movie_Rating);

         popularityView = (TextView) rootView.findViewById(R.id.movie_Popularity);

         genreView = (TextView) rootView.findViewById(R.id.movie_genre);

         backgroundView = (ImageView) rootView.findViewById(R.id.movie_background_poster);

         favouriteView = (TextView) rootView.findViewById(R.id.movie_favourite);

         favouriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = MovieContract.FavouriteEntry.buildFavouriteUriwith_ID(movie_id);
                String[] projection = new String[] {Integer.toString(movie_id)};
                String selection = MovieContract.FavouriteEntry.MOVIE_ID + " =? ";
                String[] selectionargs = new String[] {Integer.toString(movie_id)};
                if(is_favourite){
                    is_favourite = !is_favourite;
                    int delete_row = getActivity().getContentResolver().delete(uri ,selection,selectionargs);
                    favouriteView.setText("Add as favourite");
                    favouriteView.setBackgroundColor(getResources().getColor(R.color.dark_green));
                }
                else{
                    is_favourite = !is_favourite;
                    ContentValues favValues = new ContentValues();
                    favValues.put(MovieContract.FavouriteEntry.MOVIE_ID, movie_id);
                    favValues.put(MovieContract.FavouriteEntry.ORIGINAL_TITLE, original_title);
                    favValues.put(MovieContract.FavouriteEntry.OVERVIEW, overview);
                    favValues.put(MovieContract.FavouriteEntry.POPULARITY, popularity);
                    favValues.put(MovieContract.FavouriteEntry.POSTER_PATH, posterpath);
                    favValues.put(MovieContract.FavouriteEntry.RATING, rating);
                    favValues.put(MovieContract.FavouriteEntry.BACKDROP_PATH, backdroppath);
                    favValues.put(MovieContract.FavouriteEntry.RELEASE_DATE, release_date);
                    favValues.put(MovieContract.FavouriteEntry.GENRE, genre);
                    getActivity().getContentResolver().insert(uri,favValues);
                    favouriteView.setText("Added as favourite");
                    favouriteView.setBackgroundColor(getResources().getColor(R.color.maroon));
                }
            }
        });



        return rootView;
    }
}
