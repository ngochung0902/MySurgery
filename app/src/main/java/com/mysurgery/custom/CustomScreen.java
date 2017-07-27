/*
 * Project : Sex Coupons
 * Author  : TuanCuong
 * Date    : Oct 16, 2013 - 2013 
 * Company : Mobioneer Co., Ltd
 */
package com.mysurgery.custom;


import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mysurgery.R;
import com.mysurgery.utils.QTSRun;
import com.mysurgery.screen.activities.HomeActivity;


/**
 * @author TuanCuong
 * 
 */
@SuppressWarnings("deprecation")
public abstract class CustomScreen extends Fragment {
	private ProgressDialog progressDialog;

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (hidden == false) {
			this.onResume();
		}
	}

	protected void back() {
		this.getActivity().getSupportFragmentManager().popBackStack();
	}

	public void startNewScreen(CustomScreen current, CustomScreen next) {
		// Animation ani = AnimationUtils.loadAnimation(getActivity(),
		// R.anim.slide_right_to_left);
		// this.getView().setAnimation(ani);

		current.onPause();
		FragmentManager fm = this.getActivity().getSupportFragmentManager();
		FragmentTransaction fs = fm.beginTransaction();
		fs.add(R.id.main_frag, next);
		fs.hide(current);
		fs.addToBackStack(null);
		fs.commit();
	}
	public void startNewScreen2(CustomScreen current, CustomScreen next) {
		// Animation ani = AnimationUtils.loadAnimation(getActivity(),
		// R.anim.slide_right_to_left);
		// this.getView().setAnimation(ani);

		current.onPause();
		FragmentManager fm = this.getActivity().getSupportFragmentManager();
		FragmentTransaction fs = fm.beginTransaction();
		fs.replace(R.id.main_frag, next);
		fs.hide(current);
		fs.addToBackStack(null);
		fs.commit();
	}

	@SuppressWarnings("deprecation")
	protected void startNewScreenAtOtherTab(CustomScreen new_screen, int tabId) {
		TabActivity main = (HomeActivity) this.getActivity().getParent();

		main.getTabHost().setCurrentTab(tabId);

		FragmentManager fm = ((CustomTab) main.getCurrentActivity())
				.getSupportFragmentManager();
		FragmentTransaction fs = fm.beginTransaction();
		fs.add(R.id.main_frag, new_screen);
		fs.addToBackStack(null);
		fs.commit();
	}

	protected void setFontTV(TextView tv, String font) {
		try {
			Typeface face = Typeface.createFromAsset(this.getActivity()
					.getAssets(), font);
			tv.setTypeface(face);
		} catch (Exception e) {
			Log.d("ERROR set FONTS", e.getMessage());
		}
	}
	
	protected void setFontButton(Button tv, String font) {
		try {
			Typeface face = Typeface.createFromAsset(this.getActivity()
					.getAssets(), font);
			tv.setTypeface(face);
		} catch (Exception e) {
			Log.d("ERROR set FONTS", e.getMessage());
		}
	}

	protected void setFontEditText(EditText tv, String font) {
		try {
			Typeface face = Typeface.createFromAsset(this.getActivity()
					.getAssets(), font);
			tv.setTypeface(face);
		} catch (Exception e) {
			Log.d("ERROR set FONTS", e.getMessage());
		}
	}
	

	/**
	 * Inits the components. Wayne NGUYEN
	 * 
	 * @param container
	 *            is the root view and init all elements.
	 */
	protected abstract void initComponents(View container);

	public void showLoading(boolean isShow) {
		if (isShow) {
			if (progressDialog != null)
				progressDialog.dismiss();
			progressDialog = ProgressDialog.show(getActivity(), null,
					"Loading...");
			// progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		} else {
			if (progressDialog != null)
				progressDialog.dismiss();
		}
	}
	public void showUpLoading(boolean isShow) {
		if (isShow) {
			if (progressDialog != null)
				progressDialog.dismiss();
			progressDialog = ProgressDialog.show(getActivity(), null,
					"Uploading...");
			// progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		} else {
			if (progressDialog != null)
				progressDialog.dismiss();
		}
	}
//	public void ShowDialogDelete(){
////		if (LakRun.getIsDelCard(getActivity())) {
////			LakRun.setIsDelCard(getActivity(), false);
//			final Dialog dialog = new Dialog(getActivity());
//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////			dialog.setTitle(getResources().getString(R.string.app_name));
//			dialog.setContentView(R.layout.dialog_del_card);
//			TextView tvContent = (TextView) dialog
//					.findViewById(R.id.text_dialog);
//			Button btncan = (Button) dialog.findViewById(R.id.btn_done);
//			LakRun.setFontTV(getActivity(), tvContent, LakConst.FONT_LATO_REGULAR);
//			btncan.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					
//					dialog.cancel();
//					dialog.dismiss();
//					back();
//				}
//			});
//			
//			dialog.setCancelable(false);
//			dialog.show();
//		}
	
	protected void setTopView(LinearLayout llTop) {
		int widthView = QTSRun.GetWidthDevice(getActivity());
		llTop.getLayoutParams().height = (int) (widthView / 4.4);
		llTop.getLayoutParams().width = widthView;
		llTop.requestLayout();
	}

	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		
	}



}
