package fragment;

import android.media.Image;

import java.io.Serializable;

/**
 * Created by TDLAM123 on 4/22/2017.
 * Class mô tả thông tin cần lưu của 1 địa điểm cụ thể, phục vu cho việc tạo địa điểm, chỉnh sửa và
 * tra cứu địa điểm.
 * Class gồm 2 hàm khởi tạo, hàm 1 khởi tạo với giá trị tọa độ 0,0. hàm 2 có giá trị tọa độ cho trước
 */

public class ItemDanhBa implements Serializable{
    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    private String Ten;
    private String DiaChi;
    private double Latitude;

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(double longtitude) {
        Longtitude = longtitude;
    }

    private double Longtitude;
    private String SDT;
    private String Note;
    private int HinhAnh;

    public ItemDanhBa() {
    }

    public ItemDanhBa(int id, String ten, String diaChi, String SDT, String note, int hinhAnh) {
        Id = id;
        Ten = ten;
        DiaChi = diaChi;
        this.SDT = SDT;
        Note = note;
        HinhAnh = hinhAnh;
        Latitude = 0;
        Longtitude = 0;
    }

    public ItemDanhBa(int id, String ten, String diaChi, double latitude, double longtitude, String SDT, String note, int hinhAnh) {
        Id = id;
        Ten = ten;
        DiaChi = diaChi;
        Latitude = latitude;
        Longtitude = longtitude;
        this.SDT = SDT;
        Note = note;
        HinhAnh = hinhAnh;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public int getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(int hinhAnh) {
        HinhAnh = hinhAnh;
    }


}
