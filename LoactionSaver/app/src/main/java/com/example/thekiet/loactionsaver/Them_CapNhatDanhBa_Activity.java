package com.example.thekiet.loactionsaver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import Data.MyDatabaseHelper;
import fragment.ItemDanhBa;


import fragment.ItemDanhBa;
import Data.*;
/**
 * Created by TDLAM123 on 4/23/2017.
 */

public class Them_CapNhatDanhBa_Activity extends Activity{
    ItemDanhBa item;
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private int mode;
    private boolean needRefresh=false;


    private EditText textTen,textDiaChi,textSDT,textNote;
    private Button btnluu,btnhuy;
    private ImageView imghinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemdanhba_chitiet_them_sua_layout);
        textTen=(EditText) findViewById(R.id.editTen);
        textDiaChi=(EditText) findViewById(R.id.editDiaChi);
        textSDT=(EditText) findViewById(R.id.editSDT);
        textNote=(EditText) findViewById(R.id.editNote);
        btnluu=(Button)findViewById(R.id.btnSave);
        btnhuy=(Button) findViewById(R.id.btnCancel);
        imghinh=(ImageView) findViewById(R.id.imghinh);

        Intent intentExtras=getIntent();
        Bundle mybundle=intentExtras.getExtras();

        if(null!=mybundle){// neu Bundle rong tuc la muon tao mot Item moi
            this.mode=MODE_EDIT;
            item= (ItemDanhBa) mybundle.getSerializable("item");

            textTen.setText(item.getTen());
            textDiaChi.setText(item.getDiaChi());
            textSDT.setText(item.getSDT());
            textNote.setText(item.getNote());

            if(item.getHinhAnh()!= null){// nếu có hình thì load hình
                // tạo bitmap để chuyển thành hình
                Bitmap bitmap= BitmapFactory.decodeByteArray(item.getHinhAnh(),0,item.getHinhAnh().length);
                imghinh.setImageBitmap(bitmap);
                //hinh.setImageDrawable(item.getHinhAnh().getDrawable());
            }
            else{// nếu không có hình
                imghinh.setImageResource(R.drawable.iconavatar);// hình ảnh tượng trưng
            }


        }
        else {
            // neu Bundle rong tuc la muon tao mot Item moi

            this.mode=MODE_CREATE;
        }

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttoncancelClicked();
            }

        });
        btnluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsaveClicked();

            }
        });
    }
    private void buttonsaveClicked(){
        MyDatabaseHelper db=new MyDatabaseHelper(this);
        String ten=textTen.getText().toString();
        String diachi=textDiaChi.getText().toString();
        String SDT=textSDT.getText().toString();
        String Note=textNote.getText().toString();

        if(ten.equals("")||diachi.equals("")){// nếu thông tin chưa điền đầy đủ
            Toast.makeText(getApplicationContext(),
                    "Bạn chưa điền thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }
        else{// khi đã có thông tin thì lưu xuống CSDL
            if(mode==MODE_CREATE){// trường hợp thêm mới
                this.item=new ItemDanhBa(0,ten,diachi,SDT,Note,null);
                db.addItem(item);
            }
            else {// trường hợp chỉnh sửa
                    item.setTen(ten);
                    item.setDiaChi(diachi);
                    item.setSDT(SDT);
                    item.setNote(Note);
                    db.updateItem(item);
            }


            Toast.makeText(getApplicationContext(),
                    "Đã lưu thành công", Toast.LENGTH_SHORT).show();
            this.needRefresh = true;
            // Trở lại Activity trước
            this.onBackPressed();
        }



        db.close();
    }
    private void buttoncancelClicked() {
        this.onBackPressed();
    }

    @Override
    public void finish() {
        // trả vể kết quả cho Activity trước

        Intent data = new Intent();
        // Yêu cầu MainActivity refresh lại ListView hoặc không.
        data.putExtra("needRefresh", needRefresh);

        // Activity đã hoàn thành OK, trả về dữ liệu.
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }
}
