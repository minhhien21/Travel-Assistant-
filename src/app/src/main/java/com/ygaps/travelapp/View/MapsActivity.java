package com.ygaps.travelapp.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.ygaps.travelapp.Adapter.StopPointsListAdapter;
import com.ygaps.travelapp.Model.Coordinate;
import com.ygaps.travelapp.Model.CoordinateSet;
import com.ygaps.travelapp.Model.MultiCoordinateRequest;
import com.ygaps.travelapp.Model.MyMarkerItem;
import com.ygaps.travelapp.Model.OwnIconRendered;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.StopPointRequest;
import com.ygaps.travelapp.Model.StopPointInfos;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ygaps.travelapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener
{

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private LocationRequest locationRequest;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private Marker marker;
    private ArrayList<Marker> mMarkers = new ArrayList<Marker>();
    LatLng latLng;
    private EditText mSearchText;
    private ImageButton btnStopPoint;
    private Button btnDeleteThisTour;
    private Button chooseY;
    private Button chooseN;

    private String stopAddress = null;
    private Dialog dialog;

    private ImageButton btnCancel;
    private EditText name;
    private TextView nameTv;
    private Spinner serviceType;
    private EditText address;
    private TextView addressTv;
    private Spinner province;
    private EditText minCost;
    private EditText maxCost;
    private TextView minCostTv;
    private TextView maxCostTv;
    private TextView timeStart;
    private ImageButton selectTimeStart;
    private TextView dateStart;
    private ImageButton selectDateStart;
    private TextView timeEnd;
    private ImageButton selectTimeEnd;
    private TextView dateEnd;
    private ImageButton selectDateEnd;
    private Button addStopPoint;
    private Button editStopPoint;
    private Button createStopPoints;
    private String token;
    private String[] provinceArray = {
            "Hồ Chí Minh", "Hà Nội", "Đà Nẵng", "Bình Dương", "Đồng Nai", "Khánh Hòa", "Hải Phòng", "Long An", "Quảng Nam", "Bà Rịa Vũng Tàu", "Đắk Lắk",
            "Cần Thơ", "Bình Thuận", "Lâm Đồng", "Thừa Thiên Huế", "Kiên Giang", "Bắc Ninh", "Quảng Ninh", "Thanh Hóa", "Nghệ An", "Hải Dương", "Gia Lai",
            "Bình Phước", "Hưng Yên", "Bình Định", "Tiền Giang", "Thái Bình", "Bắc Giang", "Hòa Bình", "An Giang", "Vĩnh Phúc", "Tây Ninh", "Thái Nguyên",
            "Lào Cai", "Nam Định", "Quảng Ngãi", "Bến Tre", "Đắk Nông", "Cà Mau", "Vĩnh Long", "Ninh Bình", "Phú Thọ", "Ninh Thuận", "Phú Yên", "Hà Nam",
            "Hà Tĩnh", "Đồng Tháp", "Sóc Trăng", "Kon Tum", "Quảng Bình", "Quảng Trị", "Trà Vinh", "Hậu Giang", "Sơn La", "Bạc Liêu", "Yên Bái", "Tuyên Quang",
            "Điện Biên", "Lai Châu", "Lạng Sơn", "Hà Giang", "Bắc Kạn", "Cao Bằng"
    };
    private String[] serviceTypeArray = {
            "Restaurant", "Hotel", "Rest Station", "Other"
    };

    private ArrayList<StopPointInfos> listStopPoints = new ArrayList<>();
    private ArrayList<StopPointInfos> listSuggestStopPointInfos = new ArrayList<>();

    private StopPointInfos stopPoint;
    private Polyline currentPolyline = null;
    private List<LatLng> latLngList = new ArrayList<>();

    private ArrayList<Coordinate> coordinates = new ArrayList<>();
    private ArrayList<CoordinateSet> coordList = new ArrayList<>();
    private Coordinate coord;

    private StopPointsListAdapter stopPointsListAdapter;
    private ListView lvStopPoint;

    private ClusterManager<MyMarkerItem> mClusterManager;
    private int indexInList = -1;
    private int index = -1;

    GoogleMap.OnMarkerClickListener eventMarkerClicked = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker1) {
            int index1 = 0;
            if((index = indexMarkerListStopPoint(marker1)) != -1 && (index1 = indexMarkerListSuggest(marker1)) != -1){
                displayDialogEditSuggestStopPoint();
            }
            else if ((index = indexMarkerInSPMarker(marker1)) != -1){
                displayDialogEditStopPoint();
            }
            else {
                mMap.setOnMarkerClickListener(mClusterManager);
                mClusterManager.onMarkerClick(marker1);
                mMap.setOnMarkerClickListener(eventMarkerClicked);
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SharedToken obj = new SharedToken(MapsActivity.this);
        token = obj.getToken( MapsActivity.this);

        mSearchText = findViewById(R.id.maps_search);
        btnStopPoint = findViewById(R.id.historyPoint);
        displayListStopPoint();
        btnDeleteThisTour = findViewById(R.id.delete_this_tour);
        displayDeleteThisTour();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
            init();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private int indexMarkerListStopPoint(Marker marker1){
        for(int i = 0; i < listStopPoints.size(); i++){
            if(marker1.getPosition().latitude == listStopPoints.get(i).getLatitude()
                    && marker1.getPosition().longitude == listStopPoints.get(i).getLongitude()){
                return i;
            }
        }
        return -1;
    }

    private int indexMarkerListSuggest(Marker marker1){
        for(int i = 0; i < listSuggestStopPointInfos.size(); i++){
            if(marker1.getPosition().latitude == listSuggestStopPointInfos.get(i).getLatitude()
            && marker1.getPosition().longitude == listSuggestStopPointInfos.get(i).getLongitude()){
                return i;
            }
        }
        return -1;
    }

    private int indexMarkerInSPMarker(Marker marker1){
        for(int i = 0; i < mMarkers.size(); i++){
            if(mMarkers.get(i).getId().equals(marker1.getId())){
                return i;
            }
        }
        return -1;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled( true );
            View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            rlp.setMargins(0, 1500, 180, 0);
        }
        getSuggestPoints();
        setUpClusterer();
        mClusterManager.setRenderer(new OwnIconRendered(getApplicationContext(),mMap,mClusterManager));
        mClusterManager.setAnimation(false);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng1) {
                marker = mMap.addMarker(new MarkerOptions().draggable(false).position(latLng1));
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                marker.setTitle("Stop Point");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1,20F));
                stopAddress = getMapsAddress(latLng1);
                latLng = latLng1;
                displayDialogAddStopPoint();
            }
        });
        mMap.setOnMarkerClickListener(eventMarkerClicked);
    }
    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyMarkerItem>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }
    private void addItems() {
        // Add suggest marker to map
        for(int i = 0; i< listSuggestStopPointInfos.size(); i++){
            StopPointInfos sItem = listSuggestStopPointInfos.get(i);
            LatLng lng = new LatLng(sItem.getLatitude(),sItem.getLongitude());
            MyMarkerItem item = new MyMarkerItem(sItem.getLatitude(),sItem.getLongitude(),i);
            mClusterManager.addItem(item);
        }
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyMarkerItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyMarkerItem> cluster) {
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyMarkerItem>() {
            @Override
            public boolean onClusterItemClick(MyMarkerItem myMarkerItem) {
                indexInList = myMarkerItem.getInfoPos();
                marker = mMap.addMarker(new MarkerOptions().draggable(false).position(new LatLng(listSuggestStopPointInfos.get(indexInList).getLatitude(),
                        listSuggestStopPointInfos.get(indexInList).getLongitude())));
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                displayDialogSuggestStopPoint();
                return false;
            }
        });
    }
    private void getSuggestPoints() {
        // add coordinate into arraylist:
        coord = new Coordinate(23.457796, 101.802655);
        coordinates.add(coord);
        coord = new Coordinate(8.553419,109.097577);
        coordinates.add(coord);
        coordList.add(new CoordinateSet(coordinates));
        MultiCoordinateRequest request = new MultiCoordinateRequest(coordList);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .betweenCoordinateSuggest(token,request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    try {
                        String body = response.body().string();
                        JSONObject bodyData = new JSONObject(body);
                        JSONArray bodyArray = bodyData.getJSONArray("stopPoints");
                        if(bodyArray.length()>0){
                            for(int i=0;i<bodyArray.length();i++)
                            {
                                JSONObject jb = bodyArray.getJSONObject(i);
                                listSuggestStopPointInfos.add(new StopPointInfos(null,jb.getInt("id"),jb.getString("name"),
                                        jb.getString("address"), jb.getInt("provinceId"), Double.parseDouble(jb.getString("lat")),
                                        Double.parseDouble(jb.getString("long")),0,0,jb.getInt("serviceTypeId"),
                                        jb.getString("minCost"),jb.getString("maxCost")));
                            }
                            setUpClusterer();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayListStopPoint(){
        btnStopPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(MapsActivity.this);
                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.95);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_list_stop_point);
                dialog.getWindow().setLayout(width,height);
                btnCancel = dialog.findViewById(R.id.cancel_create_stop_point);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                createStopPoints = dialog.findViewById(R.id.Create);
                createStopPoints.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Build body of request:
                        StopPointRequest body = new StopPointRequest(String.valueOf(CreateTour.createTourId),listStopPoints);
                        // Attempt to send request:
                        Call<ResponseBody> call = RetrofitClient
                                .getInstance()
                                .getUserService()
                                .addStopPoints(token,body);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                int code = response.code();
                                if(code == 200){
                                    Toast.makeText(getApplicationContext(), "Create successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MapsActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    MapsActivity.this.finish();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),"fail", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                lvStopPoint = dialog.findViewById(R.id.lvHistoryPoint);
                stopPointsListAdapter = new StopPointsListAdapter(getApplicationContext(),listStopPoints);
                lvStopPoint.setAdapter(stopPointsListAdapter);
                dialog.show();

                RemoveEditStopPoint();
            }
        });
    }

    private void displayDeleteThisTour(){
        btnDeleteThisTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(MapsActivity.this);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_delete_tour);
                chooseY = dialog.findViewById(R.id.delete_tour_yes);
                chooseN = dialog.findViewById(R.id.delete_tour_no);
                chooseN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                chooseY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<ResponseBody> call = RetrofitClient
                                .getInstance()
                                .getUserService()
                                .deleteTour(token,String.valueOf(CreateTour.createTourId),-1);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.code() == 200){
                                    Toast.makeText(getApplicationContext(), "Delete successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MapsActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    MapsActivity.this.finish();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Some thing wrong!!!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }

    private void displayDialogAddStopPoint() {
        dialog = new Dialog(MapsActivity.this);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.95);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_stop_point_add);
        dialog.getWindow().setLayout(width,height);

        btnCancel = dialog.findViewById(R.id.cancel_add_stop_point);
        name = dialog.findViewById(R.id.stop_point_name);
        serviceType = dialog.findViewById(R.id.stop_point_service_type);
        address = dialog.findViewById(R.id.stop_point_address);
        province = dialog.findViewById(R.id.stop_point_province);
        minCost = dialog.findViewById(R.id.stop_point_min_cost);
        maxCost = dialog.findViewById(R.id.stop_point_max_cost);

        timeStart = dialog.findViewById(R.id.stop_point_time_start);
        selectTimeStart = dialog.findViewById(R.id.stop_point_button_select_time_start);
        dateStart = dialog.findViewById(R.id.stop_point_select_date_start);
        selectDateStart = dialog.findViewById(R.id.stop_point_button_select_date_start);

        timeEnd = dialog.findViewById(R.id.stop_point_time_end);
        selectTimeEnd = dialog.findViewById(R.id.stop_point_button_select_time_end);
        dateEnd = dialog.findViewById(R.id.stop_point_select_date_end);
        selectDateEnd = dialog.findViewById(R.id.stop_point_button_select_date_end);
        addStopPoint = dialog.findViewById(R.id.stop_point_add);

        if(stopAddress!=null) {
            address.setText(stopAddress);
        }
        stopAddress = null;

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marker.remove();
                dialog.dismiss();
            }
        });
        dialog.show();
        ClickSelectTime();
        ClickSelectDate();
        ClickAddStopPoint();

    }

    private void displayDialogEditStopPoint() {
        dialog = new Dialog(MapsActivity.this);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.95);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_stop_point_edit);
        dialog.getWindow().setLayout(width,height);

        btnCancel = dialog.findViewById(R.id.cancel_add_stop_point_edit);
        name = dialog.findViewById(R.id.stop_point_name_edit);
        serviceType = dialog.findViewById(R.id.stop_point_service_type_edit);
        address = dialog.findViewById(R.id.stop_point_address_edit);
        province = dialog.findViewById(R.id.stop_point_province_edit);
        minCost = dialog.findViewById(R.id.stop_point_min_cost_edit);
        maxCost = dialog.findViewById(R.id.stop_point_max_cost_edit);

        timeStart = dialog.findViewById(R.id.stop_point_time_start_edit);
        selectTimeStart = dialog.findViewById(R.id.stop_point_button_select_time_start_edit);
        dateStart = dialog.findViewById(R.id.stop_point_select_date_start_edit);
        selectDateStart = dialog.findViewById(R.id.stop_point_button_select_date_start_edit);

        timeEnd = dialog.findViewById(R.id.stop_point_time_end_edit);
        selectTimeEnd = dialog.findViewById(R.id.stop_point_button_select_time_end_edit);
        dateEnd = dialog.findViewById(R.id.stop_point_select_date_end_edit);
        selectDateEnd = dialog.findViewById(R.id.stop_point_button_select_date_end_edit);
        editStopPoint = dialog.findViewById(R.id.stop_point_add_edit);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        if(index != -1){
            address.setText(listStopPoints.get(index).getAddress());
            name.setText(listStopPoints.get(index).getName());
            minCost.setText(listStopPoints.get(index).getMinCost());
            maxCost.setText(listStopPoints.get(index).getMaxCost());
            String sArrivalAt = simpleDateFormat.format(new Date(listStopPoints.get(index).getArrivalAt()));
            String[] separated = sArrivalAt.split(" ");
            dateStart.setText(separated[0]);
            timeStart.setText(separated[1]);
            String sLeaveAt = simpleDateFormat.format(new Date(listStopPoints.get(index).getLeaveAt()));
            separated = sLeaveAt.split(" ");
            dateEnd.setText(separated[0]);
            timeEnd.setText(separated[1]);
        }
        stopAddress = null;

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        ClickSelectTime();
        ClickSelectDate();
        ClickEditStopPoint();
    }

    private void displayDialogSuggestStopPoint() {
        dialog = new Dialog(MapsActivity.this);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.95);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_stop_point_suggest);
        dialog.getWindow().setLayout(width,height);

        btnCancel = dialog.findViewById(R.id.cancel_add_stop_point_suggest);
        nameTv = dialog.findViewById(R.id.stop_point_name_suggest);
        serviceType = dialog.findViewById(R.id.stop_point_service_type_suggest);
        addressTv = dialog.findViewById(R.id.stop_point_address_suggest);
        province = dialog.findViewById(R.id.stop_point_province_suggest);
        minCostTv = dialog.findViewById(R.id.stop_point_min_cost_suggest);
        maxCostTv = dialog.findViewById(R.id.stop_point_max_cost_suggest);

        timeStart = dialog.findViewById(R.id.stop_point_time_start_suggest);
        selectTimeStart = dialog.findViewById(R.id.stop_point_button_select_time_start_suggest);
        dateStart = dialog.findViewById(R.id.stop_point_select_date_start_suggest);
        selectDateStart = dialog.findViewById(R.id.stop_point_button_select_date_start_suggest);

        timeEnd = dialog.findViewById(R.id.stop_point_time_end_suggest);
        selectTimeEnd = dialog.findViewById(R.id.stop_point_button_select_time_end_suggest);
        dateEnd = dialog.findViewById(R.id.stop_point_select_date_end_suggest);
        selectDateEnd = dialog.findViewById(R.id.stop_point_button_select_date_end_suggest);
        addStopPoint = dialog.findViewById(R.id.stop_point_add_suggest);

        if(indexInList != -1) {
            nameTv.setText(listSuggestStopPointInfos.get(indexInList).getName());
            addressTv.setText(listSuggestStopPointInfos.get(indexInList).getAddress());
        }
        stopAddress = null;
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                marker.remove();
                dialog.dismiss();
            }
        });
        dialog.show();
        ClickSelectTime();
        ClickSelectDate();
        ClickAddStopPointSuggest();

    }

    private void displayDialogEditSuggestStopPoint() {
        dialog = new Dialog(MapsActivity.this);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.95);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_stop_point_suggest_edit);
        dialog.getWindow().setLayout(width,height);

        btnCancel = dialog.findViewById(R.id.cancel_add_stop_point_suggest_edit);
        nameTv = dialog.findViewById(R.id.stop_point_name_suggest_edit);
        serviceType = dialog.findViewById(R.id.stop_point_service_type_suggest_edit);
        addressTv = dialog.findViewById(R.id.stop_point_address_suggest_edit);
        province = dialog.findViewById(R.id.stop_point_province_suggest_edit);
        minCostTv = dialog.findViewById(R.id.stop_point_min_cost_suggest_edit);
        maxCostTv = dialog.findViewById(R.id.stop_point_max_cost_suggest_edit);

        timeStart = dialog.findViewById(R.id.stop_point_time_start_suggest_edit);
        selectTimeStart = dialog.findViewById(R.id.stop_point_button_select_time_start_suggest_edit);
        dateStart = dialog.findViewById(R.id.stop_point_select_date_start_suggest_edit);
        selectDateStart = dialog.findViewById(R.id.stop_point_button_select_date_start_suggest_edit);

        timeEnd = dialog.findViewById(R.id.stop_point_time_end_suggest_edit);
        selectTimeEnd = dialog.findViewById(R.id.stop_point_button_select_time_end_suggest_edit);
        dateEnd = dialog.findViewById(R.id.stop_point_select_date_end_suggest_edit);
        selectDateEnd = dialog.findViewById(R.id.stop_point_button_select_date_end_suggest_edit);
        editStopPoint = dialog.findViewById(R.id.stop_point_add_suggest_edit);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        if(index != -1){
            addressTv.setText(listStopPoints.get(index).getAddress());
            nameTv.setText(listStopPoints.get(index).getName());
            minCostTv.setText(listStopPoints.get(index).getMinCost());
            maxCostTv.setText(listStopPoints.get(index).getMaxCost());
            String sArrivalAt = simpleDateFormat.format(new Date(listStopPoints.get(index).getArrivalAt()));
            String[] separated = sArrivalAt.split(" ");
            dateStart.setText(separated[0]);
            timeStart.setText(separated[1]);
            String sLeaveAt = simpleDateFormat.format(new Date(listStopPoints.get(index).getLeaveAt()));
            separated = sLeaveAt.split(" ");
            dateEnd.setText(separated[0]);
            timeEnd.setText(separated[1]);
        }
        stopAddress = null;
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        ClickSelectTime();
        ClickSelectDate();
        ClickEditStopPointSuggest();

    }

    private void ClickSelectTime(){
        selectTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int mintute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(MapsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                        String sHour = String.valueOf(Hour);
                        if(Hour <=9){
                            sHour = "0" + Hour;
                        }
                        String sMinute = String.valueOf(Minute);
                        if(Minute <=9){
                            sMinute = "0" + Minute;
                        }
                        timeStart.setText(sHour + ":" + sMinute);
                    }
                },hour, mintute, true);
                timePickerDialog.show();
            }
        });

        selectTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int mintute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(MapsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int Hour, int Minute) {
                        String sHour = String.valueOf(Hour);
                        if(Hour <=9){
                            sHour = "0" + Hour;
                        }
                        String sMinute = String.valueOf(Minute);
                        if(Minute <=9){
                            sMinute = "0" + Minute;
                        }
                        timeEnd.setText(sHour + ":" + sMinute);
                    }
                },hour, mintute, true);
                timePickerDialog.show();
            }
        });
    }

    private void ClickSelectDate(){
        selectDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year,month,dayOfMonth;
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog datePickerDialog = new DatePickerDialog(MapsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String sMonth = String.valueOf(month);
                                if(month <=9){
                                    sMonth = "0" + month;
                                }
                                String sDay = String.valueOf(day);
                                if(day <=9){
                                    sDay = "0" + day;
                                }
                                dateStart.setText(sDay + "/" + sMonth + "/" + year);
                            }
                        },year,month,dayOfMonth);
                datePickerDialog.show();
            }
        });
        selectDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year,month,dayOfMonth;
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog datePickerDialog = new DatePickerDialog(MapsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String sMonth = String.valueOf(month);
                                if(month <=9){
                                    sMonth = "0" + month;
                                }
                                String sDay = String.valueOf(day);
                                if(day <=9){
                                    sDay = "0" + day;
                                }
                                dateEnd.setText(sDay + "/" + sMonth + "/" + year);
                            }
                        },year,month,dayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    private void ClickAddStopPoint(){
        addStopPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName = name.getText().toString().trim();
                String sServiceType = serviceType.getSelectedItem().toString().trim();
                String sAddress = address.getText().toString().trim();
                String sProvince = province.getSelectedItem().toString().trim();
                String sMinCost = minCost.getText().toString().trim();
                String sMaxCost = maxCost.getText().toString().trim();
                String sTimeStart = timeStart.getText().toString().trim();
                String sDateStart = dateStart.getText().toString().trim();
                String sTimeEnd = timeEnd.getText().toString().trim();
                String sDateEnd = dateEnd.getText().toString().trim();

                if(sName.isEmpty()){
                    name.setError("Tour name is empty");
                    name.requestFocus();
                    return;
                }
                if(sAddress.isEmpty()){
                    address.setError("Address is empty");
                    address.requestFocus();
                    return;
                }
                if(sMinCost.isEmpty()){
                    minCost.setError("Min cost is empty");
                    minCost.requestFocus();
                    return;
                }
                if(sMaxCost.isEmpty()){
                    maxCost.setError("Max cost is empty");
                    maxCost.requestFocus();
                    return;
                }
                if(sTimeStart.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Time start is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sDateStart.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Start date is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sTimeEnd.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Time end is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sDateEnd.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Date end is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                long arrivalAt = 0;
                long leaveAt = 0;
                long convertMilisecond = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                try {
                    Date dateS = sdf.parse(sDateStart + " " + sTimeStart);
                    convertMilisecond = dateS.getTime();
                    arrivalAt += convertMilisecond;
                }catch (ParseException e){
                    e.printStackTrace();
                }
                try {
                    Date dateE = sdf.parse(sDateEnd + " " + sTimeEnd);
                    convertMilisecond = dateE.getTime();
                    leaveAt += convertMilisecond;
                }catch (ParseException e){
                    e.printStackTrace();
                }

                int provinceId = 0;
                int serviceTypeId = 0;
                for(int i=0;i< provinceArray.length;i++) {
                    if(sProvince.equals(provinceArray[i])){
                        provinceId = i+1;
                    }
                }
                for(int i=0;i< serviceTypeArray.length;i++) {
                    if(sServiceType.equals(serviceTypeArray[i])){
                        serviceTypeId = i+1;
                    }
                }


                // add stop point in listStopPoints:
                stopPoint = new StopPointInfos(null,null,sName,sAddress,provinceId, latLng.latitude, latLng.longitude,arrivalAt,leaveAt,serviceTypeId,sMinCost,sMaxCost);
                listStopPoints.add( stopPoint );

                // Display marker:
                mMarkers.add(marker);
                latLngList.add(latLng);
                if(currentPolyline != null){
                    currentPolyline.remove();
                }
                if(listStopPoints.size() >= 2) {
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(latLngList).clickable(true);
                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
                Toast.makeText(getApplicationContext(), "Add successful", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void ClickEditStopPoint(){
        editStopPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName = name.getText().toString().trim();
                String sServiceType = serviceType.getSelectedItem().toString().trim();
                String sAddress = address.getText().toString().trim();
                String sProvince = province.getSelectedItem().toString().trim();
                String sMinCost = minCost.getText().toString().trim();
                String sMaxCost = maxCost.getText().toString().trim();
                String sTimeStart = timeStart.getText().toString().trim();
                String sDateStart = dateStart.getText().toString().trim();
                String sTimeEnd = timeEnd.getText().toString().trim();
                String sDateEnd = dateEnd.getText().toString().trim();

                if(sName.isEmpty()){
                    name.setError("Tour name is empty");
                    name.requestFocus();
                    return;
                }
                if(sAddress.isEmpty()){
                    address.setError("Address is empty");
                    address.requestFocus();
                    return;
                }
                if(sMinCost.isEmpty()){
                    minCost.setError("Min cost is empty");
                    minCost.requestFocus();
                    return;
                }
                if(sMaxCost.isEmpty()){
                    maxCost.setError("Max cost is empty");
                    maxCost.requestFocus();
                    return;
                }
                if(sTimeStart.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Time start is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sDateStart.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Start date is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sTimeEnd.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Time end is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sDateEnd.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Date end is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                long arrivalAt = 0;
                long leaveAt = 0;
                long convertMilisecond = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                try {
                    Date dateS = sdf.parse(sDateStart + " " + sTimeStart);
                    convertMilisecond = dateS.getTime();
                    arrivalAt += convertMilisecond;
                }catch (ParseException e){
                    e.printStackTrace();
                }
                try {
                    Date dateE = sdf.parse(sDateEnd + " " + sTimeEnd);
                    convertMilisecond = dateE.getTime();
                    leaveAt += convertMilisecond;
                }catch (ParseException e){
                    e.printStackTrace();
                }

                int provinceId = 0;
                int serviceTypeId = 0;
                for(int i=0;i< provinceArray.length;i++) {
                    if(sProvince.equals(provinceArray[i])){
                        provinceId = i+1;
                    }
                }
                for(int i=0;i< serviceTypeArray.length;i++) {
                    if(sServiceType.equals(serviceTypeArray[i])){
                        serviceTypeId = i+1;
                    }
                }


                // edit stop point in listStopPoints:
                if(index != -1){
                    stopPoint = new StopPointInfos(null,listStopPoints.get(index).getServiceId(),sName,sAddress,provinceId, latLng.latitude, latLng.longitude,arrivalAt,leaveAt,serviceTypeId,sMinCost,sMaxCost);
                    listStopPoints.remove(index);
                    listStopPoints.add(index,stopPoint);
                    Toast.makeText(getApplicationContext(), "Edit successful", Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();
            }
        });
    }

    private void ClickAddStopPointSuggest(){
        addStopPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName = nameTv.getText().toString().trim();
                String sAddress = addressTv.getText().toString().trim();
                String sMinCost = minCostTv.getText().toString().trim();
                String sMaxCost = maxCostTv.getText().toString().trim();
                String sTimeStart = timeStart.getText().toString().trim();
                String sDateStart = dateStart.getText().toString().trim();
                String sTimeEnd = timeEnd.getText().toString().trim();
                String sDateEnd = dateEnd.getText().toString().trim();
                if(sTimeStart.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Time start is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sDateStart.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Start date is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sTimeEnd.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Time end is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sDateEnd.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Date end is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                long arrivalAt = 0;
                long leaveAt = 0;
                long convertMilisecond = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                try {
                    Date dateS = sdf.parse(sDateStart + " " + sTimeStart);
                    convertMilisecond = dateS.getTime();
                    arrivalAt += convertMilisecond;
                }catch (ParseException e){
                    e.printStackTrace();
                }
                try {
                    Date dateE = sdf.parse(sDateEnd + " " + sTimeEnd);
                    convertMilisecond = dateE.getTime();
                    leaveAt += convertMilisecond;
                }catch (ParseException e){
                    e.printStackTrace();
                }

                int provinceId = 0;
                int serviceTypeId = 0;

                Number serviceId = 0;
                Double mLat = 0.0;
                Double mLong = 0.0;
                if(indexInList != -1) {
                    serviceId = listSuggestStopPointInfos.get(indexInList).getServiceId();
                    mLat = listSuggestStopPointInfos.get(indexInList).getLatitude();
                    mLong = listSuggestStopPointInfos.get(indexInList).getLongitude();
                    provinceId = listSuggestStopPointInfos.get(indexInList).getProvinceId();
                    serviceTypeId = listSuggestStopPointInfos.get(indexInList).getServiceTypeId();
                }
                // add stop point in listStopPoints:
                stopPoint = new StopPointInfos(null,serviceId,sName,sAddress,provinceId, mLat,mLong,arrivalAt,leaveAt,serviceTypeId,sMinCost,sMaxCost);
                listStopPoints.add( stopPoint );
                //indexInList

                // Display marker:
                if(indexInList != -1) {
                    latLngList.add(new LatLng(listSuggestStopPointInfos.get(indexInList).getLatitude(),listSuggestStopPointInfos.get(indexInList).getLongitude()));
                }
                mMarkers.add(marker);
                if(listStopPoints.size() >= 2) {
                    if(currentPolyline != null){
                        currentPolyline.remove();
                    }
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .addAll(latLngList).clickable(true);
                    currentPolyline = mMap.addPolyline(polylineOptions);
                }
                Toast.makeText(getApplicationContext(), "Add successful", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    private void ClickEditStopPointSuggest(){
        editStopPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName = nameTv.getText().toString().trim();
                String sAddress = addressTv.getText().toString().trim();
                String sMinCost = minCostTv.getText().toString().trim();
                String sMaxCost = maxCostTv.getText().toString().trim();
                String sTimeStart = timeStart.getText().toString().trim();
                String sDateStart = dateStart.getText().toString().trim();
                String sTimeEnd = timeEnd.getText().toString().trim();
                String sDateEnd = dateEnd.getText().toString().trim();
                if(sTimeStart.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Time start is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sDateStart.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Start date is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sTimeEnd.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Time end is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sDateEnd.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(),"Date end is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                long arrivalAt = 0;
                long leaveAt = 0;
                long convertMilisecond = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                try {
                    Date dateS = sdf.parse(sDateStart + " " + sTimeStart);
                    convertMilisecond = dateS.getTime();
                    arrivalAt += convertMilisecond;
                }catch (ParseException e){
                    e.printStackTrace();
                }
                try {
                    Date dateE = sdf.parse(sDateEnd + " " + sTimeEnd);
                    convertMilisecond = dateE.getTime();
                    leaveAt += convertMilisecond;
                }catch (ParseException e){
                    e.printStackTrace();
                }

                // edit stop point in listStopPoints:
                if(index != -1){
                    stopPoint = new StopPointInfos(null,listStopPoints.get(index).getServiceId(),sName,sAddress,listStopPoints.get(index).getProvinceId()
                            , listStopPoints.get(index).getLatitude(), listStopPoints.get(index).getLongitude(),arrivalAt,leaveAt,
                            listStopPoints.get(index).getServiceTypeId(),sMinCost,sMaxCost);
                    listStopPoints.remove(index);
                    listStopPoints.add(index,stopPoint);
                    Toast.makeText(getApplicationContext(), "Edit successful", Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();
            }
        });
    }

    private void RemoveEditStopPoint(){
        lvStopPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int flag = 0;
                for(int i = 0; i < listSuggestStopPointInfos.size(); i++){
                    if(listStopPoints.get(position).getLatitude() == listSuggestStopPointInfos.get(i).getLatitude() &&
                    listStopPoints.get(position).getLongitude() == listSuggestStopPointInfos.get(i).getLongitude()){
                        flag = 1;
                        break;
                    }
                }
                index = position;
                dialog.dismiss();
                LatLng ll = new LatLng(listStopPoints.get(index).getLatitude(),listStopPoints.get(index).getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, 20F);
                mMap.animateCamera(cameraUpdate);
                if(flag == 0) {
                    displayDialogEditStopPoint();
                }else{
                    displayDialogEditSuggestStopPoint();
                }
            }
        });
        lvStopPoint.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setMessage("Bạn có muốn xóa điểm dừng này không?");
                builder.setTitle("Thông báo");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listStopPoints.remove(position);
                        stopPointsListAdapter.notifyDataSetChanged();
                        mMarkers.get(position).remove();
                        mMarkers.remove(position);
                        latLngList.remove(position);
                        if(currentPolyline != null){
                            currentPolyline.remove();
                        }
                        if(listStopPoints.size() >= 2) {
                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .addAll(latLngList).clickable(true);
                            currentPolyline = mMap.addPolyline(polylineOptions);
                        }
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });
    }

    private String getMapsAddress(LatLng latlng)
    {
        String address =null;

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        return address;
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder( this )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi( LocationServices.API )
                .build();
        googleApiClient.connect();
    }

    private void init(){
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    geoLocate();
                }
                return false;
            }
        });

    }

    public void geoLocate(){
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString,5);
        }catch(IOException e){
            Log.e( "TAG","geoLocate: IOException "+e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            latLng = new LatLng(address.getLatitude(),address.getLongitude());
//            marker = mMap.addMarker(new MarkerOptions().draggable(false).position(latLng));
//            marker.setTitle("Marker");
//            marker.setIcon(BitmapDescriptorFactory.defaultMarker());
//            currentUserLocationMarker = marker;
//            mMarkers.add(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,20F));
        }
    }

    public boolean checkUserLocationPermission(){
        if (ContextCompat.checkSelfPermission( this,Manifest.permission.ACCESS_FINE_LOCATION )!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale( this,Manifest.permission.ACCESS_FINE_LOCATION )){
                ActivityCompat.requestPermissions( this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            else {
                ActivityCompat.requestPermissions( this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case Request_User_Location_Code:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )==PackageManager.PERMISSION_GRANTED)
                    {
                        if (googleApiClient==null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled( true );
                    }
                }
                else
                {
                    Toast.makeText( this,"Permission Denied",Toast.LENGTH_SHORT ).show();
                }
                return;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval( 1100 );
        locationRequest.setFastestInterval( 1100 );
        locationRequest.setPriority( LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY );
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if (currentUserLocationMarker!=null)
        {
            currentUserLocationMarker.remove();
        }

        //Toast.makeText( this,location.getLatitude()+"  "+location.getLongitude(),Toast.LENGTH_SHORT ).show();
        latLng = new LatLng( location.getLatitude(),location.getLongitude() );
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position( latLng );
//        markerOptions.title( "Vị trí hiện tại" );
//        markerOptions.icon( BitmapDescriptorFactory.defaultMarker() );
//
//        currentUserLocationMarker = mMap.addMarker( markerOptions );
        // Add a marker in current location and move the camera
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( latLng,20F) );

        if (googleApiClient!=null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates( googleApiClient,this );
        }
    }


}
