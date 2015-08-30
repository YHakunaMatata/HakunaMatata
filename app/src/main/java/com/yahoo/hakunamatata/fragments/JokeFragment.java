package com.yahoo.hakunamatata.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.activities.RestApplication;
import com.yahoo.hakunamatata.adapters.ContentAdapter;
import com.yahoo.hakunamatata.element.Post;
import com.yahoo.hakunamatata.network.FacebookClient;
import com.yahoo.hakunamatata.network.MyJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JokeFragment extends ListingFragment {

    private OnFragmentInteractionListener mListener;
    private ContentAdapter contentAdapter;

    public static JokeFragment newInstance() {
        JokeFragment fragment = new JokeFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public JokeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookClient client = RestApplication.getRestClient();
        client.getPosts(new MyJsonHttpResponseHandler(getActivity()) {
            @Override
            public void successCallBack(int statusCode, Header[] headers, Object data) {
                JSONObject dataJSON = (JSONObject) data;
                bindToAdapter(dataJSON);
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
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .registerTypeAdapter(Post.From.Picture.class, new Post.Deserializer<Post.From.Picture>() {
                            @Override
                            public JsonElement getDeserializeData(JsonElement je) {
                                return je.getAsJsonObject().get("data");
                            }
                        })
                        .create();
                Post post = gson.fromJson(postJSON.toString(), Post.class);
                posts.add(post);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        contentAdapter.setPostList(posts);
        contentAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_joke, container, false);
        RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        contentAdapter = new ContentAdapter(getActivity());
        recList.setAdapter(contentAdapter);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
