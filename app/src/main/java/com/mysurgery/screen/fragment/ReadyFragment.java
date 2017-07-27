package com.mysurgery.screen.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mysurgery.R;
import com.mysurgery.custom.CustomScreen;
import com.mysurgery.screen.activities.ViewReadyActivity;
import com.mysurgery.utils.FontUtils;
import com.mysurgery.utils.QTSConst;
import com.mysurgery.utils.QTSRun;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 4/13/2017.
 */

public class ReadyFragment extends CustomScreen {
    TextView toolbar_title;
    ListView lv;
    List<String> arrListView = null;
    private String[] arrTitle ={"Introduction", "Waiting times", "Medications", "Fasting", "Wellness", "Who will be at home", "What to bring", "Getting to Hospital"};
    int[] thumbnailVideos = {R.drawable.v1, R.drawable.v2, R.drawable.v3, R.drawable.v4, R.drawable.v5, R.drawable.v6, R.drawable.v7, R.drawable.v8_wollong, R.drawable.v8_shellharbour, R.drawable.v8_shoalhaven };
    String[] durationVideos = {"00:43","00:47","00:46","00:41","00:41","00:48","00:35", "1:04", "00:59","00:56"};
    ReadyListAdapter adapter;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ready, container, false);

        this.initComponents(view);
        return view;
    }
    @Override
    protected void initComponents(View container) {
        Toolbar toolbar = (Toolbar) container.findViewById(R.id.toolbar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        FontUtils.loadFont(getActivity(), QTSConst.FONT_SANSPRO_LIGHT);
        FontUtils.setFont(toolbar_title);
        QTSRun.setFontTV(getActivity(),(TextView)container.findViewById(R.id.lbtopvideo), QTSConst.FONT_SANSPRO_LIGHT);
        lv = (ListView) container.findViewById(R.id.lvReady);

        arrListView = new ArrayList<String>();
        if (QTSRun.readList(getActivity(), "arrList_sync") != null) {
            arrListView = QTSRun.readList(getActivity(), "arrList_sync");
        }
        adapter = new ReadyListAdapter(getActivity(), arrListView, arrTitle, thumbnailVideos, durationVideos);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvVideoDetails = (TextView) view.findViewById(R.id.tvTitle);
//                arrListView.set(position,"1");
//                QTSRun.writeList(getActivity(), arrListView, "arrList_sync");
//                adapter.setData(arrListView);
                Intent intent = new Intent(getActivity(),ViewReadyActivity.class);
                intent.putExtra("title", arrTitle[position]);
                if (position == 7){
                    if (QTSRun.getHospital(getActivity()).equalsIgnoreCase("Wollongong Hospital")){
                        intent.putExtra("position", 7);
                        startActivity(intent);
                    }else if (QTSRun.getHospital(getActivity()).equalsIgnoreCase("Shellharbour Hospital")){
                        intent.putExtra("position", 8);
                        startActivity(intent);
                    }else {
                        intent.putExtra("position", 9);
                        startActivity(intent);
                    }
                }else {
                    intent.putExtra("position", position);
                    startActivity(intent);
                }


            }
        });
    }
    public class ReadyListAdapter extends BaseAdapter {
        Context context;

        private String[] arrVideos ;
        List<String> arrSticked;
        LayoutInflater inflater;
        int[] thumbnailVideos;
        String[] durationVideos;
        String[] arrContents;


        public ReadyListAdapter(Context context, List<String> arrSticked, String[] arrContents, int[] thumbnailVideos, String[] durationVideos) {
            this.context = context;
            this.arrSticked = arrSticked;
            this.arrContents = arrContents;
            this.thumbnailVideos = thumbnailVideos;
            this.durationVideos = durationVideos;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arrVideos = context.getResources().getStringArray(R.array.arr_content_video);
        }

        @Override
        public int getCount() {
            return arrContents.length;
        }

        @Override
        public Object getItem(int position) {
           return arrContents[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        public class ViewHolder {
            TextView tvIntroduc , answer, tvDuration;
            ImageView ivView, ivTicked, ivPause;
        }
        @Override
        public View getView(final int position, View converview, ViewGroup container) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            if (converview == null) {
                holder = new ViewHolder();
                converview = inflater.inflate(R.layout.row_ready_list, container, false);
                holder.tvIntroduc = (TextView) converview.findViewById(R.id.tvIntro);
                holder.answer = (TextView) converview.findViewById(R.id.tvTitle);
                holder.tvDuration = (TextView) converview.findViewById(R.id.tvDuration);
                holder.ivView = (ImageView) converview.findViewById(R.id.ivImageView);
                holder.ivPause = (ImageView) converview.findViewById(R.id.ivPause);
                holder.ivTicked = (ImageView) converview.findViewById(R.id.ivTicked);
                converview.setTag(holder);
            } else {
                holder = (ViewHolder) converview.getTag();
            }
            holder.tvIntroduc.setText(Html.fromHtml("<u>"+arrContents[position]+"</u>"));
            holder.answer.setText(arrVideos[position]);
            if (position >= 7){
                if (QTSRun.getHospital(context).equalsIgnoreCase("Wollongong Hospital")){
                    holder.ivView.setBackgroundResource(thumbnailVideos[7]);
                    holder.tvDuration.setText(durationVideos[7]);
                }else if (QTSRun.getHospital(context).equalsIgnoreCase("Shellharbour Hospital")){
                    holder.ivView.setBackgroundResource(thumbnailVideos[8]);
                    holder.tvDuration.setText(durationVideos[8]);
                }else {
                    holder.ivView.setBackgroundResource(thumbnailVideos[9]);
                    holder.tvDuration.setText(durationVideos[9]);
                }
            }else {
                holder.ivView.setBackgroundResource(thumbnailVideos[position]);
                holder.tvDuration.setText(durationVideos[position]);
            }
//            thumbnail = QTSRun.getThumbNailUrl(context, arrMp4Videos[position]);
//            duration = QTSRun.getDurationVideo(QTSRun.getDurationLength(context, arrMp4Videos[position]));
//            holder.ivView.setImageBitmap(thumbnail);
//            holder.tvDuration.setText(""+duration);

            if (arrSticked.get(position).equalsIgnoreCase("1")){
                holder.ivTicked.setVisibility(View.VISIBLE);
            }else holder.ivTicked.setVisibility(View.GONE);
            QTSRun.setFontTV(getActivity(), holder.answer, QTSConst.FONT_SANSPRO_LIGHT);
            QTSRun.setFontTV(getActivity(), holder.tvDuration, QTSConst.FONT_LATO_REGULAR);
            QTSRun.setLayoutView(holder.ivView, QTSRun.GetWidthDevice(context)*2/5, QTSRun.GetWidthDevice(context)*2/5 *145/232);
            QTSRun.setLayoutView(holder.ivPause, QTSRun.GetWidthDevice(context)/8, QTSRun.GetWidthDevice(context)/8);
            return converview;
        }
        public void setData(List<String> arrSticked) {
            this.arrSticked = arrSticked;
            this.notifyDataSetChanged();
        }

    }

}
