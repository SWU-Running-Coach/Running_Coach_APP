package com.example.runningcoach_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class StretchingActivity extends AppCompatActivity {
    CameraSurfaceView surfaceView;

    ImageView imageView;
    TextView textView;
    TextView countTxt;
    private int[] imageResources = {R.drawable.shoulder_l, R.drawable.shoulder_r, R.drawable.knee_l, R.drawable.knee_r, R.drawable.hip_l, R.drawable.hip_r, R.drawable.doublekick_l, R.drawable.doublekick_r,
    R.drawable.sidekick_l, R.drawable.sidekick_r};
    private String[] textResources = {"왼쪽 어깨 스트레칭", "오른쪽 어깨 스트레칭", "왼쪽 무릎 스트레칭", "오른쪽 무릎 스트레칭", "왼쪽 엉덩이 스트레칭", "오른쪽 엉덩이 스트레칭", "왼쪽 무릎 차기 스트레칭", "오른쪽 무릎 차기 스트레칭",
            "왼쪽 옆차기 스트레칭", "오른쪽 옆차기 스트레칭"};
    private String[] cnttextResources = {"15초 유지", "15초 유지", "10회", "10회", "10회", "10회", "10회", "10회",
            "10회", "10회"};

    private int currentImageIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stretching);

        surfaceView=findViewById(R.id.surfaceView);
        imageView= findViewById(R.id.imageView3);
        textView = findViewById(R.id.titletext);
        countTxt = findViewById(R.id.counttext);

        //뒤로가기 버튼
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        Intent intent = getIntent();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //홈 버튼
        ImageButton btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // 카메라 권한 요청
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        updateImage();
                    }
                });
            }
        }, 0, 15000); //15초마다 이미지 변경
    }


    private void updateImage() {
        // 이미지를 변경하고 다음 인덱스로 이동
        imageView.setImageResource(imageResources[currentImageIndex]);
        textView.setText(textResources[currentImageIndex]);
        countTxt.setText(cnttextResources[currentImageIndex]);
        currentImageIndex = (currentImageIndex + 1) % imageResources.length;
    }

    //카메라
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 101:
                if(grantResults.length > 0){
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "카메라 권한 사용자가 승인함",Toast.LENGTH_LONG).show();
                    }
                    else if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(this, "카메라 권한 사용자가 허용하지 않음.",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(this, "수신권한 부여받지 못함.",Toast.LENGTH_LONG).show();
                    }
                }
        }
    }

}