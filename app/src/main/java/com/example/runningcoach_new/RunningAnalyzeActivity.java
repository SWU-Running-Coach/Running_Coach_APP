package com.example.runningcoach_new;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class RunningAnalyzeActivity extends AppCompatActivity {

    private Classifier classifier;
    private MoveNet movenet;

//    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_analyze);

//        //이미지 시각화 위해 잠시 사용, 추후 삭제
//        setContentView(R.layout.bitmap);
//        imageView = findViewById(R.id.imageView_bitmap);

        //동영상 프레임 분할을 위한 테스트(임시), 추후 삭제
        // 비디오 Uri 생성
        Uri videoUri = Uri.parse("assets://videorun.mp4");
        // VideoFrameExtractor 인스턴스 생성
        VideoFrameExtractor frameExtractor = new VideoFrameExtractor();
        // extractFrames 메서드 호출
        VideoFrameExtractor.extractFrames(videoUri);


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
            Bitmap image = loadImageFromAssets("ok_data108.jpg");
            String result = classifier.classifyImage(image);

            System.out.println(result);

            // 분류 결과 처리

            if (result == "OK")
            {
                //1. openpose를 사용하여 키 포인트 추출하기
                movenet = new MoveNet();
                Bitmap bitmap = movenet.MoveNetClass(assetManager, "lite-model_movenet_singlepose_thunder_3.tflite",
                              image.getWidth(), image.getHeight(), image);

//                //이미지 시각화 위해 잠시 사용, 추후 삭제
//                if (imageView != null) {
//                    imageView.setImageBitmap(bitmap);
//                }

                //2. 추출된 키포인트로 각도 계산하기
                //movenet.MoveNetClass 실행시, 각도 계산까지 완료
                //이후 각도를 통해 자세 분류 예정
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //달리기 피드백 화면으로 이동
        ImageButton btnGofeedback = (ImageButton) findViewById(R.id.btnGofeedback);
        btnGofeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RunningFeedbackActivity.class);
                //각도 데이터를 intent로 전달
//                intent.putExtra("name",@@@@.getText().toString());
                startActivity(intent);
            }
        });

    }


    public Bitmap loadImageFromAssets(String fileName) throws IOException {
        InputStream inputStream = getAssets().open(fileName);
        return BitmapFactory.decodeStream(inputStream);
    }

}