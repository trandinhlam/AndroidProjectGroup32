package com.example.thekiet.loactionsaver;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import fragment.ItemDanhBa;

/**
 * Created by TDLAM123 on 4/23/2017.
 */

public class Them_CapNhatDanhBa_Activity extends Activity{
    ItemDanhBa item;
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private int mode;
    private EditText textTen,textDiaChi,textSDT,textNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemdanhba_chitiet_them_sua_layout);
        textTen=(EditText) findViewById(R.id.editTen);
        textDiaChi=(EditText) findViewById(R.id.editDiaChi);
        textSDT=(EditText) findViewById(R.id.editSDT);
        textNote=(EditText) findViewById(R.id.editNote);

        Intent intentExtras=getIntent();
        Bundle mybundle=intentExtras.getExtras();
        if(null!=mybundle){// neu Bundle rong tuc la muon tao mot Item moi
            this.mode=MODE_EDIT;
            item=new ItemDanhBa(mybundle.getInt("Id"),mybundle.getString("Ten"),
                    mybundle.getString("DiaChi"),
                    mybundle.getString("SDT"),
                    mybundle.getString("Note"),
                    0);
            textTen.setText(mybundle.getString("Ten"));
            textDiaChi.setText(mybundle.getString("DiaChi"));
            textSDT.setText(mybundle.getString("SDT"));
            textNote.setText(mybundle.getString("Note"));
            //hinh.setImageResource(mybundle.getInt("HinhAnh"));
            //hinh.setImageResource(R.drawable.icon_menu);// hình ảnh tượng trưng

        }
        else {
            // neu Bundle rong tuc la muon tao mot Item moi

            this.mode=MODE_CREATE;
        }


    }
}
