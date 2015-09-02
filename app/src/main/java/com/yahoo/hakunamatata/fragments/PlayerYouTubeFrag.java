package com.yahoo.hakunamatata.fragments;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.yahoo.hakunamatata.lib.util;

/**
 * Created by jonaswu on 2015/8/31.
 */
public class PlayerYouTubeFrag extends YouTubePlayerSupportFragment {

    private String currentVideoID = "video_id";
    private YouTubePlayer activePlayer;

    public static PlayerYouTubeFrag newInstance(String url) {

        Log.e("url", url);
        PlayerYouTubeFrag playerYouTubeFrag = new PlayerYouTubeFrag();

        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        playerYouTubeFrag.setArguments(bundle);

        return playerYouTubeFrag;
    }

    public void init() {
        initialize("AIzaSyBlDFaohXcFA-ZSJFrk63Y-eFnDi5JZB0I", new YouTubePlayer.OnInitializedListener() {


            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                activePlayer = player;
                activePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                if (!wasRestored) {
                    activePlayer.loadVideo(util.expandUrl(getArguments().getString("url")));
                    activePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {

                        @Override
                        public void onVideoStarted() {
                        }

                        @Override
                        public void onVideoEnded() {
                        }

                        @Override
                        public void onLoading() {
                        }

                        @Override
                        public void onLoaded(String videoId) {
                            activePlayer.play();
                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason reason) {
                        }

                        @Override
                        public void onAdStarted() {
                        }
                    });
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

}