package com.example.thekiet.loactionsaver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import Class.*;
import Data.AdapterGoiY;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.thekiet.loactionsaver.R.id.map;
import static java.security.AccessController.getContext;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import Data.*;
/**
 * Created by TheKiet on 5/6/2017.
 */

public class activiti_GoiY extends Activity {
    ListView listview_goiy;
    ArrayList<GoiY> listdulieu=new ArrayList<GoiY>();
    AdapterGoiY adapter=null;
    ViTriThem vitrithem = new ViTriThem();
    String KhoangCach = "";
    private String URL_FEED = "http://locationsaver.somee.com/api/GoiY/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.layout_goiy);
        listview_goiy = (ListView) findViewById(R.id.listviewgoiy);
        adapter = new AdapterGoiY(this, R.layout.item_list_goiy, listdulieu);
        listview_goiy.setAdapter(adapter);

        Bundle extras = new Bundle();
        extras = getIntent().getExtras();
        vitrithem = (ViTriThem) extras.getSerializable("Address");
        KhoangCach = extras.getString("khoangcach");

//        vitrithem.setLatitude(10.771108);
//        vitrithem.setLongtitude(106.675589);
//        KhoangCach = "20";

        getData();

        listview_goiy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoiY temp = new GoiY();
                temp = (GoiY) parent.getItemAtPosition(position);

                Intent myIntent = new Intent(getApplication(), ThongTinChiTietGoiY.class);

                Bundle extras = new Bundle();
                extras.putSerializable("Infomation",(Serializable) temp);
               // extras.putString("image", "");

                myIntent.putExtras(extras);
                try{
                    startActivityForResult(myIntent, 0);}
                catch (Exception e)
                {
                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void getData()
    {
        String Url = URL_FEED + "GetGoiYKhoangCach?khoangcach=" + KhoangCach + "&lat=" + vitrithem.getLatitude()
            + "&lng=" + vitrithem.getLongtitude();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    String jsonText = response.replace("\\", "").toString();


                    jsonText = jsonText.substring(1, jsonText.length());

                    try {
                        JSONArray jsonArray = new JSONArray(jsonText);
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                GoiY goiy = Parser_GoiY.parseGoiY(jsonObject);
                                listdulieu.add(goiy);
                            }
//                            Toast.makeText(getApplication(), String.valueOf(listdulieu.size()), Toast.LENGTH_LONG).show();
                            adapter.notifyDataSetChanged();
//                            Toast.makeText(getApplication(), String.valueOf(adapter.getCount()), Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplication(), "Không có địa điểm thỏa điều kiện", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        try {
                            JSONObject jsonObj = new JSONObject(jsonText);
                            if (jsonObj.getString("ketqua").equals("fail")) {

                                Toast.makeText(getApplication(), "Không có gợi ý", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException error) {
                            error.printStackTrace();
                        }
                    }
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplication(), "Không kết nối được server. Hãy thử lại", Toast.LENGTH_SHORT).show();

                    }
                });

        queue.add(strReq);
        //Toast.makeText(getApplication(), String.valueOf(listdulieu.size()), Toast.LENGTH_SHORT).show();
       // AppController.getInstance().addToRequestQueue(strReq);
    }

}
