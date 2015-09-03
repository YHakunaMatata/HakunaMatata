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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.view.SecretTextView;

public class AboutFragment extends DialogFragment {
    SecretTextView secretTextView;
    AuthorPhotoView authorPhoto;

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about, container, false);

        authorPhoto = new AuthorPhotoView(view);


        secretTextView = (SecretTextView) view.findViewById(R.id.stvAbout);
        secretTextView.setDuration(2500);
        secretTextView.setIsVisible(false);
        secretTextView.show();
        secretTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secretTextView.toggle();
                authorPhoto.toggle();
            }
        });

        // Close
        view.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.fragment_about);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private class AuthorPhotoView {
        private ImageView ivJonas;
        private ImageView ivSinze;
        private ImageView ivYutu;
        private boolean isShown;

        public AuthorPhotoView(View view) {
            this.ivJonas = (ImageView) view.findViewById(R.id.ivJonas);
            this.ivSinze = (ImageView) view.findViewById(R.id.ivSinze);
            this.ivYutu = (ImageView) view.findViewById(R.id.ivYutu);

            ivJonas.setVisibility(View.INVISIBLE);
            ivSinze.setVisibility(View.INVISIBLE);
            ivYutu.setVisibility(View.INVISIBLE);
            isShown = false;
        }

        public void show() {
            isShown = true;
            fadeIn(ivJonas, 2500, 1750);
            fadeIn(ivSinze, 2250, 2000);
            fadeIn(ivYutu, 2000, 2250);
        }

        public void hide() {
            isShown = false;
            fadeOut(ivSinze, 2000, 0);
            fadeOut(ivJonas, 2000, 0);
            fadeOut(ivYutu, 2000, 0);
        }

        public void toggle() {
            if (isShown) {
                hide();
            } else {
                show();
            }
        }

        private void fadeIn(final ImageView imageView, int fadeInDuration, int startOffset) {
            imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends

            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
            fadeIn.setStartOffset(startOffset);
            fadeIn.setDuration(fadeInDuration);

            imageView.startAnimation(fadeIn);

            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    imageView.setVisibility(View.VISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub
                }

                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub
                }
            });
        }

        private void fadeOut(final ImageView imageView, int fadeOutDuration, int startOffset) {
            imageView.setVisibility(View.VISIBLE);    //Visible or invisible by default - this will apply when the animation ends


            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
            fadeOut.setStartOffset(startOffset);
            fadeOut.setDuration(fadeOutDuration);

            imageView.startAnimation(fadeOut);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationEnd(Animation animation) {
                    imageView.setVisibility(View.INVISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub
                }

                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }
}
