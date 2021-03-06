package com.example.android.theguardiannews;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.app.SearchManager.QUERY;
import static android.content.ContentValues.TAG;
import static com.example.android.theguardiannews.WebQueryParameter.*;
import static com.example.android.theguardiannews.WebQueryParameter.API_KEY;
import static com.example.android.theguardiannews.WebQueryParameter.API_KEY_VALE;
import static com.example.android.theguardiannews.WebQueryParameter.DEAFAULT;
import static com.example.android.theguardiannews.WebQueryParameter.WBBMAIN;

/**
 * Created by 张俊秋 on 2017/3/30.
 */

public class GuardianNews {
    public final static  String QUERYTAG="querytag";
    private Context mContext;
    public GuardianNews(Context context){
        mContext=context;
    }
    private byte[] getUrlByte(String urlWebsite) throws IOException {
        URL url=new URL(urlWebsite);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setReadTimeout(15000);
        connection.setConnectTimeout(10000);
        try{
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            InputStream in=connection.getInputStream();
            if (connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage()+"with "+urlWebsite);
            }
            int byteRead=0;
            byte[] bytes=new byte[1024];
            while ((byteRead=in.read(bytes))>0){
                out.write(bytes,0,byteRead);
            }
            out.close();
            return out.toByteArray();
        }finally {
            connection.disconnect();
        }
    }
    private String getUrlString(String urlWebsite) throws IOException {
        return new String(getUrlByte(urlWebsite));
    }

    public List<NewsInfo> fetchUrlData(){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        String queryText=sharedPreferences.getString(QUERYTAG,DEAFAULT);
        String uri=Uri.parse(WBBMAIN).buildUpon()
                .appendQueryParameter(QUERY,queryText)
                .appendQueryParameter(API_KEY,API_KEY_VALE)
                .build().toString();
        Log.d(TAG, "fetchUrlData: "+uri);
        String json=null;
        try{
            json=getUrlString(uri);
        } catch (IOException e){
            Log.d(TAG, "fetchUrlData: can not get item.");
        }
        return parsingJson(json);
    }
    private List<NewsInfo> parsingJson(String json)  {
        NewsInfoLab newLab=NewsInfoLab.getNewsInfoLabInstance(mContext);
        List<NewsInfo> newsInfos=newLab.getNewsInfos();
        try {
            JSONObject main = new JSONObject(json);
            JSONObject respond=main.getJSONObject(RESPONSE);
            JSONArray results=respond.getJSONArray(RESULTS);
            for (int i=0;i<results.length();i++){
                JSONObject result=results.getJSONObject(i);
                String sectionName=result.getString(SECTIONNAME);
                String webPubDate=result.getString(WEBPUBLICATIONDATE);
                String webTitle=result.getString(WEBTITLE);
                String webUrl=result.getString(WEBURL);
                NewsInfo newsInfo=new NewsInfo(sectionName,webPubDate,webTitle,webUrl);
                newsInfos.add(newsInfo);
            }
        } catch (JSONException e) {
            Log.d(TAG, "parsingJson: problem with parseing json.");
        }
        Log.d(TAG,newsInfos.size()+"");
        return newsInfos;
    }
}
