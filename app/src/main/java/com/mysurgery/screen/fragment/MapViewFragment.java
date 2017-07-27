package com.mysurgery.screen.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mysurgery.R;
import com.mysurgery.custom.CustomScreen;
import com.mysurgery.utils.FontUtils;
import com.mysurgery.utils.QTSConst;
import com.mysurgery.utils.QTSRun;
import com.mysurgery.utils.TrackGPS;

/**
 * Created by Administrator on 4/13/2017.
 */

public class MapViewFragment extends CustomScreen implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    TextView toolbar_title, tvContact;
    MapView mMapView;
    Button btnOpenMap;
    private GoogleMap googleMap;
    double longitude;
    double latitude;
    LatLng myLocation;
    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private LocationManager locationManager;
    private static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapview, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
//        gps=new TrackGPS(getActivity());
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
        toolbar_title = (TextView) container.findViewById(R.id.toolbar_title);
        tvContact = (TextView) container.findViewById(R.id.lb1);
        btnOpenMap = (Button) container.findViewById(R.id.btnOpenMap);
        FontUtils.loadFont(getActivity(), QTSConst.FONT_SANSPRO_LIGHT);
        FontUtils.setFont(toolbar_title);
        FontUtils.setFont(btnOpenMap);
        FontUtils.setFont(tvContact);
        QTSRun.setLayoutView(mMapView, QTSRun.GetWidthDevice(getActivity()), QTSRun.GetHeightDevice(getActivity()) * 2 / 5);
        tvContact.setText(Html.fromHtml("" + QTSRun.getContactHop(getActivity())));
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

                googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(QTSRun.getLatitude(getActivity())), Double.parseDouble(QTSRun.getLongitude(getActivity())))).title(QTSRun.getHospital(getActivity())).snippet(""));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.parseDouble(QTSRun.getLatitude(getActivity())), Double.parseDouble(QTSRun.getLongitude(getActivity())))).zoom(13).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    public void onInfoWindowClick(Marker marker) {
                        if (myLocation != null){
                            String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + Double.parseDouble(QTSRun.getLatitude(getActivity())) + "," + Double.parseDouble(QTSRun.getLongitude(getActivity())) + " (" + QTSRun.getHospital(getActivity()) + ")";
                            Uri gmmIntentUri = Uri.parse(uri);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                            startActivity(mapIntent);
                        }else {
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
//                        if (gps.canGetLocation()) {
//                            longitude = gps.getLongitude();
//                            latitude = gps.getLatitude();
//                            String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + Double.parseDouble(QTSRun.getLatitude(getActivity())) + "," + Double.parseDouble(QTSRun.getLongitude(getActivity())) + " (" + QTSRun.getHospital(getActivity()) + ")";
//                            Uri gmmIntentUri = Uri.parse(uri);
//                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                            mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                            startActivity(mapIntent);
//                        } else {
//                            gps.showSettingsAlert();
//                        }
                    }
                });
            }
        });
        btnOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myLocation != null){
                    String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + Double.parseDouble(QTSRun.getLatitude(getActivity())) + "," + Double.parseDouble(QTSRun.getLongitude(getActivity())) + " (" + QTSRun.getHospital(getActivity()) + ")";
                    Uri gmmIntentUri = Uri.parse(uri);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(mapIntent);
                }else {
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
//                if (gps.canGetLocation()) {
//                    longitude = gps.getLongitude();
//                    latitude = gps.getLatitude();
//                    String uri = "http://maps.google.com/maps?saddr=" + latitude + "," + longitude + "&daddr=" + Double.parseDouble(QTSRun.getLatitude(getActivity())) + "," + Double.parseDouble(QTSRun.getLongitude(getActivity())) + " (" + QTSRun.getHospital(getActivity()) + ")";
//                    Uri gmmIntentUri = Uri.parse(uri);
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//                    startActivity(mapIntent);
//                } else {
//                    gps.showSettingsAlert();
//                }
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (Build.VERSION.SDK_INT >=23){
            if (!checkPermission()) {

                requestPermission();

            } else {
                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLocation == null) {
                    startLocationUpdates();
                }
            }
        }else {
            startLocationUpdates();
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLocation == null) {
                startLocationUpdates();
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


//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
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
        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
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

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
