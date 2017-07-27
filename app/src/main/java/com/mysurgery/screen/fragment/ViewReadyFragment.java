package com.mysurgery.screen.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.mysurgery.R;
import com.mysurgery.custom.CustomScreen;
import com.mysurgery.utils.FontUtils;
import com.mysurgery.utils.QTSConst;

/**
 * Created by Administrator on 4/13/2017.
 */

public class ViewReadyFragment extends CustomScreen {
    VideoView Videoview;
//    TextView toolbar_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_ready, container, false);

        this.initComponents(view);
        return view;
    }

    @Override
    protected void initComponents(View container) {
//        Toolbar toolbar = (Toolbar) container.findViewById(R.id.toolbar);
//        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        FontUtils.loadFont(getActivity(), QTSConst.FONT_LATO_REGULAR);
//        FontUtils.setFont(toolbar_title);
        FontUtils.setFonts((LinearLayout) container.findViewById(R.id.ll_group_details));
        FontUtils.loadFont(getActivity(), QTSConst.FONT_LATO_BOLD);
        FontUtils.setFont((TextView) container.findViewById(R.id.lb1));
        Videoview = (VideoView)container.findViewById(R.id.vdView);
//        Videoview.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.intro_surgeryapp));
//        Videoview.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        Videoview.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Videoview.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
