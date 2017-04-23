package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.thekiet.loactionsaver.R;

import java.util.ArrayList;
import java.util.List;

import fragment.ItemDanhBa;

/**
 * Created by TDLAM123 on 4/22/2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="db";
    private static final int DATABASE_VERSION = 1 ;
    private static final String TABLE_DanhBa ="danhba";
    private static final String COLUMN_ID ="ID" ;
    private static final String COLUMN_NAME ="Ten" ;
    private static final String COLUMN_ADDRESS ="DiaChi" ;
    private static final String COLUMN_PHONE ="SDT" ;
    private static final String COLUMN_NOTE ="Note" ;
    private static final String COLUMN_IMAGE ="HinhAnh" ;

    private static final String TAG = "SQLite";
    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script to create table.
        String script = "CREATE TABLE " + TABLE_DanhBa + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME+ " TEXT, "
                + COLUMN_ADDRESS+ " TEXT, "
                + COLUMN_PHONE+ " TEXT, "
                + COLUMN_NOTE + " TEXT, "
                + COLUMN_IMAGE + " TEXT"+")";
        // Execute script.
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DanhBa);


        // Recreate
        onCreate(db);
    }
    public void createDefaultDataIfNeed(){
        int count=this.getItemsCount();
        if(0==count){// nếu dữ liệu rỗng
            ItemDanhBa item1=new ItemDanhBa(1, "Kiệt","P. 12, Quận 5, Thành phố Hồ Chí Minh"," 0153245874","Note 1",0);
            this.addItem(item1);
            ItemDanhBa item2=new ItemDanhBa(2, "Lâm","Địa chỉ 2"," 01683522356","Note 2",0);
            this.addItem(item2);
            ItemDanhBa item3=new ItemDanhBa(3, "Long","Địa chỉ 3"," 0153245874","Note 3",0);
            this.addItem(item3);
        }
    }

    private void addItem(ItemDanhBa item1) {
        Log.i(TAG, "MyDatabaseHelper.addItem ... " + item1.getTen());
        // mở kết nối database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item1.getTen());
        values.put(COLUMN_ADDRESS,item1.getDiaChi());
        values.put(COLUMN_PHONE,item1.getSDT());
        values.put(COLUMN_NOTE,item1.getNote());


        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_DanhBa, null, values);
        // Đóng kết nối database.
        db.close();

    }


    public int getItemsCount() {
        Log.i(TAG, "MyDatabaseHelper.getItemsCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_DanhBa;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();
        // return count
        return count;
    }

    public ArrayList<ItemDanhBa> getAllItems() {
        Log.i(TAG,"MyDatabaseHelper.getAllItems....");
        List<ItemDanhBa> list=new ArrayList<ItemDanhBa>();
        // Select All query

        String query="SELECT * FROM "+TABLE_DanhBa;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query,null);

        // Duyệt trên cursor và thêm vào danh sách
        if(cursor.moveToFirst()){
            do{
                ItemDanhBa newitem=new ItemDanhBa(cursor.getInt(0),
                                                  cursor.getString(1),
                                                  cursor.getString(2),
                                                  cursor.getString(3),
                                                  cursor.getString(4),
                                                  cursor.getInt(5));
                list.add(newitem);
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return (ArrayList<ItemDanhBa>) list;
    }

    public void deleteItem(ItemDanhBa item) {
        Log.i(TAG, "MyDatabaseHelper.deleteItem ... " + item.getTen() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DanhBa, COLUMN_ID + " = ?",
                new String[] { String.valueOf(item.getId())});
        db.close();
    }
}
