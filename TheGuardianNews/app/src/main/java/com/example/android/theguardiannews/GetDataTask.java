package com.example.android.theguardiannews;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by 张俊秋 on 2017/3/30.
 */

public class GetDataTask extends AsyncTaskLoader<List<NewsInfo>> {
    private Context mContext;
    public GetDataTask(Context context) {
        super(context);
        mContext=context;
    }

    @Override
    public List<NewsInfo> loadInBackground() {
        return new GuardianNews(mContext).fetchUrlData();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
