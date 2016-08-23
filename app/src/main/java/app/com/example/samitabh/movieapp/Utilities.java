package app.com.example.samitabh.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;

import app.com.example.samitabh.movieapp.data.MovieContract;

/**
 * Created by SAmitabh on 29-06-2016.
 */
public class Utilities {

    private static final int JANUARY = 1;
    private static final int FEBRUARY = 2;
    private static final int MARCH = 3;
    private static final int APRIL = 4;
    private static final int MAY = 5;
    private static final int JUNE = 6;
    private static final int JULY = 7;
    private static final int AUGUST = 8;
    private static final int SEPTEMBER = 9;
    private static final int OCTOBER = 10;
    private static final int NOVEMBER = 11;
    private static final int DECEMBER = 12;

///// Godfather content values --- default fav movie(for many not for me) /////

    public static void setGodfatherAsFav(Context context) {

    ContentValues favValues = new ContentValues();
    favValues.put(MovieContract.FavouriteEntry.MOVIE_ID,0);
    favValues.put(MovieContract.FavouriteEntry.ORIGINAL_TITLE,"The Godfather");
    favValues.put(MovieContract.FavouriteEntry.OVERVIEW,"Crime makes you as many enemies as friends");
    favValues.put(MovieContract.FavouriteEntry.POPULARITY,95.0);
    favValues.put(MovieContract.FavouriteEntry.POSTER_PATH,"http://www.thegraphicpath.com/wp-content/uploads/ghen-martin-the-godfather.jpg");
    favValues.put(MovieContract.FavouriteEntry.RATING,9.2);
    favValues.put(MovieContract.FavouriteEntry.BACKDROP_PATH,"null");
    favValues.put(MovieContract.FavouriteEntry.RELEASE_DATE,"1971-08-15");
    favValues.put(MovieContract.FavouriteEntry.GENRE,"Crime/Drama");
        context.getContentResolver().insert(MovieContract.FavouriteEntry.CONTENT_URI,favValues);

}

    private static final int YEAR_FORMAT_LENGTH  = 4;

    private static final int RATING_OUT_OF = 10;

    private static final String LOG_TAG = Utilities.class.getSimpleName();
    public static HashMap <Integer ,String> map = new HashMap<>();

    public static String getDefaultPosterPath () {
        return "https://s-media-cache-ak0.pinimg.com/originals/d3/8b/c3/d38bc38ad9ba60f9091aa2a9b3f4190f.png";
    }
    public static String getSortOrderPreference(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String sortorder = preferences.getString(context.getString(R.string.pref_sort_by_key)
                                                 ,context.getString(R.string.pref_sort_by_popularity));
        return sortorder;
    }
    public static String getGenreNames(ArrayList<Integer> list){
        String result ="";
        StringBuilder sb = new StringBuilder();
        if(list != null) {
            for (Integer list_id : list) {
                if(map.containsKey(list_id))
                sb.append(map.get(list_id)).append(" ");
            }
            result = sb != null ? sb.toString() : "";
        }
        return result;
    }

    public static String getFriendlyReleaseDate(String raw_date){

        String[] date_details= raw_date.split("-");
        String year = date_details[0];
        int month = Integer.parseInt(date_details[1]);
        String date = date_details[2];
        String month_str = "";
       switch(month) {
           case (JANUARY):
               month_str = "Jan";
               break;
           case (FEBRUARY):
               month_str = "Feb";
               break;
           case (MARCH):
               month_str = "Mar";
               break;
           case (APRIL):
               month_str = "Apr";
               break;
           case (MAY):
               month_str = "May";
               break;
           case (JUNE):
               month_str = "Jun";
               break;
           case (JULY):
               month_str = "Jul";
               break;
           case (AUGUST):
               month_str = "Aug";
               break;
           case (SEPTEMBER):
               month_str = "Sep";
               break;
           case (OCTOBER):
               month_str = "Oct";
               break;
           case (NOVEMBER):
               month_str = "Nov";
               break;
           case (DECEMBER):
               month_str = "Dec";
       }
        StringBuilder sb = new StringBuilder();
        String res =sb.append(date)
                       .append(" ")
                       .append(month_str)
                       .append(" ")
                       .append(year)
                       .toString();


        return res;
    }

    public static String getRating(Double rating,Context mContext){

        if(rating < 1.0){
            return mContext.getString(R.string.unrated);
        }
        if(rating == 10) {
            return String.format(mContext.getString(R.string.ratingfor10),rating,RATING_OUT_OF);
        }
         return String.format(mContext.getString(R.string.rating),rating,RATING_OUT_OF);
    }
    public static String getPopularity(Double popularity,Context mContext){

        return String.format(mContext.getString(R.string.popularity),popularity);
    }

    //////////// Returns Genre in a readable format /////

    public static String getGenre(String genre,Context mContext){

        return String.format(mContext.getString(R.string.genre),genre);
    }

}


