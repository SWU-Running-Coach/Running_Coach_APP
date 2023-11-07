package com.example.runningcoach_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class StretchingActivity extends AppCompatActivity {
    CameraSurfaceView surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stretching);

        surfaceView=findViewById(R.id.surfaceView);

        //뒤로가기 버튼
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        Intent intent = getIntent();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 카메라 권한 요청
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
        }

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