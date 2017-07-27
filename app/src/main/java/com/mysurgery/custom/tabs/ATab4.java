package com.mysurgery.custom.tabs;

import android.os.Bundle;

import com.mysurgery.R;
import com.mysurgery.custom.CustomTab;
import com.mysurgery.screen.fragment.MapViewFragment;

/**
 * Created by Administrator on 9/19/2016.
 */
public class ATab4 extends CustomTab {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        setRootScreen();
    }

    @Override
    protected void setRootScreen() {
        MapViewFragment aTab4 = new MapViewFragment();
        setScreen(aTab4);
    }

}