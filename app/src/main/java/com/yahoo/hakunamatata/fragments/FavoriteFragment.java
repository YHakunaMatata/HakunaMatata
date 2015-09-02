package com.yahoo.hakunamatata.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.adapters.FavoriteContentAdapter;
import com.yahoo.hakunamatata.dao.Post;
import com.yahoo.hakunamatata.dao.PostDao;
import com.yahoo.hakunamatata.interfaces.Progressable;
import com.yahoo.hakunamatata.lib.EndlessRecyclerOnScrollListener;
import com.yahoo.hakunamatata.lib.util;
import com.yahoo.hakunamatata.models.FacebookPaging;

import java.util.List;

import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;

/**
 * Created by jonaswu on 2015/9/2.
 */
public class FavoriteFragment extends BaseFragment {
    private FavoriteContentAdapter favoriteContentAdapter;
    private FacebookPaging facebookPaging;
    private LinearLayoutManager llm;
    private Progressable progressable;

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public FavoriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        final RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(
                recList,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view) {
                        // Do what you want when dismiss
                        int position = recList.getChildPosition(view);
                        Post post = favoriteContentAdapter.getItemWithRemove(position);
                        postDao.queryBuilder().where(PostDao.Properties.Id.eq(post.getId())
                        ).buildDelete().executeDeleteWithoutDetachingEntities();
                        daoSession.clear();
                        favoriteContentAdapter.notifyDataSetChanged();
                        util.showToast(getActivity(), getActivity().getResources().getString(R.string.delete_success));

                    }
                })
                .setIsVertical(false)
                .setItemTouchCallback(
                        new SwipeDismissRecyclerViewTouchListener.OnItemTouchCallBack() {
                            @Override
                            public void onTouch(int index) {
                                // Do what you want when item be touched
                            }
                        })
                .create();
        recList.setOnTouchListener(listener);

        recList.setOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                if (facebookPaging != null) {
                    initData(false);
                }
            }
        });

        favoriteContentAdapter = new FavoriteContentAdapter(getActivity());
        recList.setAdapter(favoriteContentAdapter);
        initData(true);
        return view;
    }

    private void initData(boolean isCleanAdapter) {
        if (isCleanAdapter) {
            favoriteContentAdapter.clearAll();
            facebookPaging = null;
        }
        bindToAdapter();
    }

    private void bindToAdapter() {

        List<com.yahoo.hakunamatata.dao.Post> posts = postDao.queryBuilder().list();
        favoriteContentAdapter.addWithPostList(posts);
        favoriteContentAdapter.notifyDataSetChanged();
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

    @Override
    public void reload() {
        initData(true);
    }
}
