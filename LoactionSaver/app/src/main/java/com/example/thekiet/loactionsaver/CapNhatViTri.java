package com.example.thekiet.loactionsaver;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Class.PlaceJSONParser;
import Class.ViTriThem;
import fragment.ItemDanhBa;


/**
 * Created by TheKiet on 4/25/2017.
 */

public class CapNhatViTri extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, DirectionCallback {

    private GoogleMap map;
    LatLng tmp;
    private static final String LOG_TAG = "GGPlaces Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";

    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyCuvb4nDjQONv7nqV9igaOn6cC7l2uRwn8";


    PlacesTask placesTask;
    ParserTask parserTask;
    static ItemDanhBa item;
    static ViTriThem vitrithem = new ViTriThem();
    AutoCompleteTextView autodiachi;
    Button btnok;
    private GoogleApiClient mGoogleApiClient;    ;
    String id_place = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_timvitri);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnok = (Button) findViewById(R.id.btnOk);
        autodiachi = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        autodiachi.setThreshold(1);


        autodiachi.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Toast.makeText(getApplication(), "your selected item" + String.valueOf(position), Toast.LENGTH_SHORT).show();
                autodiachi.setText("123");
                Object item = arg0.getItemAtPosition(position);
                HashMap<String, String> place = new HashMap<String, String>();
                place = (HashMap<String, String>) item;
                // now you can show the value on textview.
                autodiachi.setText(place.get("description"));
                autodiachi.dismissDropDown();
                id_place = place.get("_id");
            }
        });

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Getlaln getlaln = new Getlaln();
                getlaln.execute(id_place);


            }
        });

        autodiachi.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

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

    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }
/*-------------------------------------------------------------------------------------*/
    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            // String key = "key=AIzaSyCNYFQYAqx3LgqPL7YfFwwuMZyy6F-5mdE";
            String key = "key=" + API_KEY;
            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters + "&components=country:VNM";

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            autodiachi.setAdapter(adapter);
            autodiachi.showDropDown();
        }
    }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
        } else
            map.setMyLocationEnabled(true);

        map.getUiSettings().setCompassEnabled(true);

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent myIntent = new Intent();
                Bundle extras = new Bundle();
                extras.putSerializable("newAddress",(Serializable) vitrithem);
                myIntent.putExtras(extras);
                try{
                    Them_CapNhatDanhBa_Activity.vitrimoi = vitrithem;// chuyen du lieu
                    onBackPressed();



                }
                catch (Exception e)
                {
                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Intent myIntent = new Intent();
                Bundle extras = new Bundle();
                extras.putSerializable("newAddress",(Serializable) vitrithem);
                myIntent.putExtras(extras);
                try{
                    Them_CapNhatDanhBa_Activity.vitrimoi = vitrithem;// chuyen du lieu
                    onBackPressed();
                    return true;
                }
                catch (Exception e)
                {
                     Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        });

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                //  Toast.makeText(getActivity(), "ád", Toast.LENGTH_SHORT);
                Location myloction = map.getMyLocation();

                if(myloction == null) return false;
                LatLng lalng = new LatLng(myloction.getLatitude(), myloction.getLongitude());

                Geocoder geocoder = new Geocoder(getApplication(), Locale.forLanguageTag("vi"));
                List<Address> address = null;
                try {
                    address = geocoder.getFromLocation( myloction.getLatitude(), myloction.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "Kiem tra ket noi mang", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Address diachi = address.get(0);
                map.addMarker(new MarkerOptions().position(lalng).title(diachi.getFeatureName()));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lalng, 15));
                map.animateCamera(CameraUpdateFactory.zoomIn());
                map.animateCamera(CameraUpdateFactory.zoomTo(25), 2000, null);



                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < diachi.getMaxAddressLineIndex(); i++) {
                    sb.append(diachi.getAddressLine(i)).append(", ");
                }

                String result = sb.toString();
                vitrithem.setDiaChi(result);
                // vitrithem.setLalng(latln);
                vitrithem.setLatitude(diachi.getLatitude());
                vitrithem.setLongtitude(diachi.getLongitude());
                vitrithem.setTenViTri(diachi.getFeatureName());

                return false;
            }
        });

    }


    private class Getlaln extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... ID_PLACE) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            // String key = "key=AIzaSyCNYFQYAqx3LgqPL7YfFwwuMZyy6F-5mdE";
            String key = "key=" + API_KEY;
            String input = "";

            try {
                input = URLEncoder.encode(ID_PLACE[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+ input +"&key="+ API_KEY;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            HashMap<String, String> place = new HashMap<String, String>();

            try {
                JSONObject jObject = new JSONObject(result);
               String a  = jObject.getString("result");
                JSONObject jObject1 = new JSONObject(a);

                JSONObject jObject2 = new JSONObject(jObject1.getString("geometry"));
                JSONObject jObject3 = new JSONObject(jObject2.getString("location"));

                 tmp = new LatLng(jObject3.getDouble("lat"), jObject3.getDouble("lng"));


                vitrithem.setLongtitude(tmp.longitude);
                vitrithem.setLatitude(tmp.latitude);
                vitrithem.setTenViTri(autodiachi.getText()+"");
                vitrithem.setDiaChi(autodiachi.getText()+"");


                map.addMarker(new MarkerOptions().position(tmp).title(autodiachi.getText()+""));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(tmp, 25));
                map.animateCamera(CameraUpdateFactory.zoomIn());
                map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
