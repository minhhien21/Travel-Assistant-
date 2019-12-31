package com.ygaps.travelapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ygaps.travelapp.Model.RegisterResponse;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText inputName;
    private EditText inputEmail;
    private EditText inputPhone;
    private EditText inputPassword;
    private EditText confirmPassword;
    private RadioButton rdbFemale;
    private RadioButton rdbMale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void SendSignUpInfo(View view) {
        inputName = findViewById(R.id.InputName);
        inputEmail = findViewById(R.id.InputEmail);
        inputPhone = findViewById(R.id.InputPhone);
        inputPassword = findViewById(R.id.InputPassword);
        confirmPassword = findViewById(R.id.ConfirmPassword);
        rdbMale = findViewById(R.id.register_radioButton_male);
        rdbFemale = findViewById(R.id.register_radioButton_female);

        String aName = inputName.getText().toString().trim();
        String aEmail = inputEmail.getText().toString().trim();
        String aPhone = inputPhone.getText().toString().trim();
        String aPassword = inputPassword.getText().toString().trim();
        String confirmpassword = confirmPassword.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (aEmail.isEmpty()) {
            inputEmail.setError("Email required");
            inputEmail.requestFocus();
            return;
        }
        if (!aEmail.matches(emailPattern)) {
            inputEmail.setError("Invalid email !");
            inputEmail.requestFocus();
            return;
        }
        if (aName.isEmpty()) {
            inputName.setError("Name required");
            inputName.requestFocus();
            return;
        }
        if (aPhone.length() < 10) {
            inputPhone.setError("Invalid phone");
            inputPhone.requestFocus();
            return;
        }

        if (aPhone.isEmpty()) {
            inputPhone.setError("Phone required");
            inputPhone.requestFocus();
            return;
        }
        if (aPassword.isEmpty()) {
            inputPassword.setError("Password required");
            inputPassword.requestFocus();
            return;
        }
        if (confirmpassword.isEmpty()) {
            confirmPassword.setError("Confirm password required");
            confirmPassword.requestFocus();
            return;
        }
        if (!confirmpassword.equals(aPassword)) {
            confirmPassword.setError("Confirm password does not match");
            confirmPassword.requestFocus();
            return;
        }
        int check = -1;
        if(rdbFemale.isChecked()){
            check = 0;
        }
        if(rdbMale.isChecked()){
            check = 1;
        }

        Call<RegisterResponse> call = RetrofitClient
                .getInstance()
                .getUserService()
                .createUser(aName, aEmail, aPassword, aPhone, check);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.code() == 200) {
                    RegisterResponse dr = response.body();
                    Toast.makeText(SignupActivity.this,
                            "Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    String s = null;
                    try {
                        s = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("message");

                        if (jsonArray.length() == 2) {
                            Toast.makeText(SignupActivity.this, "Phone number and Email is used by someone", Toast.LENGTH_SHORT).show();

                        } else {

                            JSONObject messageExisted = jsonArray.getJSONObject(0);
                            if (!messageExisted.getString("msg").isEmpty()) {

                                Toast.makeText(SignupActivity.this,
                                        messageExisted.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
            }
        });
    }
    public void GoToLoginScreen(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
