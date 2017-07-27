package com.mysurgery.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mysurgery.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author QTS
 */
public class QTSRun {
    private static ProgressDialog progressDialog;
    // Flag
    private static final float MAX_IMAGE_DIMENSION = 1280;

    public static void setLayoutView(View view, int width, int height) {
        view.getLayoutParams().width = width;
        view.getLayoutParams().height = height;
    }

    public static void CallPhone(Context activity, String phone) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(callIntent);
            return;
        }
    }

    public static void createDialog(Context context, String title,
                                    boolean isShow) {
        if (isShow) {
            if (progressDialog != null)
                progressDialog.dismiss();
            progressDialog = ProgressDialog.show(context, title, "Loading...");
        } else
            progressDialog.dismiss();
    }

    ///////// Set Font
    public static void setFontTV(Context context, TextView tv, String font) {
        try {
            Typeface face = Typeface.createFromAsset(context
                    .getAssets(), font);
            tv.setTypeface(face);
        } catch (Exception e) {
            Log.d("ERROR set FONTS", e.getMessage());
        }
    }

    public static void setFontEditText(Context context, EditText tv, String font) {
        try {
            Typeface face = Typeface.createFromAsset(context
                    .getAssets(), font);
            tv.setTypeface(face);
        } catch (Exception e) {
            Log.d("ERROR set FONTS", e.getMessage());
        }
    }

    public static void setFontButton(Context context, Button tv, String font) {
        try {
            Typeface face = Typeface.createFromAsset(context
                    .getAssets(), font);
            tv.setTypeface(face);
        } catch (Exception e) {
            Log.d("ERROR set FONTS", e.getMessage());
        }
    }

    public static void ShowpopupMessage(Activity activity, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage(message);
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();

    }

    // Show Toast
    @SuppressWarnings("WrongConstant")
    public static void showToast(Context context, String str_message) {
        Toast toast = Toast.makeText(context, str_message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 50);
        toast.setDuration(QTSConst.TIME_WAIT);
        toast.show();
    }

    // -------- share app ---------
    public static void shareApp(Context context, String text) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Share from " + text);
        share.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(share, "Share"));
    }

    // Send E-mail
    public static void sendFeedback(Context context, String emailAddress,
                                    String title) {
        final Intent emailIntent = new Intent(
                Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");//message/rfc822
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);

        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
//		emailIntent.setData(Uri.parse("mailto:"+emailAddress)); // or just "mailto:" for blank
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(Intent.createChooser(emailIntent,
                    "Complete action using"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(null, "There are no email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Share via E-mail
    public static void shareViaEmail(Context context, String content,
                                     String title) {
        final Intent emailIntent = new Intent(
                Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        try {
            context.startActivity(Intent.createChooser(emailIntent,
                    "Complete action using"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(null, "There are no email clients installed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Share via SMS
    public static void shareViaSMS(Context context, String content) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", content);
        context.startActivity(sendIntent);
    }

    // Share via MMS
    public static void sendMMS(Context context, String patch) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setClassName("com.android.mms",
                "com.android.mms.ui.ComposeMessageActivity");
        sendIntent.putExtra("sms_body", "Content");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(patch));
        sendIntent.setType("image/png");
        context.startActivity(sendIntent);
    }

    // Check INTERNET connection
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Go to web page
    public static void goWebPage(Activity context, String URL) {
        Intent browse = new Intent(Intent.ACTION_VIEW);
        browse.setData(Uri.parse(URL));
        context.startActivity(browse);
    }

    // Go to Google Play store
    public static void goGooglePlayStore(Context context, String packageName) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri
                    .parse("http://play.google.com/store/apps/details?id="
                            + packageName));
            context.startActivity(intent);
        }
    }

    // Go to Amazon appstore
    public static void goAmazonAppStore(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.amazon.com/gp/mas/dl/android?p="
                + packageName));
        context.startActivity(intent);
    }

    public static void savePointObj(Context context, String object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("pointObject", object);
        editor.commit();
    }

    public static String getPointObj(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getString("pointObject", "");
    }

    public static void setSecret(Context context, String secret) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("secret", secret);
        editor.commit();
    }

    public static String getToken(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getString("token", "");
    }

    public static void setToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.commit();
    }

    public static String getTokenhash(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getString("tokenhash", "");
    }

    public static void setTokenhash(Context context, String tokenhash) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("tokenhash", tokenhash);
        editor.commit();
    }

    // //////////////////////////
    public static Bitmap getBitmapfromURL(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) (new URL(url))
                    .openConnection();
            conn.connect();
            return BitmapFactory.decodeStream(conn.getInputStream());
        } catch (Exception ex) {
            return null;
        }
    }

    // ///////////
    public static void SetRemind(Context context, Boolean num) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("remind", num);
        editor.commit();
    }

    public static boolean GetRemind(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getBoolean("remind", false);
    }


    // /////////////////////////// Height device
    public static int GetHeightDevice(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getInt("height_device", 800);
    }

    public static void SetHeightDevice(Context context, int num) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putInt("height_device", num);
        editor.commit();
    }

    // /////////////////////////// Width device
    public static int GetWidthDevice(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getInt("width_device", 480);
    }

    public static void SetWidthDevice(Context context, int num) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putInt("width_device", num);
        editor.commit();
    }

    // ///////////////////////////If this is the first time the app open
    public static void SetIsFirstTimeOpenApp(Context context, Boolean num) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("first_time_open", num);
        editor.commit();
    }

    public static boolean GetIsFirstTimeOpenApp(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getBoolean("first_time_open", true);
    }

    //// save base 64 of image
    public static String GetImagebase64(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getString("Imagebase64", "");
    }

    public static void SetImagebase64(Context context, String content) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("Imagebase64", content);
        editor.commit();
    }

    // ////////////////////////////////////////////////
    //////////////////check email /////////////////
    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean checkEmailCorrect(String Email) {
        String pttn = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern p = Pattern.compile(pttn);
        Matcher m = p.matcher(Email);

        if (m.matches()) {
            return true;
        }
        return false;
    }

    public static boolean checkPassword(String pass) {
        String pttn = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,30})";
        Pattern p = Pattern.compile(pttn);
        Matcher m = p.matcher(pass);

        if (m.matches()) {
            return true;
        }
        return false;
    }

    ////////////get Date ///////////////
    public static Date getDate(String dt) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            return formatter.parse(dt);
        } catch (Exception e) {
            return new Date();
        }
    }

    public static String getDate(Date dt) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            return formatter.format(dt);
        } catch (Exception e) {
            return "";
        }
    }

    ////////////////////save form information ///////////////
    public static int getAuthId(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getInt("authId", 0);
    }

    public static void setAuthId(Context context, int authId) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putInt("authId", authId);
        editor.commit();
    }

    public static String getDeviceTokenID(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("DeviceTokenID", "");
    }

    public static void setDeviceTokenID(Context context, String DeviceTokenID) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("DeviceTokenID", DeviceTokenID);
        editor.commit();
    }

    public static String getAccessToken(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("accessToken", "");
    }

    public static void setAccessToken(Context context, String accessToken) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("accessToken", accessToken);
        editor.commit();
    }

    public static String getLatitude(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("latitude", "0");
    }

    public static void setLatitude(Context context, String latitude) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("latitude", latitude);
        editor.commit();
    }

    public static String getLongitude(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("longitude", "0");
    }

    public static void setLongitude(Context context, String longitude) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("longitude", longitude);
        editor.commit();
    }

    public static String getHospital(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("hospital", "");
    }

    public static void setHospital(Context context, String hospital) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("hospital", hospital);
        editor.commit();
    }

    public static String getContactHop(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("ContactHop", "");
    }

    public static void setContactHop(Context context, String ContactHop) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("ContactHop", ContactHop);
        editor.commit();
    }

    public static String getTypeSurgery(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("TypeSurgery", "");
    }

    public static void setTypeSurgery(Context context, String TypeSurgery) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("TypeSurgery", TypeSurgery);
        editor.commit();
    }

    public static String getProcedure(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("Procedure", "");
    }

    public static void setProcedure(Context context, String Procedure) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("Procedure", Procedure);
        editor.commit();
    }

    public static String getUrlHPT(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("UrlHPT", "");
    }

    public static void setUrlHPT(Context context, String UrlHPT) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("UrlHPT", UrlHPT);
        editor.commit();
    }

    public static String getDateOfSurgery(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("DateOfSurgery", "");
    }

    public static void setDateOfSurgery(Context context, String DateOfSurgery) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("DateOfSurgery", DateOfSurgery);
        editor.commit();
    }

    public static int getPositionChoose1(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getInt("PositionChoose1", 0);
    }

    public static void setPositionChoose1(Context context, int PositionChoose1) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putInt("PositionChoose1", PositionChoose1);
        editor.commit();
    }

    public static int getPositionChoose2(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getInt("PositionChoose2", 0);
    }

    public static void setPositionChoose2(Context context, int PositionChoose2) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putInt("PositionChoose2", PositionChoose2);
        editor.commit();
    }

    public static int getPositionDate(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getInt("PositionDate", 2);
    }

    public static void setPositionDate(Context context, int PositionDate) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putInt("PositionDate", PositionDate);
        editor.commit();
    }

    public static int getPositionSpinner(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getInt("PositionSpinner", 0);
    }

    public static void setPositionSpinner(Context context, int PositionSpinner) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putInt("PositionSpinner", PositionSpinner);
        editor.commit();
    }

    public static String getDates(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("date_surgery", "");
    }

    public static void setDates(Context context, String date_surgery) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("date_surgery", date_surgery);
        editor.commit();
    }

    public static String getDateSurgery(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("DateSurgery", "0");
    }

    public static void setDateSurgery(Context context, String DateSurgery) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("DateSurgery", DateSurgery);
        editor.commit();
    }

    public static String getPhoneNumber1(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("PhoneNumber1", "");
    }

    public static void setPhoneNumber1(Context context, String PhoneNumber1) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("PhoneNumber1", PhoneNumber1);
        editor.commit();
    }

    public static String getPhoneNumber2(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("PhoneNumber2", "");
    }

    public static void setPhoneNumber2(Context context, String PhoneNumber2) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("PhoneNumber2", PhoneNumber2);
        editor.commit();
    }

    public static String getPhoneNumber3(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("PhoneNumber3", "");
    }

    public static void setPhoneNumber3(Context context, String PhoneNumber3) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("PhoneNumber3", PhoneNumber3);
        editor.commit();
    }

    public static String getLname(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("Lname", "");
    }

    public static void setLname(Context context, String lname) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("Lname", lname);
        editor.commit();
    }

    public static String getemail(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("Email", "");
    }

    public static void setemail(Context context, String email) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("Email", email);
        editor.commit();
    }

    public static String getProfileUrl(Context context) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        return sharedPre.getString("ProfileUrl", "");
    }

    public static void setProfileUrl(Context context, String profileUrl) {
        SharedPreferences sharedPre = context.getSharedPreferences(
                QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedPre.edit();
        editor.putString("ProfileUrl", profileUrl);
        editor.commit();
    }

    public static void writeList(Context context, List<String> list, String prefix) {
        SharedPreferences prefs = context.getSharedPreferences(QTSConst.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int size = prefs.getInt(prefix + "_size", 0);

        // clear the previous data if exists
        for (int i = 0; i < size; i++)
            editor.remove(prefix + "_" + i);

        // write the current list
        for (int i = 0; i < list.size(); i++)
            editor.putString(prefix + "_" + i, list.get(i));

        editor.putInt(prefix + "_size", list.size());
        editor.commit();
    }

    public static List<String> readList(Context context, String prefix) {
        SharedPreferences prefs = context.getSharedPreferences(QTSConst.PREFERENCES, Context.MODE_PRIVATE);

        int size = prefs.getInt(prefix + "_size", 0);

        List<String> data = new ArrayList<String>(size);
        for (int i = 0; i < size; i++)
            data.add(prefs.getString(prefix + "_" + i, null));

        return data;
    }

    // /////////////////////////
    public static boolean getIsRegister(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getBoolean("isRegister", false);
    }

    public static void setIsRegister(Context context, boolean isRegister) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("isRegister", isRegister);
        editor.commit();
    }

    // /////////////////////////
    public static boolean getIsCheck(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getBoolean("ischeck", false);
    }

    public static void setIsCheck(Context context, boolean ischeck) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("ischeck", ischeck);
        editor.commit();
    }

    //===========login with facebook ==========
    public static boolean getIsFBLogin(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getBoolean("islogin", false);
    }

    public static void setIsFBLogin(Context context, boolean islogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("islogin", islogin);
        editor.commit();
    }

    //===========login with twitter ==========
    public static boolean getIsLogin(Context context) {
        int mode = Activity.MODE_PRIVATE;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, mode);
        return sharedPreferences.getBoolean("isTwlogin", false);
    }

    public static void setIsLogin(Context context, boolean isTwlogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                QTSConst.PREFERENCES, 0);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("isTwlogin", isTwlogin);
        editor.commit();
    }

    public static int checkRotation(Uri uri) throws IOException {
        ExifInterface ei = new ExifInterface(uri.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    public static Bitmap scaleDown(Bitmap realImage,
                                   boolean filter) {
        float ratio = Math.min(
                (float) MAX_IMAGE_DIMENSION / realImage.getWidth(),
                (float) MAX_IMAGE_DIMENSION / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static Bitmap scaleImage(Context context, Uri photoUri) throws IOException, FileNotFoundException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    public static Bitmap getclip(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        int radius;
        if (bitmap.getWidth() > bitmap.getHeight())
            radius = bitmap.getHeight() / 2;
        else radius = bitmap.getWidth() / 2;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getRect(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        int radius;
        if (bitmap.getWidth() > bitmap.getHeight())
            radius = bitmap.getHeight() / 2;
        else radius = bitmap.getWidth() / 2;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap changeBitmapColor(Bitmap src, int color) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        // config paint
        final Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        paint.setColorFilter(filter);
//		paint.setAlpha(alpha);
        canvas.drawBitmap(src, 0, 0, paint);
        return transBitmap;
    }

    public static String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("MMM dd, yyyy, HH:mm");
        return format.format(date);
    }

    public static File getFiles(Bitmap bitmap, String file_name) {
        File file = null;
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            file = new File(path, file_name + ".jpg");
            fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Point getDisplaySize(WindowManager windowManager) {
        try {
            if (Build.VERSION.SDK_INT > 16) {
                Display display = windowManager.getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
            } else {
                return new Point(0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Point(0, 0);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static String loadJSONFromAsset(Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open("hospital_infor.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //	public static Bitmap getThumbNailUrl(Context mContext){
//		Bitmap thumb = null;
//		Uri videoURI = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.intro_surgeryapp);
//		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//		retriever.setDataSource(mContext.getApplicationContext(), videoURI);
//		thumb = retriever.getFrameAtTime(10, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
//		return scaleDownThumbnail(thumb, true);
//	}
//	public static Bitmap getThumbNailUrl(Context mContext, int video){
//		Bitmap thumb = null;
//		Uri videoURI = Uri.parse("android.resource://" + mContext.getPackageName() + "/" + video);
//		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//		retriever.setDataSource(mContext.getApplicationContext(), videoURI);
//		thumb = retriever.getFrameAtTime(10, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
//		return scaleDownThumbnail(thumb, true);
//	}
    public static Bitmap scaleDownThumbnail(Bitmap realImage,
                                            boolean filter) {
        float ratio = Math.min(
                (float) 360 / realImage.getWidth(),
                (float) 360 / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
//	public static int getDurationLength(Context mContext) {
//		MediaPlayer mp = MediaPlayer.create(mContext, R.raw.intro_surgeryapp);
//		return mp.getDuration();
//	}
//	public static int getDurationLength(Context mContext, int video) {
//		MediaPlayer mp = MediaPlayer.create(mContext, video);
//		return mp.getDuration();
//	}
//	public static String getDurationVideo(int totalSecs){
//		int minutes = (totalSecs % 3600) / 60;
//		int seconds = totalSecs % 60;
//		return minutes+":"+seconds;
//	}
}
