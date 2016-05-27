package com.example.roi.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String> myStrings = new ArrayList<String>();
        for(int i = 0; i < 100; i++){
            myStrings.add(Integer.toString(i));
        }
        GridView gridView = (GridView) findViewById(R.id.gridview);
        ListAdapter listAdapter =  new ImageAdapter(this,myStrings);
        gridView.setAdapter(listAdapter);



    }
}
