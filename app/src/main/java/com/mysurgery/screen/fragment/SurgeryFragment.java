package com.mysurgery.screen.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mysurgery.R;
import com.mysurgery.custom.CustomScreen;
import com.mysurgery.utils.FontUtils;
import com.mysurgery.utils.QTSConst;
import com.mysurgery.utils.QTSRun;
import com.mysurgery.screen.activities.MainActivity;
import com.mysurgery.utils.TrackGPS;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Administrator on 4/13/2017.
 */

public class SurgeryFragment extends CustomScreen implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, View.OnClickListener {
    TextView toolbar_title, tvLongLat;
    TextView tvDistance, tvDays, tvAbout, tvGetReady, tvDetail;
    LinearLayout ll_Termination, ll_Distance, ll_Ready, ll_Days, ll_ChangeDetails, ll_mapview;
    MapView mMapView;
    private GoogleMap googleMap;
    LatLng locHospital = null;
    LatLng myLocation;//= new LatLng(16.0496484,108.206854);
    String distance = "0 mins";
    double longitude;
    double latitude;
    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;
    private long FASTEST_INTERVAL = 2000;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_surgery, container, false);
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        ll_mapview = (LinearLayout) view.findViewById(R.id.ll_mapview);
//        QTSRun.setLayoutView(ll_mapview, QTSRun.GetWidthDevice(getActivity()), QTSRun.GetHeightDevice(getActivity()) * 2 / 5);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
//        gps = new TrackGPS(getActivity());
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >=23){
            if (!checkPermission()) {

                requestPermission();
            } else {
                checkLocation();
            }
        }else {
            checkLocation();
        }
        this.initComponents(view);
        return view;
    }

    @Override
    protected void initComponents(View container) {
        Toolbar toolbar = (Toolbar) container.findViewById(R.id.toolbar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        tvLongLat = (TextView) container.findViewById(R.id.tvLongLat);
        FontUtils.loadFont(getActivity(), QTSConst.FONT_SANSPRO_LIGHT);
        FontUtils.setFont(toolbar_title);
        ll_Days = (LinearLayout) container.findViewById(R.id.ll_Days);
        ll_Distance = (LinearLayout) container.findViewById(R.id.ll_Distance);
        ll_Ready = (LinearLayout) container.findViewById(R.id.ll_Ready);
        ll_Termination = (LinearLayout) container.findViewById(R.id.ll_Termination);
        ll_ChangeDetails = (LinearLayout) container.findViewById(R.id.ll_ChangeDetails);

        QTSRun.setLayoutView(mMapView, QTSRun.GetWidthDevice(getActivity()), QTSRun.GetHeightDevice(getActivity()) * 2 / 5);
        tvDistance = (TextView) container.findViewById(R.id.tvDistance);
        tvDays = (TextView) container.findViewById(R.id.tvDays);
        tvAbout = (TextView) container.findViewById(R.id.tvAbout);
        tvGetReady = (TextView) container.findViewById(R.id.tvGetReady);
        tvDetail = (TextView) container.findViewById(R.id.tvDetail);
        QTSRun.setFontTV(getActivity(), tvDistance, QTSConst.FONT_SANSPRO_LIGHT);
        QTSRun.setFontTV(getActivity(), tvDays, QTSConst.FONT_SANSPRO_LIGHT);
        QTSRun.setFontTV(getActivity(), tvAbout, QTSConst.FONT_SANSPRO_LIGHT);
        QTSRun.setFontTV(getActivity(), tvGetReady, QTSConst.FONT_SANSPRO_LIGHT);
        QTSRun.setFontTV(getActivity(), tvDetail, QTSConst.FONT_SANSPRO_LIGHT);
        locHospital = new LatLng(Double.parseDouble(QTSRun.getLatitude(getActivity())), Double.parseDouble(QTSRun.getLongitude(getActivity())));

        if (QTSRun.getPositionDate(getActivity()) == 0) {
            tvDays.setText("Your surgery is " + formatDate(QTSRun.getDates(getActivity())));
        } else if (QTSRun.getPositionDate(getActivity()) == 1) {
            switch (QTSRun.getPositionSpinner(getActivity())) {
                case 0:
                    tvDays.setText("Your estimated waiting time is " + QTSRun.getDates(getActivity()) + ", your surgery is likely to be between " + fromDate(1) + " and " + toDate(30));
                    break;
                case 1:
                    tvDays.setText("Your estimated waiting time is " + QTSRun.getDates(getActivity()) + ", your surgery is likely to be between " + fromDate(30) + " and " + toDate(90));
                    break;
                case 2:
                    tvDays.setText("Your estimated waiting time is " + QTSRun.getDates(getActivity()) + ", your surgery is likely to be between " + fromDate(90) + " and " + toDate(180));
                    break;
                case 3:
                    tvDays.setText("Your estimated waiting time is " + QTSRun.getDates(getActivity()) + ", your surgery is likely to be between " + fromDate(180) + " and " + toDate(365));
                    break;
                default:
                    tvDays.setText("You haven't entered a date for surgery.");
                    break;
            }
        } else {
            tvDays.setText("You haven't entered a date for surgery.");
        }

        if (QTSRun.getTypeSurgery(getActivity()).equalsIgnoreCase("I don't know")) {
            tvAbout.setText("You haven't entered a procedure");
        } else {
            if (QTSRun.getProcedure(getActivity()).equalsIgnoreCase("I don't know")) {
                tvAbout.setText("You are having " + QTSRun.getTypeSurgery(getActivity()) + " surgery");
            } else {
                tvAbout.setText(Html.fromHtml("Your procedure is " + "<u> <font color=\"#0071bc\">" + QTSRun.getProcedure(getActivity()) + "</font></u>"));
            }
        }
        tvGetReady.setText(Html.fromHtml("GET ME READY!<br><u> <font color=\"#0071bc\">Learn how</font></u> to prepare for your surgery."));
        tvDetail.setText(Html.fromHtml("<u>Change my surgery details</u>"));
        if (myLocation != null) {
            getDirections();
        }

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setTrafficEnabled(true);

                googleMap.addMarker(new MarkerOptions().position(locHospital).title(QTSRun.getHospital(getActivity())).snippet(""));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(locHospital).zoom(13).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    public void onInfoWindowClick(Marker marker) {
                        if (myLocation != null) {
                            String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + locHospital.latitude + "," + locHospital.longitude + " (" + QTSRun.getHospital(getActivity()) + ")";
                            Uri gmmIntentUri = Uri.parse(uri);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                            startActivity(mapIntent);
                        } else {
                            if (Build.VERSION.SDK_INT >=23){
                                if (!checkPermission()) {
                                    requestPermission();
                                } else {
                                    checkLocation();
                                    startLocationUpdates();
                                }
                            }else {
                                checkLocation();
                                startLocationUpdates();
                            }
                        }
                    }
                });
            }
        });

        ll_Distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myLocation != null) {
                    String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + locHospital.latitude + "," + locHospital.longitude + " (" + QTSRun.getHospital(getActivity()) + ")";
                    Uri gmmIntentUri = Uri.parse(uri);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(mapIntent);
                } else {
                    if (Build.VERSION.SDK_INT >=23){
                        if (!checkPermission()) {
                            requestPermission();
                        } else {
                            checkLocation();
                            startLocationUpdates();
                        }
                    }else {
//                        startLocationUpdates();
                        checkLocation();
                        startLocationUpdates();
                    }
                }
            }
        });
        ll_ChangeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        ll_Ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewScreenAtOtherTab(new ReadyFragment(), 1);
            }
        });
        ll_Termination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QTSRun.getUrlHPT(getActivity()).toString().length() != 0)
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(QTSRun.getUrlHPT(getActivity()))));
            }
        });
    }
