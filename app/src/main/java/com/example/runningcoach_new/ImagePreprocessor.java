package com.example.runningcoach_new;

import android.graphics.Bitmap;
        import android.graphics.Matrix;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;

public class ImagePreprocessor {
    private int inputImageWidth;
    private int inputImageHeight;

    public ImagePreprocessor(int inputImageWidth, int inputImageHeight) {
        this.inputImageWidth = inputImageWidth;
        this.inputImageHeight = inputImageHeight;
    }

    public TensorImage processImage(Bitmap bitmap) {
        // 이미지 정규화
        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(bitmap);
        tensorImage = normalizeImage(tensorImage);

        return tensorImage;
    }

    private TensorImage normalizeImage(TensorImage tensorImage) {
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                .add(new NormalizeOp(0.0f, 1.0f))
                .build();

        return imageProcessor.process(tensorImage);
    }

}
