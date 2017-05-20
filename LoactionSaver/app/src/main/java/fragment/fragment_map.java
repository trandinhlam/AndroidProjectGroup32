package fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.thekiet.loactionsaver.AddDanhBa;
import com.example.thekiet.loactionsaver.R;
import com.example.thekiet.loactionsaver.activiti_GoiY;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import Class.*;

import static android.R.attr.y;


/**
 * Created by TheKiet on 4/18/2017.
 */

public class fragment_map extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private FloatingActionButton fab_menu, fab_search, fab_idea;

    private View view_search, view_idea;
    private Animation ani_search, ani_idea, ani_back_search, ani_back_idea;


    EditText editSearch, editidea;
    Button btnSearch, btnIdea;


    private TextView tvTimKiem, tvGoiY;
    Boolean moveback = false;

    String ViTriSearch = null;
    String ViTriMap = null;
    ImageView btntat, btntatidea;

    static ViTriThem vitrithem = new ViTriThem();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);


        fab_menu = (FloatingActionButton) rootView.findViewById(R.id.fb_menu);
        fab_search = (FloatingActionButton) rootView.findViewById(R.id.fb_search);
        fab_idea = (FloatingActionButton) rootView.findViewById(R.id.fb_idea);


        tvTimKiem = (TextView) rootView.findViewById(R.id.textViewSearch);
        tvGoiY = (TextView) rootView.findViewById(R.id.textViewIdea);

        editSearch = (EditText) rootView.findViewById(R.id.editSearch);
        btnSearch = (Button) rootView.findViewById(R.id.btnTimKiem);

        editidea = (EditText) rootView.findViewById(R.id.editKhoangCach);
        btnIdea = (Button) rootView.findViewById(R.id.btnTimKiemidea);
        btntat = (ImageView)rootView.findViewById(R.id.btntat);
        btntatidea = (ImageView)rootView.findViewById(R.id.btntatidea);


        view_search = (View) rootView.findViewById(R.id.layout_search);
        view_idea = (View) rootView.findViewById(R.id.layout_idea);


        /*animation*/
        ani_search = AnimationUtils.loadAnimation(getContext(), R.anim.move_search);
        ani_idea = AnimationUtils.loadAnimation(getContext(), R.anim.move_idea);

        ani_back_search = AnimationUtils.loadAnimation(getContext(), R.anim.back_search);
        ani_back_idea = AnimationUtils.loadAnimation(getContext(), R.anim.back_idea);

        btntatidea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_search.setVisibility(View.GONE);
                view_idea.setVisibility(View.GONE);
            }
        });

        btntat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_search.setVisibility(View.GONE);
                view_idea.setVisibility(View.GONE);
            }
        });
