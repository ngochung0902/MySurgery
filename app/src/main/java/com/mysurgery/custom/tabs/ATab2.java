package com.mysurgery.custom.tabs;

import android.os.Bundle;

import com.mysurgery.R;
import com.mysurgery.custom.CustomTab;
import com.mysurgery.screen.fragment.ReadyFragment;

/**
 * Created by Administrator on 9/19/2016.
 */
public class ATab2 extends CustomTab {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        setRootScreen();
    }

    @Override
    protected void setRootScreen() {
        ReadyFragment aTab2 = new ReadyFragment();
        setScreen(aTab2);
    }
}