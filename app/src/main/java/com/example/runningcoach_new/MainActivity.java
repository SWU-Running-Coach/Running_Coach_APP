package com.example.runningcoach_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //텐서플로우 모델 확인을 위해 잠시 사용
//        Intent intent = new Intent(getApplicationContext(), RunningAnalyzeActivity.class);
//        startActivity(intent);

        //비디오 업로드 버튼 테스트
        Button imageButton = (Button) findViewById(R.id.goVideoUpload);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VideoUploadActivity.class);
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

        //마이페이지 화면 테스트
        Button goMypage = (Button) findViewById(R.id.gomypagebtn);
        goMypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                startActivity(intent);
            }
        });

        //스트레칭 화면 테스트
        Button goStretching = (Button) findViewById(R.id.gostretchingbtn);
        goStretching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StretchingActivity.class);
                startActivity(intent);
            }
        });

        //캘린더 화면 테스트
        Button goCalendar = (Button) findViewById(R.id.gocalendarbtn);
        goCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });

    }
}