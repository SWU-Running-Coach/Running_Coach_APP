package com.example.runningcoach_new;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VideoFrameExtractor {
    private static final long FRAME_INTERVAL = 500; // 0.5초 간격
    private static final int RESIZED_WIDTH = 640; // 리사이즈할 가로 크기
    private static final int RESIZED_HEIGHT = 480; // 리사이즈할 세로 크기


//    public static ArrayList<VideoFrame> extractFrames(Context context, AssetManager assetManager) {
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        AssetFileDescriptor assetFileDescriptor;
//        try {
//            assetFileDescriptor = assetManager.openFd("videonew.mp4");
//            retriever.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
//            assetFileDescriptor.close();
//        } catch (IOException e){
//            e.printStackTrace();
//        }

    public static ArrayList<VideoFrame> extractFrames(Context context, AssetManager assetManager, Uri fileUri) {
        // fileUri를 사용하여 비디오 파일을 처리
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        AssetFileDescriptor assetFileDescriptor;
        try {
            if (context == null)
                System.out.println("context");

            if (fileUri == null)
                System.out.println("fileuri");
            retriever.setDataSource(context, fileUri); // fileUri를 사용하여 데이터 소스 설정
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("비디오 파일을 열 수 없습니다.");
        }


        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        if (duration == null) {
            // duration이 null이면 비디오 파일의 기간을 가져오지 못한 경우이므로 오류 처리를 수행합니다.
            System.out.println("비디오 파일의 기간을 가져올 수 없습니다.");
        }


        long videoDuration = Long.parseLong(duration);

        long currentTime = 0;

        ArrayList<VideoFrame> videoFrames = new ArrayList<>();

        while (currentTime < videoDuration) {
            Bitmap frame = retriever.getFrameAtTime(currentTime * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

            // TODO: 필요한 대로 프레임 이미지를 처리합니다.
            // 이미지 리사이즈
            Bitmap resizedFrame = ImageUtils.resizeBitmap(frame, RESIZED_WIDTH, RESIZED_HEIGHT);

            // 프레임 이미지를 저장할 디렉토리 경로
            String directoryPath = context.getExternalFilesDir(null).getAbsolutePath();;

            // 프레임 이미지 파일명 생성
            String fileName = "frame_" + currentTime + ".jpg";

            // 프레임 이미지 파일 생성
            File file = new File(directoryPath, fileName);

            // 프레임 이미지 저장
            if (ImageUtils.saveBitmapToFile(resizedFrame, file.getAbsolutePath())) {
                videoFrames.add(new VideoFrame(file));
                System.out.println("프레임 파일이 성공적으로 저장되었습니다.");
            } else {
                System.out.println("프레임 파일 저장에 실패했습니다.");
            }
            // 아래의 코드는 파일명을 출력하는 예시입니다.
            System.out.println("프레임 파일명: " + file.getAbsolutePath());

            currentTime += FRAME_INTERVAL;
        }

        try {
            retriever.release();
        } catch (IOException e) {
            System.out.println (e.toString());
        }


        return videoFrames;
    }

}
