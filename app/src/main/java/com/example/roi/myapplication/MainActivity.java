package com.example.roi.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

    ImageAdapter mImageAdapter;
    ArrayList<String> mMoviesPosterPath;
    JSONArray mJSONMoviesArray;
    int mPage;
    boolean flag_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesPosterPath = new ArrayList<>();
        flag_loading = false;
        mJSONMoviesArray = new JSONArray();

        mPage = 1;
        getMoviesData();


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
                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount != 0){
                    if(flag_loading == false){
                        flag_loading = true;
                        mPage++;
                        getMoviesData();
                        Log.e("Hey", "there");
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
            getMoviesData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getMoviesData(){
        GetMovieDataTesk myTesk = new GetMovieDataTesk();



        String url = "http://api.themoviedb.org/3/movie/top_rated?api_key=5af9081f3908d163822f5c8fda9660ae&page=" + mPage;

        myTesk.execute(url);
        flag_loading = false;
    }

    public class GetMovieDataTesk extends AsyncTask<String,Void,String> {

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



                for(int i =0 ; i<jsonArray.length();i++){
                    JSONObject movie = jsonArray.getJSONObject(i);
                    mJSONMoviesArray.put(movie);


                    ArrayList<String> paths = new ArrayList<>();
                    paths.add(movie.getString("poster_path"));
                    mImageAdapter.addAll(paths);
                }

            }catch (Throwable t){
                Log.e("My App", "Could not parse malformed JSON: \"" + stringResult + "\"");
            }

        }
    }
}



