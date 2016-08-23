package app.com.example.samitabh.movieapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by SAmitabh on 12-07-2016.
 */
public class MovieAdapter extends CursorAdapter {

    private final static String LOG_TAG = MovieAdapter.class.getSimpleName();



    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_movies_fragment,parent,false);
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView)view.findViewById(R.id.movies_image_view);
        String imageString = cursor.getString(Movie_pop_fragment.COL_POSTERPATH);
        Log.e(LOG_TAG,"The image path is: "+imageString+ "and movie name is: "+cursor.getString(Movie_pop_fragment.COL_ORIGINAL_TITLE));
        if(imageString.charAt(imageString.length()-1)=='l'){
            //Log.d(LOG_TAG,"no movie poster");
            imageString = Utilities.getDefaultPosterPath();
    }
        Picasso.with(context)
                .load(imageString)
                .resize(200, 300)
                .into(imageView);


//

    }

}


