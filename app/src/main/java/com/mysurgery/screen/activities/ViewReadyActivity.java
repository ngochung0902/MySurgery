package com.mysurgery.screen.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.mysurgery.R;
import com.mysurgery.ZipFileContentProvider;
import com.mysurgery.custom.CustomScreen;
import com.mysurgery.custom.VideoViewCustom;
import com.mysurgery.utils.FontUtils;
import com.mysurgery.utils.QTSConst;
import com.mysurgery.utils.QTSRun;

import java.io.IOException;

/**
 * Created by Administrator on 4/13/2017.
 */

public class ViewReadyActivity extends Activity {
private static final int PERMISSION_REQUEST_CODE = 122;
    VideoViewCustom Videoview;
    TextView toolbar_title, tvDetails;
    ImageView ivPause;
    String titleVideo = "";
    String[] arrVideos = {"<h4>Introduction</h4><p>Every week, thousands of Australians have surgery, and you're one of them, which means you need to be prepared.</p>The <em><strong>My Surgery Journey</strong></em> app provides information about the time before surgery. You should use it together with your family and talk about the information. The app will help you get ready for surgery, remind you of important dates and help you get to the hospital. <p>Ask your local doctor or surgeon if you have any questions.</p> <p>If you're late to hospital, miss your appointment, or haven't followed your doctor's instructions your surgery may be delayed or even cancelled.</p> <p>When surgery is cancelled it's not possible to simply bring in the next patient, as the theatre has been set up just for you.</p> So, make the most of your <em><strong>My Surgery Journey</strong></em> app to ensure you're ready for surgery.",
            "<h4>Waiting times</h4><p>Surgery waiting times can be long. It may take days, weeks or even months from the time you first visit your GP to when you have your surgery.</p><p>Many things can happen to change your waiting time so the hospital will only give you an estimated waiting time until they really know when to contact you stating the exact time of your surgery.</p>It is important to let the Hospital know if you no longer require surgery or have already had it elsewhere.",
    "<h4>Medications</h4><p>Any medicines you're taking could react with the anaesthetic or other medicines you may receive during surgery. It's important to stop taking some medicines several weeks before surgery. Your doctor will tell you exactly what medicines to stop and when.</p><p>Remember, medicine is a science, not guesswork.</p><p>If you are not sure about something, simply call your doctor.</p><p>It's even important to tell your doctor if you are taking natural remedies such as fish oil, and as good as the human memory can be, it's far from perfect, so be sure to mark your medicine stop times in a diary.</p>If you take any blood thinning medicines you must get special instructions from your doctor or the hospital.",
    "<h4>Fasting</h4><p>For most surgery you'll require medicine to go to sleep. When you're asleep your throat muscles relax and any food in your stomach can escape and get into your lungs.</p><p>That's why, it's important to stop eating anything at least 6 hoursbefore your surgery.</p><p>Hospital staff will contact you in the days before your surgery with exact instructions.</p><p>Fasting means going without all food and liquid. Sometimes in hospital we call this “Nil By Mouth”.  Fasting is needed before general anaesthesia or sedation.</p>    •  You can have solid food until 6 hours before surgery - this should only be a light meal.<br>    •  Do not chew gum or lollies- these count as food because they cause the stomach to produce extra acid.<br>    •  Adult patients can have clear fluids until 2 hours before surgery. This should be no more than 2 cups or 400mls. <br>    •  Ask when you should fast from.",
    "<h4>Wellness</h4><p>The whole point of surgery is to help you get well. So it makes sense that you might not feel like turning somersaults in the weeks before your procedure. Nevertheless, do try to do some light exercise every day. It'll help you recover faster after surgery.</p><p>It's also important to be clean. Having a bath or shower before coming in reduces the risk of infection during your procedure.</p><p>If you are unwell within 3 days before your surgery you will need to call the hospital or contact your local doctor for advice. If you are too unwell for surgery, it may be postponed until you are feeling better and it is safer for you to have an anaesthetic. </p><p>If you have any of the following please call us: </p>    •  Temperature or fever- feeling hot or cold 	<br>    •  Sore throat 	<br>    •  Rash or swelling 	<br>    •  Feel unwell 	<br>    •  A cut, break or tear in your skin 	<br>    •  Any infected wounds 	<br>    •  Diarrhoea or vomiting 	<br>    •  A recent unplanned visit to the Emergency Department or local doctor",
    "<h4>Who will be at home</h4><p>If you are cleared to go home, you must have someone stay with you the night after your surgery.</p><p>If you have had a General Anaesthetic or sedation you will need to have a responsible adult take you home and stay overnight with you. This is for your own safety as you may be lightheaded and drowsy. Your ability to do tasks may be affected. A small amount of anaesthetic may still be in your body.</p><p>You must not drive home after surgery. It is not safe.</p><p>If you don't have a family member or friend able to pick you up and stay with you please tell the hospital staff. We want you to have your surgery but we also want you to be safe afterwards. </p>",
    "<h4>What to bring</h4><p>Almost everything you need for your hospital stay will be supplied for you. If you are staying overnight, all you need to bring is a single small bag with toiletries, the medicine you are taking and pyjamas.</p><p>Bring to hospital:</p>    •  Two (2) pairs of clean pyjamas/nighties (labelled with your name).	<br>    •  Dressing gown and slippers (non-slip sole)	<br>    •  Toiletries (soap, toothpaste, toothbrush, hairbrush/comb, razor, tissues) 	<br>    •  Small amount of money under $10 (for phone or newspaper) <br>    •  Current medicines (these will be returned to you when you go home) and a list of ALL of your medicines	<br>    •  Comfortable clean day clothing	<br>    •  Glasses, hearing aids and non-electric walking aids (labelled with your name)	<br>    •  Any letters from your doctors	<br>    •  All relevant x-rays, scans and blood test results	<br>    •  Medicare card and (if applicable) private health insurance fund card/book, veterans affairs repat card, details of workers compensation, public liability or third party case<p>Theft does occur in hospitals.</p><p>Do NOT bring to hospital:</p>	<br>    •  Valuables (any jewellery or large amounts of money over $10) 	<br>    •  Radios without headphones	<br>    •  Mobile phones (they can get lost or become a target for thieves)	<br>    •  Electrical appliances (including electric shavers)	<br>    •  Pot plants or flowers	<br>    •  Large bags or excessive clothing- bring only the bare minimum	<br>    •  Alcohol or illegal drugs	<br>    •  Video games",
    "<h4>Getting to Hospital</h4><p>We understand that sometimes people run late, but running too late could mean that your surgery is postponed.</p><p>Hospitals are complex and busy places. Sometimes just finding the right entrance can be tricky.</p><p>The hospital will tell you which entrance to use and where it is.</p><p>Hospital car parks are busy too. So leave plenty of time to drive to hospital and some extra time to find a park.</p><p>If you're unsure, consider taking a taxi or getting a lift.</p><p><strong>Wollongong Hospital</strong></p><p>The Main Entrance with a space for patient drop off is via Loftus Street, Wollongong.</p><p>There are two ways to get into the car park off Dudley Street. </p><p>There are also two ways to get into the car park off New Dapto Road.</p><p>There is a fee to park with the price displayed at the entrance. Disabled and concession parking up to 3 hours is free - check at the entry and at the ticket machine on Level 4 for more information.</p><p>Very little free parking is available on the streets around Wollongong hospital. Please check the signposts as there are strict time limits.</p>",
    "<h4>Getting to Hospital</h4><p>We understand that sometimes people run late, but running too late could mean that your surgery is postponed.</p><p>Hospitals are complex and busy places. Sometimes just finding the right entrance can be tricky.</p><p>The hospital will tell you which entrance to use and where it is.</p><p>Hospital car parks are busy too. So leave plenty of time to drive to hospital and some extra time to find a park.</p><p>If you're unsure, consider taking a taxi or getting a lift.</p><p><strong>Shellharbour Hospital</strong></p><p>The Main Entrance with a space for patient drop off is via 15-17 Madigan Boulevard, Mount Warrigal. </p><p>You can access the hospital car park via Madigan Boulevard, Mount Warrigal.</p><p>Disabled parking is available.</p><p>Some parking is available on the streets around the hospital. Please check the signposts as there may be time limits.</p>",
    "<h4>Getting to Hospital</h4><p>We understand that sometimes people run late, but running too late could mean that your surgery is postponed.</p><p>Hospitals are complex and busy places. Sometimes just finding the right entrance can be tricky.</p><p>The hospital will tell you which entrance to use and where it is.</p><p>Hospital car parks are busy too. So leave plenty of time to drive to hospital and some extra time to find a park.</p><p>If you're unsure, consider taking a taxi or getting a lift.</p><p><strong>Shoalhaven Hospital</strong></p><p>The Main Entrance with a space for patient drop off is via Scenic Drive, Nowra.</p><p>You can access the hospital car park via Scenic Drive.</p><p>Disabled parking is available. </p><p>Some parking is available on the streets around the hospital. Please check the signposts as there are time limits.</p>"};
    Toolbar toolbar;
    int position = 0;
    String[] arrMp4Videos = {"my_surgery_0.mp4", "my_surgery_1.mp4", "my_surgery_2.mp4", "my_surgery_3.mp4", "my_surgery_4.mp4", "my_surgery_5.mp4", "my_surgery_6.mp4", "surgery_8_wollongong.mp4", "surgery_8_shellharbour.mp4", "surgery_8_shoalhaven.mp4" };
    int displayHeight, displayWidth;
    PackageInfo pInfo = null;
    int versionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        position = getIntent().getIntExtra("position", 0);
        titleVideo =  getIntent().getStringExtra("title");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tvDetails = (TextView) findViewById(R.id.tvDetails);
        Videoview = (VideoViewCustom) findViewById(R.id.vdView);
        ivPause = (ImageView) findViewById(R.id.ivPause);
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!"mounted".equals(Environment.getExternalStorageState())) {

        }
        displayWidth = QTSRun.GetWidthDevice(getApplicationContext());
        displayHeight = QTSRun.GetHeightDevice(getApplicationContext());
        toolbar_title.setText(titleVideo.toUpperCase());
        tvDetails.setText((Html.fromHtml(arrVideos[position])));
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                if (position >= 7){
                    if (QTSRun.getHospital(getApplicationContext()).equalsIgnoreCase("Wollongong Hospital")){
//                QTSRun.showToast(getApplicationContext(), "VDeo Uri:" + getUriForVideo(arrMp4Videos[7]));
                        Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[7])));
                    }else if (QTSRun.getHospital(getApplicationContext()).equalsIgnoreCase("Shellharbour Hospital")){
                        Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[8])));
                    }else {
                        Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[9])));
                    }
                }else {
                    Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[position])));
                }

                Videoview.requestFocus();
                Videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    public void onPrepared(MediaPlayer mp) {
                        Videoview.start();
                        ivPause.setVisibility(View.GONE);
                    }
                });
            } else {
                requestPermission(); // Code for permission
            }
        } else {
            if (position >= 7){
                if (QTSRun.getHospital(getApplicationContext()).equalsIgnoreCase("Wollongong Hospital")){
//                QTSRun.showToast(getApplicationContext(), "VDeo Uri:" + getUriForVideo(arrMp4Videos[7]));
                    Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[7])));
                }else if (QTSRun.getHospital(getApplicationContext()).equalsIgnoreCase("Shellharbour Hospital")){
                    Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[8])));
                }else {
                    Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[9])));
                }
            }else {
                Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[position])));
            }

            Videoview.requestFocus();
            Videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    Videoview.start();
                    ivPause.setVisibility(View.GONE);
                }
            });
        }

        FontUtils.loadFont(getApplicationContext(), QTSConst.FONT_SANSPRO_LIGHT);
        FontUtils.setFonts((LinearLayout) findViewById(R.id.ll_group_details));
        FontUtils.loadFont(getApplicationContext(), QTSConst.FONT_SANSPRO_LIGHT);
        FontUtils.setFont(toolbar_title);
        Videoview.setDimensions(displayWidth, displayHeight*2/5);
        Videoview.getHolder().setFixedSize(displayWidth, displayHeight*2/5);
        Videoview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Videoview.pause();
                    ivPause.setVisibility(View.VISIBLE);
                    return true;
                }else{
                    return false;
                }
            }
        });
        Videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivPause.setVisibility(View.VISIBLE);
            }
        });
        ivPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Videoview.start();
                    ivPause.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        Videoview.suspend();
    }

    @Override
    public void onResume() {
        super.onResume();
        Videoview.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Videoview.stopPlayback();
    }
    public int getScreenOrientation()
    {
        Display getOrient = this.getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            Videoview.setDimensions(displayHeight, displayWidth);
            Videoview.getHolder().setFixedSize(displayHeight, displayWidth);
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
            Videoview.setDimensions(displayWidth, displayHeight*2/5);
            Videoview.getHolder().setFixedSize(displayWidth, displayHeight*2/5);

        }
    }
    private String getUriForVideo(String nameVideo) {
        Uri contentUri = null;
        String videoName = nameVideo;

        try {
            ZipFileContentProvider contentProvider = new ZipFileContentProvider();
            String authority = contentProvider.getAuthority();
            contentUri = Uri.parse("content://" + authority);
            ZipResourceFile expansionFile = //new ZipResourceFile(Environment.getExternalStorageDirectory() + "/Android/obb/com.mysurgery/main."+versionCode+".com.mysurgery.obb");
					APKExpansionSupport
					.getAPKExpansionZipFile(this, versionCode, versionCode);
            if (expansionFile != null) {
                Log.e("IntroduceActivity", "File: Ok" );
                ZipResourceFile.ZipEntryRO[] ziro = expansionFile.getAllEntries();
                for (ZipResourceFile.ZipEntryRO entry : ziro) {
                    if (entry.mFileName.equals(nameVideo)) {
                        videoName = entry.mFileName;
                        Log.e("IntroduceActivity", "File: " +videoName);
                    }
                }
            }else {
                videoName = nameVideo;
            }
            if (videoName == null)
                videoName = nameVideo;
            Log.e("IntroduceActivity", "File: null" );
        } catch (IOException e) {
            Log.e("IntroduceActivity", "File not found!");
        }
        return contentUri + "/" + videoName;
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store video. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                    if (position >= 7){
                        if (QTSRun.getHospital(getApplicationContext()).equalsIgnoreCase("Wollongong Hospital")){
//                QTSRun.showToast(getApplicationContext(), "VDeo Uri:" + getUriForVideo(arrMp4Videos[7]));
                            Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[7])));
                        }else if (QTSRun.getHospital(getApplicationContext()).equalsIgnoreCase("Shellharbour Hospital")){
                            Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[8])));
                        }else {
                            Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[9])));
                        }
                    }else {
                        Videoview.setVideoURI(Uri.parse(getUriForVideo(arrMp4Videos[position])));
                    }

                    Videoview.requestFocus();
                    Videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        public void onPrepared(MediaPlayer mp) {
                            Videoview.start();
                            ivPause.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                    finish();
                }
                break;
        }
    }
}
