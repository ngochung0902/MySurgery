package com.mysurgery.screen.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.crashlytics.android.Crashlytics;
import com.mysurgery.R;
import com.mysurgery.ZipFileContentProvider;
import com.mysurgery.utils.QTSRun;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Administrator on 4/12/2017.
 */

public class IntroduceActivity extends AppCompatActivity {
    private final static String EXP_PATH = "/Android/obb/";
    Button btnNext;
    VideoView view;
    List<String> arrListView = null;
    ImageView ivPause;
    PackageInfo pInfo = null;
    int versionCode = 1;
    private static final int PERMISSION_REQUEST_CODE = 121;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_introduce);

        view = (VideoView) findViewById(R.id.vdView);
        btnNext = (Button) findViewById(R.id.btnNext);
        ivPause = (ImageView) findViewById(R.id.ivPause);
        QTSRun.setLayoutView(btnNext, QTSRun.GetWidthDevice(getApplicationContext()) / 4, QTSRun.GetWidthDevice(getApplicationContext()) / 4 * 72 / 186);
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        QTSRun.setLayoutView(view, QTSRun.GetHeightDevice(getApplicationContext()), QTSRun.GetWidthDevice(getApplicationContext()));
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                view.setVideoURI(Uri.parse(getUriForVideo("my_surgery_0.mp4")));
                view.requestFocus();
                view.setVisibility(VideoView.VISIBLE);
                view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    public void onPrepared(MediaPlayer mp) {
                        view.start();
                        ivPause.setVisibility(View.GONE);
                    }
                });
            } else {
                requestPermission(); // Code for permission
            }
        } else {
            view.setVideoURI(Uri.parse(getUriForVideo("my_surgery_0.mp4")));
            view.requestFocus();
            view.setVisibility(VideoView.VISIBLE);
            view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    view.start();
                    ivPause.setVisibility(View.GONE);
//                throw new RuntimeException("This is a crash");
                }
            });
        }
//        QTSRun.showToast(getApplicationContext(), "Video Uri:" + getUriForVideo("my_surgery_0.mp4"));
//        view.setVideoURI(Uri.parse(getUriForVideo("my_surgery_0.mp4")));
//        view.requestFocus();
//        view.setVisibility(VideoView.VISIBLE);
//        view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//            public void onPrepared(MediaPlayer mp) {
//                view.start();
//                ivPause.setVisibility(View.GONE);
////                throw new RuntimeException("This is a crash");
//            }
//        });
        view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivPause.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
            }
        });
        arrListView = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            arrListView.add("0");
        }
        QTSRun.writeList(getApplicationContext(), arrListView, "arrList_sync");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QTSRun.setIsRegister(getApplicationContext(), true);
                Intent intent = new Intent(IntroduceActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ivPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.start();
                btnNext.setVisibility(View.GONE);
                ivPause.setVisibility(View.GONE);
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    view.pause();
                    btnNext.setVisibility(View.VISIBLE);
                    ivPause.setVisibility(View.VISIBLE);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private String getUriForVideo(String nameVideo) {
        String videoName = nameVideo;
        Uri contentUri = null;
        try {
            ZipFileContentProvider contentProvider = new ZipFileContentProvider();
            String authority = contentProvider.getAuthority();
            contentUri = Uri.parse("content://" + authority);
            ZipResourceFile expansionFile = //new ZipResourceFile(Environment.getExternalStorageDirectory() + "/Android/obb/com.mysurgery/main."+versionCode+".com.mysurgery.obb");
                    APKExpansionSupport
                            .getAPKExpansionZipFile(this, versionCode, versionCode);
            if (expansionFile != null) {
                Log.e("IntroduceActivity", "File: Ok");
                ZipResourceFile.ZipEntryRO[] ziro = expansionFile.getAllEntries();
                for (ZipResourceFile.ZipEntryRO entry : ziro) {
                    if (entry.mFileName.equals(nameVideo)) {
                        videoName = entry.mFileName;
                        Log.e("IntroduceActivity", "File: " + videoName);
                    }
                }
            } else {
                videoName = nameVideo;
            }
            if (videoName == null)
                videoName = nameVideo;
//            Log.e("IntroduceActivity", "File: null");
        } catch (IOException e) {
            Log.e("IntroduceActivity", "File not found!");
        }
//        QTSRun.ShowpopupMessage(IntroduceActivity.this, "VD Uri:" + contentUri + "/" + videoName);
        return contentUri + "/" + videoName;
    }

    @Override
    public void onPause() {
        super.onPause();
        view.suspend();
    }

    @Override
    public void onResume() {
        super.onResume();
        view.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.stopPlayback();
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
                    view.setVideoURI(Uri.parse(getUriForVideo("my_surgery_0.mp4")));
                    view.requestFocus();
                    view.setVisibility(VideoView.VISIBLE);
                    view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        public void onPrepared(MediaPlayer mp) {
                            view.start();
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
