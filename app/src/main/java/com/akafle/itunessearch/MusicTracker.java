package com.akafle.itunessearch;

import org.json.JSONException;
import org.json.JSONObject;


public class MusicTracker {


        protected String mtrackname;
        protected String martistname;
        protected String mcollectionname;
        protected String mpreviewUrl;
        protected String martworkUrl;
        protected String mtrackViewUrl;

        public MusicTracker(JSONObject articleObj) {
            try {
                // We expect that these two keys will be in the response.
                mtrackname = articleObj.getString("trackName");
                martistname= articleObj.getString("artistName");
                mcollectionname=articleObj.getString("collectionName");
                mpreviewUrl =articleObj.getString("previewUrl");
                martworkUrl=articleObj.getString("artworkUrl60");
                mtrackViewUrl=articleObj.getString("trackViewUrl");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String gettrackname() {

            return mtrackname;
        }

        public String getartistname() {

            return martistname;
        }

        public String getcollectionname() {

            return mcollectionname;
        }

        public String getpreviewUrl() {

            return mpreviewUrl;
        }
        public String getartworkUrl(){

            return martworkUrl;
        }
        public String gettrackViewUrl(){
            return mtrackViewUrl;
        }

}
