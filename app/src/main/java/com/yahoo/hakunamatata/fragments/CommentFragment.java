package com.yahoo.hakunamatata.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.activities.RestApplication;
import com.yahoo.hakunamatata.adapters.CommentAdapter;
import com.yahoo.hakunamatata.interfaces.Progressable;
import com.yahoo.hakunamatata.lib.EndlessRecyclerOnScrollListener;
import com.yahoo.hakunamatata.models.Comment;
import com.yahoo.hakunamatata.models.FacebookPaging;
import com.yahoo.hakunamatata.network.FacebookClient;
import com.yahoo.hakunamatata.network.MyJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonaswu on 2015/9/3.
 */
public class CommentFragment extends DialogFragment {
    private CommentAdapter commentAdapter;
    private Progressable progressable;
    private SwipeRefreshLayout swipeContainer;
    private FacebookPaging facebookPaging;
    private LinearLayoutManager llm;

    public static CommentFragment newInstance(Progressable progressable, String id) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        fragment.setArguments(args);
        fragment.setProgressable(progressable);
        return fragment;
    }

    public CommentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reply_fragment, container, false);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(0, 0, 0, 0);
        swipeContainer.setProgressBackgroundColor(android.R.color.transparent);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CommentFragment.this.initData(true);
            }
        });


        RecyclerView recList = (RecyclerView) view.findViewById(R.id.replylist);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        recList.addOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                if (facebookPaging != null) {
                    initData(false);
                }
            }
        });

        commentAdapter = new CommentAdapter(getActivity());
        recList.setAdapter(commentAdapter);
        initData(true);

        // Close

        view.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void initData(boolean isCleanAdapter) {
        if (isCleanAdapter) {
            commentAdapter.clearAll();
            facebookPaging = null;
        }
        FacebookClient client = RestApplication.getRestClient();
        progressable.setBusy();
        client.getCommentsOfObject(getArguments().getString("id"), facebookPaging, new MyJsonHttpResponseHandler(getActivity()) {
            @Override
            public void successCallBack(int statusCode, Header[] headers, Object data) {
                Log.e("data", data.toString());
                JSONObject dataJSON = (JSONObject) data;
                bindToAdapter(dataJSON);
                swipeContainer.setRefreshing(false);
                progressable.setFinish();
            }

            @Override
            public void errorCallBack() {

            }
        });
    }

    private void bindToAdapter(JSONObject data) {
        List<Comment> commentList = new ArrayList<>();
        try {
            JSONArray dataArray = data.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject postJSON = dataArray.getJSONObject(i);
                Comment comment = Comment.fromJSON(postJSON.toString());
                commentList.add(comment);
            }

            try {
                if (dataArray.length() == Integer.valueOf(this.getResources().getString(R.string.limit_of_api_return))) {
                    JSONObject pagingJSON = data.getJSONObject("paging");
                    Gson gson = new GsonBuilder().create();
                    facebookPaging = gson.fromJson(pagingJSON.toString(), FacebookPaging.class);
                } else {
                    facebookPaging = null;
                }
            } catch (Exception e) {
                facebookPaging = null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        commentAdapter.addWithPostList(commentList);
        commentAdapter.notifyDataSetChanged();
    }

    public void setProgressable(Progressable progressable) {
        this.progressable = progressable;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.fragment_reply);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }
}
