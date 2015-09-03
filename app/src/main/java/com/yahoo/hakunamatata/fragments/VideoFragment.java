package com.yahoo.hakunamatata.fragments;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.yahoo.hakunamatata.R;

public class VideoFragment extends DialogFragment {

    private String url;
    private VideoView videoView;

    public VideoFragment() {
    }

    public static VideoFragment newInstance(String url, boolean isYoutube) {
        VideoFragment frag = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("isYoutube", isYoutube);
        frag.setArguments(bundle);
        frag.setUrl(url);
        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setFormat(PixelFormat.TRANSLUCENT);

        // fix dialog darkness
        WindowManager.LayoutParams a = getDialog().getWindow().getAttributes();
        a.dimAmount = 0;
        getDialog().getWindow().setAttributes(a);

        View view = inflater.inflate(R.layout.youtube_fragment, container);
        PlayerYouTubeFrag youTubePlayerFragment = PlayerYouTubeFrag.newInstance(getArguments().getString("url"));
        getChildFragmentManager().beginTransaction().add(R.id.youtubeplayerfragment_container, youTubePlayerFragment).commit();
        getChildFragmentManager().executePendingTransactions();

        youTubePlayerFragment.init();
//        youTubePlayerFragment.initialize(getActivity().getResources().getString(R.string.google_developer_key), new YouTubePlayer.OnInitializedListener() {
//
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                if (!b) {
//                    youTubePlayer.cueVideo("3OhGkg_XT3o");
//                }
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//            }
//        });


//        myPlayerStateChangeListener = new MyPlayerStateChangeListener();
//        myPlaybackEventListener = new MyPlaybackEventListener();
//
//        btnViewFullScreen = (Button) findViewById(R.id.btnviewfullscreen);
//        btnViewFullScreen.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                youTubePlayer.setFullscreen(true);
//            }
//        });


        /*videoView = (VideoView) view.findViewById(R.id.video);
        MediaController mc = new MediaController(getActivity());
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        Uri video = Uri.parse(this.url);
        videoView.setMediaController(mc);
        videoView.setZOrderOnTop(true);
        videoView.canPause();
        videoView.setVideoURI(video);
        videoView.requestFocus();
        videoView.start();*/
        return view;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
