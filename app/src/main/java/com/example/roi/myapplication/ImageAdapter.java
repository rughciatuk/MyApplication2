package com.example.roi.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Roi on 27/05/2016.
 */
public class ImageAdapter extends ArrayAdapter<String>{

    private Context myContext;

    public ImageAdapter(Context context, List<String> strings) {

        super(context,0, strings);
        myContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView1;

        String placeHolderString = "https://placeholdit.imgix.net/~text?txtsize=18&w=120&h=120&txt=image" + position;



        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_text_view,parent,false);
        }
        Log.e("##############", placeHolderString);

        imageView1 = (ImageView) convertView;

        Picasso.with(myContext).load(placeHolderString).into(imageView1);
        return imageView1;


    }
}
