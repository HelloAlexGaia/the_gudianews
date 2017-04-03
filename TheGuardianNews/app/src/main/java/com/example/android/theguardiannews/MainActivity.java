package com.example.android.theguardiannews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsInfo>>, SearchView.OnQueryTextListener {
    private int mLoader=1;
    private static final String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private ProgressBar mIndeterPro;
    private LoaderManager mLoaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager=getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment==null){
            fragment=new NewsFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment,TAG).commit();
        }
        mIndeterPro= (ProgressBar) findViewById(R.id.progress_indter);
        mLoaderManager=getSupportLoaderManager();
        mLoaderManager.initLoader(mLoader,null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item=menu.findItem(R.id.menu_search);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public Loader<List<NewsInfo>> onCreateLoader(int id, Bundle args) {
        showProgressBar(true);
        return new GetDataTask(getBaseContext());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsInfo>> loader, List<NewsInfo> data) {
        Fragment fragment=fragmentManager.findFragmentByTag(TAG);
        boolean judge=(fragment instanceof onDataFinish);
        if (judge){
            ((onDataFinish) fragment).dataFinished();
        }
        showProgressBar(false);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsInfo>> loader) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        List<NewsInfo> newsInfos=NewsInfoLab.getNewsInfoLabInstance(this).getNewsInfos();
        newsInfos.clear();
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(GuardianNews.QUERYTAG,query);
        editor.apply();
        mLoaderManager.restartLoader(mLoader,null,this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public interface onDataFinish{
        public void dataFinished();
    }
    private void showProgressBar(boolean state){
        if (state){
            mIndeterPro.setVisibility(View.VISIBLE);
        }else {
            mIndeterPro.setVisibility(View.GONE);
        }
    }
}
