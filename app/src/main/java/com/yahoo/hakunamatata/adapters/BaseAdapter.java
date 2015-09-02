package com.yahoo.hakunamatata.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.dao.DaoMaster;
import com.yahoo.hakunamatata.dao.DaoSession;
import com.yahoo.hakunamatata.dao.LikeDao;
import com.yahoo.hakunamatata.dao.PictureDao;
import com.yahoo.hakunamatata.dao.PostDao;
import com.yahoo.hakunamatata.dao.UserDao;
import com.yahoo.hakunamatata.fragments.VideoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonaswu on 2015/9/2.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected PostDao postDao;
    protected LikeDao likeDao;
    protected UserDao userDao;
    protected PictureDao pictureDao;
    protected List<T> postList = new ArrayList<>();
    protected Context context;


    protected void initDb() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "greendao", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        postDao = daoSession.getPostDao();
        likeDao = daoSession.getLikeDao();
        userDao = daoSession.getUserDao();
        pictureDao = daoSession.getPictureDao();
    }


    public BaseAdapter(Context context) {
        this.context = context;
        initDb();
    }


    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    @Override
    public abstract int getItemViewType(int position);

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void addWithPostList(List<T> postList) {
        for (T post : postList) {
            this.postList.add(post);
        }
    }

    public void clearAll() {
        postList.clear();
        this.notifyDataSetChanged();
    }

    public static class JokeHolder extends RecyclerView.ViewHolder {
        ImageView archive;
        ImageView image;
        LinearLayout item_action_panel;
        TextView like;
        TextView userName;
        TextView message;
        ImageView profileImage;
        View view;

        public JokeHolder(View view) {
            super(view);
            this.view = view;
            userName = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            profileImage = (ImageView) view.findViewById(R.id.profile_image);
            like = (TextView) view.findViewById(R.id.like);
            image = (ImageView) view.findViewById(R.id.main_image);
            item_action_panel = (LinearLayout) view.findViewById(R.id.item_action_panel);
            archive = (ImageView) view.findViewById(R.id.archive);
        }
    }

    protected void showVideoDialog(String url) {
        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        VideoFragment videoFragment = VideoFragment.newInstance(url, true);
        videoFragment.show(fm, "fragment_edit_name");
    }

}
