package com.yahoo.hakunamatata.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.activities.RestApplication;
import com.yahoo.hakunamatata.adapters.JokeContentAdapter;
import com.yahoo.hakunamatata.interfaces.Progressable;
import com.yahoo.hakunamatata.lib.EndlessRecyclerOnScrollListener;
import com.yahoo.hakunamatata.models.FacebookPaging;
import com.yahoo.hakunamatata.models.Post;
import com.yahoo.hakunamatata.network.FacebookClient;
import com.yahoo.hakunamatata.network.MyJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;

public class JokeFragment extends Fragment {

    private JokeContentAdapter contentAdapter;
    private Progressable progressable;
    private SwipeRefreshLayout swipeContainer;
    private FacebookPaging facebookPaging;
    private LinearLayoutManager llm;

    public static JokeFragment newInstance() {
        JokeFragment fragment = new JokeFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public JokeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_joke, container, false);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // swipeContainer.setColorSchemeColors(0, 0, 0, 0);
        swipeContainer.setProgressBackgroundColor(android.R.color.transparent);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                JokeFragment.this.initData(true);
            }
        });


        RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        recList.setOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                if (facebookPaging != null) {
                    initData(false);
                }
            }
        });

        contentAdapter = new JokeContentAdapter(getActivity());
        recList.setAdapter(contentAdapter);
        initData(true);
        return view;
    }

    private void initData(boolean isCleanAdapter) {
        if (isCleanAdapter) {
            contentAdapter.clearAll();
            facebookPaging = null;
        }
        FacebookClient client = RestApplication.getRestClient();
        progressable.setBusy();
        client.getPosts(facebookPaging, new MyJsonHttpResponseHandler(getActivity()) {
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
        List<Post> posts = new ArrayList<>();
        try {
            JSONArray dataArray = data.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject postJSON = dataArray.getJSONObject(i);
                Post post = Post.fromJSON(postJSON.toString());
                posts.add(post);
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
        contentAdapter.addWithPostList(posts);
        contentAdapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(getActivity() instanceof Progressable)) {
            Log.e("error", "activity should implement Progressable interface");
        }
        progressable = (Progressable) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        progressable = null;
    }
}
