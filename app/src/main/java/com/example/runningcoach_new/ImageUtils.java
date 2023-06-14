package com.example.runningcoach_new;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import java.io.File;
import java.io.FileOutputStream;

public class ImageUtils {
    public static Bitmap resizeBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // 리사이즈할 비율 계산
        float scaleWidth = ((float) targetWidth) / width;
        float scaleHeight = ((float) targetHeight) / height;

        // 매트릭스 설정
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // 리사이즈된 이미지 생성
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, String filePath) {
        File file = new File(filePath);
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
