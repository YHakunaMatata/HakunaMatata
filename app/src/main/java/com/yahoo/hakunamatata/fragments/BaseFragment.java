package com.yahoo.hakunamatata.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yahoo.hakunamatata.dao.DaoMaster;
import com.yahoo.hakunamatata.dao.DaoSession;
import com.yahoo.hakunamatata.dao.LikeDao;
import com.yahoo.hakunamatata.dao.PictureDao;
import com.yahoo.hakunamatata.dao.PostDao;
import com.yahoo.hakunamatata.dao.UserDao;
import com.yahoo.hakunamatata.interfaces.Reloadable;

/**
 * Created by jonaswu on 2015/9/2.
 */
public abstract class BaseFragment extends Fragment implements Reloadable {
    protected PostDao postDao;
    protected LikeDao likeDao;
    protected UserDao userDao;
    protected PictureDao pictureDao;
    protected DaoSession daoSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDb();
    }

    protected void initDb() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "greendao", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        postDao = daoSession.getPostDao();
        likeDao = daoSession.getLikeDao();
        userDao = daoSession.getUserDao();
        pictureDao = daoSession.getPictureDao();
    }
}
