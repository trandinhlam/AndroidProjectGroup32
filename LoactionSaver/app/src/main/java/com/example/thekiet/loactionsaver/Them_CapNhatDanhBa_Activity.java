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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import Data.MyDatabaseHelper;
import fragment.ItemDanhBa;

import fragment.ItemDanhBa;
import Data.*;
import Class.ViTriThem;
/**
 * Created by TDLAM123 on 4/23/2017.
 */

public class Them_CapNhatDanhBa_Activity extends Activity{
    ItemDanhBa item;
    public static ViTriThem vitrimoi = null;
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    public static int RESULT_LOAD_IMAGE = 1;
    public static int RESULT_CAMERA_IMAGE = 2;
    private int mode;
    private boolean needRefresh=false;

    private EditText textTen,textDiaChi,textSDT,textNote;
    private Button btnluu,btnhuy,btnTimViTriMoi;
    private ImageView imghinh,imgChange,imgNewAvatar;
    Bundle extras = new Bundle();
    Bitmap anhchup1=null;

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
        btnTimViTriMoi =(Button) findViewById(R.id.btnTimDiaChi);
        imghinh=(ImageView) findViewById(R.id.imghinh);
        imgChange= (ImageView) findViewById(R.id.imgButtonChange);
        imgChange.setImageResource(R.drawable.changeavatar);
        imgNewAvatar= (ImageView) findViewById(R.id.imgButtonNewAvatar);
        imgNewAvatar.setImageResource(R.drawable.iconcamera);

        // Kiem tra vi tri moi co duoc cap nhat khong


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
        // backup vi tri cap nhat bang vi tri cũ

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
        btnTimViTriMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent Timvitri = new Intent(getApplication(), CapNhatViTri.class);

                startActivityForResult(Timvitri, -1);
            }
        });

        //nút thay đổi hình đại diện
        imgChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        imgNewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, RESULT_CAMERA_IMAGE);
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
                // tọa độ đã được thay đổi rồi
                    item.setNote(Note);
                    item.setHinhAnh(ImageView_To_Byte(imghinh));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {// lay ket qua chup hinh moi, hoac tai hinh moi
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK &&null !=data){//truong hop tai hinh tu may
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imghinh.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
        else if(requestCode ==RESULT_CAMERA_IMAGE &&resultCode ==RESULT_OK && null !=data){// truong hop chup hinh moi
                Bitmap photo= (Bitmap) data.getExtras().get("data");
                photo = Bitmap.createScaledBitmap(photo, 150,150, true);
                imghinh.setImageBitmap(photo);
        }
        if(requestCode == -1 && resultCode == RESULT_OK){//sau khi cap nhat toa do vi tri

        }

    }

    private byte[] ImageView_To_Byte(ImageView im){
        BitmapDrawable drawable=(BitmapDrawable) im.getDrawable();
        Bitmap bmp=drawable.getBitmap();

        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();
        return byteArray;
    }
    public static void setnewAddress(ViTriThem vitri){
        vitrimoi=vitri;
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(vitrimoi==null){// nếu ban đầu chưa cập nhật thì bỏ qua thao tác này

        }
        else {
            //cập nhật cho vị trí
            textDiaChi.setText(vitrimoi.getTenViTri());
            item.setLatitude(vitrimoi.getLatitude());
            item.setLongtitude(vitrimoi.getLongtitude());
            item.setDiaChi(vitrimoi.getDiaChi());
            // trả về rỗng cho các lần cập nhật sau
            vitrimoi=null;
        }
    }
}
