package Data;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thekiet.loactionsaver.R;

import java.util.ArrayList;
import java.util.List;

import fragment.ItemDanhBa;
import Class.*;

import static com.example.thekiet.loactionsaver.R.id.listdanhba;
import static com.example.thekiet.loactionsaver.R.id.tvDiaChi;
import static com.example.thekiet.loactionsaver.R.id.txtTenItem;

/**
 * Created by TheKiet on 5/6/2017.
 */

public class AdapterGoiY extends ArrayAdapter<GoiY> {
    Activity context=null;
    ArrayList<GoiY>myArray=null;
    int layoutId;

    public AdapterGoiY(Activity context, int resource, ArrayList<GoiY> listdanhba) {
        super(context, resource, listdanhba);
        this.context=context;
        this.myArray=listdanhba;
        this.layoutId=resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        convertView=inflater.inflate(layoutId,null);

        if(myArray.size()>0&&position>=0){// kiem tra mang du lieu co rong hay khong
            TextView tvTen=(TextView) convertView.findViewById(R.id.tvTen);
            TextView tvDiaChi =(TextView) convertView.findViewById(R.id.tvDiaChi);

            GoiY itemchon=myArray.get(position);

            tvTen.setText(itemchon.getTEN());
            tvDiaChi.setText("Địa chỉ : \n" + itemchon.getDIACHI());

        }
        return convertView;
    }


}
