package com.example.thekiet.loactionsaver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TDLAM123 on 4/22/2017.
 */

public class ThongTinViTri extends Activity {

    TextView txtTen,txtDiachi,txtDT, txtnote;
    ImageView hinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemdanhba_chitiet_layout);

        txtTen=(TextView) findViewById(R.id.TenDiaDiem);
        txtDiachi= (TextView) findViewById(R.id.DiaChi);
        txtDT= (TextView) findViewById(R.id.SDT);
        txtnote =(TextView) findViewById(R.id.Note);

        hinh= (ImageView) findViewById(R.id.hinhanhchitiet);



        Intent intentExtras=getIntent();
        Bundle mybundle=intentExtras.getExtras();
        if(!mybundle.isEmpty()){
                txtTen.setText(mybundle.getString("Ten"));
                txtDiachi.setText("Địa chỉ: "+mybundle.getString("DiaChi"));
                txtDT.setText("ĐT: "+mybundle.getString("SDT"));
                txtnote.setText("Note: \n\t"+mybundle.getString("Note"));
                hinh.setImageResource(mybundle.getInt("HinhAnh"));
        }
        else {
            Toast.makeText(getApplicationContext(), "Lỗi truyền dữ liệu", Toast.LENGTH_SHORT).show();
        }




    }
}
