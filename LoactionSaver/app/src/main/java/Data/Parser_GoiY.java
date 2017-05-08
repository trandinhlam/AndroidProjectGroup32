package Data;

import org.json.JSONException;
import org.json.JSONObject;
import Class.*;
/**
 * Created by TheKiet on 5/6/2017.
 */

public class Parser_GoiY {

    public static GoiY parseGoiY(JSONObject jsonObject) throws JSONException {
        GoiY goiy = new GoiY();
        goiy.setID(jsonObject.getString("ID1"));
        goiy.setTEN(jsonObject.getString("Ten"));
        goiy.setDIACHI(jsonObject.getString("Diachi"));
        goiy.setLAT(jsonObject.getString("Lat"));
        goiy.setLNG(jsonObject.getString("Lng"));
        goiy.setNOTE(jsonObject.getString("Note"));
        return goiy;
    }
    public static GoiY parseGoiYImage(JSONObject jsonObject) throws JSONException {
        GoiY goiy = new GoiY();
        goiy.setID(jsonObject.getString("ID"));
        goiy.setTEN(jsonObject.getString("TEN"));
        goiy.setDIACHI(jsonObject.getString("DIACHI"));
        goiy.setLAT(jsonObject.getString("LAT"));
        goiy.setLNG(jsonObject.getString("LNG"));
        goiy.setNOTE(jsonObject.getString("NOTE"));
        goiy.setImage1(jsonObject.getString("HINH1"));
        goiy.setImage2(jsonObject.getString("HINH2"));
        return goiy;
    }

}
