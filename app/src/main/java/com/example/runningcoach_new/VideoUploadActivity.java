package com.example.runningcoach_new;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;
import android.net.Uri;
import android.media.MediaMetadataRetriever;
import android.graphics.Bitmap;




import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class VideoUploadActivity extends AppCompatActivity {

    VideoView uploadVideoView;

    Intent intenturi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);

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

        //업로드 버튼, 여기 수정해야함
        uploadVideoView = findViewById(R.id.uploadVideoView);
        ImageButton btnVideoupload = (ImageButton) findViewById(R.id.btnVideoupload);
        btnVideoupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //동영상 선택 누르면 실행됨 동영상 고를 갤러리 오픈
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 101);
            }
        });

        //


        //달리기 분석 화면으로 이동
        ImageButton btnGoanalyze = (ImageButton) findViewById(R.id.btnGoanalyze);
        btnGoanalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent = new Intent(getApplicationContext(), RunningAnalyzeActivity.class);
                startActivity(intenturi);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                uploadVideoView.setVideoPath(String.valueOf(fileUri));    // 선택한 비디오 경로 비디오뷰에 셋
                uploadVideoView.start();  // 비디오뷰 시작

                 intenturi = new Intent(getApplicationContext(), RunningAnalyzeActivity.class);
//
                System.out.println("2222 : " + fileUri);
                intenturi.setData(fileUri);

            }
        }
    }


}

