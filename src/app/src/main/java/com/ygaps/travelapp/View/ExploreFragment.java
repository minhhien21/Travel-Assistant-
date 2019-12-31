package com.ygaps.travelapp.View;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.ygaps.travelapp.Adapter.SearchDestinationAdapter;
import com.ygaps.travelapp.Adapter.TourAdapter;
import com.ygaps.travelapp.Model.Coordinate;
import com.ygaps.travelapp.Model.CoordinateSet;
import com.ygaps.travelapp.Model.MultiCoordinateRequest;
import com.ygaps.travelapp.Model.MyMarkerItem;
import com.ygaps.travelapp.Model.OwnIconRendered;
import com.ygaps.travelapp.Model.SearchDestination;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.StopPointInfos;
import com.ygaps.travelapp.Model.TourInformations;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ExploreFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{
    private String token;
    private EditText searchText;
    private ImageButton dlgListSearch;
    private Dialog dialog;
    private ImageButton btnCancel;

    private int count = 0;
    private TextView countResult;
    private ListView listView;
    private List<SearchDestination> list = new ArrayList<SearchDestination>();
    private SearchDestinationAdapter adapter;

    private String[] provinceArray = {
            "Hồ Chí Minh", "Hà Nội", "Đà Nẵng", "Bình Dương", "Đồng Nai", "Khánh Hòa", "Hải Phòng", "Long An", "Quảng Nam", "Bà Rịa Vũng Tàu", "Đắk Lắk",
            "Cần Thơ", "Bình Thuận", "Lâm Đồng", "Thừa Thiên Huế", "Kiên Giang", "Bắc Ninh", "Quảng Ninh", "Thanh Hóa", "Nghệ An", "Hải Dương", "Gia Lai",
            "Bình Phước", "Hưng Yên", "Bình Định", "Tiền Giang", "Thái Bình", "Bắc Giang", "Hòa Bình", "An Giang", "Vĩnh Phúc", "Tây Ninh", "Thái Nguyên",
            "Lào Cai", "Nam Định", "Quảng Ngãi", "Bến Tre", "Đắk Nông", "Cà Mau", "Vĩnh Long", "Ninh Bình", "Phú Thọ", "Ninh Thuận", "Phú Yên", "Hà Nam",
            "Hà Tĩnh", "Đồng Tháp", "Sóc Trăng", "Kon Tum", "Quảng Bình", "Quảng Trị", "Trà Vinh", "Hậu Giang", "Sơn La", "Bạc Liêu", "Yên Bái", "Tuyên Quang",
            "Điện Biên", "Lai Châu", "Lạng Sơn", "Hà Giang", "Bắc Kạn", "Cao Bằng"
    };

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private LocationRequest locationRequest;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    LatLng latLng;
    private ClusterManager<MyMarkerItem> mClusterManager;
    private ArrayList<StopPointInfos> listSuggestStopPointInfos = new ArrayList<>();
    private int indexInList = -1;
    private ArrayList<Coordinate> coordinates = new ArrayList<>();
    private ArrayList<CoordinateSet> coordList = new ArrayList<>();
    private Coordinate coord;

    GoogleMap.OnMarkerClickListener eventMarkerClicked = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker1) {
            mMap.setOnMarkerClickListener(mClusterManager);
            mClusterManager.onMarkerClick(marker1);
            mMap.setOnMarkerClickListener(eventMarkerClicked);
            return false;
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        SharedToken obj = new SharedToken(ExploreFragment.super.getActivity());
        token = obj.getToken(ExploreFragment.super.getActivity());
        searchText = view.findViewById(R.id.explore_search);
        dlgListSearch = view.findViewById(R.id.explore_list_search);
        dlgListSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ExploreFragment.super.getContext());
                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.95);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_result_destination);
                dialog.getWindow().setLayout(width,height);
                btnCancel = dialog.findViewById(R.id.explore_cancel_list_result_search);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                countResult= dialog.findViewById(R.id.explore_count_result);
                countResult.setText(count+ " results");
                listView = dialog.findViewById(R.id.explore_listView);
                adapter = new SearchDestinationAdapter(getContext(),list);
                listView.setAdapter(adapter);
                dialog.show();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        dialog.dismiss();
                        goToLocationZoom(Double.parseDouble(list.get(position).getmLat()),Double.parseDouble(list.get(position).getmLong()),20F);
                        for(int i=0;i<listSuggestStopPointInfos.size();i++){
                            if(listSuggestStopPointInfos.get(i).getLatitude() == Double.parseDouble(list.get(position).getmLat())
                                && listSuggestStopPointInfos.get(i).getLongitude() == Double.parseDouble(list.get(position).getmLong())){
                                indexInList = i;
                                break;
                            }
                        }
                        displayDialogReviewStopPoint();
                    }
                });
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
            init();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container).getChildFragmentManager()
                .findFragmentById(R.id.map_review);
        mapFragment.getMapAsync(this);
        return view;
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getSuggestPoints();
        setUpClusterer();
        goToLocationZoom(10.763182,106.682494,20F);
        mClusterManager.setRenderer(new OwnIconRendered(getApplicationContext(),mMap,mClusterManager));
        mClusterManager.setAnimation(false);
        mMap.setOnMarkerClickListener(eventMarkerClicked);
    }

    private void setUpClusterer() {

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyMarkerItem>(ExploreFragment.super.getContext(), mMap);

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
                displayDialogReviewStopPoint();
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

    private void displayDialogReviewStopPoint(){
        StopPointDialog stopPointDialog = new StopPointDialog();
        Bundle bundle = new Bundle();
        bundle.putString("StopPointInfo",new Gson().toJsonTree(listSuggestStopPointInfos.get(indexInList)).getAsJsonObject().toString());
        stopPointDialog.setArguments(bundle);
        stopPointDialog.setStyle(DialogFragment.STYLE_NO_TITLE,0);
        stopPointDialog.show(getChildFragmentManager(),"Stop point dialog");
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder( ExploreFragment.super.getContext())
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi( LocationServices.API )
                .build();
        googleApiClient.connect();
    }

    private void init(){
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    resultSearch();
                    Toast.makeText( ExploreFragment.super.getContext(),"Success",Toast.LENGTH_SHORT ).show();
                }
                return false;
            }
        });

    }

    private void resultSearch(){
        String sSearchText = searchText.getText().toString();
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .searchDestination(token,sSearchText,1,10);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    list.clear();
                    count = 0;
                    try {
                        String body = response.body().string();
                        JSONObject searchData = new JSONObject(body);
                        JSONArray searchArray = searchData.getJSONArray("stopPoints");
                        count = searchArray.length();
                        if(searchArray.length()>0){
                            list.clear();
                            for(int j=0;j<searchArray.length();j++)
                            {
                                JSONObject  jb = searchArray.getJSONObject(j);
                                list.add(new SearchDestination(jb.getInt("id"),jb.getString("name"),jb.getString("address"),
                                        jb.getInt("provinceId"),
                                        jb.getString("lat"),jb.getString("long"),jb.getString("minCost"),
                                        jb.getString("maxCost"),jb.getString("avatar"),jb.getInt("serviceTypeId")));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText( getActivity(), "Server error on getting destination", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.animateCamera(cameraUpdate);
    }

    public boolean checkUserLocationPermission(){
        if (ContextCompat.checkSelfPermission( ExploreFragment.super.getContext(),Manifest.permission.ACCESS_FINE_LOCATION )!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(  ExploreFragment.super.getActivity(),Manifest.permission.ACCESS_FINE_LOCATION )){
                ActivityCompat.requestPermissions(  ExploreFragment.super.getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            else {
                ActivityCompat.requestPermissions(  ExploreFragment.super.getActivity(),new String[] {Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
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
                    if (ContextCompat.checkSelfPermission( ExploreFragment.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION )==PackageManager.PERMISSION_GRANTED)
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
                    Toast.makeText( ExploreFragment.super.getContext(),"Permission Denied",Toast.LENGTH_SHORT ).show();
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
        if (ContextCompat.checkSelfPermission( ExploreFragment.super.getContext(), Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED)
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
