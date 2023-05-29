package com.example.runningcoach_new;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Classifier {
    private Interpreter tflite;

    public Classifier(AssetManager assetManager) {
        try {
            // 모델 파일 로드
            MappedByteBuffer model = loadModelFile(assetManager);

            // TensorFlow Lite 인터프리터 초기화
            Interpreter.Options options = new Interpreter.Options();
            tflite = new Interpreter(model, options);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String classifyImage(Bitmap image) {
        // 이미지 전처리
        ByteBuffer inputBuffer = preprocessImage(image);

        // 모델 추론
        float[][] output = new float[1][2];  // 분류 결과를 저장할 배열
        tflite.run(inputBuffer, output);

        // 분류 결과 분석
        float probability = output[0][0];  // 예측된 클래스 0의 확률
        if (probability >= 0.5) {
            return "OK";
        } else {
            return "NO";
        }
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd("model_unquant.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private ByteBuffer preprocessImage(Bitmap image) {
        int inputSize = 224;  // 모델에 맞는 입력 이미지 크기로 수정

        // 입력 이미지를 모델에 맞는 크기로 변환 및 전처리 작업 수행
        Bitmap resizedImage = Bitmap.createScaledBitmap(image, inputSize, inputSize, true);
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * 4);
        inputBuffer.order(ByteOrder.nativeOrder());

        int[] pixels = new int[inputSize * inputSize];
        resizedImage.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize);

        for (int pixelValue : pixels) {
            // 이미지 전처리 작업을 수행하여 inputBuffer에 입력 데이터 저장
            inputBuffer.putFloat(((pixelValue >> 16) & 0xFF) / 255.0f);
            inputBuffer.putFloat(((pixelValue >> 8) & 0xFF) / 255.0f);
            inputBuffer.putFloat((pixelValue & 0xFF) / 255.0f);
        }

        return inputBuffer;
    }
}