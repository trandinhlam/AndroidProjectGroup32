package fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
    private static final int MENU_ITEM_VIEW =111 ;
    private static final int MENU_ITEM_CREATE =222 ;
    private static final int MENU_ITEM_EDIT =333 ;
    private static final int MENU_ITEM_DELETE =444 ;
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

        // Đăng ký Context menu cho ListView.
        registerForContextMenu(this.lvdanhba);

        lvdanhba.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemDanhBa duocchon=listdulieu.get(position);
                showItem(duocchon);


            }
        });

        return layout_danhba;
    }
    private void showItem(ItemDanhBa duocchon){
        // hien thi chi tiet mot item
        Intent intent=new Intent(getActivity() ,ThongTinViTri.class);
        Bundle mybundle=new Bundle();
        mybundle.putInt("Id",duocchon.getId());
        mybundle.putString("Ten",duocchon.getTen());
        mybundle.putString("DiaChi",duocchon.getDiaChi());
        mybundle.putString("SDT",duocchon.getSDT());
        mybundle.putString("Note",duocchon.getNote());
        mybundle.putInt("HinhAnh",duocchon.getHinhAnh());

        intent.putExtras(mybundle);
        getActivity().startActivity(intent);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Chọn thao tác");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_VIEW , 0, "Chi tiết");
        //menu.add(0, MENU_ITEM_CREATE , 1, "Create Note");
        menu.add(0, MENU_ITEM_EDIT , 1, "Sửa");
        menu.add(0, MENU_ITEM_DELETE, 2, "Xóa");
    }
    private void deleteItem(ItemDanhBa itemxoa)  {
        MyDatabaseHelper db = new MyDatabaseHelper(this.getContext());
        db.deleteItem(itemxoa);
        this.listdulieu.remove(itemxoa);
        // Refresh ListView adapter
        this.adapter.notifyDataSetChanged();
        db.close();
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final ItemDanhBa duocchon=listdulieu.get(info.position);
        switch(item.getItemId()){
            case MENU_ITEM_VIEW:
                {
                    showItem(duocchon);
                }
                break;
            case MENU_ITEM_EDIT:
                {

                }
                break;
            case MENU_ITEM_DELETE:
                {
                    // Hỏi trước khi xóa.
                    new AlertDialog.Builder(this.getContext())
                            .setMessage("Bạn chắc chắn muốn xóa "+duocchon.getTen() +" khỏi danh bạ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                        deleteItem(duocchon);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public void updateStatus(ItemDanhBa itemdeleted){
        this.listdulieu.remove(itemdeleted);
        adapter.notifyDataSetChanged();
    }
}
