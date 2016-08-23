package app.com.example.samitabh.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.Vector;

import app.com.example.samitabh.movieapp.data.MovieContract;

/**
 * Created by SAmitabh on 13-06-2016.
 */
public class getMoviesFromAPI extends AsyncTask<Void,Void,Void> {
//
        private final String LOG_TAG=getMoviesFromAPI.class.getSimpleName();
        private ImageAdapter imageAdapter;
        private Context mContext;
        public getMoviesFromAPI(Context context){
            mContext=context;
        }
        private ArrayList<Movie_data_class> movieList=new ArrayList<>();

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection;
            BufferedReader reader;
            String JSONString;

            try {
                    getgenreId();
                    for(int i= 1 ; i <= 5 ; i++) {
                    extractJSON(i);
                }
            }
            catch(IOException e){
                Log.e(LOG_TAG,"IO exception",e);
            }
            catch(JSONException e){
                Log.e(LOG_TAG,"Json exception",e);
            }
            return null;
        }
        /////// Method to get total pages in JSON string////////////
        ////********************************************************//////
        public int getJSONPageNumber(String JSONString)throws JSONException{
            final String TOTAL_PAGES="total_pages";
            JSONObject movieJSONObject=new JSONObject(JSONString);
            int pages=Integer.parseInt(movieJSONObject.getString(TOTAL_PAGES));
            return pages;
        }

       ////////    Method to get genre API and fill a genre Map with it   ///////////////
      void getgenreId() throws IOException {
          HttpURLConnection urlConnection;
          BufferedReader reader;
          StringBuilder stringBuilder;

          String apikey = BuildConfig.POPULAR_MOVIES_API_KEY;
          ///// Get the list of movies String/////

          final String baseURL = "https://api.themoviedb.org/3";
          final String GENRE = "genre";
          final String MOVIE = "movie";
          final String LIST = "list";
          final String API_KEY = "api_key";

          Uri uri = Uri.parse(baseURL).buildUpon()
                  .appendPath(GENRE)
                  .appendPath(MOVIE)
                  .appendPath(LIST)
                  .appendQueryParameter(API_KEY, apikey)
                  .build();
          Log.e(LOG_TAG ,"The URI is: "+uri.toString());
          URL url = new URL(uri.toString());
          urlConnection = (HttpURLConnection) url.openConnection();
          urlConnection.setRequestMethod("GET");
          urlConnection.setDoOutput(false);
          urlConnection.connect();
          InputStream inputStream = urlConnection.getInputStream();
          if (inputStream == null) return;
          reader = new BufferedReader(new InputStreamReader(inputStream));
          stringBuilder = new StringBuilder();
          String inputString;
          while ((inputString = reader.readLine()) != null) {
              stringBuilder.append(inputString);
          }
          if (stringBuilder == null) {
              Log.e(LOG_TAG, "Unable to get JSON string for genre");
          } else {
              ////////// Parse the JSON obtained in the same function as it is quite small ///////
              try {
                  JSONObject genre_object = new JSONObject(stringBuilder.toString());
                  final String GENRES = "genres";
                  final String GENRE_ID = "id";
                  final String GENRE_NAME = "name";
                  JSONArray genre_array = genre_object.getJSONArray(GENRES);
                  for (int i = 0; i < genre_array.length(); i++) {
                      int genre_id = genre_array.getJSONObject(i).getInt(GENRE_ID);
                      String genre_name = genre_array.getJSONObject(i).getString(GENRE_NAME);
                      Utilities.map.put(genre_id, genre_name);
                  }
              } catch (JSONException e) {
                  Log.e(LOG_TAG, "Error in formatting JSON for genre");
              }

          }
      }


        /////// Method to extract JSON from a page, page is the page number in Json////////////
        ////********************************************************//////

        public void extractJSON(int page) throws IOException,JSONException{
            HttpURLConnection urlConnection;
            BufferedReader reader;
            StringBuilder stringBuilder;

                String apikey = BuildConfig.POPULAR_MOVIES_API_KEY;
            ///// Get the list of movies String/////
                String number_pages=Integer.toString(page);
                final String baseURL = "https://api.themoviedb.org/3/movie/now_playing";
                final String API_KEY = "api_key";
                final String PAGE="page";
                Uri uri = Uri.parse(baseURL).buildUpon()
                        .appendQueryParameter(API_KEY,apikey)
                        .appendQueryParameter(PAGE,number_pages).build();
                URL url = new URL(uri.toString());
                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(false);
                urlConnection.connect();
                InputStream inputStream=urlConnection.getInputStream();
                if(inputStream==null) return ;
                reader=new BufferedReader(new InputStreamReader(inputStream));
                stringBuilder=new StringBuilder();
                String inputString;
                while((inputString=reader.readLine())!=null){
                    stringBuilder.append(inputString);
                }
                if(stringBuilder==null) return ;

            parseJson(stringBuilder.toString());
        }
        public void parseJson(String JSONstring) throws JSONException {

            ////// Data to be parsed from JSON//////
            final String ORIGINAL_TITLE = "original_title";
            final String OVERVIEW = "overview";
            final String POSTER_PATH = "poster_path";
            final String BACKDROP_PATH = "backdrop_path";
            final String MOVIE_ID = "id";
            final String POPULARITY = "popularity";
            final String RATING = "vote_average";
            final String RESULTS = "results";
            final String RELEASE_DATE = "release_date";
            final String GENRE_IDS = "genre_ids";
            /////////////////////////////////////////
            final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
            final String POSTER_TYPE = "w185";

            StringBuilder sb = new StringBuilder();
            final String PATH_WITH_SIZE = sb.append(BASE_POSTER_URL).append(POSTER_TYPE).toString();
            JSONObject movieJSONObject = new JSONObject(JSONstring);
            JSONArray movieDetails = movieJSONObject.getJSONArray(RESULTS);
            if(movieDetails == null){
                Log.v(LOG_TAG,"movie details blank");
            }
            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieDetails.length());
            int arrayLength=movieDetails.length();
            for(int i = 0 ; i < arrayLength ; i++) {
                String original_title = movieDetails.getJSONObject(i).getString(ORIGINAL_TITLE);

                String overview = movieDetails.getJSONObject(i).getString(OVERVIEW);

                double popularity = movieDetails.getJSONObject(i).getDouble(POPULARITY);

                String posterpath = PATH_WITH_SIZE + movieDetails.getJSONObject(i).getString(POSTER_PATH);

                double rating = movieDetails.getJSONObject(i).getDouble(RATING);

                String backdroppath = PATH_WITH_SIZE + movieDetails.getJSONObject(i).getString(BACKDROP_PATH);
                int movieid = movieDetails.getJSONObject(i).getInt(MOVIE_ID);
                String release_date_str =movieDetails.getJSONObject(i).getString(RELEASE_DATE);

                //////// Get a list of GENRE_NAMES /////////////
                ArrayList<Integer> gId_list = new ArrayList<>();
                String genre_name ="Genre not known";
                JSONArray genre_array = movieDetails.getJSONObject(i).getJSONArray(GENRE_IDS);
                if(genre_array.length() > 0) {
                    for (int j = 0; j < genre_array.length(); j++) {
                        gId_list.add(genre_array.getInt(j));
                    }
                    genre_name = Utilities.getGenreNames(gId_list);
                }

                ////////// Storing data in ContentValues ////////////////
                ContentValues movieValues = new ContentValues();
                movieValues.put(MovieContract.MovieEntry.MOVIE_ID, movieid);
                movieValues.put(MovieContract.MovieEntry.ORIGINAL_TITLE, original_title);
                movieValues.put(MovieContract.MovieEntry.OVERVIEW, overview);
                movieValues.put(MovieContract.MovieEntry.POPULARITY, popularity);
                movieValues.put(MovieContract.MovieEntry.POSTER_PATH, posterpath);
                movieValues.put(MovieContract.MovieEntry.RATING, rating);
                movieValues.put(MovieContract.MovieEntry.BACKDROP_PATH, backdroppath);
                movieValues.put(MovieContract.MovieEntry.RELEASE_DATE, release_date_str);
                movieValues.put(MovieContract.MovieEntry.GENRE, genre_name);
                cVVector.add(movieValues);

                ////// Update the favourite movies' data for any updates //////////////

                    ContentValues favValues = new ContentValues();
                    favValues.put(MovieContract.FavouriteEntry.MOVIE_ID, movieid);
                    favValues.put(MovieContract.FavouriteEntry.ORIGINAL_TITLE, original_title);
                    favValues.put(MovieContract.FavouriteEntry.OVERVIEW, overview);
                    favValues.put(MovieContract.FavouriteEntry.POPULARITY, popularity);
                    favValues.put(MovieContract.FavouriteEntry.POSTER_PATH, posterpath);
                    favValues.put(MovieContract.FavouriteEntry.RATING, rating);
                    favValues.put(MovieContract.FavouriteEntry.BACKDROP_PATH, backdroppath);
                    favValues.put(MovieContract.FavouriteEntry.RELEASE_DATE, release_date_str);
                    favValues.put(MovieContract.FavouriteEntry.GENRE, genre_name);
                    String selection = MovieContract.FavouriteEntry.MOVIE_ID + " = ?";
                    String[] selectionArgs = new String[]{Integer.toString(movieid)};
                    //mContext.getContentResolver().insert(MovieContract.FavouriteEntry.CONTENT_URI, favValues);

//               mContext.getContentResolver().update(MovieContract.FavouriteEntry.CONTENT_URI
//                                                    ,favValues
//                                                    ,selection,selectionArgs);
            }
                int inserted = 0;
                if(cVVector.size() > 0){
                    inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI
                            ,cVVector.toArray(new ContentValues[cVVector.size()]));
                }
               // Log.d(LOG_TAG ,"The number of rows inserted to DB is: "+inserted);
            }

        }


