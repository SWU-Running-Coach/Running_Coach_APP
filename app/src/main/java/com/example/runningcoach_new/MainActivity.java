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

    //네비게이션 하단바
    CalendarFragment calendarFragment;
    HomeFragment homeFragment;
    RunningFragment runningFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        //텐서플로우 모델 확인을 위해 잠시 사용
//        Intent intent = new Intent(getApplicationContext(), RunningAnalyzeActivity.class);
//        startActivity(intent);

        //네비게이션 하단바
        calendarFragment = new CalendarFragment();
        homeFragment = new HomeFragment();
        runningFragment = new RunningFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.tab_calendar) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, calendarFragment).commit();
                    return true;
                } else if (itemId == R.id.tab_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, homeFragment).commit();
                    return true;
                } else if (itemId == R.id.tab_running) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containers, runningFragment).commit();
                    return true;
                }
                return false;
            }
        });

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
        Button gojoin = (Button) findViewById(R.id.gojoinbtn);
        gojoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
    }
}