package com.example.roi.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListAdapter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
        String url = "http://api.themoviedb.org/3/movie/top_rated?api_key=5af9081f3908d163822f5c8fda9660ae";

        GetMovieDataTesk myTesk = new GetMovieDataTesk();
        myTesk.execute(url);



    }


    public class GetMovieDataTesk extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected JSONObject doInBackground(String... arg){
            try{
            String jsonString = "";
            if (jsonString == null || jsonString == "") {
                jsonString = getJsonFromServer(arg[0]);
                try {
                    JSONObject obj = new JSONObject(jsonString);
                    Log.d("Test", obj.toString());
                    return obj;
                }catch (Throwable t){
                    Log.e("My App", "Could not parse malformed JSON: \"" + jsonString + "\"");
                }

            }
            } catch (IOException e) {
              // TODO Auto-generated catch block

              e.printStackTrace();
            }
            return null;}

            public String getJsonFromServer(String url) throws IOException{

                BufferedReader inputStream = null;

                URL jsonUrl = new URL(url);
                URLConnection dc = jsonUrl.openConnection();

                dc.setConnectTimeout(5000);
                dc.setReadTimeout(5000);

                inputStream = new BufferedReader(new InputStreamReader(dc.getInputStream()));

                String jsonResult = inputStream.readLine();
                inputStream.close();
                return jsonResult;


        }


    }

}