/*Thao tac menu*/
        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(), "Bạn đã chọn menu", Toast.LENGTH_SHORT).show();
                if (moveback == false) {
                    Move();
                    moveback = true;
                } else {
                    Back();
                    moveback = false;

                    editidea.setText("");
                    editSearch.setText("");
                }
            }
        });

        fab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
                moveback = false;
                view_search.setVisibility(View.VISIBLE);
                view_idea.setVisibility(View.GONE);
                editidea.setText("");
                editSearch.setText("");
            }
        });


        fab_idea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
                moveback = false;
                view_idea.setVisibility(View.VISIBLE);
                view_search.setVisibility(View.GONE);
                editidea.setText("");
                editSearch.setText("");
            }
        });

        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(true);




        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            }
        } else
            map.setMyLocationEnabled(true);

        map.getUiSettings().setCompassEnabled(true);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                ViTriSearch = editSearch.getText() + "";
                List<Address> listAddress = null;
                if (!ViTriSearch.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.forLanguageTag("vi"));

                    try {
                        listAddress = geocoder.getFromLocationName(ViTriSearch, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Kiem tra ket noi mang", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (listAddress.size() == 0) {
                        Toast.makeText(getContext(), "Không có địa điểm vừa tìm!", Toast.LENGTH_SHORT).show();
                    } else {
                        Address address = listAddress.get(0);
                        LatLng latln = new LatLng(address.getLatitude(), address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latln).title(address.getFeatureName()));
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latln, 25));
                        map.animateCamera(CameraUpdateFactory.zoomIn());
                        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append(", ");
                        }

                        String result = sb.toString();
                        vitrithem.setDiaChi(result);
                        // vitrithem.setLalng(latln);
                        vitrithem.setLatitude(address.getLatitude());
                        vitrithem.setLongtitude(address.getLongitude());
                        vitrithem.setTenViTri(address.getFeatureName());
                    }
                } else {
                    Toast.makeText(getContext(), "Nhập vào địa điểm cần tìm!", Toast.LENGTH_SHORT);
                }
            }
        });

        /*btn idea*/
        btnIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String khoangcach = editidea.getText() + "";
                if (khoangcach.equals("")) {
                    Toast.makeText(getActivity(),"Nhập vào khoảng cách trước khi bấm tìm kiếm!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Location myloction = map.getMyLocation();

                if (myloction == null)
                {
                    Toast.makeText(getActivity(), "Mở GPS để thực hiện chức năng này", Toast.LENGTH_SHORT).show();
                    return ;
                }

                ViTriThem tmpViTriThem = new ViTriThem();
                tmpViTriThem.setTenViTri("");
                tmpViTriThem.setDiaChi("");
                tmpViTriThem.setLatitude(myloction.getLatitude());
                tmpViTriThem.setLongtitude(myloction.getLongitude());

                Intent myIntent = new Intent(getActivity().getBaseContext(), activiti_GoiY.class);


                Bundle extras = new Bundle();
                extras.putSerializable("Address", (Serializable) tmpViTriThem);
                extras.putString("khoangcach", khoangcach);

                myIntent.putExtras(extras);
                try {
                    startActivityForResult(myIntent, 0);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        map.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                Toast.makeText(getContext(), "Bấm giữ lêu để hiện thôn tin địa điểm", Toast.LENGTH_SHORT).show();
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getContext(), "Thêm vào danh bạ (sqlite)", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(getActivity().getBaseContext(), AddDanhBa.class);

                Bundle extras = new Bundle();
                extras.putSerializable("Address", (Serializable) vitrithem);
                extras.putString("request", "0");

                myIntent.putExtras(extras);
                try {
                    startActivityForResult(myIntent, 0);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                Geocoder geocoder = new Geocoder(getContext(), Locale.forLanguageTag("vi"));
                List<Address> address = null;
                try {
                    address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Kiem tra ket noi mang", Toast.LENGTH_SHORT).show();
                    return ;
                }

                Address diachi = address.get(0);

                map.addMarker(new MarkerOptions().position(latLng).title(diachi.getFeatureName()));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                map.animateCamera(CameraUpdateFactory.zoomIn());
                map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < diachi.getMaxAddressLineIndex(); i++) {
                    sb.append(diachi.getAddressLine(i)).append(", ");
                }

                String result = sb.toString();
                vitrithem.setDiaChi(result);
                // vitrithem.setLalng(latln);
                vitrithem.setLatitude(diachi.getLatitude());
                vitrithem.setLongtitude(diachi.getLongitude());
                vitrithem.setTenViTri(diachi.getFeatureName());
            }
        });

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                //  Toast.makeText(getActivity(), "ád", Toast.LENGTH_SHORT);
                Location myloction = map.getMyLocation();
                map.clear();

                if (myloction == null) {
                    Toast.makeText(getActivity(), "Bật GPS để thực hiện chức năng này", Toast.LENGTH_SHORT).show();
                    return false;

                }
                LatLng lalng = new LatLng(myloction.getLatitude(), myloction.getLongitude());

                Geocoder geocoder = new Geocoder(getContext(), Locale.forLanguageTag("vi"));
                List<Address> address = null;
                try {
                    address = geocoder.getFromLocation(myloction.getLatitude(), myloction.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Kiem tra ket noi mang", Toast.LENGTH_SHORT).show();
                    return false;
                }

                Address diachi = address.get(0);
                map.addMarker(new MarkerOptions().position(lalng).title(diachi.getFeatureName()));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lalng, 15));
                map.animateCamera(CameraUpdateFactory.zoomIn());
                map.animateCamera(CameraUpdateFactory.zoomTo(25), 2000, null);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < diachi.getMaxAddressLineIndex(); i++) {
                    sb.append(diachi.getAddressLine(i)).append(", ");
                }

                String result = sb.toString();
                vitrithem.setDiaChi(result);
                // vitrithem.setLalng(latln);
                vitrithem.setLatitude(diachi.getLatitude());
                vitrithem.setLongtitude(diachi.getLongitude());
                vitrithem.setTenViTri(diachi.getFeatureName());

                return false;
            }
        });



            LatLng lalng = new LatLng(10.765977, 106.679409);

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lalng, 10));
            map.animateCamera(CameraUpdateFactory.zoomIn());
            map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

    }

    private void Move() {

        FrameLayout.LayoutParams params_Search = (FrameLayout.LayoutParams) fab_search.getLayoutParams();
        params_Search.bottomMargin = (int) (fab_search.getWidth() * 2.5);
        fab_search.setLayoutParams(params_Search);
        fab_search.startAnimation(ani_search);


        FrameLayout.LayoutParams params_timkiem = (FrameLayout.LayoutParams) tvTimKiem.getLayoutParams();
        params_timkiem.bottomMargin = (int) (fab_search.getWidth() * 2.7);
        tvTimKiem.setLayoutParams(params_timkiem);
        tvTimKiem.setVisibility(View.VISIBLE);
        tvTimKiem.startAnimation(ani_search);


        FrameLayout.LayoutParams params_Idea = (FrameLayout.LayoutParams) fab_idea.getLayoutParams();
        params_Idea.bottomMargin = (int) (fab_search.getWidth() * 3.8);
        fab_idea.setLayoutParams(params_Idea);
        fab_idea.startAnimation(ani_idea);

        FrameLayout.LayoutParams params_GoiY = (FrameLayout.LayoutParams) tvGoiY.getLayoutParams();
        params_GoiY.bottomMargin = (int) (fab_search.getWidth() * 4.0);
        tvGoiY.setLayoutParams(params_GoiY);
        tvGoiY.setVisibility(View.VISIBLE);
        tvGoiY.startAnimation(ani_idea);

    }

    private void Back() {

        FrameLayout.LayoutParams params_Search = (FrameLayout.LayoutParams) fab_search.getLayoutParams();
        params_Search.bottomMargin -= (int) (fab_search.getWidth() * 2.5) + 70;
        fab_search.setLayoutParams(params_Search);
        fab_search.startAnimation(ani_back_search);


        FrameLayout.LayoutParams params_timkiem = (FrameLayout.LayoutParams) tvTimKiem.getLayoutParams();
        params_timkiem.bottomMargin -= (int) (fab_search.getWidth() * 2.7) + 70;
        tvTimKiem.setLayoutParams(params_timkiem);
        tvTimKiem.startAnimation(ani_back_search);


        FrameLayout.LayoutParams params_Idea = (FrameLayout.LayoutParams) fab_idea.getLayoutParams();
        params_Idea.bottomMargin -= (int) (fab_search.getWidth() * 3.8) + 70;
        fab_idea.setLayoutParams(params_Idea);
        fab_idea.startAnimation(ani_back_idea);

        FrameLayout.LayoutParams params_GoiY = (FrameLayout.LayoutParams) tvGoiY.getLayoutParams();
        params_GoiY.bottomMargin -= (int) (fab_search.getWidth() * 4.0) + 70;
        tvGoiY.setLayoutParams(params_GoiY);
        tvGoiY.startAnimation(ani_back_idea);

        tvTimKiem.setVisibility(View.INVISIBLE);
        tvGoiY.setVisibility(View.INVISIBLE);

    }
}
