package com.example.runningcoach_new;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.ImageView;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

public class MoveNet{
    private Interpreter interpreter;
    private ImagePreprocessor imageProcessor;

    public MoveNet() {

    }

    public Bitmap MoveNetClass(AssetManager assetManager, String modelPath, int inputImageWidth, int inputImageHeight, Bitmap bitmap) throws IOException {
        // 모델 로드
        MappedByteBuffer modelBuffer = loadModelFile(assetManager, modelPath);
        interpreter = new Interpreter(modelBuffer);

        // 이미지 전처리
        imageProcessor = new ImagePreprocessor(inputImageWidth, inputImageHeight);

        Bitmap finish = runInference(bitmap);
        return finish;
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

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
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

        int numKeypoints = outputBuffer.getShape()[2];


        // 관절 포인트를 순회하며 선 그리기
        for (int i = 0; i < numKeypoints; i++) {
            // 관절 포인트의 x, y 좌표 추출
            float x = keypointArray[i * 3];
            float y = keypointArray[i * 3 + 1];

            // 이미지 좌표로 변환
            float imageX = x * bitmap.getHeight();
            float imageY = y * bitmap.getWidth();

            canvas.drawCircle(imageY, imageX, 5f, paint);
        }

        return bitmap;
    }

}
