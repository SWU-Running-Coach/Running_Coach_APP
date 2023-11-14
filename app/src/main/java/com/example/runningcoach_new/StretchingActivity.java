package com.example.runningcoach_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class StretchingActivity extends AppCompatActivity {
    CameraSurfaceView surfaceView;

    private Interpreter interpreter;
    private ImagePreprocessor imageProcessor;

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

        // ViewTreeObserver를 사용하여 surfaceView 크기 측정 대기
        ViewTreeObserver viewTreeObserver = surfaceView.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int width = surfaceView.getWidth();
                int height = surfaceView.getHeight();

                // 크기가 측정되면 이 리스너를 제거
                surfaceView.getViewTreeObserver().removeOnPreDrawListener(this);

                Log.e("test", "width : " + width + " height : " +height);

                // 여기서 이미지 처리 또는 다른 작업 수행


        //모델

        AssetManager assetManager = getAssets();

        MappedByteBuffer modelBuffer = null;

        try {
            modelBuffer = loadModelFile(assetManager, "lite-model_movenet_singlepose_thunder_3.tflite");
        } catch (IOException e) {
            e.printStackTrace();
        }

        interpreter = new Interpreter(modelBuffer);

        // 이미지 전처리
        Bitmap bitmap = Bitmap.createBitmap(surfaceView.getWidth(), surfaceView.getHeight(), Bitmap.Config.ARGB_8888);
        imageProcessor = new ImagePreprocessor(bitmap.getWidth(), bitmap.getHeight());

        Bitmap result = runInference(bitmap);

        //result 굳이 사용하지 말기
                return true;
            }
        });
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

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public Bitmap runInference(Bitmap bitmap) {
        Bitmap newbitmap = ImageUtils.resizeBitmap(bitmap, 256, 256);

        // 이미지 전처리
        TensorImage inputImage = imageProcessor.processImage(newbitmap);

        // 추론 실행
        TensorBuffer outputBuffer = runInference(inputImage);

        // 결과 처리
        Bitmap outBitmap = processOutput(outputBuffer, newbitmap);

        return outBitmap;

    }

    private TensorBuffer runInference(TensorImage inputImage) {

        // 입력과 출력 텐서 버퍼 생성
        TensorBuffer inputBuffer = TensorBuffer.createFixedSize(interpreter.getInputTensor(0).shape(), interpreter.getInputTensor(0).dataType());
        TensorBuffer outputBuffer = TensorBuffer.createFixedSize(interpreter.getOutputTensor(0).shape(), interpreter.getOutputTensor(0).dataType());

        // 입력 데이터 설정
        inputBuffer.loadBuffer(inputImage.getBuffer());

        // 추론 실행
        interpreter.run(inputBuffer.getBuffer(), outputBuffer.getBuffer());

        return outputBuffer;
    }

    private Bitmap processOutput(TensorBuffer outputBuffer, Bitmap bitmap) {

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10f);

        // Canvas 객체 생성하여 비트맵 이미지에 그리기
        Canvas canvas = new Canvas(bitmap);

        // 추론 결과에서 관절 포인트 추출
        float[] keypointArray = outputBuffer.getFloatArray();
        String[] jointName = {"nose", "left eye", "right eye", "left ear", "right ear",
                "left shoulder", "right shoulder", "left elbow", "right elbow", "left wrist",
                "right wrist", "left hip", "right hip", "left knee", "right knee",
                "left ankle", "right ankle"};

        int numKeypoints = outputBuffer.getShape()[2];

        // 관절 포인트를 담을 ArraryList 생성
        ArrayList<Joint> joints = new ArrayList<>();

        // 관절 포인트를 순회하며 선 그리기
        for (int i = 0; i < numKeypoints; i++) {
            // 관절 포인트의 x, y 좌표 추출
            float x = keypointArray[i * 3];
            float y = keypointArray[i * 3 + 1];

            // 이미지 좌표로 변환
            float imageX = x * bitmap.getHeight();
            float imageY = y * bitmap.getWidth();

            canvas.drawCircle(imageY, imageX, 5f, paint);

            //Joint 객체 추가
            joints.add(new Joint(i, jointName[i], x, y));
        }

        //스켈레톤 선 그리기

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2f);

        // left shoulder-left elbow(5, 7)
        drawLine(5, 7, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // left elbow-left wrist(7, 9)
        drawLine(7, 9, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // left shoulder-left hip(5, 11)
        drawLine(5, 11, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // left hip-left knee(11, 13)
        drawLine(11, 13, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // left knee-left ankle(13, 15)
        drawLine(13, 15, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // left shoulder-right shoulder(5, 6)
        drawLine(5, 6, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // right shoulder-right elbow(6, 8)
        drawLine(6, 8, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // right elbow-right wrist(8, 10)
        drawLine(8, 10, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // right shoulder-right hip(6, 12)
        drawLine(6, 12, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // right hip-right knee(12, 14)
        drawLine(12, 14, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // right knee-right ankle(14, 16)
        drawLine(14, 16, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        Log.e("test", "왼쪽 어깨 : " + joints.get(5).getX());
        return bitmap;
    }

    private void drawLine(int start, int end, float width, float height, Paint paint, ArrayList<Joint> joints, Canvas canvas)
    {
        float startX = joints.get(start).getX() * height;
        float startY = joints.get(start).getY() * width;
        float endX = joints.get(end).getX() * height;
        float endY = joints.get(end).getY() * width;

        canvas.drawLine(startY, startX, endY, endX, paint);
    }

}