package com.yahoo.hakunamatata.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.melnykov.fab.FloatingActionButton;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.fragments.SubmitFragment;
import com.yahoo.hakunamatata.interfaces.Progressable;
import com.yahoo.hakunamatata.interfaces.Reloadable;

import is.arontibo.library.ElasticDownloadView;
import is.arontibo.library.ProgressDownloadView;

/**
 * Created by jonaswu on 2015/8/30.
 */
public abstract class BaseActivity extends AppCompatActivity implements Progressable, Reloadable, SubmitFragment.PostSuccessDelegator {


    private ElasticDownloadView mElasticDownloadView;
    private RelativeLayout rl;
    private boolean isBusy = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpActionBar();
        setBusy();
    }

    @Override
    public void setBusy() {
        if (rl.getVisibility() == View.GONE) {
            rl.setVisibility(View.VISIBLE);
            mElasticDownloadView.startIntro();
            mElasticDownloadView.setProgress(0);
            isBusy = true;
            this.invalidateOptionsMenu();
        }
    }

    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        // toolbar.inflateMenu(R.menu.menu_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rl = (RelativeLayout) findViewById(R.id.elastic_download_view_container);
        mElasticDownloadView = (ElasticDownloadView) findViewById(R.id.elastic_download_view);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPostDialog();
            }
        });
    }

    @Override
    public void setFinish() {
        if (rl.getVisibility() == View.VISIBLE) {
            mElasticDownloadView.setProgress(100);
            mElasticDownloadView.success();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBusy = false;
                    BaseActivity.this.invalidateOptionsMenu();
                    mElasticDownloadView.setProgress(0);
                    rl.setVisibility(View.GONE);
                }
            }, ProgressDownloadView.ANIMATION_DURATION_BASE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (isBusy == false) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        } else {
            // in busy state we do not show any element
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    protected void showPostDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SubmitFragment alertDialog = SubmitFragment.newInstance(this);
        alertDialog.show(fm, "filter");
    }
}
