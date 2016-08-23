package app.com.example.samitabh.movieapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by SAmitabh on 12-07-2016.
 */

    public class ImageUploadPicasso extends AsyncTask<ImageView,Void,Void> {
    Context context;
    String imageString;
        public ImageUploadPicasso(Context context ,String imageString){
            this.context=context;
            this.imageString =imageString;
        }
        @Override
        protected Void doInBackground(ImageView... params) {
            Picasso.with(context)
                    .load(imageString)
                    .resize(600, 900)
                    .into(params[0]);
            return null;
        }
    }

