package fragment;

import android.media.Image;

/**
 * Created by TDLAM123 on 4/22/2017.
 */

public class ItemDanhBa {
    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    private String Ten;
    private String DiaChi;
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
