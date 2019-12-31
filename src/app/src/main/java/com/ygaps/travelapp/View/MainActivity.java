package com.ygaps.travelapp.View;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ygaps.travelapp.Adapter.TourAdapter;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.TourInformations;
import com.ygaps.travelapp.MyFirebaseService;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

public class MainActivity extends AppCompatActivity {
    private String token;
    private List<TourInformations> listTour = new ArrayList<TourInformations>();
    private ListView listView;
    private TourAdapter tourAdapter;
    private Dialog dialog;
    private NumberPicker npPageIndex;
    private NumberPicker npPageSize;
    private TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      SharedToken obj = new SharedToken(MainActivity.this);
      token = obj.getToken( MainActivity.this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.v("hihi", "getInstanceId failed", task.getException());
                    return;
                }
                // Get new Instance ID token
                String fmcToken = task.getResult().getToken();
                sendRegistrationToServer(fmcToken);
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        listView = findViewById(R.id.listView);
        textView = findViewById(R.id.count_trip);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_update:
                            selectedFragment = new HistoryFragment();
                            break;
                        case R.id.nav_map:
                            selectedFragment = new ExploreFragment();
                            break;
                        case R.id.nav_notifications:
                            selectedFragment = new NotificationsFragment();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    public void PageCommentSettingClick(View view) {
        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_page_settings);
        dialog.getWindow();
        npPageIndex = dialog.findViewById(R.id.page_index_number);
        npPageIndex.setMinValue(1);
        npPageIndex.setMaxValue(10);
        npPageIndex.setWrapSelectorWheel(true);
        npPageSize = dialog.findViewById(R.id.page_size_number);
        npPageSize.setMinValue(1);
        npPageSize.setMaxValue(10);
        npPageSize.setWrapSelectorWheel(true);
        dialog.show();
    }

    public void CancelPageSettingClick(View view) {
        dialog.dismiss();
    }

    public void ApplyPageSettingClick(View view) {
        Toast.makeText(getApplicationContext(), "Paging applied" + " Size:"+npPageSize.getValue()+" Row:"+
                npPageIndex.getValue(), Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }


    private void sendRegistrationToServer(String fcmToken) {
        // TODO: Implement this method to send token to your app server.
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .registerFirebase(token,fcmToken, android_id,1 , "1.0");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Ready for notification", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Not ready for notification", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
