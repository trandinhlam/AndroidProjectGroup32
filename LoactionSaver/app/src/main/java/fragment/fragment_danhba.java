package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thekiet.loactionsaver.R;

/**
 * Created by TheKiet on 4/18/2017.
 */

public class fragment_danhba extends Fragment {
    TextView tbao;
    ListView lvdanhba;
    final String arr[]={"Trương Thế Kiệt",
            "Trần Đình Lâm",
            "Long"
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {



        View layout_danhba=inflater.inflate(R.layout.fragment_danhba, container,false);
        tbao=(TextView) layout_danhba.findViewById(R.id.tvdanhba);
        tbao.setText("Danh sách danh bạ");

        lvdanhba=(ListView) layout_danhba.findViewById(R.id.listdanhba);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1,arr);
        lvdanhba.setAdapter(adapter);
        return layout_danhba;
    }
}
