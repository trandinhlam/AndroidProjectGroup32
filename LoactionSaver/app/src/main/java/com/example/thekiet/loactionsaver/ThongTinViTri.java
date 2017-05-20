package com.example.thekiet.loactionsaver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
    ImageView imageGoiDien,imageNhanTin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemdanhba_chitiet_layout);

        //Ánh xạ các Object trong view ra Controler Activity
        currentAct=this;

        txtTen=(TextView) findViewById(R.id.TenDiaDiem);
        txtDiachi= (TextView) findViewById(R.id.DiaChi);
        txtDT= (TextView) findViewById(R.id.SDT);
        txtnote =(TextView) findViewById(R.id.Note);

        hinh= (ImageView) findViewById(R.id.hinhanhchitiet);
        btnChiDuong=(Button) findViewById(R.id.btnChiDuong);
        imageGoiDien=(ImageView) findViewById(R.id.ImageGoiDienThoai);
        imageGoiDien.setImageResource(R.drawable.icon_call);
        imageNhanTin=(ImageView) findViewById(R.id.ImageGuiTinNhan);
        imageNhanTin.setImageResource(R.drawable.icon_message);

        Intent intentExtras=getIntent();
        Bundle mybundle=intentExtras.getExtras();
        if(!mybundle.isEmpty()){
            // xuất thông tin chi tiết ra màn hình
            item= (ItemDanhBa) mybundle.getSerializable("item");

            txtTen.setText(item.getTen());
            txtDiachi.setText("Địa chỉ: "+item.getDiaChi());
            txtDT.setText("ĐT: "+item.getSDT());
            txtnote.setText("Note: \n\t"+item.getNote());


            if(item.getHinhAnh()!= null){
                // nếu có hình thì load hình
                // tạo bitmap để chuyển thành hình
                Bitmap bitmap= BitmapFactory.decodeByteArray(item.getHinhAnh(),0,item.getHinhAnh().length);
                hinh.setImageBitmap(bitmap);
                //hinh.setImageDrawable(item.getHinhAnh().getDrawable());
            }
            else{// nếu không có hình
                hinh.setImageResource(R.drawable.iconavatar);// hình ảnh tượng trưng
            }
            // thực hiện gọi điện
            imageGoiDien.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String chuoiSDT=item.getSDT().toString().trim();
                    if(!chuoiSDT.equals("")){// kiểm tra số điện thoại có hay không
                            Intent phoneIntent=new Intent(Intent.ACTION_DIAL);
                            phoneIntent.setData(Uri.parse("tel:"+chuoiSDT));
                            startActivity(phoneIntent);
                    }

                }
            });
            // thực hiện nhắn tin
            imageNhanTin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String chuoiSDT=item.getSDT().toString().trim();
                    if(!chuoiSDT.equals("")){// kiểm tra số điện thoại có hay không
                        Intent SMSIntent=new Intent(Intent.ACTION_SENDTO);
                        SMSIntent.setData(Uri.parse("smsto:"+chuoiSDT));
                        startActivity(SMSIntent);
                    }
                }
            });
            btnChiDuong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Intent myIntent = new Intent(getApplication().getBaseContext(), ChiDuong.class);

                        Bundle extras = new Bundle();
                        extras.putDouble("Lat",item.getLatitude());
                        extras.putDouble("Lng" , item.getLongtitude());

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
        else {
            Toast.makeText(getApplicationContext(), "Lỗi truyền dữ liệu", Toast.LENGTH_SHORT).show();
        }



    }
}
