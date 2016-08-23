package app.com.example.samitabh.movieapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
/**
 * Created by SAmitabh on 07-06-2016.
 */
public class ImageAdapter extends ArrayAdapter {
    private final String LOG_TAG=ImageAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Movie_data_class> moviedetails;
    //private ArrayList<String> picassopath;
    ImageAdapter(Context c, ArrayList<Movie_data_class> moviedetail){
        super(c,R.layout.popular_movies_fragment,moviedetail);
        mContext=c;
        moviedetails=moviedetail;
    }
    @Override
    public int getCount() {
        return moviedetails.size();
    }

    @Override
    public Object getItem(int position) {
        return moviedetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView==null){
            LayoutInflater inflater=((Activity)mContext).getLayoutInflater();
            convertView=inflater.inflate(R.layout.popular_movies_fragment,parent,false);
        }

            imageView=(ImageView) convertView.findViewById(R.id.movies_image_view);


        if(imageView!=null) {
            String imageString = moviedetails.get(position).posterPath;
            if(imageString.charAt(imageString.length()-1)=='l') {
                //Log.d(LOG_TAG,"no movie poster");
                imageString = Utilities.getDefaultPosterPath();
            }
            //Log.v(LOG_TAG,"The image path is: "+imageString);
            Picasso.with(mContext)
                    .load(imageString)
                    .resize(600, 900)
                    .into(imageView);
        }
        else{
            Log.e(LOG_TAG,"Image view is null");
        }
        //Log.v(LOG_TAG,"The image URL got is"+moviedetails.get(position).posterPath);
        return imageView;
    }

}
