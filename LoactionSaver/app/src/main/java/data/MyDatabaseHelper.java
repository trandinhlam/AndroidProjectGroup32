package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

import com.example.thekiet.loactionsaver.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import fragment.ItemDanhBa;

/**
 * Created by TDLAM123 on 4/22/2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="db1";
    private static final int DATABASE_VERSION =1 ;
    private static final String TABLE_DanhBa ="danhba";
    private static final String COLUMN_ID ="ID";
    private static final String COLUMN_NAME ="Ten" ;
    private static final String COLUMN_ADDRESS ="DiaChi" ;
    private static final String COLUMN_LATITUDE="Latitude";
    private static final String COLUMN_LONGTITUDE="Longtitude";
    private static final String COLUMN_PHONE ="SDT" ;
    private static final String COLUMN_NOTE ="Note" ;
    private static final String COLUMN_IMAGE ="HinhAnh" ;

    private static final String TABLE_YEUTHICH = "yeuthich";
    private static final String COLUMN_IDL ="ID";


    private static final String TAG = "SQLite";
    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Script to create table.
        //tạo bảng danh bạ
        String script = "CREATE TABLE " + TABLE_DanhBa + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NAME+ " TEXT, "
                + COLUMN_ADDRESS+ " TEXT, "
                + COLUMN_LATITUDE + " REAL, "
                + COLUMN_LONGTITUDE + " REAL, "
                + COLUMN_PHONE+ " TEXT, "
                + COLUMN_NOTE + " TEXT, "
                + COLUMN_IMAGE + " BLOB"+")";
        // Execute script.
        db.execSQL(script);

        //tạo bảng yêu thích
        String script1 = "CREATE TABLE yeuthich" +
                "("
                + "ID INTEGER NOT NULL CONSTRAINT ID REFERENCES danhba(ID) ON DELETE CASCADE" +
                ")";
        db.execSQL(script1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_YEUTHICH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DanhBa);
        // Recreate
        onCreate(db);
    }

    public void createDefaultDataIfNeed(){
        int count=this.getItemsCount();
        if(0==count){// nếu dữ liệu rỗng
            ItemDanhBa item1=new ItemDanhBa(1, "Kiệt","P. 12, Quận 5, Thành phố Hồ Chí Minh","0153245874","Note 1",null);
            this.addItem(item1);
            ItemDanhBa item2=new ItemDanhBa(2, "Lâm","Địa chỉ 2","01683522356","Note 2",null);
            this.addItem(item2);
            ItemDanhBa item3=new ItemDanhBa(3, "Long","Địa chỉ 3","0153245874","Note 3",null);
            this.addItem(item3);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
    // phần like list
    public void addLikeListItem(Integer IDDB)
    {
        // mở kết nối database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IDL, IDDB);

        // chèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_YEUTHICH, null, values);
        // Đóng kết nối database.
        db.close();
    }

    public ArrayList<ItemDanhBa> getAllLikeListItems() {
        List<ItemDanhBa> list=new ArrayList<ItemDanhBa>();
        // Select All query

        String query="SELECT * FROM danhba WHERE ID IN (SELECT ID FROM yeuthich)";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query,null);

        // Duyệt trên cursor và thêm vào danh sách
        if(cursor.moveToFirst()){
            do{
                ItemDanhBa newitem=new ItemDanhBa(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getBlob(7));
                list.add(newitem);
                Log.i(TAG,"\n..."+newitem.getTen()
                        +"("+newitem.getLatitude()+","+newitem.getLongtitude()+")");


            }
            while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return (ArrayList<ItemDanhBa>) list;
    }

    public void deleteLikeListItem(Integer IDDB) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_YEUTHICH, COLUMN_ID + " = ?", new String[]{""+IDDB});
        db.close();
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
    // phần danh bạ
    public void addItem(ItemDanhBa item1) {
        Log.i(TAG, "MyDatabaseHelper.addItem ... " + item1.getTen());
        // mở kết nối database
        SQLiteDatabase db = this.getWritableDatabase();

       /* ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item1.getTen());
        values.put(COLUMN_ADDRESS,item1.getDiaChi());
        values.put(COLUMN_LATITUDE,item1.getLatitude());
        values.put(COLUMN_LONGTITUDE,item1.getLongtitude());
        values.put(COLUMN_PHONE,item1.getSDT());
        values.put(COLUMN_NOTE,item1.getNote());*/



        // chèn một dòng dữ liệu vào bảng.
        String sql="INSERT INTO "+TABLE_DanhBa+" VALUES(null,?,?,?,?,?,?,?)";
        SQLiteStatement statement=db.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1,item1.getTen());
        statement.bindString(2,item1.getDiaChi());
        statement.bindDouble(3,item1.getLatitude());
        statement.bindDouble(4,item1.getLongtitude());
        statement.bindString(5,item1.getSDT());
        statement.bindString(6,item1.getNote());
        statement.bindBlob(7,item1.getHinhAnh());


        statement.executeInsert();
        //db.insert(TABLE_DanhBa, null, values);
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
                                                  cursor.getDouble(3),
                                                  cursor.getDouble(4),
                                                  cursor.getString(5),
                                                  cursor.getString(6),
                                                  cursor.getBlob(7));




                list.add(newitem);
                Log.i(TAG,"\n..."+newitem.getTen()
                        +"("+newitem.getLatitude()+","+newitem.getLongtitude()+")");


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

    public int updateItem(ItemDanhBa item) {
        Log.i(TAG, "MyDatabaseHelper.updateItem ... "  + item.getTen());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getTen());
        values.put(COLUMN_ADDRESS,item.getDiaChi());
        values.put(COLUMN_LATITUDE,item.getLatitude());
        values.put(COLUMN_LONGTITUDE,item.getLongtitude());
        values.put(COLUMN_PHONE,item.getSDT());
        values.put(COLUMN_NOTE,item.getNote());


        // updating row
        int kq= db.update(TABLE_DanhBa, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return kq;
    }

    // hàm bổ trợ tham khảo trên mạng

    private byte[] ImageView_To_Byte(ImageView im){
        BitmapDrawable drawable=(BitmapDrawable) im.getDrawable();
        Bitmap bmp=drawable.getBitmap();

        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray=stream.toByteArray();
        return byteArray;
    }
}
