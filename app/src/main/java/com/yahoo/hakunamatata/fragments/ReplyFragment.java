package com.yahoo.hakunamatata.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.activities.RestApplication;
import com.yahoo.hakunamatata.models.Post;
import com.yahoo.hakunamatata.network.FacebookClient;

public class ReplyFragment extends DialogFragment {
    private Post post;

    public static ReplyFragment newInstance(Post post) {
        ReplyFragment fragment = new ReplyFragment();
        fragment.post = post;
        return fragment;
    }

    public ReplyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reply, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.fragment_reply);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final FacebookClient client = RestApplication.getRestClient();
        // OK
        dialog.findViewById(R.id.positive_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Close

        dialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return dialog;
    }
}
