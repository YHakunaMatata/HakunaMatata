package com.yahoo.hakunamatata.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.squareup.picasso.Picasso;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.activities.RestApplication;
import com.yahoo.hakunamatata.interfaces.Progressable;
import com.yahoo.hakunamatata.interfaces.Reloadable;
import com.yahoo.hakunamatata.models.User;
import com.yahoo.hakunamatata.network.FacebookClient;
import com.yahoo.hakunamatata.network.MyJsonHttpResponseHandler;

import org.apache.http.Header;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by jonaswu on 2015/8/30.
 */
public class SubmitFragment extends DialogFragment implements TextView.OnKeyListener {

    View view;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private String photoFileName = "photo.jpg";
    private String APP_TAG = "my";
    private byte[] photoForUpload;


    public PostSuccessDelegator getPostSuccessDelegator() {
        return postSuccessDelegator;
    }

    public void setPostSuccessDelegator(PostSuccessDelegator postSuccessDelegator) {
        this.postSuccessDelegator = postSuccessDelegator;
    }


    public interface PostSuccessDelegator {
        public void postSuccess();
    }

    private Long replyTo;
    private TextView name;
    private EditText body;
    private TextView length;
    private ImageView image;
    private ImageView ivCaptureBtn;
    private ImageView ivPreview;
    private Button confirmButton;
    private Button closeButton;
    private PostSuccessDelegator postSuccessDelegator;
    private Progressable progressable;
    private Reloadable reloadableActivity;

    public static SubmitFragment newInstance(Reloadable reloadableActivity) {
        SubmitFragment frag = new SubmitFragment();
        frag.reloadableActivity = reloadableActivity;
        return frag;
    }

    public void onCapture(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName)); // set the image file name
        // Start the image capture intent to take photo
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {

            // Get safe storage directory for photos
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    private byte[] convertBitmap2BiteAry(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                // resize
                // Bitmap bMapScaled = Bitmap.createScaledBitmap(takenImage, 900, 600, true);

                // Load the taken image into a preview
                ivPreview.setImageBitmap(takenImage);
                ivPreview.setVisibility(View.VISIBLE);
                photoForUpload = convertBitmap2BiteAry(takenImage);

            } else { // Result was a failure
                Log.d("my", "capture fail");
            }
        }
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    private void reload() {
        reloadableActivity.reload();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.fragment_submit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        User user = RestApplication.getMe();
        View view = inflater.inflate(R.layout.fragment_submit, container, false);

        name = (TextView) view.findViewById(R.id.name);
        length = (TextView) view.findViewById(R.id.length);
        body = (EditText) view.findViewById(R.id.body);
        image = (ImageView) view.findViewById(R.id.profile_image);
        confirmButton = (Button) view.findViewById(R.id.confirmBtn);
        ivPreview = (ImageView) view.findViewById(R.id.ivPreview);
        closeButton = (Button) view.findViewById(R.id.close_button);

        // set camera button
        ivCaptureBtn = (ImageView) view.findViewById(R.id.ivCaptureBtn);
        ivCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCapture(v);
            }
        });

        name.setText(user.name);
        body.setOnKeyListener(this);
        body.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                length.setText(String.valueOf(cs.toString().length()) + "/" + getActivity().getResources().getString(R.string.maxlenghtofatweet));
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

        });

        Picasso.with(getActivity())
                .load(user.picture.url)
                .error(R.drawable.images)
                .placeholder(R.drawable.placeholder)
                .centerInside()
                .noFade()
                .fit()
                .into(image);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacebookClient client = RestApplication.getRestClient();
                // progressable.setBusy();
                if (photoForUpload != null) {
                    client.postPhoto(
                            body.getText().toString(),
                            photoForUpload,
                            new GraphRequest.Callback() {
                                @Override
                                public void onCompleted(GraphResponse graphResponse) {

                                    //check graphResponse for success or failure
                                    if (graphResponse.getError() == null) {
                                        Log.d("my", "Success: " + graphResponse.toString());
                                        reload();
                                        // postSuccessDelegator.postSuccess();
                                        // progressable.setFinish();
                                    } else {
                                        Log.d("my", "Post photo fail: " + graphResponse.toString());
                                    }
                                }
                            }
                    );
                } else {
                    client.post(body.getText().toString(),
                            new MyJsonHttpResponseHandler(getActivity()) {
                                @Override
                                public void successCallBack(int statusCode, Header[] headers, Object data) {
                                    Log.d("my", data.toString());
                                    reload();
                                    // postSuccessDelegator.postSuccess();
                                    // progressable.setFinish();
                                }

                                @Override
                                public void errorCallBack() {
                                }
                            }
                    );
                }
                dismiss();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        length.setText(String.valueOf(body.getText().toString().length()) + "/" + getActivity().getResources().getString(R.string.maxlenghtofatweet));
        return false;
    }
}
