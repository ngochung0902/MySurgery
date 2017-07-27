package com.mysurgery.custom.tabs;

import android.os.Bundle;

import com.mysurgery.R;
import com.mysurgery.custom.CustomTab;
import com.mysurgery.screen.fragment.SurgeryFragment;

/**
 * Created by Administrator on 9/19/2016.
 */
public class ATab1 extends CustomTab {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        setRootScreen();
    }

    @Override
    protected void setRootScreen() {

        SurgeryFragment aTab1 = new SurgeryFragment();
        setScreen(aTab1);
    }
}