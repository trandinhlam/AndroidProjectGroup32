package fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thekiet.loactionsaver.R;
import com.example.thekiet.loactionsaver.ThongTinViTri;

import java.util.ArrayList;

import data.MyDatabaseHelper;

/**
 * Created by TheKiet on 4/18/2017.
 */

public class fragment_danhba extends Fragment {
    TextView tbao;
    ListView lvdanhba;
    ArrayList<ItemDanhBa> listdulieu=new ArrayList<ItemDanhBa>();
    MyArrayAdapter adapter=null;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // tạo dữ liệu cho listdulieu;

        MyDatabaseHelper db=new MyDatabaseHelper(this.getContext());
        db.createDefaultDataIfNeed();

        listdulieu=db.getAllItems();


        View layout_danhba=inflater.inflate(R.layout.fragment_danhba, container,false);
        tbao=(TextView) layout_danhba.findViewById(R.id.tvdanhba);
        //tbao.setText("Danh sách danh bạ");

        lvdanhba=(ListView) layout_danhba.findViewById(R.id.listdanhba);

        adapter = (MyArrayAdapter) new MyArrayAdapter(this.getActivity(),
                R.layout.itemdanhba_layout,listdulieu);
        lvdanhba.setAdapter(adapter);

        lvdanhba.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemDanhBa duocchon=listdulieu.get(position);

                // hien thi chi tiet mot item
                Intent intent=new Intent(getActivity() ,ThongTinViTri.class);
                Bundle mybundle=new Bundle();
                mybundle.putString("Ten",duocchon.getTen());
                mybundle.putString("DiaChi",duocchon.getDiaChi());
                mybundle.putString("SDT",duocchon.getSDT());
                mybundle.putString("Note",duocchon.getNote());
                mybundle.putInt("HinhAnh",duocchon.getHinhAnh());

                intent.putExtras(mybundle);
                getActivity().startActivity(intent);


            }
        });

        return layout_danhba;
    }
}
