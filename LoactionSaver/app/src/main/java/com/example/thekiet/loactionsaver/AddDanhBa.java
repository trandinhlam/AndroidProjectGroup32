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
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import Class.*;

import Data.MyDatabaseHelper;
import Data.Parser_Image;
import Data.*;
import fragment.ItemDanhBa;

import static android.R.attr.bitmap;
import static android.R.attr.data;
import static android.R.attr.width;
import static com.example.thekiet.loactionsaver.R.id.image1;
import static com.example.thekiet.loactionsaver.R.id.image2;
import static java.security.AccessController.getContext;

/**
 * Created by TheKiet on 4/23/2017.
 */

public class AddDanhBa extends Activity {
    ViTriThem vitrithem = new ViTriThem();
    public static int RESULT_LOAD_IMAGE = 1;
    public static int RESULT_CAMERA1_IMAGE = 2;
    public static int RESULT_CAMERA2_IMAGE = 3;

    EditText edit_Ten, edit_DiaChi, edit_Note,edit_SDT;
    ImageView btn_Add, anhTai1, anhTai2, anhChup1, anhChup2;
    int Checkanh1 = 0, Checkanh2 = 0;
    Bundle extras = new Bundle();
    Button btnTim;
    // ảnh lưu lại để lưu vào CSDL
    Bitmap anhchup1=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_themdanhba);
//        Intent myIntent = getIntent();
        Anhxa();

        extras = getIntent().getExtras();
        int requestcode = Integer.parseInt(extras.getString("request"));

        if(requestcode == 2)
        {
            btnTim.setVisibility(View.INVISIBLE);
            String Hinh1 = extras.getString("Hinh1");
            String Hinh2 = extras.getString("Hinh2");
            if(Hinh1.length() !=0 )
            {
                anhChup1.setImageBitmap(Parser_Image.Byte_to_Image(Base64.decode(Hinh1,Base64.DEFAULT)));

            }


            if(Hinh2.length() !=0 )
            {
                anhChup2.setImageBitmap(Parser_Image.Byte_to_Image(Base64.decode(Hinh2, Base64.DEFAULT)));

            }

        }


        vitrithem = (ViTriThem) extras.getSerializable("Address");

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


        /*Chổ này làm đỡ ở đây lúc bấm nút chỉ đường thì copy đoạn code này vô*/
        /*
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplication().getBaseContext(), ChiDuong.class);

                Bundle extras = new Bundle();
                extras.putDouble("Lat",10.764120);
                extras.putDouble("Lng" , 106.682472);

                myIntent.putExtras(extras);
                try{
                    startActivityForResult(myIntent, 0);}
                catch (Exception e)
                {
                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });*/

        /*Thêm vào danh bạ khi bấm vào nút btn_Add*/
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo 1 ItemDanhBa để thêm vào CSDL

                ItemDanhBa itemmoi=new ItemDanhBa();
                itemmoi.setTen(edit_Ten.getText().toString());
                itemmoi.setDiaChi(edit_DiaChi.getText().toString());
                itemmoi.setSDT(edit_SDT.getText().toString());
                itemmoi.setNote(edit_Note.getText().toString());
                itemmoi.setLatitude(vitrithem.getLatitude());
                itemmoi.setLongtitude(vitrithem.getLongtitude());
                itemmoi.setHinhAnh(ImageView_To_Byte(anhChup1));
                MyDatabaseHelper db=new MyDatabaseHelper(getApplicationContext());
                if(itemmoi.getTen().equals("")||itemmoi.getDiaChi().equals("")){// nếu thông tin chưa điền đầy đủ
                    Toast.makeText(getApplicationContext(),
                            "Bạn chưa điền thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.addItem(itemmoi);
                db.close();
                Toast.makeText(getApplicationContext(),
                        "Đã lưu thành công", Toast.LENGTH_SHORT).show();
                // Lưu thành công sau đó tải lại danh sách, chưa tìm ra cách nên tạm thời restart app
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(i);

                //.......


            }
        });
    }

    private void Anhxa()
    {
        btnTim = (Button)findViewById(R.id.btnTim);
        edit_Ten = (EditText)findViewById(R.id.tvten_add);
        edit_DiaChi = (EditText)findViewById(R.id.tvdiachi_add);
        edit_Note= (EditText)findViewById(R.id.tvnote_add);
        edit_SDT=(EditText) findViewById(R.id.tvSDT_add);
        btn_Add = (ImageView)findViewById(R.id.btnadd);

        anhTai1 = (ImageView)findViewById(R.id.anhtai1);
        anhTai2 = (ImageView)findViewById(R.id.anhtai2);
        anhChup1 = (ImageView)findViewById(R.id.anhchup1);
        anhChup2 = (ImageView)findViewById(R.id.anhchup2);

    }
    private byte[] ImageView_To_Byte(ImageView im){
        BitmapDrawable drawable=(BitmapDrawable) im.getDrawable();
        Bitmap bmp=drawable.getBitmap();

        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();
        return byteArray;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplication(),String.valueOf(requestCode), Toast.LENGTH_LONG).show();

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
            anhchup1=photo;
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
