package com.example.runningcoach_new;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.io.File;
import java.io.IOException;

public class VideoFrameExtractor {
    private static final long FRAME_INTERVAL = 500; // 0.5초 간격
    private static final int RESIZED_WIDTH = 640; // 리사이즈할 가로 크기
    private static final int RESIZED_HEIGHT = 480; // 리사이즈할 세로 크기

    public static void extractFrames(Context context, AssetManager assetManager) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        AssetFileDescriptor assetFileDescriptor;
        try {
            assetFileDescriptor = assetManager.openFd("videorun.mp4");
            retriever.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            assetFileDescriptor.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);


        long videoDuration = Long.parseLong(duration);

        long currentTime = 0;

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


    }

}
