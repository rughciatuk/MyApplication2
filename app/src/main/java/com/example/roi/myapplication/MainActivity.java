package com.example.roi.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageAdapter mListAdapter;
    ArrayList<String> mMoviesPosterPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesPosterPath = new ArrayList<>();


        GetMovieDataTesk myTesk = new GetMovieDataTesk();


        String url = "http://api.themoviedb.org/3/movie/top_rated?api_key=5af9081f3908d163822f5c8fda9660ae";

        myTesk.execute(url);


        GridView gridView = (GridView) findViewById(R.id.gridview);
        mListAdapter =  new ImageAdapter(this,mMoviesPosterPath);



        gridView.setAdapter(mListAdapter);






    }


    public class GetMovieDataTesk extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... arg){
            try{

            String jsonString = getJsonFromServer(arg[0]);
            return jsonString;

            } catch (IOException e) {

              e.printStackTrace();
            }
            return null;
        }

        public String getJsonFromServer(String url) throws IOException{

            BufferedReader inputStream;

            URL jsonUrl = new URL(url);
            URLConnection dc = jsonUrl.openConnection();

            dc.setConnectTimeout(5000);
            dc.setReadTimeout(5000);

            inputStream = new BufferedReader(new InputStreamReader(dc.getInputStream()));

            String jsonResult = inputStream.readLine();
            inputStream.close();
            return jsonResult;
        }

        @Override
        public void onPostExecute(String stringResult){



            try {
                JSONObject obj = new JSONObject(stringResult);

                JSONArray resultArray = obj.getJSONArray("results");

                for(int i =0 ; i<resultArray.length();i++){
                    JSONObject movie = resultArray.getJSONObject(i);

                    String path = movie.getString("poster_path");
                    mListAdapter.add(path);



                }

            }catch (Throwable t){
                Log.e("My App", "Could not parse malformed JSON: \"" + stringResult + "\"");
            }

        }
    }
}



