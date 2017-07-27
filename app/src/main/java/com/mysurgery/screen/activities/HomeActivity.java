package com.mysurgery.screen.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.mysurgery.R;
import com.mysurgery.custom.CustomTab;
import com.mysurgery.custom.tabs.ATab1;
import com.mysurgery.custom.tabs.ATab2;
import com.mysurgery.custom.tabs.ATab3;
import com.mysurgery.custom.tabs.ATab4;
import com.mysurgery.utils.QTSConst;
import com.mysurgery.utils.QTSRun;

@SuppressWarnings("deprecation")
public class HomeActivity extends TabActivity {
	public static TabHost tabHost;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);

		initTabs();


		this.getTabWidget().getChildAt(0)
				.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						QTSConst.iTab = 0;
						getTabHost().setCurrentTab(0);
						String tabTag = getTabHost().getCurrentTabTag();
						CustomTab activity = (CustomTab) getLocalActivityManager()
								.getActivity(tabTag);
						activity.backToRoot();
					}
				});

		this.getTabHost().getTabWidget().getChildAt(1)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						QTSConst.iTab = 1;
						getTabHost().setCurrentTab(1);
						String tabTag = getTabHost().getCurrentTabTag();
						CustomTab activity = (CustomTab) getLocalActivityManager()
								.getActivity(tabTag);
						activity.backToRoot();
					}
				});
		this.getTabHost().getTabWidget().getChildAt(2)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// setNumber("");
						QTSConst.iTab = 2;
						getTabHost().setCurrentTab(2);
						String tabTag = getTabHost().getCurrentTabTag();
						CustomTab activity = (CustomTab) getLocalActivityManager()
								.getActivity(tabTag);
						activity.backToRoot();

					}
				});

		this.getTabWidget().getChildAt(3)
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						QTSConst.iTab = 3;
						getTabHost().setCurrentTab(3);
						String tabTag = getTabHost().getCurrentTabTag();
						CustomTab activity = (CustomTab) getLocalActivityManager()
								.getActivity(tabTag);
						activity.backToRoot();
					}
				});
		tabHost.setCurrentTab(0);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@SuppressWarnings("deprecation")
	private void initTabs() {
		addTab("My Surgery", R.drawable.tab_1, ATab1.class, 0, true);
		addTab("Getting ready", R.drawable.tab_2, ATab2.class, 0, true);
		addTab("Contact", R.drawable.tab_3,ATab3.class, 0, true);
		addTab("Getting there", R.drawable.tab_4, ATab4.class, 0, true);

	}

	@SuppressWarnings("deprecation")
	private void addTab(String label, int drawableId, Class<?> c, int number,
						boolean isNormal) {
		tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + label);
		View tabIndicator;

		tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator_normal, getTabWidget(), false);

		ImageView v_ic = (ImageView) tabIndicator.findViewById(R.id.icon_tab);
		v_ic.setBackgroundResource(drawableId);
		QTSRun.setLayoutView(v_ic,
				QTSRun.GetWidthDevice(getApplicationContext()) / 7,
				QTSRun.GetWidthDevice(getApplicationContext()) / 7);
		QTSRun.setLayoutView(tabIndicator,
				QTSRun.GetWidthDevice(getApplicationContext()) / 4,
				(int) (QTSRun.GetWidthDevice(getApplicationContext()) / 6));
		spec.setIndicator(tabIndicator);

		spec.setContent(intent);
		tabHost.addTab(spec);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@SuppressWarnings("unused")
	private void setLayoutButtons(Button btn, int w, int h) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				w, h);
		btn.setLayoutParams(layoutParams);
	}

	protected void setFontTV(TextView tv, String font) {
		try {
			Typeface face = Typeface.createFromAsset(this.getAssets(), font);
			tv.setTypeface(face);
		} catch (Exception e) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		// unregister listener
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		releaseCamera();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

