package com.ygaps.travelapp.View;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// classes needed to initialize map
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

// classes needed to add the location component
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;

// classes needed to add a marker

import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.geometry.LatLng;

// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mapbox.turf.TurfMeasurement;
import com.ygaps.travelapp.Adapter.MessageAdapter;
import com.ygaps.travelapp.Model.MessageSend;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FollowTour extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, PermissionsListener {
    private String token;
    // variables for adding location layer
    private MapView mapView;
    private MapboxMap mapboxMap;
    // variables for adding location layer
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    // variables for calculating and drawing a route
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    // variables needed to initialize navigation

    private Marker markerView;
    private Point destinationPoint;
    private Point originPoint;

    private int tourId;
    private int userId;
    private int hostId;
    private List<Point> pointList = new ArrayList<>();
    int index = 0;
    // private Point curPoint;
    double distanceBetweenPoint = 0;
    boolean checkMain = true;
    boolean isEndTrip = false;
    FloatingActionButton chatMessage,notifi_speed,notifi_problem,notifi_police,speed_40,speed_60,speed_80;
    boolean isShow = false;

    //For chat in maps
    LinearLayout linearLayout;
    TextView nameTourMoving;
    ImageButton hideChatInMap;
    ListView messages_in_map;
    ImageButton recordFile_in_map;
    EditText comment_of_user_in_map;
    ImageButton send_comment_in_map;
    ArrayList<MessageSend> messageSends = new ArrayList<>();
    MessageAdapter messageAdapter;
    int indexRecord = 0;
    private static final String LOG_TAG = "AudioRecordFile";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //access tokens of your account in strings.xml
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_follow_tour);
        mapView = findViewById(R.id.mapView);
        SharedToken obj = new SharedToken(FollowTour.this);
        token = obj.getToken( FollowTour.this);
        Intent intent = getIntent();
        tourId = intent.getIntExtra("tourId",0);
        userId = intent.getIntExtra("userId",0);
        hostId = intent.getIntExtra("hostId",0);


        chatMessage = findViewById(R.id.follow_tour_message);
        linearLayout = findViewById( R.id.follow_tour_chatInMap );
        nameTourMoving = findViewById( R.id.follow_tour_nameTourMoving );
        hideChatInMap = findViewById( R.id.follow_tour_hideChatInMap );
        messages_in_map = findViewById( R.id.follow_tour_messages_in_map );
        recordFile_in_map = findViewById( R.id.follow_tour_recordFile_in_map );
        comment_of_user_in_map = findViewById( R.id.follow_tour_comment_of_user_in_map );
        send_comment_in_map = findViewById(R.id.follow_tour_send_comment_in_map );
        notifi_speed = findViewById( R.id.follow_tour_speed );
        notifi_problem = findViewById( R.id.follow_tour_problem );
        notifi_police = findViewById( R.id.follow_tour_police );
        speed_40 = findViewById( R.id.speed_40 );
        speed_60 = findViewById( R.id.speed_60 );
        speed_80 = findViewById( R.id.speed_80 );
        getSupportActionBar().setTitle("Follow tour");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        openMessage();
        sendMessage();
        clickNotifyPolice();
        clickNotifyProblem();
        clickNotifySpeed40();
        clickNotifySpeed60();
        clickNotifySpeed80();
        sendRecordingFile();
        notifi_speed.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShow == false){
                    notifi_problem.hide();
                    notifi_police.hide();
                    speed_40.show();
                    speed_60.show();
                    speed_80.show();
                    isShow = true;
                }
                else {
                    notifi_problem.show();
                    notifi_police.show();
                    speed_40.hide();
                    speed_60.hide();
                    speed_80.hide();
                    isShow = false;
                }

            }
        } );

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }

    private void sendRecordingFile(){

        recordFile_in_map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                fileName = getExternalCacheDir().getAbsolutePath()+"/" + String.valueOf( indexRecord )+"message.3gp";

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    if(checkPermission()){
                        OnRecord();
                        String msg = "Start recording";
                        int duration = Snackbar.LENGTH_SHORT;
                        showSnackbar(view,msg,duration);
                        //comment_of_user_in_map.setText(fileName);

                    }
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(checkPermission()){
                        CancelRecord();
                        String msg = "Stop recording";
                        int duration = Snackbar.LENGTH_SHORT;
                        showSnackbar(view,msg,duration);
                        //comment_of_user_in_map.setText(fileName);
                    }
                }
                return false;
            }
        });
    }

    private void showSnackbar(View view, String msg, int duration) {

        // Create snackbar
        final Snackbar snackbar = Snackbar.make(view, msg, duration);

        // Set an action on it, and a handler
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.show();
    }

    // Stop record
    private void CancelRecord() {
        try{
            recorder.stop();
            recorder.release();
            recorder = null;
            messageSends.add(new MessageSend( fileName,"","",userId,true,true ));
            if (messageAdapter == null){
                messageAdapter = new MessageAdapter( FollowTour.this,R.layout.my_message, messageSends );
                messages_in_map.setAdapter( messageAdapter );
            }
            else {
                messageAdapter.notifyDataSetChanged();
            }
            messages_in_map.setSelection(messages_in_map.getCount() - 1);
        }catch(RuntimeException stopException){
            //handle cleanup here
        }
    }

    //Start record
    private void OnRecord() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Record failure");
        }
        recorder.start();
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            Toast.makeText(getApplicationContext(), "back action bar", Toast.LENGTH_LONG).show();
            FollowTour.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
