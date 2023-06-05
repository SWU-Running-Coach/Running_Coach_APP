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
import java.util.ArrayList;
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


        calculateJointAngle(joints);

        return bitmap;
    }

    private void calculateJointAngle(ArrayList<Joint> joints) {
        // 각 관절 포인트의 좌표를 담을 배열 생성
        //left Shoulder = 5, right Shoulder = 6, left hip = 11, right hip = 12
        //left knee = 13, right knee = 14, left ankle = 15, right ankle = 16

        double legAngle = calculateLegAngle(joints.get(11), joints.get(12), joints.get(13), joints.get(14), joints.get(15), joints.get(16));

        System.out.println("legAngle = " + legAngle);

        double uppderBodyAngle = calculateUppderBodyAngle(joints.get(5), joints.get(6), joints.get(11), joints.get(12), joints.get(13), joints.get(14));

        System.out.println("상체 = " + uppderBodyAngle);

    }

    private double calculateLegAngle(Joint leftHip, Joint rightHip, Joint leftKnee, Joint rightKnee, Joint leftAnkle, Joint rightAnkle) {
        //디딛는 발이 왼발인 경우
        if (leftKnee.getX() < rightKnee.getX()) {
            float A[] = {leftKnee.getX(), leftKnee.getY()};
            float B[] = {leftHip.getX(), leftHip.getY()};
            float C[] = {leftAnkle.getX(), leftAnkle.getY()};

            // 각도 계산
            double rad = Math.atan2(C[1] - A[1], C[0] - A[0]) - Math.atan2(B[1] - A[1], B[0] - A[0]);
            double deg = rad * (180 / Math.PI);

            System.out.println("left");
            if (deg >= -180 && deg <= 180) {
                deg = Math.abs(deg);
                System.out.println(deg);
                return deg;
            } else {
                deg = 360 - Math.abs(deg);
                deg = Math.abs(deg);
                System.out.println(deg);
                return deg;
            }

        }
        else {
            float A[] = {rightKnee.getX(), rightKnee.getY()};
            float B[] = {rightHip.getX(), rightHip.getY()};
            float C[] = {rightAnkle.getX(), rightAnkle.getY()};

            // 각도 계산
            double rad = Math.atan2(C[1] - A[1], C[0] - A[0]) - Math.atan2(B[1] - A[1], B[0] - A[0]);
            double deg = rad * (180 / Math.PI);

            System.out.println("right");
            if (deg >= -180 && deg <= 180) {
                deg = Math.abs(deg);
                System.out.println(deg);
                return deg;
            } else {
                deg = 360 - Math.abs(deg);
                deg = Math.abs(deg);
                System.out.println(deg);
                return deg;
            }
        }
    }

    private double calculateUppderBodyAngle(Joint leftShoulder, Joint rightShoulder, Joint leftHip, Joint rightHip, Joint leftKnee, Joint rightKnee) {
        //디딛는 발이 왼발인 경우
        if (leftKnee.getX() < rightKnee.getX()) {
            float A[] = {rightShoulder.getX(), rightShoulder.getY()};
            float B[] = {rightHip.getX(), rightHip.getY()};

            // 각 좌표간의 차이 계산
            double delta_x = B[0] - A[0];
            double delta_y = B[1] - A[1];

            // 각도 계산
            double angle = Math.toDegrees(Math.atan2(Math.abs(delta_y), Math.abs(delta_x)));
            angle = Math.abs(angle);

            System.out.println("right로 계산");
            return angle;
        }
        else {
            float A[] = {leftShoulder.getX(), leftShoulder.getY()};
            float B[] = {leftHip.getX(), leftHip.getY()};

            // 각 좌표간의 차이 계산
            double delta_x = B[0] - A[0];
            double delta_y = B[1] - A[1];

            // 각도 계산
            double angle = Math.toDegrees(Math.atan2(Math.abs(delta_y), Math.abs(delta_x)));
            angle = Math.abs(angle);

            System.out.println("left로 계산");
            return angle;
        }

    }
}
