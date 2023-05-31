package com.example.runningcoach_new;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class RunningAnalyzeActivity extends AppCompatActivity {

    private Classifier classifier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_analyze);

        //뒤로가기 버튼
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        Intent intent=getIntent();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // MyClassifier 인스턴스 생성
        AssetManager assetManager = getAssets();
        classifier = new Classifier(assetManager);

        // 이미지 파일 로드 및 분류 작업 수행
        try {
            Bitmap image = loadImageFromAssets("no_img.jpg");
            String result = classifier.classifyImage(image);

            System.out.println(result);

            // 분류 결과 처리

//            if (result == "OK")
//            {
//                //1. openpose를 사용하여 키 포인트 추출하기
//                //2. 추출된 키포인트로 각도 계산하기
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //달리기 피드백 화면으로 이동
        ImageButton btnGofeedback = (ImageButton) findViewById(R.id.btnGofeedback);
        btnGofeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RunningFeedbackActivity.class);
                startActivity(intent);
            }
        });

    }

    private Bitmap loadImageFromAssets(String fileName) throws IOException {
        InputStream inputStream = getAssets().open(fileName);
        return BitmapFactory.decodeStream(inputStream);
    }
}