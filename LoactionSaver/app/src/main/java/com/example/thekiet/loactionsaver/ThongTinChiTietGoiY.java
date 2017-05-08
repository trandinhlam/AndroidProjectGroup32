package com.example.thekiet.loactionsaver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TheKiet on 5/7/2017.
 */
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import Class.*;
import Data.Parser_GoiY;
import Data.Parser_Image;

import static android.R.attr.data;
import static android.os.Build.VERSION_CODES.N;
import static java.security.AccessController.getContext;

public class ThongTinChiTietGoiY extends Activity {
    private String URL_FEED = "http://locationsaver.somee.com/api/GoiY/";

    ImageView btnThem, btnChiDuong, image1, image2;
    TextView tvTen, tvDiaChi, tvNote;
    GoiY tmp = new GoiY();
    String ID= "";
    private GoiY goiy = new GoiY();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_xemthongtingoiy);
        AnhXa();
        Bundle extras = new Bundle();
        extras = getIntent().getExtras();
        tmp = (GoiY) extras.getSerializable("Infomation");

         ID = tmp.getID();
        getData();







        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViTriThem vitrithem = new ViTriThem();
                vitrithem.setTenViTri(goiy.getTEN());
                vitrithem.setDiaChi(goiy.getDIACHI());
                vitrithem.setLatitude(Double.parseDouble(goiy.getLAT()));
                vitrithem.setLongtitude(Double.parseDouble(goiy.getLNG()));
                Intent myIntent = new Intent(getApplication().getBaseContext(), AddDanhBa.class);

                Bundle extras = new Bundle();
                extras.putSerializable("Address",(Serializable) vitrithem);
                extras.putString("Hinh1", goiy.getImage1());
                extras.putString ("Hinh2", goiy.getImage2());
                extras.putString("request", "2");
                myIntent.putExtras(extras);
                try{
                   startActivityForResult(myIntent,5);}
                catch (Exception e)
                {
                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnChiDuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplication().getBaseContext(), ChiDuong.class);

                Bundle extras = new Bundle();
                extras.putDouble("Lat",Double.parseDouble(goiy.getLAT()));
                extras.putDouble("Lng" , Double.parseDouble(goiy.getLNG()));

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

    private void AnhXa() {
        btnChiDuong = (ImageView) findViewById(R.id.btnTim);
        btnThem = (ImageView) findViewById(R.id.btnadd);

        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);

        tvTen = (TextView) findViewById(R.id.tvten_tt);
        tvDiaChi = (TextView) findViewById(R.id.tvdiachi_tt);
        tvNote = (TextView) findViewById(R.id.tvnote_tt);

    }


    private void getData()
    {
        String Url = URL_FEED + "GetGoiYTheoID?ID=" + ID ;
       //String Url = "www.google.com";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {
                    String jsonText = response.replace("\\", "").toString();


                    jsonText = jsonText.substring(1, jsonText.length());

                    try {

                                JSONObject jsonObject = new JSONObject(jsonText);
                                 goiy = Parser_GoiY.parseGoiYImage(jsonObject);


                            tvTen.setText(goiy.getTEN());
                            tvDiaChi.setText(goiy.getDIACHI());
                            tvNote.setText(goiy.getNOTE());
                            if(goiy.getImage1().length() !=0 )
                            {
                                image1.setImageBitmap(Parser_Image.Byte_to_Image(Base64.decode(goiy.getImage1(),Base64.DEFAULT)));

                            }


                            if(goiy.getImage2().length() !=0 )
                            {
                                image2.setImageBitmap(Parser_Image.Byte_to_Image(Base64.decode(goiy.getImage2(), Base64.DEFAULT)));

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
               // Toast.makeText(getApplication(), response.toString()    , Toast.LENGTH_SHORT).show();
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
