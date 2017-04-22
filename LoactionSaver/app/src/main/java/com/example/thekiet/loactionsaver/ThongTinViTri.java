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

import data.MyDatabaseHelper;
import fragment.ItemDanhBa;

/**
 * Created by TDLAM123 on 4/22/2017.
 */

public class ThongTinViTri extends Activity {
    ItemDanhBa item;
    TextView txtTen,txtDiachi,txtDT, txtnote;
    ImageView hinh;
    Button btnSua,btnXoa;
    Activity currentAct;
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

        btnXoa=(Button) findViewById(R.id.btnXoa);
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(currentAct)
                        .setMessage("Bạn chắc chắn muốn xóa "+item.getTen() +" ra khỏi danh bạ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               //Nếu đồng ý thì xóa
                                MyDatabaseHelper db = new MyDatabaseHelper(currentAct);
                                //db.deleteItem(item);

                                //quay lại màn hình fragment_danhba và cập nhật lại listview


                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        Intent intentExtras=getIntent();
        Bundle mybundle=intentExtras.getExtras();
        if(!mybundle.isEmpty()){
                item=new ItemDanhBa(mybundle.getInt("Id"),mybundle.getString("Ten"),
                                    mybundle.getString("DiaChi"),
                                    mybundle.getString("SDT"),
                                    mybundle.getString("Note"),
                                    0);

                txtTen.setText(mybundle.getString("Ten"));
                txtDiachi.setText("Địa chỉ: "+mybundle.getString("DiaChi"));
                txtDT.setText("ĐT: "+mybundle.getString("SDT"));
                txtnote.setText("Note: \n\t"+mybundle.getString("Note"));
                //hinh.setImageResource(mybundle.getInt("HinhAnh"));
                hinh.setImageResource(R.drawable.icon_menu);// hình ảnh tượng trưng

        }
        else {
            Toast.makeText(getApplicationContext(), "Lỗi truyền dữ liệu", Toast.LENGTH_SHORT).show();
        }




    }
}
