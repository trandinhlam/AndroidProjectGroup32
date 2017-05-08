package com.example.thekiet.loactionsaver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Data.MyDatabaseHelper;
import fragment.ItemDanhBa;

/**
 * Created by TDLAM123 on 4/22/2017.
 */

public class ThongTinViTri extends Activity {
    ItemDanhBa item;
    TextView txtTen,txtDiachi,txtDT, txtnote;
    ImageView hinh;
    Activity currentAct;
    Button btnChiDuong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemdanhba_chitiet_layout);


        currentAct=this;

        txtTen=(TextView) findViewById(R.id.TenDiaDiem);
        txtDiachi= (TextView) findViewById(R.id.DiaChi);
        txtDT= (TextView) findViewById(R.id.SDT);
        txtnote =(TextView) findViewById(R.id.Note);

        hinh= (ImageView) findViewById(R.id.hinhanhchitiet);
        btnChiDuong=(Button) findViewById(R.id.btnChiDuong);

        Intent intentExtras=getIntent();
        Bundle mybundle=intentExtras.getExtras();
        if(!mybundle.isEmpty()){
                item= (ItemDanhBa) mybundle.getSerializable("item");

               txtTen.setText(item.getTen());
            txtDiachi.setText("Địa chỉ: "+item.getDiaChi());
            txtDT.setText("ĐT: "+item.getSDT());
            txtnote.setText("Note: \n\t"+item.getNote());
                //hinh.setImageResource(mybundle.getInt("HinhAnh"));
            hinh.setImageResource(R.drawable.icon_menu);// hình ảnh tượng trưng

        }
        else {
            Toast.makeText(getApplicationContext(), "Lỗi truyền dữ liệu", Toast.LENGTH_SHORT).show();
        }



    }
}
