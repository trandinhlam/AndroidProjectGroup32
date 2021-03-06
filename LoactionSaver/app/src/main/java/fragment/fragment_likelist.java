package fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thekiet.loactionsaver.MainActivity;
import com.example.thekiet.loactionsaver.R;
import com.example.thekiet.loactionsaver.Them_CapNhatDanhBa_Activity;
import com.example.thekiet.loactionsaver.ThongTinViTri;

import java.io.Serializable;
import java.util.ArrayList;

import Data.MyDatabaseHelper;



/**
 * Created by TheKiet on 4/18/2017.
 */

public class fragment_likelist extends Fragment {
    private static final int MENU_ITEM_VIEW =1111 ;
    private static final int MENU_ITEM_EDIT =2222 ;
    private static final int MENU_ITEM_DELETE =3333 ;
    private static final int MY_REQUEST_CODE =100 ;

    MainActivity mainactivity;

    TextView tbao;
    ListView lvdanhbayeuthich;
    ArrayList<ItemDanhBa> listdulieu=new ArrayList<ItemDanhBa>();
    MyArrayAdapter adapter=null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // tạo dữ liệu cho listdulieu;

        MyDatabaseHelper db=new MyDatabaseHelper(this.getContext());
        //db.createDefaultDataIfNeed();

        listdulieu=db.getAllLikeListItems();


        View layout_danhba=inflater.inflate(R.layout.fragment_likelist, container,false);
        tbao=(TextView) layout_danhba.findViewById(R.id.tvdanhba);
        //tbao.setText("Danh sách danh bạ");

        lvdanhbayeuthich=(ListView) layout_danhba.findViewById(R.id.ListYeuThich);

        adapter = (MyArrayAdapter) new MyArrayAdapter(this.getActivity(),
                R.layout.itemdanhba_layout,listdulieu);
        lvdanhbayeuthich.setAdapter(adapter);

        // Đăng ký Context menu cho ListView.
        registerForContextMenu(this.lvdanhbayeuthich);

        lvdanhbayeuthich.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        Intent intent=new Intent(getActivity().getBaseContext() ,ThongTinViTri.class);
        Bundle mybundle=new Bundle();

        mybundle.putSerializable("item", (Serializable) duocchon);
        intent.putExtras(mybundle);
        getActivity().startActivity(intent);
    }

    private void UpDateItem(ItemDanhBa duocchon){
        // hien thi chi tiet mot item
        Intent intent=new Intent(getActivity().getBaseContext() ,Them_CapNhatDanhBa_Activity.class);
        Bundle mybundle=new Bundle();
        mybundle.putSerializable("item", (Serializable) duocchon);
        intent.putExtras(mybundle);
        this.startActivityForResult(intent,MY_REQUEST_CODE);
    }

    public void deleteItem(ItemDanhBa itemxoa)  {
        MyDatabaseHelper db = new MyDatabaseHelper(this.getContext());
        db.deleteLikeListItem(itemxoa.getId());
        this.listdulieu.remove(itemxoa);
        // Refresh ListView adapter
        this.adapter.notifyDataSetChanged();
        db.close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Chọn thao tác");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_VIEW , 0, "Chi tiết");
        menu.add(0, MENU_ITEM_EDIT , 1, "Sửa");
        menu.add(0, MENU_ITEM_DELETE, 2, "Xóa khỏi yêu thích");
    }

    //  Phương thức này được gọi sau khi Fragment được ghép vào Activity.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            this.mainactivity = (MainActivity) context;
        }
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
                UpDateItem(duocchon);
            }
            break;
            case MENU_ITEM_DELETE:
            {
                // Hỏi trước khi xóa.
                new AlertDialog.Builder(this.getContext())
                        .setMessage("Bạn chắc chắn muốn xóa "+duocchon.getTen() +" khỏi danh sách yêu thích?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteItem(duocchon);
                                RefreshLikeList();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                Toast.makeText(this.getContext(),
                        "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
            }
            break;
            default:
                return false;
        }
        return true;
    }

    public void RefreshLikeList()
    {

            this.listdulieu.clear();
            MyDatabaseHelper db = new MyDatabaseHelper(this.getContext());
            this.listdulieu = db.getAllLikeListItems();
            db.close();
            adapter = (MyArrayAdapter) new MyArrayAdapter(this.getActivity(),
                    R.layout.itemdanhba_layout, listdulieu);
            lvdanhbayeuthich.setAdapter(adapter);

    }

    // Khi một Activity hoàn thành thì nó gửi phản hồi lại, ta cần override hàm dưới đây để xử lí phản hồi
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE ) {
            boolean needRefresh = data.getBooleanExtra("needRefresh",true);
            // Refresh ListView
            if(needRefresh) {
                this.listdulieu.clear();
                MyDatabaseHelper db = new MyDatabaseHelper(this.getContext());
                this.listdulieu =  db.getAllLikeListItems();
                db.close();
                adapter = (MyArrayAdapter) new MyArrayAdapter(this.getActivity(),
                        R.layout.itemdanhba_layout,listdulieu);
                lvdanhbayeuthich.setAdapter(adapter);
                // Thông báo dữ liệu thay đổi (Để refresh ListView).

                mainactivity.refreshDanhBa();

                Log.i("ThongBao","RefreshListview....size="+ listdulieu.size());
            }
        }
    }
}
