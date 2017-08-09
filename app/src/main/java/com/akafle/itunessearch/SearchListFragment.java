package com.akafle.itunessearch;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SearchListFragment extends Fragment {

    private ListView mlistview;
    private List<MusicTracker> mMusiclist;


    private ItunesItemAdapter mMusicAdapter;
    private MediaPlayer mMediaPlayer;
    private String mCurrentlyPlayingUrl;
//    private TextView mtext;
    private String MTextsearch;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        MTextsearch = getArguments().getString("key");
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_search_list, container, false);

//        mtext = (TextView) v.findViewById(R.id.display_results);
        mlistview = (ListView) v.findViewById(R.id.listview);
        mMusicAdapter = new ItunesItemAdapter (getActivity());
        mlistview.setAdapter(mMusicAdapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                MusicTracker musictracker = (MusicTracker) parent.getAdapter().getItem(i);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "This track is good");
                shareIntent.putExtra(Intent.EXTRA_TEXT, musictracker.gettrackViewUrl());
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });


        if ( mMusiclist != null) {
            mMusicAdapter.setItems(mMusiclist);
        }
        else {
            songsRefresh();
        }
        return v;
    }
    private void clickedAudioURL(String url) {
        if (mMediaPlayer.isPlaying()) {
            if (mCurrentlyPlayingUrl.equals(url)) {
                mMediaPlayer.stop();
                mMusicAdapter.notifyDataSetChanged();
                return;
            }
        }

        mCurrentlyPlayingUrl = url;
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mMusicAdapter.notifyDataSetChanged();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMusicAdapter.notifyDataSetChanged();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void songsRefresh() {
        ItunesSource.get(getContext()).getItunesSource(MTextsearch, new ItunesSource.ItunesResultsListener(){
            @Override
            public void onMusicTrackerResponse(List<MusicTracker> musicList) {
                mMusiclist = musicList;
                mMusicAdapter.setItems(musicList);
            }
        });
    }

        private class ItunesItemAdapter extends BaseAdapter {
            private TextView mSong;
            private TextView mArtistname;
            private TextView mCollectionname;
            private NetworkImageView mImage;
            private ImageButton mButton;

            private Context mContext;
            private LayoutInflater mInflater;
            private List<MusicTracker> mdatasource;

            public ItunesItemAdapter(Context context) {
                mContext = context;
                mdatasource = new ArrayList<>();
                mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            }

            public void setItems(List<MusicTracker> msongList) {
                mdatasource.clear();
                mdatasource.addAll(msongList);
                notifyDataSetChanged();
            }

            @Override
            public int getCount() {
                return mdatasource.size();
            }

            @Override
            public Object getItem(int position) {
                return mdatasource.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertview, ViewGroup parent) {
                final MusicTracker music = mdatasource.get(position);
                View rowView = mInflater.inflate(R.layout.fragment_songlist, parent, false);

                mSong = (TextView) rowView.findViewById(R.id.title);
                mSong.setText(music.gettrackname());

                mArtistname = (TextView) rowView.findViewById(R.id.artist);
                mArtistname.setText(music.getartistname());

                mCollectionname = (TextView) rowView.findViewById(R.id.album);
                mCollectionname.setText(music.getartistname());

                mImage = (NetworkImageView) rowView.findViewById(R.id.thumbnail);
                ImageLoader loader = ItunesSource.get(getContext()).getImageLoader();
                mImage.setImageUrl(music.getartworkUrl(),loader);
                boolean isPlaying = mMediaPlayer.isPlaying() &&   mCurrentlyPlayingUrl.equals(music.getpreviewUrl());
                mButton = (ImageButton) rowView.findViewById(R.id.playbutton);


                if (isPlaying) {
                    mButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_pause));
                } else {
                    mButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_media_play));
                }

                mButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickedAudioURL(music.getpreviewUrl());
                    }
                });
                return rowView;
            }
        }
}









