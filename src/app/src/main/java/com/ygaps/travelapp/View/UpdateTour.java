package com.ygaps.travelapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.TourInfoResponse;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTour extends AppCompatActivity {
    private String token;
    private int tourId;
    private TourInfoResponse tourInfoResponse = new TourInfoResponse();
    private EditText TourName;
    private TextView StartDate;
    private TextView EndDate;
    private EditText Adults;
    private EditText Children;
    private EditText MinCost;
    private EditText MaxCost;
    private TextView ChooseTourImage;
    private CheckBox CheckPrivate;
    private RadioButton RdbOpen;
    private RadioButton RdbStarted;
    private RadioButton RdbClosed;
    private Button BtnUpdate;
    private Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tour);
        SharedToken obj = new SharedToken(UpdateTour.this);
        token = obj.getToken( UpdateTour.this);
        Intent intent = getIntent();
        tourId = (int) intent.getSerializableExtra(HistoryFragment.EXTRA_MESSAGE);
        getSupportActionBar().setTitle("Update tour");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TourName = findViewById(R.id.Update_Tour_Name);
        StartDate = findViewById(R.id.Update_Tour_DayStart);
        EndDate = findViewById(R.id.Update_Tour_DayEnd);
        Adults = findViewById(R.id.Update_Tour_Adults);
        Children = findViewById(R.id.Update_Tour_Children);
        MinCost = findViewById(R.id.Update_Tour_MinCost);
        MaxCost = findViewById(R.id.Update_Tour_MaxCost);
        ChooseTourImage = findViewById(R.id.Update_Tour_Image);
        CheckPrivate = findViewById(R.id.Update_Tour_Check);
        RdbOpen = findViewById(R.id.radioButton_open);
        RdbStarted = findViewById(R.id.radioButton_started);
        RdbClosed = findViewById(R.id.radioButton_closed);
        BtnUpdate = findViewById(R.id.Update_Tour_Button);
        GetTourInfo();
        UpdateTour();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), TourDetailActivity.class);
            intent.putExtra(HistoryFragment.EXTRA_MESSAGE, tourId);
            UpdateTour.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void GetTourInfo() {
        Call<TourInfoResponse> call = RetrofitClient.getInstance()
                .getUserService()
                .getTourDetail(token, tourId);
        call.enqueue(new Callback<TourInfoResponse>() {
            @Override
            public void onResponse(Call<TourInfoResponse> call, Response<TourInfoResponse> response) {
                if (response.isSuccessful()) {
                    tourInfoResponse = response.body();
                    TourName.setText(tourInfoResponse.getmName());
                    String stringDate = MilisecondToDate(tourInfoResponse.getmStartDate());
                    StartDate.setText(stringDate);
                    stringDate = MilisecondToDate(tourInfoResponse.getmEndDate());
                    EndDate.setText(stringDate);
                    MinCost.setText(tourInfoResponse.getmMinCost());
                    MaxCost.setText(tourInfoResponse.getmMaxCost());
                    Adults.setText(String.valueOf(tourInfoResponse.getmAdults()));
                    Children.setText(String.valueOf(tourInfoResponse.getmChilds()));
                    if(!tourInfoResponse.getmIsPrivate()){
                        CheckPrivate.setChecked(false);
                    }else{
                        CheckPrivate.setChecked(true);
                    }
                    if(tourInfoResponse.getmStatus() == 0){
                        RdbOpen.setChecked(true);
                    }
                    else if(tourInfoResponse.getmStatus() == 1){
                        RdbStarted.setChecked(true);
                    }
                    else{
                        RdbClosed.setChecked(true);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), " Khong thanh cong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TourInfoResponse> call, Throwable t) {
            }
        });
    }

    public void SelectStartDay(View view){
        int year,month,dayOfMonth;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateTour.this,
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
                        StartDate.setText(sDay + "/" + sMonth + "/" + year);
                    }
                },year,month,dayOfMonth);
        datePickerDialog.show();
    }

    public void SelectEndDay(View view){
        int year,month,dayOfMonth;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateTour.this,
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
                        EndDate.setText(sDay + "/" + sMonth + "/" + year);
                    }
                },year,month,dayOfMonth);
        datePickerDialog.show();
    }

    private String MilisecondToDate(String milisecond) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        long milliSeconds = Long.parseLong(milisecond);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

    }

    private void UpdateTour(){
        BtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sTourName = TourName.getText().toString().trim();
                String sStartDate = StartDate.getText().toString().trim();
                String sEndDate = EndDate.getText().toString().trim();
                String sAdults = Adults.getText().toString().trim();
                String sChildren = Children.getText().toString().trim();
                String sMinCost = MinCost.getText().toString().trim();
                String sMaxCost = MaxCost.getText().toString().trim();
                String sChooseTourImage = ChooseTourImage.getText().toString().trim();

                if (sTourName.isEmpty()) {
                    TourName.setError("Tour name is empty");
                    TourName.requestFocus();
                    return;
                }
                if (sStartDate.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),"Start date is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (sEndDate.isEmpty()) {
                    Toast toast = Toast.makeText(getApplicationContext(),"End date is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (sAdults.isEmpty()) {
                    Adults.setError("Adults is empty");
                    Adults.requestFocus();
                    return;
                }
                if (sChildren.isEmpty()) {
                    Children.setError("Children is empty");
                    Children.requestFocus();
                    return;
                }
                if (sMinCost.isEmpty()) {
                    MinCost.setError("Min cost is empty");
                    MinCost.requestFocus();
                    return;
                }
                if (sMaxCost.isEmpty()) {
                    MaxCost.setError("Max cost is empty");
                    MaxCost.requestFocus();
                    return;
                }
//        if (sChooseTourImage.isEmpty()) {
//            ChooseTourImage.setError("Image is empty");
//            ChooseTourImage.requestFocus();
//            return;
//        }

                boolean check;
                check = CheckPrivate.isChecked();
                long milisecondS = 0;
                long milisecondE = 0;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date dateS = sdf.parse(sStartDate);
                    milisecondS = dateS.getTime();
                }catch (ParseException e){
                    e.printStackTrace();
                }
                try {
                    Date dateE = sdf.parse(sEndDate);
                    milisecondE = dateE.getTime();
                }catch (ParseException e){
                    e.printStackTrace();
                }
                int RdbCheck = -1;
                if(RdbOpen.isChecked()){
                    RdbCheck = 0;
                }
                else if(RdbStarted.isChecked()){
                    RdbCheck = 1;
                }
                if(RdbClosed.isChecked()){
                    RdbCheck = 2;
                }
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .updateTour(token,String.valueOf(tourId),sTourName,milisecondS,milisecondE,100,100,100,100,
                                Integer.parseInt(sAdults),Integer.parseInt(sChildren),Long.parseLong(sMinCost),Long.parseLong(sMaxCost),check,RdbCheck);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        int code = response.code();
                        if(code == 200) {
                            Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(UpdateTour.this,TourDetailActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra(HistoryFragment.EXTRA_MESSAGE, tourId);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "Some thing wrong!!!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("Fail","Error");
                    }
                });
            }
        });
    }
}
