package com.ygaps.travelapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Button sendOTP;
    private EditText codeOTP;
    private EditText newPassword;
    private Button confirm;
    private Number userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email = findViewById(R.id.sign_up_email);
        sendOTP = findViewById(R.id.sign_up_sendOTP);
        codeOTP = findViewById(R.id.sign_up_codeOTP);
        newPassword = findViewById(R.id.sign_up_password);
        confirm = findViewById(R.id.sign_up_confirm);

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sEmail = email.getText().toString();
                if (sEmail.isEmpty()) {
                    email.setError("Email is empty");
                    email.requestFocus();
                    return;
                }

                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .RequestOTP("email",sEmail);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==200){
                            codeOTP.setVisibility(View.VISIBLE);
                            newPassword.setVisibility(View.VISIBLE);
                            confirm.setVisibility(View.VISIBLE);
                            try {
                                String body = response.body().string();
                                JSONObject bodyData = new JSONObject(body);
                                userId = bodyData.getInt("userId");
                            }catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(response.code()==404){
                            Toast.makeText(ForgotPasswordActivity.this, "EMAIL doesn't exist", Toast.LENGTH_SHORT).show();
                            codeOTP.setVisibility(View.INVISIBLE);
                            newPassword.setVisibility(View.INVISIBLE);
                            confirm.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sCodeOTP = codeOTP.getText().toString();
                String sNewPassword = newPassword.getText().toString();
                if (sCodeOTP.isEmpty()) {
                    codeOTP.setError("Code OTP is empty");
                    codeOTP.requestFocus();
                    return;
                }
                if (sNewPassword.isEmpty()) {
                    newPassword.setError("New password is empty");
                    newPassword.requestFocus();
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
                            Toast.makeText( ForgotPasswordActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                        }
                        if(response.code() == 403){
                            Toast.makeText( ForgotPasswordActivity.this, "Wrong OTP or your OTP expired", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
