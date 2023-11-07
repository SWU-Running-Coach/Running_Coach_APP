package com.example.runningcoach_new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.runningcoach_new.data.LoginResponse;
import com.example.runningcoach_new.data.RegisterData;
import com.example.runningcoach_new.data.RegisterResponse;
import com.example.runningcoach_new.network.RetrofitClient;
import com.example.runningcoach_new.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    private EditText emailText;
    private EditText nicknameText;
    private EditText pwText;
    private ImageButton btnRegister;
    private ServiceApi service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        nicknameText=(EditText) findViewById(R.id.nicknameText);
        pwText = (EditText) findViewById(R.id.pwText);
        btnRegister = (ImageButton) findViewById(R.id.btnRegister);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {
        emailText.setError(null);
        nicknameText.setError(null);
        pwText.setError(null);

        String email = emailText.getText().toString();
        String nickname = nicknameText.getText().toString();
        String password = pwText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 패스워드의 유효성 검사
        if (password.isEmpty()) {
            pwText.setError("비밀번호를 입력해주세요.");
            focusView = pwText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            pwText.setError("6자 이상의 비밀번호를 입력해주세요.");
            focusView = pwText;
            cancel = true;
        }

        // 이메일의 유효성 검사
        if (email.isEmpty()) {
            emailText.setError("이메일을 입력해주세요.");
            focusView = emailText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailText.setError("@를 포함한 유효한 이메일을 입력해주세요.");
            focusView = emailText;
            cancel = true;
        }

        // 이름의 유효성 검사
        if (nickname.isEmpty()) {
            nicknameText.setError("닉네임을 입력해주세요.");
            focusView = nicknameText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            startRegister(new RegisterData(email, nickname, password));
        }
    }

    private void startRegister(RegisterData data) {
        service.userRegister(data).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse result = response.body();
                Toast.makeText(Register.this, result.getMessage(), Toast.LENGTH_SHORT).show();

                if (result.getStatusCode() == 200) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(Register.this, "회원가입 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원가입 에러 발생", t.getMessage());
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
}