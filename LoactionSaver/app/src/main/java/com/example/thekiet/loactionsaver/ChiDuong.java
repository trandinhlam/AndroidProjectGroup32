package com.example.thekiet.loactionsaver;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


import Class.*;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.google.android.gms.maps.model.Polyline;

import static android.R.attr.direction;
import static android.media.CamcorderProfile.get;
import static com.example.thekiet.loactionsaver.R.id.editSearch;
import static java.lang.Long.parseLong;
import static java.security.AccessController.getContext;


/**
 * Created by TheKiet on 4/25/2017.
 */

public class ChiDuong extends FragmentActivity implements GoogleMap.OnPolylineClickListener, LocationListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, DirectionCallback, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap map;
    Polyline polyline;
    LatLng tmp;
    LatLng From;
    Boolean checkmovecamera = true;
    private static final String LOG_TAG = "GGPlaces Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";

    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyCuvb4nDjQONv7nqV9igaOn6cC7l2uRwn8";

    static ViTriThem vitrithem = new ViTriThem();

    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chiduong);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /*-----------------------------------------------------------------------------------*/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {

            a = direction.getRouteList().get(0).getLegList().get(0).getDistance().getText();
            Toast.makeText(getApplication(), "Khoảng cách " + a, Toast.LENGTH_SHORT).show();
            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            polyline = map.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.BLUE));
            polyline.setClickable(true);

        }

    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Toast.makeText(getApplicationContext(), "Không lấy được chỉ đường. Hãy thử lại!",
                Toast.LENGTH_SHORT).show();

    }
    /*-------------------------------------------------------------------------------------*/
    // Fetches all places from GooglePlaces AutoComplete Web Service

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
        } else {
            map.setMyLocationEnabled(true);


        }

       // mGoogleApiClient.connect();

        map.getUiSettings().setCompassEnabled(true);

        Bundle extras = new Bundle();
        extras = getIntent().getExtras();
        Double lat = extras.getDouble("Lat");
        Double lng = extras.getDouble("Lng");
        tmp = new LatLng(lat, lng);

        //getDirection(tmp);


//        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                Toast.makeText(getApplication(), "Thêm vào danh bạ (sqlite)", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(getApplication(), AddDanhBa.class);
//
//                Bundle extras = new Bundle();
//                extras.putSerializable("Address",(Serializable) vitrithem);
//
//                myIntent.putExtras(extras);
//                try{
//                    startActivityForResult(myIntent, 0);}
//                catch (Exception e)
//                {
//                    // Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//
//                Toast.makeText(getApplication(), "Thêm vào danh bạ (sqlite)", Toast.LENGTH_SHORT).show();
//                Intent myIntent = new Intent(getApplication(), AddDanhBa.class);
//
//                Bundle extras = new Bundle();
//                extras.putSerializable("Address",(Serializable) vitrithem);
//
//                myIntent.putExtras(extras);
//                try{
//                    startActivityForResult(myIntent, 0);}
//                catch (Exception e)
//                {
//                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//                return false;
//            }
//        });



        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

//                //  Toast.makeText(getActivity(), "ád", Toast.LENGTH_SHORT);
//
//
//                map.addMarker(new MarkerOptions().position(lalng).title("Your Location"));
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lalng, 15));
//                map.animateCamera(CameraUpdateFactory.zoomIn());
//                map.animateCamera(CameraUpdateFactory.zoomTo(25), 2000, null);
//
                return false;
            }
        });

    }

    private void getDirection(LatLng destination, LatLng from) {

        map.clear();
//        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String provider = service.getBestProvider(criteria, false);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//        }
//        Location location = service.getLastKnownLocation(provider);
//        // LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

//        Location location = map.getMyLocation();
//        if(location == null)
//        {
//            Toast.makeText(getApplication(), "Không tìm thấy địa chỉ!", Toast.LENGTH_SHORT).show();
//         //   finish();
//            return ;
//        }
//        // vị trí thật sự của máy
//        LatLng lalng = new LatLng(location.getLatitude(), location.getLongitude());
        // đưa dữ liệu thử vào
        //LatLng lalng = new LatLng(destination.latitude+0.1, destination.longitude+0.1);
        map.addMarker(new MarkerOptions().position(destination).title("Vị trí cần đến"));

        map.addMarker(new MarkerOptions().position(from).title("Vị trí của bạn"));


        if(checkmovecamera == true) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(from, 15));
            map.animateCamera(CameraUpdateFactory.zoomIn());
            map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
            checkmovecamera = false;
        }

        GoogleDirection.withServerKey(API_KEY).from(from).to(destination)
                .transportMode(TransportMode.DRIVING).execute(this);
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        Toast.makeText(getApplication(), a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        checkLocationPermission();
        if (location == null) {
            Toast.makeText(getApplication(), "Không tìm thấy địa chỉ, Vui lòng mở GPS", Toast.LENGTH_LONG).show();
        } else {
            From = new LatLng(location.getLatitude(), location.getLongitude());
            getDirection(tmp, From);
        }
       // removeLocationUpdates();
    }


    @Override
    public void onStart() {
        super.onStart();
//        if(mGoogleApiClient != null)
        checkmovecamera = true;
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }


    private void removeLocationUpdates() {
        if (this.mGoogleApiClient != null) {
            if (this.mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.removeLocationUpdates(this.mGoogleApiClient, this);
            this.mGoogleApiClient.disconnect();
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this).setTitle("Bật GPS").setMessage("Hãy bật GPS để sử dụng chức năng này!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 104);
                                }
                            }
                        })
                        .create()
                        .show();

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 104);
            }
        }
    }

    private void requestLocation() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

}
