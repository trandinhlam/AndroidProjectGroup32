package fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

/**
 * Created by TDLAM123 on 4/22/2017.
 */

public class MyArrayAdapter extends ArrayAdapter<ItemDanhBa>{
    Activity context=null;
    ArrayList<ItemDanhBa> arrayItems=null;
    int LayoutId;

    // constructor co ban cua cac class ke thua ArrayAdapter
    public MyArrayAdapter(Activity context, int resource, ArrayList<ItemDanhBa> listdanhba) {
        super(context, resource, listdanhba);
        this.context=context;
        this.arrayItems=listdanhba;
        this.LayoutId=resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        convertView=inflater.inflate(LayoutId,null);

        if(arrayItems.size()>0&&position>=0){// kiem tra mang du lieu co rong hay khong
            ImageView imgItem=(ImageView) convertView.findViewById(R.id.HinhanhItem);
            TextView txtTenItem=(TextView) convertView.findViewById(R.id.txtTenItem);

            ItemDanhBa itemchon=arrayItems.get(position);

            imgItem.setImageResource(R.drawable.icon_menu);
            txtTenItem.setText(itemchon.getTen());

        }


        return convertView;
    }
}
