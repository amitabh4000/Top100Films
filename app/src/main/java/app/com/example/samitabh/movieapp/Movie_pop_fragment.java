package app.com.example.samitabh.movieapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import app.com.example.samitabh.movieapp.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class Movie_pop_fragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int LOADER_ID = 888;
    private int count = 0;
    private String present_sort_order = "";
    private boolean fav_view_on = false;
    public Movie_pop_fragment() {

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
    };
     static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_ORIGINAL_TITLE = 2;
    static final int COL_OVERVIEW = 3;
    static final int COL_POSTERPATH = 4;
    static final int COL_BACKDROP_PATH = 5;
    static final int COL_POPULARITY = 6;
    static final int COL_RATING = 7;





    private final String LOG_TAG=Movie_pop_fragment.class.getSimpleName();
    public GridView mGridView;
    public MovieAdapter movieAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID,null ,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        present_sort_order = Utilities.getSortOrderPreference(getActivity());
        String choice = present_sort_order
                                    .equals(getActivity().getString(R.string.pref_sort_by_popularity))
                                    ? getActivity().getString(R.string.pref_popularity_col_name)
                                    : getActivity().getString(R.string.pref_rating_col_name) ;
        String sortorder = new StringBuilder().append(choice)
                                              .append(" ")
                                              .append(getActivity().getString(R.string.pref_descending_order))
                                              .toString();

        if(!fav_view_on) {

            Uri movieuri = MovieContract.MovieEntry.buildmovieUri();

            return new CursorLoader(getActivity(), movieuri, MOVIE_COLUMNS, null, null, sortorder);
        }
        else{
            Uri favuri = MovieContract.FavouriteEntry.buildFavouriteUri();

            return new CursorLoader(getActivity(), favuri, MOVIE_COLUMNS, null, null, sortorder);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
     movieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
     movieAdapter.swapCursor(null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//        c.moveToFirst();
//        while(!c.isAfterLast()){
//            Log.e(LOG_TAG,"The tables are: "+c.getString(0));
//        }
        Utilities.setGodfatherAsFav(getActivity());
        present_sort_order = getActivity().getString(R.string.pref_sort_by_popularity);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieAdapter = new MovieAdapter(getActivity(),null,0);
        mGridView =(GridView) rootView.findViewById(R.id.grid_view_main);
        mGridView.setAdapter(movieAdapter);

        if(mGridView==null){
            Log.v(LOG_TAG,"gridview is null");
        }
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int position, long id) {


                Cursor cursor=(Cursor) adapterView.getItemAtPosition(position);
                if(cursor != null) {
                    Intent intent = new Intent(getActivity(), MovieDetail.class);
                    if(!fav_view_on)
                        intent.setData(MovieContract.MovieEntry.buildUriWithMovieIDUri(cursor.getInt(COL_MOVIE_ID)));
                    else
                        intent.setData(MovieContract.FavouriteEntry.buildFavouriteUriwith_ID(cursor.getInt(COL_MOVIE_ID)));
                    intent.putExtra("fav or not",fav_view_on);
                    if (intent != null)
                        startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_frag,menu);
        SpannableString span = new SpannableString("Show Favourites:OFF");
        menu.getItem(0).setTitle(span);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        if(count < 1 ) {
            count++;
            getMoviesFromAPI getMovies = new getMoviesFromAPI(getActivity());
            getMovies.execute();
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        String cursort = Utilities.getSortOrderPreference(getActivity());
        if( !cursort.equals(present_sort_order)){
            getLoaderManager().restartLoader(LOADER_ID,null,this);
            present_sort_order=cursort;
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final MenuItem item_selected = item;
        switch(item.getItemId()){

            case R.id.action_refresh:
                getMoviesFromAPI getMovies=new getMoviesFromAPI(getActivity());
                getMovies.execute();
                break;
            case R.id.action_setting:
                Intent intent = new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_fav:
                fav_view_on = !fav_view_on;
                SpannableString span;

                getLoaderManager().restartLoader(LOADER_ID,null,this);
        if(fav_view_on) {
                span = new SpannableString("Show favourites: ON");
                span.setSpan(new ForegroundColorSpan(Color.BLUE), 0, span.length(), 0);
        }
        else{
            span = new SpannableString("Show Favourites: OFF");
            span.setSpan(new ForegroundColorSpan(Color.BLUE), 0, span.length(), 0);
        }
                item_selected.setTitle(span);
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }




}
