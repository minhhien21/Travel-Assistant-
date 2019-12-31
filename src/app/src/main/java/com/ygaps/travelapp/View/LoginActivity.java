package com.ygaps.travelapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ygaps.travelapp.Model.LoginResponse;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.TokenInfo;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.ygaps.travelapp.R;

import java.util.Arrays;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class  LoginActivity extends AppCompatActivity {
    public static String userId;
    public static String TOKEN;
    private EditText et_User;
    private EditText et_Pass;
    private LoginButton btn_FB;
    private Button btn_SignIn;
    private Button btn_SignUp;
    private Button btn_Forgot;
    CallbackManager callbackManager;

    String TAG = "Login Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        //Login using FB account:
        btn_FB = findViewById( R.id.btn_LoginFB );
        callbackManager = CallbackManager.Factory.create();
        btn_FB.setPermissions( Arrays.asList("email"));
        checkLoginStatus();
        btn_FB.registerCallback( callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                Call<LoginResponse> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .loginFB(accessToken);
                call.enqueue( new Callback<LoginResponse>() {

                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if(response.code() == 200){
                            LoginResponse body = response.body();
                            TokenInfo token = new TokenInfo(null,null,body.getToken());

                            userId = body.getUserId();
                            TOKEN = token.getToken();
                            Log.i("Login",TOKEN);

                            Toast.makeText( LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                            SharedToken obj = new SharedToken(LoginActivity.this);
                            obj.saveToken( LoginActivity.this,token );

                            Intent intent = new Intent( LoginActivity.this,MainActivity.class );
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity( intent );
                            LoginActivity.this.finish();
                        }
                        else{
                            Toast.makeText( LoginActivity.this, "Login Failed, try again !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d(TAG,"Error Login");
                    }
                } );


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        } );



        // Sign in:
        btn_SignIn = findViewById( R.id.btn_SignIn );
        btn_SignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        } );

        //Sign up:
        btn_SignUp = findViewById( R.id.btn_SignUp );
        btn_SignUp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( LoginActivity.this, SignupActivity.class );
                startActivity( intent );
            }
        } );

        // Forgot password:
        btn_Forgot = findViewById( R.id.btn_ForgotPass );
        btn_Forgot.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( LoginActivity.this, ForgotPasswordActivity.class );
                startActivity( intent );
            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult( requestCode,resultCode,data );
        super.onActivityResult( requestCode, resultCode, data );
    }

    private void checkLoginStatus() {
        if(AccessToken.getCurrentAccessToken()!= null)
            Log.d( TAG,AccessToken.getCurrentAccessToken().toString() );
    }

    // Login :
    public void attemptLogin(){
        et_User = findViewById( R.id.ed_user );
        et_Pass = findViewById( R.id.ed_pass );

        final String emailPhone = et_User.getText().toString().trim();
        String password = et_Pass.getText().toString().trim();

        boolean isValid = isValidRequest(emailPhone,password);
        if(!isValid)
            return;

        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getUserService()
                .login( emailPhone,password );
        call.enqueue( new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.code() == 200){
                    LoginResponse body = response.body();
                    TokenInfo token = new TokenInfo(body.getUserId(),emailPhone,body.getToken());
                    userId = body.getUserId();
                    TOKEN = token.getToken();
                    Log.i("Login",TOKEN);
                    Toast.makeText( LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                    SharedToken obj = new SharedToken(LoginActivity.this);
                    obj.saveToken( LoginActivity.this,token );

                    Intent intent = new Intent( LoginActivity.this,MainActivity.class );
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity( intent );
                    LoginActivity.this.finish();
                }
                else{
                    Toast.makeText( LoginActivity.this, "Login Failed, try again !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d(TAG,"Error Login");
            }
        } );

    }

    private boolean isValidRequest(String emailPhone, String password) {

        if (!emailPhone.contains( "@" )){
            et_User.setError( "User or Phone number is invalid" );
            et_User.requestFocus();
            return false;
        }
        else if(password.length() < 4){
            et_Pass.setError( "Password must be at least 5 characters" );
            et_Pass.requestFocus();
            return false;
        }
        else
            return true;
    }

}