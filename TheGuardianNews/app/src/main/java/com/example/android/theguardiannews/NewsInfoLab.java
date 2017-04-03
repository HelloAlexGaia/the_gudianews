package com.example.android.theguardiannews;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张俊秋 on 2017/3/31.
 */

public class NewsInfoLab {
    private static NewsInfoLab mNewsInfoLab;
    private List<NewsInfo> mNewsInfos;
    private NewsInfoLab(){
        mNewsInfos=new ArrayList<>();
    }
    public static NewsInfoLab getNewsInfoLabInstance(Context context){
        if (mNewsInfoLab==null){
            mNewsInfoLab=new NewsInfoLab();
        }
        return mNewsInfoLab;
    }

    public List<NewsInfo> getNewsInfos() {
        return mNewsInfos;
    }
}
