package com.ygaps.travelapp.View;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.ygaps.travelapp.Model.RemoveStopPointsRequest;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.StopPointInfos;
import com.ygaps.travelapp.Model.StopPointRequest;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateStopPointInformationFragment extends AppCompatDialogFragment {
    private String token;
    private EditText name;
    private Spinner serviceType;
    private EditText address;
    private Spinner province;
    private EditText minCost;
    private EditText maxCost;
    private TextView timeStart;
    private ImageButton selectTimeStart;
    private TextView dateStart;
    private ImageButton selectDateStart;
    private TextView timeEnd;
    private ImageButton selectTimeEnd;
    private TextView dateEnd;
    private ImageButton selectDateEnd;
    private Button btnUpdate;
    private Button btnRemove;
    private StopPointInfos stopPointInfos;

    private int tourId;
    private ArrayList<StopPointInfos> listStopPoints = new ArrayList<>();

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
    private long arrivalAt = 0;
    private long leaveAt = 0;
    private int provinceId = 0;
    private int serviceTypeId = 0;
    private UpdateStopPointListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_point_information, container, false);

        SharedToken obj = new SharedToken(UpdateStopPointInformationFragment.super.getContext());
        token = obj.getToken( UpdateStopPointInformationFragment.super.getContext());
        Bundle bundle = getArguments();
        String JSONPointInfo = bundle.getString("StopPointInfo");
        tourId = bundle.getInt("tourId",0);

        Gson gson = new Gson();
        stopPointInfos = gson.fromJson(JSONPointInfo, StopPointInfos.class);
        name = view.findViewById(R.id.stop_point_name);
        serviceType = view.findViewById(R.id.stop_point_service_type);
        address = view.findViewById(R.id.stop_point_address);
        province = view.findViewById(R.id.stop_point_province);
        minCost = view.findViewById(R.id.stop_point_min_cost);
        maxCost = view.findViewById(R.id.stop_point_max_cost);

        timeStart = view.findViewById(R.id.stop_point_time_start);
        selectTimeStart = view.findViewById(R.id.stop_point_button_select_time_start);
        dateStart = view.findViewById(R.id.stop_point_select_date_start);
        selectDateStart = view.findViewById(R.id.stop_point_button_select_date_start);

        timeEnd = view.findViewById(R.id.stop_point_time_end);
        selectTimeEnd = view.findViewById(R.id.stop_point_button_select_time_end);
        dateEnd = view.findViewById(R.id.stop_point_select_date_end);
        selectDateEnd = view.findViewById(R.id.stop_point_button_select_date_end);
        btnUpdate = view.findViewById(R.id.stop_point_update);
        btnRemove = view.findViewById(R.id.stop_point_remove);

        name.setText(stopPointInfos.getName());
        address.setText(stopPointInfos.getAddress());
        serviceType.setSelection(stopPointInfos.getServiceTypeId() - 1);
        province.setSelection(stopPointInfos.getProvinceId() - 1);
        minCost.setText(stopPointInfos.getMinCost());
        maxCost.setText(stopPointInfos.getMaxCost());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String sArrivalAt = simpleDateFormat.format(new Date(stopPointInfos.getArrivalAt()));
        String[] separated = sArrivalAt.split(" ");
        dateStart.setText(separated[0]);
        timeStart.setText(separated[1]);
        String sLeaveAt = simpleDateFormat.format(new Date(stopPointInfos.getLeaveAt()));
        separated = sLeaveAt.split(" ");
        dateEnd.setText(separated[0]);
        timeEnd.setText(separated[1]);
        ClickSelectTime();
        ClickSelectDate();
        updateStopPoint();
        removeStopPoint();
        return view;
    }

    private void ClickSelectTime(){
        selectTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int mintute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateStopPointInformationFragment.super.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
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
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateStopPointInformationFragment.super.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
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
                final DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateStopPointInformationFragment.super.getContext(),
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
                final DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateStopPointInformationFragment.super.getContext(),
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

    private void updateStopPoint(){
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sName = name.getText().toString().trim();
                String sServiceType = serviceType.getSelectedItem().toString().trim();
                final String sAddress = address.getText().toString().trim();
                String sProvince = province.getSelectedItem().toString().trim();
                final String sMinCost = minCost.getText().toString().trim();
                final String sMaxCost = maxCost.getText().toString().trim();
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
                    Toast toast = Toast.makeText(UpdateStopPointInformationFragment.super.getContext(),"Time start is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sDateStart.isEmpty()){
                    Toast toast = Toast.makeText(UpdateStopPointInformationFragment.super.getContext(),"Start date is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sTimeEnd.isEmpty()){
                    Toast toast = Toast.makeText(UpdateStopPointInformationFragment.super.getContext(),"Time end is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if(sDateEnd.isEmpty()){
                    Toast toast = Toast.makeText(UpdateStopPointInformationFragment.super.getContext(),"Date end is empty",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

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
                listStopPoints.add( new StopPointInfos(stopPointInfos.getId(),stopPointInfos.getServiceId(),sName,sAddress,provinceId,
                        stopPointInfos.getLatitude(), stopPointInfos.getLongitude(),arrivalAt,leaveAt,serviceTypeId,sMinCost,sMaxCost));


                StopPointRequest body = new StopPointRequest(String.valueOf(tourId),listStopPoints);
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
                            Toast.makeText(UpdateStopPointInformationFragment.super.getContext(), "Update successful", Toast.LENGTH_LONG).show();
                            listener.applyItemList(sName,serviceTypeId,sAddress,provinceId,sMinCost,sMaxCost,String.valueOf(arrivalAt),String.valueOf(leaveAt));
                        }else{
                            Toast.makeText(UpdateStopPointInformationFragment.super.getContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(UpdateStopPointInformationFragment.super.getContext(),"fail", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void removeStopPoint(){
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Number> deleteIds = new ArrayList<>();
                deleteIds.add(stopPointInfos.getId());
                RemoveStopPointsRequest body = new RemoveStopPointsRequest(String.valueOf(tourId),deleteIds);
                // Attempt to send request:
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .removeStopPoints(token,body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        int code = response.code();
                        if(code == 200){
                            Toast.makeText(UpdateStopPointInformationFragment.super.getContext(), "Remove successful", Toast.LENGTH_LONG).show();
                            listener.applyItemList(stopPointInfos.getName(),stopPointInfos.getServiceTypeId(),stopPointInfos.getAddress()
                                    ,stopPointInfos.getProvinceId(),stopPointInfos.getMinCost(),stopPointInfos.getMaxCost()
                                    ,String.valueOf(stopPointInfos.getArrivalAt()),String.valueOf(stopPointInfos.getLeaveAt()));
                        }else{
                            Toast.makeText(UpdateStopPointInformationFragment.super.getContext(), "Some thing went wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(UpdateStopPointInformationFragment.super.getContext(),"fail", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (UpdateStopPointListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface UpdateStopPointListener{
        void applyItemList(String name, int serviceTypeId, String address, int provinceId, String minCost, String maxCost,String arriveAt,String leaveAt);
    }

}
