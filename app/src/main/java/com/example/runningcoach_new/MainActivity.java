package com.example.runningcoach_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //텐서플로우 모델 확인을 위해 잠시 사용
//        Intent intent = new Intent(getApplicationContext(), RunningAnalyzeActivity.class);
//        startActivity(intent);

        //뒤로가기 버튼
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        Intent intent = getIntent();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //스트레칭 화면 테스트
        ImageButton goStretching = (ImageButton) findViewById(R.id.gostretchingbtn);
        goStretching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StretchingActivity.class);
                startActivity(intent);
            }
        });

        //달리기 분석 버튼 테스트
        ImageButton goVideo = (ImageButton) findViewById(R.id.goVideoUpload);
        goVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VideoUploadActivity.class);
                startActivity(intent);
            }
        });

        //캘린더 화면 테스트
        ImageButton goCalendar = (ImageButton) findViewById(R.id.gocalendarbtn);
        goCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        //마이페이지 화면 테스트
        ImageButton goMypage = (ImageButton) findViewById(R.id.gomypagebtn);
        goMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
            }
        });

        //로그인 화면 테스트
        Button goLogin = (Button) findViewById(R.id.gologinbtn);
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        //회원가입 화면 테스트
        Button goJoin = (Button) findViewById(R.id.gojoinbtn);
        goJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });




    }
}