package com.ygaps.travelapp.View;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.UserInfoRequest;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {
    private Button btnLogOut;
    private Dialog dialog;
    private TextView tvUserName;
    private Button btnEdit;
    private Button btnChangePass;
    private String fullName;
    private String email;
    private String phone;
    private String dob;
    private int gender;
    private EditText codeOTP;
    private EditText newPass;
    private Button btnConfirm;
    private ImageButton btnCancel;
    private int userId;
    private String token;
    private UserInfoRequest userInfoRequest = new UserInfoRequest();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        tvUserName = view.findViewById(R.id.settings_user_name);
        btnEdit = view.findViewById(R.id.settings_edit_profile);
        btnChangePass = view.findViewById(R.id.settings_change_password);
        btnLogOut = view.findViewById(R.id.settings_log_out);
        SharedToken obj = new SharedToken(SettingsFragment.super.getActivity());
        token = obj.getToken(SettingsFragment.super.getActivity());

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getUserInfo(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                if(code == 200){
                    try {
                        String bodyUserInfo = response.body().string();
                        JSONObject userInfo = new JSONObject(bodyUserInfo);
                        userId = userInfo.getInt("id");
                        fullName = userInfo.getString("fullName");
                        email = userInfo.getString("email");
                        phone = userInfo.getString("phone");
                        dob = userInfo.getString("dob");
                        try{
                            dob = dob.substring(0, 10);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        gender = userInfo.getInt("gender");
                        tvUserName.setText(fullName);
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

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.95);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_edit_user_info);
                dialog.getWindow().setLayout(width,height);
                final EditText edFullName = dialog.findViewById(R.id.user_info_full_name);
                final EditText edEmail = dialog.findViewById(R.id.user_info_email);
                final EditText edPhone = dialog.findViewById(R.id.user_info_phone);
                final RadioButton rdbFemale = dialog.findViewById(R.id.radioButton_female);
                final RadioButton rdbMale = dialog.findViewById(R.id.radioButton_male);
                final TextView tvDob = dialog.findViewById(R.id.user_info_dob);
                ImageButton btnSelectDate = dialog.findViewById(R.id.user_info_select_date);
                Button btnEdit = dialog.findViewById(R.id.user_info_eidt);

                edFullName.setText(fullName);
                edEmail.setText(email);
                edPhone.setText(phone);
                tvDob.setText(dob);
                if(gender == 0){
                    rdbFemale.setChecked(true);
                }else{
                    rdbMale.setChecked(true);
                }

                ImageButton btnCancel = dialog.findViewById(R.id.cancel_edit_profile);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                btnSelectDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int year,month,dayOfMonth;
                        Calendar calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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
                                        tvDob.setText(year + "-" + sMonth + "-" + sDay);
                                    }
                                },year,month,dayOfMonth);
                        datePickerDialog.show();
                    }
                });
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fullName = edFullName.getText().toString();
                        email = edEmail.getText().toString();
                        phone = edPhone.getText().toString();
                        dob = tvDob.getText().toString();

                        if(fullName.isEmpty()){
                            edFullName.setError("Full name is empty");
                            edFullName.requestFocus();
                            return;
                        }
                        userInfoRequest.setFullName(fullName);
                        if(email.isEmpty()){
                            edEmail.setError("Email is empty");
                            edEmail.requestFocus();
                            return;
                        }
                        userInfoRequest.setEmail(email);
                        userInfoRequest.setPhone(phone);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            userInfoRequest.setDob(dateFormat.parse(dob));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(rdbMale.isChecked()){
                            gender = 1;
                            userInfoRequest.setGender(1);
                        }else{
                            gender = 0;
                            userInfoRequest.setGender(0);
                        }
                        Call<ResponseBody> call = RetrofitClient
                                .getInstance()
                                .getUserService()
                                .updateUserInfo(token,userInfoRequest);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText( getActivity(), "Update successfully", Toast.LENGTH_SHORT).show();
                                tvUserName.setText(fullName);
                                dialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                });
            }
        });
        clickChangePass();
        return view;
    }
    private void clickChangePass(){
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(SettingsFragment.super.getContext());
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_change_password);
                btnCancel = dialog.findViewById(R.id.settings_cancel);
                codeOTP = dialog.findViewById(R.id.settings_codeOTP);
                newPass = dialog.findViewById(R.id.settings_new_password);
                btnConfirm = dialog.findViewById(R.id.settings_confirm);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sCodeOTP = codeOTP.getText().toString();
                        String sNewPassword = newPass.getText().toString();
                        if (sCodeOTP.isEmpty()) {
                            codeOTP.setError("Code OTP is empty");
                            codeOTP.requestFocus();
                            return;
                        }
                        if (sNewPassword.isEmpty()) {
                            newPass.setError("New password is empty");
                            newPass.requestFocus();
                            return;
                        }
                        Call<ResponseBody> call = RetrofitClient
                                .getInstance()
                                .getUserService()
                                .VerifyOTP(userId,sNewPassword,sCodeOTP);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.code() == 200){
                                    Toast.makeText( SettingsFragment.super.getContext(), "Successful", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                if(response.code() == 403){
                                    Toast.makeText(  SettingsFragment.super.getContext(), "Wrong OTP or your OTP expired", Toast.LENGTH_SHORT).show();
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
}