//        Toast.makeText(getApplicationContext(), "back presssed", Toast.LENGTH_LONG).show();
        FollowTour.this.finish();
    }

    private void clickNotifyPolice(){
        notifi_police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .sendNotification(token,locationComponent.getLastKnownLocation().getLatitude(),
                                locationComponent.getLastKnownLocation().getLongitude(),tourId,userId,1,"The police are nearby");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "Send Successful", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void clickNotifyProblem(){
        notifi_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .sendNotification(token,locationComponent.getLastKnownLocation().getLatitude(),
                                locationComponent.getLastKnownLocation().getLongitude(),tourId,userId,2,"I have problems on the road");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "Send Successful", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void clickNotifySpeed40(){
        speed_40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .sendNotification(token,locationComponent.getLastKnownLocation().getLatitude(),
                                locationComponent.getLastKnownLocation().getLongitude(),tourId,userId,3,40,"Speed 50");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "Send Successful", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void clickNotifySpeed60(){
        speed_60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .sendNotification(token,locationComponent.getLastKnownLocation().getLatitude(),
                                locationComponent.getLastKnownLocation().getLongitude(),tourId,userId,3,60,"Speed 50");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "Send Successful", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void clickNotifySpeed80(){
        speed_80.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .sendNotification(token,locationComponent.getLastKnownLocation().getLatitude(),
                                locationComponent.getLastKnownLocation().getLongitude(),tourId,userId,3,80,"Speed 50");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200){
                            Toast.makeText(getApplicationContext(), "Send Successful", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void openMessage(){
        chatMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifi_speed.hide();
                chatMessage.hide();
                if (!isShow){
                    notifi_problem.hide();
                    notifi_police.hide();
                }
                else {
                    speed_80.hide();
                    speed_60.hide();
                    speed_40.hide();
                }
                linearLayout.setVisibility( View.VISIBLE );
                hideChatInMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linearLayout.setVisibility( View.INVISIBLE );
                        notifi_speed.show();
                        chatMessage.show();
                        if (!isShow){
                            notifi_problem.show();
                            notifi_police.show();
                        }
                        else {
                            speed_80.show();
                            speed_60.show();
                            speed_40.show();
                        }
                    }
                });
                getListChat();
            }
        });
    }

    private void getListChat(){
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getListChat(token,tourId,1,50);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    try {
                        String body = response.body().string();
                        messageSends = new ArrayList<>( );
                        JSONObject object = new JSONObject( body );

                        JSONArray jsonArr = object.getJSONArray( "notiList" );


                        int getUserID;
                        if (jsonArr.length()>0){
                            for (int i=0;i<jsonArr.length();i++){
                                JSONObject jb = jsonArr.getJSONObject( i );
                                getUserID = jb.getInt( "userId" );
                                if (getUserID == userId){
                                    messageSends.add( new MessageSend( jb.getString( "notification" ),jb.getString( "avatar" ),
                                            jb.getString( "name" ),getUserID,true) );
                                }
                                else {
                                    messageSends.add( new MessageSend( jb.getString( "notification" ),jb.getString( "avatar" ),
                                            jb.getString( "name" ),getUserID,false) );
                                }
                            }

                        }
                        messageAdapter = new MessageAdapter( FollowTour.this,R.layout.my_message, messageSends );
                        messages_in_map.setAdapter( messageAdapter );
                        messages_in_map.setSelection(messages_in_map.getCount() - 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private void sendMessage(){
        send_comment_in_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmt = comment_of_user_in_map.getText().toString();
                if(cmt.isEmpty()){
                    return;
                }
                comment_of_user_in_map.setText("");
                final String comment = cmt;
                cmt = cmt.trim();
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .sendMessage(token,tourId,userId,cmt);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==200) {
                            messageSends.add(new MessageSend(comment, "", "", userId, true));
                            messageAdapter.notifyDataSetChanged();
                            messages_in_map.setSelection(messages_in_map.getCount() - 1);
                        }else{
                            Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        messages_in_map.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (messageSends.get( i ).getIsRecord()){
                    player = new MediaPlayer();
                    try {
                        player.setDataSource(messageSends.get( i ).getComment());
                        player.prepare();
                        player.start();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Open audio failed");
                    }
                }
                else if(player!=null) {
                    player.release();
                    player = null;
                }
            }
        });
    }

    private void getTourInfo(){
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getTourInfo(token,tourId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200) {
                    String bodyListTour = null;
                    try {
                        bodyListTour = response.body().string();
                        JSONObject tourData = new JSONObject(bodyListTour);
                        JSONArray responseArray = tourData.getJSONArray("stopPoints");
                        messageSends = new ArrayList<>( );
                        LatLng point = null;
                        if (responseArray.length() > 0){
                            for (int i = 0;i<responseArray.length();i++){
                                JSONObject jb = responseArray.getJSONObject( i );
                                point = new LatLng( Double.parseDouble(jb.getString( "lat" )) ,Double.parseDouble(jb.getString( "long" )) ) ;

                                if (jb.getInt( "serviceTypeId" )== 1){
                                    markerView = mapboxMap.addMarker( new MarkerOptions()
                                            .position( point )
                                            .icon( IconFactory.getInstance(FollowTour.this).fromResource(R.drawable.restaurant) )
                                            .title( jb.getString( "name") ));
                                }
                                else if (jb.getInt( "serviceTypeId" )==2){
                                    markerView = mapboxMap.addMarker( new MarkerOptions()
                                            .position( point )
                                            .icon( IconFactory.getInstance(FollowTour.this).fromResource(R.drawable.hotel) )
                                            .title( jb.getString( "name") ));
                                }
                                else if (jb.getInt( "serviceTypeId" )==3){
                                    markerView = mapboxMap.addMarker( new MarkerOptions()
                                            .position( point )
                                            .icon( IconFactory.getInstance(FollowTour.this).fromResource(R.drawable.rest_station) )
                                            .title( jb.getString( "name") ));
                                }
                                else {
                                    markerView = mapboxMap.addMarker( new MarkerOptions()
                                            .position( point )
                                            .icon( IconFactory.getInstance(FollowTour.this).fromResource(R.drawable.other) )
                                            .title( jb.getString( "name") ));
                                }
                                pointList.add(Point.fromLngLat(jb.getDouble( "long" ),jb.getDouble( "lat" )));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(FollowTour.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(FollowTour.this,
                    Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions( this,new String[] {Manifest.permission.RECORD_AUDIO},REQUEST_RECORD_AUDIO_PERMISSION);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions( this,new String[] {Manifest.permission.RECORD_AUDIO},REQUEST_RECORD_AUDIO_PERMISSION);

            }
            return false;
        } else {
            return true;
            // Permission has already been granted
        }
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(getString(R.string.navigation_guidance_day), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);

                mapboxMap.addOnMapClickListener(FollowTour.this);
                getTourInfo();
            }
        });
    }

    //@SuppressWarnings( {"MissingPermission"})
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        // Check marker if existed => delete
        if(destinationPoint!=null)
            markerView.remove();
        // draw marker
        markerView = mapboxMap.addMarker(new MarkerOptions()
                .position(point)
                .title("Eiffel Tower "));

        destinationPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                locationComponent.getLastKnownLocation().getLatitude());

        // get router to draw Direction
        getRoute(originPoint, destinationPoint);
        //button.setEnabled(true);
        //button.setBackgroundColor(R.color.mapbox_navigation_view_color_primary_text_dark);
        return true;
    }

    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }
                        currentRoute = response.body().routes().get(0);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Activate the MapboxMap LocationComponent to show user location
            // Adding in LocationComponentOptions is also an optional parameter
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(this, loadedMapStyle);
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
            new Thread( new Runnable() {
                public void run() {
                    try {
                        while (checkMain || !isEndTrip){
                            Thread.sleep(700);
                            if (locationComponent.getLastKnownLocation() != null){

                                if (pointList.size()>0){
                                    originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                                            locationComponent.getLastKnownLocation().getLatitude());
                                    checkMain = false;
                                }
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } ).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (!isEndTrip) {
                            Thread.sleep(700);
                            if (!checkMain) {
                                originPoint = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),
                                        locationComponent.getLastKnownLocation().getLatitude());
                                distanceBetweenPoint = TurfMeasurement.distance(originPoint, pointList.get(index));
                            }
                            if (distanceBetweenPoint < 0.01){
                                index++;
                                if (index >= pointList.size()) {
                                    isEndTrip = true;
                                    if(userId == hostId) {
                                        Call<ResponseBody> call = RetrofitClient
                                                .getInstance()
                                                .getUserService()
                                                .finishTrip(token, tourId);
                                        call.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.code() == 200) {
                                                    AlertDialog.Builder aleartDialog = new AlertDialog.Builder(FollowTour.this);
                                                    aleartDialog.setTitle("Tour finshed");
                                                    aleartDialog.setMessage("Congratulations!!!");
                                                    aleartDialog.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                                            }
                                        });
                                    }else{
                                        AlertDialog.Builder aleartDialog = new AlertDialog.Builder(FollowTour.this);
                                        aleartDialog.setTitle("Tour finshed");
                                        aleartDialog.setMessage("Congratulations!!!");
                                        aleartDialog.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        while (!isEndTrip){
                            if(!checkMain) {
                                Call<ResponseBody> call = RetrofitClient
                                        .getInstance()
                                        .getUserService()
                                        .sendCoordinate(token, userId, tourId, locationComponent.getLastKnownLocation().getLatitude(), locationComponent.getLastKnownLocation().getLongitude());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.code() == 200) {
                                            try {
                                                String body = response.body().string();

                                                JSONArray responseArray = new JSONArray(body);
                                                LatLng point = null;
                                                if (responseArray.length() > 0) {
                                                    for (int i = 0; i < responseArray.length(); i++) {
                                                        JSONObject jb = responseArray.getJSONObject(i);
                                                        point = new LatLng(jb.getDouble("lat"), jb.getDouble("long"));
                                                        if (jb.getInt("id") != userId) {
                                                            markerView = mapboxMap.addMarker(new MarkerOptions()
                                                                    .position(point)
                                                                    .icon(IconFactory.getInstance(FollowTour.this).fromResource(R.drawable.friends))
                                                                    .title(jb.getString("id")));
                                                        }
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });

                                call = RetrofitClient
                                        .getInstance()
                                        .getUserService()
                                        .getNotification(token, tourId, 1, 200);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.code() == 200) {
                                            try {
                                                String body = response.body().string();
                                                JSONObject jsonObject = new JSONObject(body);
                                                JSONArray jsonArray = jsonObject.getJSONArray("notiList");
                                                LatLng point = null;
                                                if (jsonArray.length() > 0) {
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jb = jsonArray.getJSONObject(i);

                                                        if (!jb.getString("notificationType").equals("null")) {
                                                            point = new LatLng(jb.getDouble("lat"), jb.getDouble("long"));

                                                            if (jb.getInt("notificationType") == 1) {
                                                                markerView = mapboxMap.addMarker(new MarkerOptions()
                                                                        .position(point)
                                                                        .icon(IconFactory.getInstance(FollowTour.this).fromResource(R.drawable.policeman))
                                                                        .title(jb.getString("note")));
                                                            } else if (jb.getInt("notificationType") == 2) {
                                                                markerView = mapboxMap.addMarker(new MarkerOptions()
                                                                        .position(point)
                                                                        .icon(IconFactory.getInstance(FollowTour.this).fromResource(R.drawable.problem))
                                                                        .title(jb.getString("note")));
                                                            } else if (jb.getInt("notificationType") == 3) {
                                                                if (jb.getInt("speed") <= 40) {
                                                                    markerView = mapboxMap.addMarker(new MarkerOptions()
                                                                            .position(point)
                                                                            .icon(IconFactory.getInstance(FollowTour.this).fromResource(R.drawable.speed_40))
                                                                            .title(jb.getString("note")));
                                                                } else if (jb.getInt("speed") <= 60) {
                                                                    markerView = mapboxMap.addMarker(new MarkerOptions()
                                                                            .position(point)
                                                                            .icon(IconFactory.getInstance(FollowTour.this).fromResource(R.drawable.speed_60))
                                                                            .title(jb.getString("note")));
                                                                } else if (jb.getInt("speed") <= 80) {
                                                                    markerView = mapboxMap.addMarker(new MarkerOptions()
                                                                            .position(point)
                                                                            .icon(IconFactory.getInstance(FollowTour.this).fromResource(R.drawable.speed_80))
                                                                            .title(jb.getString("note")));
                                                                }

                                                            }

                                                        }
                                                    }
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                            Thread.sleep( 10000 );
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent(mapboxMap.getStyle());
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
