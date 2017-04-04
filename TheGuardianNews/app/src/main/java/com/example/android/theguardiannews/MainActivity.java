package com.example.android.theguardiannews;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsInfo>>, SearchView.OnQueryTextListener {
    private int mLoader=1;
    private static final String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private ProgressBar mIndeterPro;
    private LoaderManager mLoaderManager;
    private TextView mNoNetOrData;
    private int showProBar=1;
    private int showNetState=2;
    private int showNothing=3;
    private int showNoData=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialData();
        if (getNetWorkState()){
            mLoaderManager.initLoader(mLoader,null,this);
        }else {
            showState(showNetState);
        }
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
        showState(showProBar);
        return new GetDataTask(getBaseContext());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsInfo>> loader, List<NewsInfo> data) {
        if (data.size()==0){
            showState(showNoData);
        }else {
            Fragment fragment=fragmentManager.findFragmentByTag(TAG);
            if (fragment instanceof onDataFinish){
                ((onDataFinish) fragment).dataFinished();
            }
            showState(showNothing);
        }
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
        if (getNetWorkState()){
            mLoaderManager.restartLoader(mLoader,null,this);
        }else {
            showState(showNetState);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public interface onDataFinish{
        public void dataFinished();
        public void clearData();
    }
    private void showState(int state){
        if (state==showProBar){
            mIndeterPro.setVisibility(View.VISIBLE);
            mNoNetOrData.setVisibility(View.GONE);
        }else if (state==showNetState){
            mIndeterPro.setVisibility(View.GONE);
            mNoNetOrData.setText(R.string.no_internet);
            Drawable drawable=getResources().getDrawable(R.drawable.no_internet);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            mNoNetOrData.setCompoundDrawables(null,drawable,null,null);
            mNoNetOrData.setVisibility(View.VISIBLE);
        }else if (state==showNoData){
            mIndeterPro.setVisibility(View.GONE);
            mNoNetOrData.setText(R.string.no_news);
            Drawable drawable=getResources().getDrawable(R.drawable.data);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            mNoNetOrData.setCompoundDrawables(null,drawable,null,null);
            mNoNetOrData.setVisibility(View.VISIBLE);
        }else {
            mNoNetOrData.setVisibility(View.GONE);
            mIndeterPro.setVisibility(View.GONE);
        }
    }
    private void initialData(){
        fragmentManager=getSupportFragmentManager();
        Fragment fragment=fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment==null){
            fragment=new NewsFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment,TAG).commit();
        }
        mIndeterPro= (ProgressBar) findViewById(R.id.progress_indter);
        mLoaderManager=getSupportLoaderManager();
        mNoNetOrData = (TextView) findViewById(R.id.textview_no_internet);

    }
    private boolean getNetWorkState(){
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo=connectivityManager.getActiveNetworkInfo();
        if (activeInfo!=null&&activeInfo.isConnectedOrConnecting()){
            return true;
        }
        return false;
    }
}
