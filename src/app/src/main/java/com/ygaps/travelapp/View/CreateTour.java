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
import android.widget.TextView;
import android.widget.Toast;

import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTour extends AppCompatActivity {

    public static int createTourId;
    private EditText TourName;
    private TextView StartDate;
    private TextView EndDate;
    private EditText Adults;
    private EditText Children;
    private EditText MinCost;
    private EditText MaxCost;
    private TextView ChooseTourImage;
    private CheckBox CheckPrivate;

    private Button btnCreateTour;

    private Calendar calendar;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);
        SharedToken obj = new SharedToken(CreateTour.this);
        token = obj.getToken(CreateTour.this);
        TourName = findViewById(R.id.Create_Tour_Name);
        StartDate = findViewById(R.id.Create_Tour_DayStart);
        EndDate = findViewById(R.id.Create_Tour_DayEnd);
        Adults = findViewById(R.id.Create_Tour_Adults);
        Children = findViewById(R.id.Create_Tour_Children);
        MinCost = findViewById(R.id.Create_Tour_MinCost);
        MaxCost = findViewById(R.id.Create_Tour_MaxCost);
        ChooseTourImage = findViewById(R.id.Create_Tour_Image);
        CheckPrivate = findViewById(R.id.Create_Tour_Check);

        getSupportActionBar().setTitle("Create tour");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCreateTour = findViewById(R.id.Create_Tour_Button);
        btnCreateTour.setOnClickListener(new View.OnClickListener() {
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

                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .createTour(token,sTourName,milisecondS,milisecondE,100,100,100,100,check,
                                Integer.parseInt(sAdults),Integer.parseInt(sChildren),Long.parseLong(sMinCost),Long.parseLong(sMaxCost));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        int code = response.code();
                        if(code == 200) {
                            try {
                                String bodyCreateTour = response.body().string();
                                JSONObject createTourData = new JSONObject(bodyCreateTour);
                                createTourId = createTourData.getInt("id");
                            }catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(CreateTour.this,MapsActivity.class);
                            startActivity(intent);
                            CreateTour.this.finish();
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
    public void SelectStartDay(View view){
        int year,month,dayOfMonth;
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTour.this,
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTour.this,
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
