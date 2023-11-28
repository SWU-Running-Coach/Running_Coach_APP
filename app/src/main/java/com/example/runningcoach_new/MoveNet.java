package com.example.runningcoach_new;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public ArrayList<AnalyzeResult> MoveNetClass(AssetManager assetManager, String modelPath, ArrayList<VideoFrame> videoFrames) throws IOException {
        // 모델 로드
        MappedByteBuffer modelBuffer = loadModelFile(assetManager, modelPath);
        interpreter = new Interpreter(modelBuffer);

        int i = 0;

        ArrayList<AnalyzeResult> result = new ArrayList<AnalyzeResult>();

        while (i < videoFrames.size())
        {
            Bitmap bitmap = BitmapFactory.decodeFile(videoFrames.get(i).getFile().getAbsolutePath());
            // 이미지 전처리
            imageProcessor = new ImagePreprocessor(bitmap.getWidth(), bitmap.getHeight());
            AnalyzeResult tmp = new AnalyzeResult();

            runInference(bitmap, tmp);

            result.add(tmp);
            i++;
        }


        return result;
    }

    public void runInference(Bitmap bitmap, AnalyzeResult result) {
        Bitmap newbitmap = ImageUtils.resizeBitmap(bitmap, 256, 256);

        // 이미지 전처리
        TensorImage inputImage = imageProcessor.processImage(newbitmap);

        // 추론 실행
        TensorBuffer outputBuffer = runInference(inputImage);

        // 결과 처리
        Bitmap outBitmap = processOutput(outputBuffer, newbitmap, result);

        result.setBitmap(outBitmap);

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

    private Bitmap processOutput(TensorBuffer outputBuffer, Bitmap bitmap, AnalyzeResult result) {

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
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

            canvas.drawCircle(imageY, imageX, 2f, paint);

            //Joint 객체 추가
            joints.add(new Joint(i, jointName[i], x, y));
            result.setJoints(joints);
        }

        //스켈레톤 선 그리기

        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2f);

        //디딛는 발이 왼발인 경우
        int flag = 1;
        if (joints.get(13).getX() < joints.get(14).getX())
            flag = 0;

        // left shoulder-left elbow(5, 7)
        drawLine(5, 7, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // left elbow-left wrist(7, 9)
        drawLine(7, 9, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        if (flag == 1)
            paint.setColor(Color.MAGENTA);
        // left shoulder-left hip(5, 11)
        drawLine(5, 11, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        if (flag == 1)
            paint.setColor(Color.GREEN);

        if (flag == 0)
            paint.setColor(Color.RED);
        // left hip-left knee(11, 13)
        drawLine(11, 13, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // left knee-left ankle(13, 15)
        drawLine(13, 15, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        if (flag == 0)
            paint.setColor(Color.GREEN);

        // left shoulder-right shoulder(5, 6)
        drawLine(5, 6, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // right shoulder-right elbow(6, 8)
        drawLine(6, 8, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // right elbow-right wrist(8, 10)
        drawLine(8, 10, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        if (flag == 0)
            paint.setColor(Color.MAGENTA);
        // right shoulder-right hip(6, 12)
        drawLine(6, 12, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        if (flag == 0)
            paint.setColor(Color.GREEN);

        if (flag == 1)
            paint.setColor(Color.RED);
        // right hip-right knee(12, 14)
        drawLine(12, 14, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        // right knee-right ankle(14, 16)
        drawLine(14, 16, bitmap.getWidth(), bitmap.getHeight(), paint, joints, canvas);

        if (flag == 1)
            paint.setColor(Color.GREEN);

        calculateJointAngle(joints, result);

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

    private void calculateJointAngle(ArrayList<Joint> joints, AnalyzeResult result) {
        // 각 관절 포인트의 좌표를 담을 배열 생성
        //left Shoulder = 5, right Shoulder = 6, left hip = 11, right hip = 12
        //left knee = 13, right knee = 14, left ankle = 15, right ankle = 16

        double legAngle = calculateLegAngle(joints.get(11), joints.get(12), joints.get(13), joints.get(14), joints.get(15), joints.get(16));
        double uppderBodyAngle = calculateUppderBodyAngle(joints.get(5), joints.get(6), joints.get(11), joints.get(12), joints.get(13), joints.get(14));

        result.setLegAngle(legAngle);
        result.setUppderBodyAngle(uppderBodyAngle);

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

            if (deg >= -180 && deg <= 180) {
                deg = Math.abs(deg);

                return deg;
            } else {
                deg = 360 - Math.abs(deg);
                deg = Math.abs(deg);

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

            if (deg >= -180 && deg <= 180) {
                deg = Math.abs(deg);

                return deg;
            } else {
                deg = 360 - Math.abs(deg);
                deg = Math.abs(deg);

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

            return angle;
        }
    }


}
