package com.example.android.theguardiannews;

/**
 * Created by 张俊秋 on 2017/3/31.
 */

public class NewsInfo {
    private String mSection;
    private String mWebPublicationDate;
    private String mWebTitle;
    private String mWebUrl;
    public NewsInfo(String section,String webPublicationDate,String webTitle,String webUrl){
        mSection=section;
        mWebPublicationDate=webPublicationDate;
        mWebTitle=webTitle;
        mWebUrl=webUrl;
    }

    public String getSection() {
        return mSection;
    }

    public String getWebPublicationDate() {
        return mWebPublicationDate;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getWebUrl() {
        return mWebUrl;
    }
}
