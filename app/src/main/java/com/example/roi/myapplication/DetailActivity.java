package com.example.roi.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent myIntent = getIntent();
        String title = myIntent.getStringExtra("string_title");
        String path = myIntent.getStringExtra("string_path");
        path = "http://image.tmdb.org/t/p/w500/" + path;
        String plotSynopsis = myIntent.getStringExtra("string_plot");
        String releaseDate = myIntent.getStringExtra("string_release");
        releaseDate = "Release Date: " + releaseDate;
        String userRating = myIntent.getStringExtra("string_rating");
        userRating = "User Rating: " + userRating;



        TextView titleTextView = (TextView) findViewById(R.id.detail_title_text_view);
        ImageView posterImageView = (ImageView) findViewById(R.id.detail_poster_image_view);
        TextView plotTextView = (TextView) findViewById(R.id.detail_plot_text_view);
        TextView releaseDateTextView = (TextView) findViewById(R.id.detail_release_date_text_view);
        TextView userRatingTextView = (TextView) findViewById(R.id.detail_user_rating_text_view);


        Picasso.with(this).load(path).into(posterImageView);
        titleTextView.setText(title);
        plotTextView.setText(plotSynopsis);
        releaseDateTextView.setText(releaseDate);
        userRatingTextView.setText(userRating);

    }
}
