package com.example.najmehf.instagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {
    public static final String CLIENT_ID= "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<instagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos=new ArrayList<>();
        aPhotos=new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos=(ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                swipeContainer.setRefreshing(true);
                aPhotos.clear();
                fetchPopularPhotos();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        // Send out API request to popular photos
       fetchPopularPhotos();

    }



    public void fetchPopularPhotos(){

        /*
        -Response
                - Type: {"data" => [x]=> "type" } ("image" or "video")
        - URL: {"data" => [x] => "images" => "standard_resolution" => "url"}
        - Caption: {"data" => [x] => "caption" => "text"}
        - Author Name:{ "data" => [x] => "user" => "username"}*/
        String url= "https://api.instagram.com/v1/media/popular?client_id="+CLIENT_ID;
        AsyncHttpClient clinet = new AsyncHttpClient();
        clinet.get(url, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Log.i("DEBUG", response.toString());
                        JSONArray photosJSON = null;
                        try {
                            photosJSON = response.getJSONArray("data");
                            for (int i = 0; i < photosJSON.length(); i++) {
                                // JSONArray commentsJson=null;
                                JSONObject photoJSON = photosJSON.getJSONObject(i);
                                instagramPhoto photo = new instagramPhoto();
                                photo.username = photoJSON.getJSONObject("user").getString("username");
                                photo.caption = photoJSON.getJSONObject("caption").getString("text");
                                photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                                photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                                photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                                JSONArray commentsJson = photoJSON.getJSONObject("comments").getJSONArray("data");
                                int k = Math.min(commentsJson.length(), 2);
                                if (k > 0) {
                                    for (int j = commentsJson.length() - 1; j > commentsJson.length() - 3; j--) {
                                        JSONObject commentJson = commentsJson.getJSONObject(j);
                                        ArrayList<String> cm = new ArrayList<String>();
                                        if (commentJson.has("text")) {
                                            int l = 9;
                                            String m = commentJson.getString("text");
                                            String u = commentJson.getJSONObject("from").getString("username");
                                            //cm.add(m);
                                            photo.comments.add(m);
                                            photo.commentsUsers.add(u);
                                        }
                                        //cm.add(commentJson.getString("text"));}

                                        // photo.comments.add(commentJson.getString("text"));
                                    }
                                }
                                photos.add(photo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //aPhotos.clear();
                        aPhotos.notifyDataSetChanged();
                        //aPhotos.addAll(photos);

                        swipeContainer.setRefreshing(false);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                }
        );
    }
}
