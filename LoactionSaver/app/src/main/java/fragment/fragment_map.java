package fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.TextView;
import android.widget.Toast;


import com.example.thekiet.loactionsaver.AddDanhBa;
import com.example.thekiet.loactionsaver.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
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

    static ViTriThem  vitrithem = new ViTriThem();

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


        view_search = (View) rootView.findViewById(R.id.layout_search);
        view_idea = (View) rootView.findViewById(R.id.layout_idea);


        /*animation*/
        ani_search = AnimationUtils.loadAnimation(getContext(), R.anim.move_search);
        ani_idea = AnimationUtils.loadAnimation(getContext(), R.anim.move_idea);

        ani_back_search = AnimationUtils.loadAnimation(getContext(), R.anim.back_search);
        ani_back_idea = AnimationUtils.loadAnimation(getContext(), R.anim.back_idea);

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
                    view_search.setVisibility(View.GONE);
                    view_idea.setVisibility(View.GONE);
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
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        listAddress = geocoder.getFromLocationName(ViTriSearch, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
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
                }
                else{
                    Toast.makeText(getContext(), "Nhập vào địa điểm cần tìm!", Toast.LENGTH_SHORT);
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
                extras.putSerializable("Address",(Serializable) vitrithem);

               myIntent.putExtras(extras);
                try{
                startActivityForResult(myIntent, 0);}
                catch (Exception e)
                {
                   Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
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
