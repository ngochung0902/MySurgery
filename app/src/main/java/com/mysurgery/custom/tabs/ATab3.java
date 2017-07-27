package com.mysurgery.custom.tabs;

import android.os.Bundle;

import com.mysurgery.R;
import com.mysurgery.custom.CustomTab;
import com.mysurgery.screen.fragment.ContactFragment;

/**
 * Created by Administrator on 9/19/2016.
 */
public class ATab3 extends CustomTab {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        setRootScreen();
    }

    @Override
    protected void setRootScreen() {
        ContactFragment aTab3 = new ContactFragment();
        setScreen(aTab3);
    }
}