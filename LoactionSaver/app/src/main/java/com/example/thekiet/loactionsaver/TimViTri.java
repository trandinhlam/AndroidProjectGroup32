package com.example.thekiet.loactionsaver;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import Class.*;



/**
 * Created by TheKiet on 4/25/2017.
 */

public class TimViTri extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private static final String LOG_TAG = "GGPlaces Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";

    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyBWPwvayZA3uJKBdiPHU0lhcTDW3Pk3l9Y";


    PlacesTask placesTask;
    ParserTask parserTask;

    static ViTriThem vitrithem = new ViTriThem();

    AutoCompleteTextView autodiachi;
    Button btnok;
    private GoogleApiClient mGoogleApiClient;;

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
                Toast.makeText(getApplication(), "your selected item"+String.valueOf(position), Toast.LENGTH_SHORT).show();
                autodiachi.setText("123");
                Object item = arg0.getItemAtPosition(position);
                //s1.get(position) is name selected from autocompletetextview
                HashMap<String, String> place = new HashMap<String, String>();
                place = (HashMap<String, String>) item;
                // now you can show the value on textview.
                autodiachi.setText(place.get("description"));
            }
        });

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesTask = new PlacesTask();
                placesTask.execute(autodiachi.getText()+"".toString());
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

        private String downloadUrl(String strUrl) throws IOException{
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try{
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
                while( ( line = br.readLine()) != null){
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            }catch(Exception e){
                Log.d("Exception", e.toString());
            }finally{
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        // Fetches all places from GooglePlaces AutoComplete Web Service
        private class PlacesTask extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... place) {
                // For storing data from web service
                String data = "";

                // Obtain browser key from https://code.google.com/apis/console
               // String key = "key=AIzaSyCNYFQYAqx3LgqPL7YfFwwuMZyy6F-5mdE";
                String key = "key=AIzaSyCuvb4nDjQONv7nqV9igaOn6cC7l2uRwn8";
                String input="";

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
                String parameters = input+"&"+types+"&"+sensor+"&"+key;

                // Output format
                String output = "json";

                // Building the url to the web service
                String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters+"&components=country:VNM";

                try{
                    // Fetching the data from we service
                    data = downloadUrl(url);
                }catch(Exception e){
                    Log.d("Background Task",e.toString());
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
        /** A class to parse the Google Places in JSON format */
        private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

            JSONObject jObject;

            @Override
            protected List<HashMap<String, String>> doInBackground(String... jsonData) {

                List<HashMap<String, String>> places = null;

                PlaceJSONParser placeJsonParser = new PlaceJSONParser();

                try{
                    jObject = new JSONObject(jsonData[0]);

                    // Getting the parsed data as a List construct
                    places = placeJsonParser.parse(jObject);

                }catch(Exception e){
                    Log.d("Exception",e.toString());
                }
                return places;
            }

            @Override
            protected void onPostExecute(List<HashMap<String, String>> result) {

                String[] from = new String[] { "description"};
                int[] to = new int[] { android.R.id.text1 };

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

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
        } else
            map.setMyLocationEnabled(true);

        map.getUiSettings().setCompassEnabled(true);

    }





    public LatLng getLatLngFromID_Place(String ID_Place)
    {
        LatLng tmp = null;
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, ID_Place)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place myPlace = places.get(0);
//                            LatLng queriedLocation = myPlace.getLatLng();
                        }
                        places.release();
                    }
                });

        return tmp;
    }


}