//    @Override
//    public void onMapReady(GoogleMap map) {
////        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
//        googleMap = map;
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.getUiSettings().setZoomControlsEnabled(false);
//        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//        googleMap.setTrafficEnabled(true);
//
//        googleMap.addMarker(new MarkerOptions().position(locHospital).title(QTSRun.getHospital(getActivity())).snippet(""));
//
//        // For zooming automatically to the location of the marker
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(locHospital).zoom(13).build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            public void onInfoWindowClick(Marker marker) {
//                if (myLocation != null) {
//                    String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + locHospital.latitude + "," + locHospital.longitude + " (" + QTSRun.getHospital(getActivity()) + ")";
//                    Uri gmmIntentUri = Uri.parse(uri);
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                    startActivity(mapIntent);
//                } else {
//                    if (Build.VERSION.SDK_INT >=23){
//                        if (!checkPermission()) {
//                            requestPermission();
//                        } else {
//                            checkLocation();
//                            startLocationUpdates();
//                        }
//                    }else {
//                        checkLocation();
//                        startLocationUpdates();
//                    }
//                }
//            }
//        });
//    }
    @Override
    public void onConnected(Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(getContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getContext(),
//                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                            android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
//        } else {
//            Log.e("DB", "PERMISSION GRANTED");
//        }
        if (Build.VERSION.SDK_INT >=23){
            if (!checkPermission()) {
                requestPermission();
            } else {
                startLocationUpdates();
                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLocation == null) {
                    startLocationUpdates();
                }
                if (mLocation != null) {
                    getDirections();
                }
            }
        }else {
            startLocationUpdates();
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLocation == null) {
                startLocationUpdates();
            }
            if (mLocation != null) {
                getDirections();
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
//        if (ActivityCompat.checkSelfPermission(getContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getContext(),
//                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                            android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
//        } else {
//            Log.e("DB", "PERMISSION GRANTED");
//        }
        if (Build.VERSION.SDK_INT >=23){
            if (!checkPermission()) {
                requestPermission();
            } else {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        mLocationRequest, this);
            }
        }else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }

        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        tvLongLat.setText("Longitude:"+Double.toString(location.getLongitude()) + ", Latitude:"+Double.toString(location.getLatitude()));
//        String msg = "Updated Location: " +
//                Double.toString(location.getLatitude()) + "," +
//                Double.toString(location.getLongitude());
//        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        getDirections();
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void getDirections() {
        Ion.with(getActivity())
                .load("GET", "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitude + "," + longitude + "&destinations=" + locHospital.latitude + "," + locHospital.longitude + "&mode=driving")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            try {
                                Log.e("SurgeryFragment", "resultDis:"+result.toString());
                                JSONObject json = new JSONObject(result.toString());
                                if (json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getString("status").equalsIgnoreCase("OK")) {
                                    distance = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text");
                                    tvDistance.setText(Html.fromHtml("It would take you " + distance + " to drive to " + "<u> <font color=\"#0071bc\">" + QTSRun.getHospital(getActivity()) + "</font></u> from your current location"));
                                } else {
                                    tvDistance.setText(Html.fromHtml("Cannot get the distance from your location. Please try later."));
                                }

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        } else {
                            tvDistance.setText(Html.fromHtml("Cannot get the distance from your location. Please try later."));
                        }
                    }
                });
    }

    @Override
    public void onResume() {
//        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        mMapView.onLowMemory();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvAbout) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(QTSRun.getUrlHPT(getActivity()))));
        } else if (v.getId() == R.id.tvGetReady) {
            startNewScreenAtOtherTab(new ReadyFragment(), 1);
        }
    }

    public static String formatDate(String datetime) {
        String date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//2017-03-30T22:00:00
        Date newDate = null;
        try {
            newDate = sdf.parse(datetime);
            sdf = new SimpleDateFormat("MMM dd yyyy");
            date = sdf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String fromDate(int day) {
        Calendar calendar = Calendar.getInstance();
//        Date today = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy MM dd");
        calendar.add(Calendar.DAY_OF_YEAR, day);
        Date dayFrom = calendar.getTime();
        DateFormat dfs = new SimpleDateFormat("dd MMM yyyy");

        return dfs.format(dayFrom);
    }

    private String toDate(int day) {
        Calendar calendarto = Calendar.getInstance();
//        Date today = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy MM dd");
        calendarto.add(Calendar.DAY_OF_YEAR, day);
        Date dayTo = calendarto.getTime();
        DateFormat dfs = new SimpleDateFormat("dd MMM yyyy");

        return dfs.format(dayTo);
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(getActivity(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(getActivity(),"Permission Granted, Now you can access location data.",Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(getActivity(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
