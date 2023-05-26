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

        //버튼
        Button imageButton = (Button) findViewById(R.id.goVideoUpload);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VideoUploadActivity.class);
                startActivity(intent);
            }
        });
    }
}