package com.yahoo.hakunamatata.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

import com.yahoo.hakunamatata.R;

public class VideoFragment extends DialogFragment {

    private String url;
    private VideoView videoView;

    public VideoFragment() {
    }

    public static VideoFragment newInstance(String url) {
        VideoFragment frag = new VideoFragment();
        frag.setUrl(url);
        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.video_fragment, container);
        videoView = (VideoView) view.findViewById(R.id.video);
        MediaController mc = new MediaController(getActivity());
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        Uri video = Uri.parse(this.url);
        videoView.setMediaController(mc);
        videoView.setZOrderOnTop(true);
        videoView.canPause();
        videoView.setVideoURI(video);
        videoView.requestFocus();
        videoView.start();
        return view;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
