package com.example.runningcoach_new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.runningcoach_new.network.ServiceApi;
import com.example.runningcoach_new.network.RetrofitClient;
import com.example.runningcoach_new.data.LoginResponse;
import com.example.runningcoach_new.data.LoginData;
import com.example.runningcoach_new.R;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private EditText emailText;
    private EditText passwordText;
    private Button goRegister;
    private Button goFindid;
    private ImageButton btnLogin;
    private ServiceApi service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //뒤로가기 버튼
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        Intent intent=getIntent();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        goRegister = (Button) findViewById(R.id.goRegister);
        goFindid = (Button) findViewById(R.id.goFindid);
        btnLogin = (ImageButton) findViewById(R.id.btnLogin);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
            }
        });

        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });


    }
}