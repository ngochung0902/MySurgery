/**
 * Project    : Tingle
 * Author     : TuanCuong
 * Date        : Dec 7, 2012
 **/
package com.mysurgery.custom;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.mysurgery.R;

public abstract class CustomTab extends FragmentActivity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected abstract void setRootScreen();

	protected void setScreen(CustomScreen screen) {
		this.getSupportFragmentManager().beginTransaction()
				.add(R.id.main_frag, screen).commitAllowingStateLoss();
	}

	public void backToRoot() {
		for (int i = 0; i < getSupportFragmentManager()
				.getBackStackEntryCount(); i++) {
			getSupportFragmentManager().popBackStack();
		}
	}

	public void resumeScreen() {
		onResume();
//		startAppAd.onResume();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			
			if (this.getSupportFragmentManager().getBackStackEntryCount() != 0)
				return false;
				finish();
				return true;
//			AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
//			builder.setMessage("Thoát ứng dụng?")
//					.setCancelable(false)
//					.setPositiveButton("Có",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int id) {
//									startAppAd.onBackPressed();
//									finish();
//								}
//							})
//					.setNegativeButton("Không",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int id) {
//									return;
//								}
//							});
//			AlertDialog alert = builder.create();
//			alert.show();

			
		}
		return super.onKeyDown(keyCode, event);
	}
}
