package com.example.roi.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageAdapter mImageAdapter;
    ArrayList<String> mMoviesPosterPath;
    JSONArray mJSONMoviesArray;
    boolean doneGettingData;
    int mPage;
    String mOrder;


    String TAG;


    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        String order = myPref.getString(getString(R.string.pref_order_key),"top_rated");
        if(!order.equals(mOrder)) {
            mImageAdapter.clear();
            mPage = 1;
            mOrder = order;

            try{
                mJSONMoviesArray = new JSONArray();
            }catch (Throwable t){}

            Toast.makeText(MainActivity.this, "order change to " + order , Toast.LENGTH_SHORT).show();
            getMoviesData(mOrder);
        }else{
            getMoviesData(mOrder);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TAG = getLocalClassName();
        mOrder = "top_rated";
        mMoviesPosterPath = new ArrayList<>();
        mJSONMoviesArray = new JSONArray();
        mPage = 1;
        doneGettingData = false;


        GridView gridView = (GridView) findViewById(R.id.gridview);
        mImageAdapter =  new ImageAdapter(this,mMoviesPosterPath);


        gridView.setAdapter(mImageAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String title = "Title not found :(";
                String plotSynopsis = "Plot not found";
                String releaseDate = "Release date not found";
                String userRating = "User Rating not found";

                try {
                    JSONObject movie = mJSONMoviesArray.getJSONObject(position);
                    title = movie.getString("original_title");
                    plotSynopsis = movie.getString("overview");
                    releaseDate = movie.getString("release_date");
                    userRating = movie.getString("vote_average");

                }catch (Throwable t){
                    Log.e("My App", "Could not parse malformed JSON: \"" + mJSONMoviesArray.toString() + "\"");
                }
                Intent detailActivityIntent = new Intent(getBaseContext(),DetailActivity.class);
                detailActivityIntent.putExtra("string_title",title);
                detailActivityIntent.putExtra("string_path",mMoviesPosterPath.get(position));
                detailActivityIntent.putExtra("string_plot",plotSynopsis);
                detailActivityIntent.putExtra("string_release", releaseDate);
                detailActivityIntent.putExtra("string_rating", userRating);
                startActivity(detailActivityIntent);

            }
        });
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount > 0){
                    if(doneGettingData){
                        doneGettingData = false;


                        getMoviesData(mOrder);
                    }

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.test_settings){
            mImageAdapter.clear();
            mPage = 1;
            getMoviesData(mOrder);
            return true;
        }

        if(id == R.id.setting){
            Intent myIntent = new Intent(this,SettingsActivity.class);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getMoviesData(String order){
        GetMovieDataTask myTask = new GetMovieDataTask();

        Uri.Builder urlBuilder = new Uri.Builder();
        urlBuilder.scheme("http");
        urlBuilder.authority("api.themoviedb.org");
        urlBuilder.appendPath("3");
        urlBuilder.appendPath("movie");
        urlBuilder.appendPath(order);
        urlBuilder.appendQueryParameter("api_key", "5af9081f3908d163822f5c8fda9660ae");
        urlBuilder.appendQueryParameter("page", ""+mPage);
        Log.e(TAG, "getMoviesData() called with: " + urlBuilder.build());

        doneGettingData = false;
        mPage++;

        myTask.execute(urlBuilder.build().toString());


    }

    public class GetMovieDataTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... arg){
            try{

            return getJsonFromServer(arg[0]);

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

                JSONArray jsonArray = obj.getJSONArray("results");




                Log.e("##########", "" + mPage);
                for (int i = 0; i < jsonArray.length(); i++) {


                    JSONObject movie = jsonArray.getJSONObject(i);
                    mJSONMoviesArray.put(movie);


                    ArrayList<String> paths = new ArrayList<>();
                    paths.add(movie.getString("poster_path"));
                    mImageAdapter.addAll(paths);

                }



            }catch (Throwable t){
                Log.e("My App", "Could not parse malformed JSON: \"" + stringResult + "\"");
            }
            doneGettingData = true;

        }
    }

}



