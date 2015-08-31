package com.yahoo.hakunamatata.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.activities.RestApplication;
import com.yahoo.hakunamatata.interfaces.Progressable;
import com.yahoo.hakunamatata.lib.RoundedTransformation;
import com.yahoo.hakunamatata.models.User;
import com.yahoo.hakunamatata.network.FacebookClient;
import com.yahoo.hakunamatata.network.MyJsonHttpResponseHandler;

import org.apache.http.Header;

/**
 * Created by jonaswu on 2015/8/30.
 */
public class SubmitFragment extends DialogFragment implements TextView.OnKeyListener {

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
    private TextView screenname;
    private TextView name;
    private EditText body;
    private TextView length;
    private ImageView image;
    private PostSuccessDelegator postSuccessDelegator;
    private Progressable progressable;

    public static SubmitFragment newInstance(PostSuccessDelegator postSuccessDelegator) {
        SubmitFragment frag = new SubmitFragment();
        frag.setPostSuccessDelegator(postSuccessDelegator);
        frag.setProgressableDelegator((Progressable) (postSuccessDelegator));
        return frag;
    }

    private void setProgressableDelegator(Progressable progressable) {
        this.progressable = progressable;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        User user = RestApplication.getMe();
        // getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.submit, null);

        screenname = (TextView) view.findViewById(R.id.screenname);
        name = (TextView) view.findViewById(R.id.name);
        length = (TextView) view.findViewById(R.id.length);
        body = (EditText) view.findViewById(R.id.body);
        image = (ImageView) view.findViewById(R.id.profile_image);

        screenname.setText("");
        name.setText(user.name);
        body.setOnKeyListener(this);
        Picasso.with(getActivity())
                .load(user.picture.url)
                .error(R.drawable.images)
                .placeholder(R.drawable.placeholder)
                .centerInside()
                .noFade()
                .fit()
                .into(image);

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setPositiveButton("Fire!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                FacebookClient client = RestApplication.getRestClient();
                                progressable.setBusy();
                                client.post(body.getText().toString(),
                                        new MyJsonHttpResponseHandler(getActivity()) {
                                            @Override
                                            public void successCallBack(int statusCode, Header[] headers, Object data) {
                                                postSuccessDelegator.postSuccess();
                                                progressable.setFinish();
                                            }

                                            @Override
                                            public void errorCallBack() {
                                                progressable.setFinish();
                                            }
                                        }
                                );

                            }
                        }
                )
                .setView(view);

        return b.create();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        length.setText(String.valueOf(body.getText().toString().length()) + "/" + getActivity().getResources().getString(R.string.maxlenghtofatweet));
        return false;
    }
}
