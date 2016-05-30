package com.example.roi.myapplication;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Roi on 30/05/2016.
 */
public class ImageAdapter extends ArrayAdapter<String> {

    private Context myContext;
    private List<String> mPaths;
    private static final String TAG = "ImageAdapter";


    public ImageAdapter(Context context, List<String> strings) {

        super(context,0, strings);
        myContext = context;
        mPaths = strings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView1;


        Uri.Builder imagePath = new Uri.Builder();
        imagePath.scheme("http").
                authority("image.tmdb.org").
                appendPath("t").
                appendPath("p").
                appendPath("w342").
                appendEncodedPath(mPaths.get(position));

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_image_view,parent,false);
        }

        imageView1 = (ImageView) convertView;
        Picasso.with(myContext).load(imagePath.build()).into(imageView1);

        return imageView1;


    }
}

