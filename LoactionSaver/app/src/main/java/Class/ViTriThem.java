package Class;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by TheKiet on 4/22/2017.
 */

public class ViTriThem implements Serializable {
    private String TenViTri;
    private String DiaChi;

    private double Latitude;
    private double Longtitude;

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

    public ViTriThem()
    {
          TenViTri = "";
        DiaChi = "";
        Latitude = 0;
        Longtitude = 0;

    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }
    public String getTenViTri() {
        return TenViTri;
    }

    public void setTenViTri(String tenViTri) {
        TenViTri = tenViTri;
    }



}
