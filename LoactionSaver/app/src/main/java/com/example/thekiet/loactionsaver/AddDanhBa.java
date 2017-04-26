package com.example.thekiet.loactionsaver;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import Class.*;

import static android.R.attr.bitmap;
import static android.R.attr.width;

/**
 * Created by TheKiet on 4/23/2017.
 */

public class AddDanhBa extends Activity {
    ViTriThem vitrithem = new ViTriThem();
    public static int RESULT_LOAD_IMAGE = 1;
    public static int RESULT_CAMERA1_IMAGE = 2;
    public static int RESULT_CAMERA2_IMAGE = 3;

    EditText edit_Ten, edit_DiaChi, edit_Note;
    ImageView btn_Add, anhTai1, anhTai2, anhChup1, anhChup2;
    int Checkanh1 = 0, Checkanh2 = 0;
    Button btnTim;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themdanhba);
//        Intent myIntent = getIntent();

        Bundle extras = new Bundle();
        extras = getIntent().getExtras();
        vitrithem = (ViTriThem) extras.getSerializable("Address");
        Anhxa();
        edit_Ten.setText(vitrithem.getTenViTri());
        edit_DiaChi.setText(vitrithem.getDiaChi());

        anhTai1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                Checkanh1 = 1;
            }
        });

        anhTai2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                Checkanh2= 1;
            }
        });

        anhChup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, RESULT_CAMERA1_IMAGE);
            }
        });

        anhChup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, RESULT_CAMERA2_IMAGE);
            }
        });

        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Timvitri = new Intent(getApplication(), TimViTri.class);
                startActivityForResult(Timvitri, -1);
            }
        });

    }

    private void Anhxa()
    {
        btnTim = (Button)findViewById(R.id.btnTim);
        edit_Ten = (EditText)findViewById(R.id.tvten_add);
        edit_DiaChi = (EditText)findViewById(R.id.tvdiachi_add);
        edit_Note= (EditText)findViewById(R.id.tvnote_add);

        btn_Add = (ImageView)findViewById(R.id.btnadd);

        anhTai1 = (ImageView)findViewById(R.id.anhtai1);
        anhTai2 = (ImageView)findViewById(R.id.anhtai2);
        anhChup1 = (ImageView)findViewById(R.id.anhchup1);
        anhChup2 = (ImageView)findViewById(R.id.anhchup2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            if(Checkanh1 == 1) {
                anhTai1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                Checkanh1 = 0;
            }

            if(Checkanh2 == 1) {
                anhTai2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                Checkanh2 = 0;
            }
        }

        if (requestCode == RESULT_CAMERA1_IMAGE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            anhChup1.setImageBitmap(photo);
        }

        if (requestCode == RESULT_CAMERA2_IMAGE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
           photo = Bitmap.createScaledBitmap(photo, 150,150, true);
            Integer a =  photo.getByteCount();
            try {
                Toast.makeText(this, a.toString(), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            anhChup2.setImageBitmap(photo);
        }

        BitmapDrawable drawable = (BitmapDrawable) anhChup2.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Integer a = bitmap.getByteCount();
        Toast.makeText(this, a.toString() , Toast.LENGTH_SHORT).show();

    }
}
