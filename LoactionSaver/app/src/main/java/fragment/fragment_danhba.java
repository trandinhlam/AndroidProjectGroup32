package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thekiet.loactionsaver.R;

import java.util.ArrayList;

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

        ItemDanhBa item1=new ItemDanhBa("Kiệt","Địa chỉ 1"," 0153245874","Note 1",R.drawable.icon_menu);
        listdulieu.add(item1);
        ItemDanhBa item2=new ItemDanhBa("Lâm","Địa chỉ 2"," 01683522356","Note 2",R.drawable.icon_menu);
        listdulieu.add(item2);
        ItemDanhBa item3=new ItemDanhBa("Long","Địa chỉ 3"," 0153245874","Note 3",R.drawable.icon_menu);
        listdulieu.add(item3);


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

                Toast.makeText(getContext(), duocchon.getTen()+"\n"
                                            +duocchon.getSDT()+"\n"
                                            +duocchon.getDiaChi()
                                , Toast.LENGTH_SHORT).show();
            }
        });

        return layout_danhba;
    }
}
