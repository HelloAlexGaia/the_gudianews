package com.example.android.theguardiannews;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements MainActivity.onDataFinish {
    private List<NewsInfo> mNewsInfos;
    private RecyclerView mNewsList;
    private MyViewAdapter myViewAdapter;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mNewsList= (RecyclerView) view.findViewById(R.id.news_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mNewsList.setLayoutManager(linearLayoutManager);
        return view;
    }

    @Override
    public void dataFinished() {
        myViewAdapter=new MyViewAdapter();
        mNewsList.setAdapter(myViewAdapter);
    }

    @Override
    public void clearData() {
        mNewsList.setAdapter(null);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSection;
        private TextView mTitle;
        private TextView mDate;
        private String webUri;

        private MyViewHolder(View itemView) {
            super(itemView);
            mSection= (TextView) itemView.findViewById(R.id.textview_section);
            mTitle= (TextView) itemView.findViewById(R.id.textview_webtitle);
            mDate= (TextView) itemView.findViewById(R.id.textview_date);
        }
        private void bindData(int postion){
            NewsInfo newsInfo=mNewsInfos.get(postion);
            mSection.setText(newsInfo.getSection());
            mTitle.setText(newsInfo.getWebTitle());
            mDate.setText(newsInfo.getWebPublicationDate());
            webUri=newsInfo.getWebUrl();
            mTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri url=Uri.parse(webUri);
            intent.setData(url);
            startActivity(Intent.createChooser(intent,getString(R.string.choose_one_app)));
        }


    }
    public class MyViewAdapter extends RecyclerView.Adapter<MyViewHolder>{
        MyViewAdapter(){
            mNewsInfos=NewsInfoLab.getNewsInfoLabInstance(getContext()).getNewsInfos();
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getContext());
            View view=inflater.inflate(R.layout.news_item,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return mNewsInfos.size();
        }
    }
}
