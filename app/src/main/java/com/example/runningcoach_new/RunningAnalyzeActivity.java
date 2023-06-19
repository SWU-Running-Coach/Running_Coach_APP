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
import java.util.AbstractList;
import java.util.ArrayList;

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
        // VideoFrameExtractor 인스턴스 생성
        VideoFrameExtractor frameExtractor = new VideoFrameExtractor();
        // extractFrames 메서드 호출
        AssetManager manager = getAssets();
        ArrayList<VideoFrame> videoFrames =VideoFrameExtractor.extractFrames(getApplicationContext(), manager);

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

        ArrayList<VideoFrame> ok_videoFrames = new ArrayList<>();

        int i = 0;
        while (i< videoFrames.size()) {
            // 이미지 파일 로드 및 분류 작업 수행
            Bitmap image = BitmapFactory.decodeFile(videoFrames.get(i).getFile().getAbsolutePath());
            String result = classifier.classifyImage(image);

            if (result == "OK")
                ok_videoFrames.add(videoFrames.get(i));

            i++;
        }

        movenet = new MoveNet();
        ArrayList<AnalyzeResult> analyzeResult = null;
        try {
            analyzeResult = movenet.MoveNetClass(assetManager, "lite-model_movenet_singlepose_thunder_3.tflite", ok_videoFrames);

            i = 0;
            while (i < analyzeResult.size())
            {
                System.out.println(i + "번째 다리 각도 : " + analyzeResult.get(i).getLegAngle());
                System.out.println(i + "번째 상체 각도 : " + analyzeResult.get(i).getUppderBodyAngle());
                i++;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//                //이미지 시각화 위해 잠시 사용, 추후 삭제
//                if (imageView != null) {
//                    imageView.setImageBitmap(analyzeResult.getBitmap());
//                    System.out.println("다리 각도 : " + analyzeResult.getLegAngle());
//                    System.out.println("상체 각도 : " + analyzeResult.getUppderBodyAngle());
//                }



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