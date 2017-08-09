package com.akafle.itunessearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ItunesSource {

        public interface ItunesResultsListener {
            void onMusicTrackerResponse(List<MusicTracker> musicList);
        }

        private final static int IMAGE_CACHE_COUNT = 100;
        private final static int ARTICLE_REQUEST_COUNT = 25;
        private final static int ARTICLE_REQUEST_IMAGE_WIDTH = 400;

        private static ItunesSource sItunesSourceInstance;
        private Context mContext;
        private RequestQueue mRequestQueue;
        private ImageLoader mImageLoader;




    public static ItunesSource get(Context context) {
            if (sItunesSourceInstance== null) {
                    sItunesSourceInstance = new ItunesSource(context);
            }
            return sItunesSourceInstance;
        }

        private ItunesSource(Context context) {
            mContext = context.getApplicationContext();
            mRequestQueue = Volley.newRequestQueue(mContext);

            mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> mCache = new LruCache<>(IMAGE_CACHE_COUNT);
                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }
                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }
            });
        }

    public void getItunesSource(String query, ItunesResultsListener resultsListener) {
        final ItunesResultsListener ItunesListenerInternal = resultsListener;

        String url ="https://itunes.apple.com/search?term=" + query + "&entity=musicTrack\n";
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            List<MusicTracker> musicList = new ArrayList<>();
                            JSONArray jsonarray = response.getJSONArray("results");
                            for (int i = 0; i < jsonarray.length();i++){

                                JSONObject eachjson = jsonarray.getJSONObject(i);
                                MusicTracker musicobj = new MusicTracker(eachjson);
                                musicList.add(musicobj);
                            }
                            ItunesListenerInternal.onMusicTrackerResponse(musicList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                                ItunesListenerInternal.onMusicTrackerResponse(null);
                            Toast.makeText(mContext, "Could not get songs.", Toast.LENGTH_SHORT);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            ItunesListenerInternal.onMusicTrackerResponse(null);
                        Toast.makeText(mContext, "Could not get songs.", Toast.LENGTH_SHORT);
                    }
                });

        mRequestQueue.add(jsonObjRequest);
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }
}

