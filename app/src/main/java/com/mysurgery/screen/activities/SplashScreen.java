package com.mysurgery.screen.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.app.Activity;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.mysurgery.R;
import com.mysurgery.utils.QTSRun;
import com.mysurgery.utils.TrackGPS;
import com.mysurgery.utils.ZipHelper;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity implements LocationListener {
    // Location Variables
    private LocationManager locationManager;
    private final static int DISTANCE_UPDATES = 1;
    private final static int TIME_UPDATES = 5;
    private static final int PERMISSION_REQUEST_CODE = 1;
    ImageView ivLogo;
    TrackGPS gps;
    private Button btnReady;
    PackageInfo pInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);
        getWidthHeight();
        configureStatusBarTextColor();
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            DownloaderActivity.initialAPKVersionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!"mounted".equals(Environment.getExternalStorageState())) {

        }

        gps = new TrackGPS(this);
        btnReady = (Button) findViewById(R.id.btnReady);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        QTSRun.setLayoutView(ivLogo, QTSRun.GetWidthDevice(getApplicationContext()) * 2 / 3, QTSRun.GetWidthDevice(getApplicationContext()) * 2 / 3);
        QTSRun.setLayoutView(btnReady, QTSRun.GetWidthDevice(getApplicationContext()) / 3, QTSRun.GetWidthDevice(getApplicationContext()) / 3 * 70 / 333);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
        } else {
            requestPermission();
        }
        if (gps.canGetLocation()) {
            Log.e("SplasScreen", "DownloaderActivity.expansionFilesDelivered(this):" + DownloaderActivity.expansionFilesDelivered(getApplicationContext()));
            if (!DownloaderActivity.expansionFilesDelivered(getApplicationContext())) {

                final Intent downloadResourceIntent = new Intent(
                        SplashScreen.this,
                        DownloaderActivity.class);
                startActivity(downloadResourceIntent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                finish();
            }else {
                MyCountDelay timerCount = new MyCountDelay(2000, 1000);
                timerCount.start();
            }
        } else {
            showSettingsAlert();

        }

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this,
                        IntroduceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public class MyCountDelay extends CountDownTimer {
        public MyCountDelay(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {

            if (QTSRun.getIsRegister(getApplicationContext())) {
                if (QTSRun.getIsLogin(getApplicationContext())) {
                    Intent intent = new Intent(SplashScreen.this,
                            HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                btnReady.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }

    private void getWidthHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int wwidth = displaymetrics.widthPixels;
        QTSRun.SetWidthDevice(this, wwidth);
        QTSRun.SetHeightDevice(this, height);
    }

    private void configureStatusBarTextColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS Not Enabled");
        alertDialog.setMessage("Do you wants to turn On GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.e("SplasScreen", "DownloaderActivity.expansionFilesDelivered(this):" + DownloaderActivity.expansionFilesDelivered(getApplicationContext()));
                if (!DownloaderActivity.expansionFilesDelivered(getApplicationContext())) {
                    final Intent downloadResourceIntent = new Intent(
                            SplashScreen.this,
                            DownloaderActivity.class);
                    startActivity(downloadResourceIntent);
                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    finish();
                }else {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    MyCountDelay timerCount = new MyCountDelay(2000, 1000);
                    timerCount.start();
                }
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Log.e("SplasScreen", "DownloaderActivity.expansionFilesDelivered(this):" + DownloaderActivity.expansionFilesDelivered(getApplicationContext()));
                if (!DownloaderActivity.expansionFilesDelivered(getApplicationContext())) {
                    final Intent downloadResourceIntent = new Intent(
                            SplashScreen.this,
                            DownloaderActivity.class);
                    startActivity(downloadResourceIntent);
                    overridePendingTransition(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    finish();
                }else {
                    MyCountDelay timerCount = new MyCountDelay(2000, 1000);
                    timerCount.start();
                }
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    @Override
    public void onLocationChanged(Location location) {
    }

    /**
     * GPS turned off, stop watching for updates.
     *
     * @param provider contains data on which provider was disabled
     */
    @Override
    public void onProviderDisabled(String provider) {
        if (checkPermission()) {
            locationManager.removeUpdates(this);
        } else {
            requestPermission();
        }
    }

    /**
     * GPS turned back on, re-enable monitoring
     *
     * @param provider contains data on which provider was enabled
     */
    @Override
    public void onProviderEnabled(String provider) {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
        } else {
            requestPermission();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    /**
     * See if we have permissionf or locations
     *
     * @return boolean, true for good permissions, false means no permission
     */
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Request permissions from the user
     */
    private void requestPermission() {

        /**
         * Previous denials will warrant a rationale for the user to help convince them.
         */
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Toast.makeText(this, "This app relies on location data for it's main functionality. Please enable GPS data to access all features.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Monitor for permission changes.
     *
     * @param requestCode  passed via PERMISSION_REQUEST_CODE
     * @param permissions  list of permissions requested
     * @param grantResults the result of the permissions requested
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /**
                     * We are good, turn on monitoring
                     */
                    if (checkPermission()) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
                    } else {
                        requestPermission();
                    }
                } else {
                    /**
                     * No permissions, block out all activities that require a location to function
                     */
                    Toast.makeText(this, "Permission Not Granted.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
