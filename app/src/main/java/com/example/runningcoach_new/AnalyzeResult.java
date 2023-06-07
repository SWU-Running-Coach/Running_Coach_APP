package com.example.runningcoach_new;

import android.graphics.Bitmap;

public class AnalyzeResult {
    private Bitmap bitmap;
    private double legAngle;
    private double uppderBodyAngle;

    public AnalyzeResult() {

    }

    public AnalyzeResult(Bitmap bitmap, double legAngle, double uppderBodyAngle) {
        this.bitmap = bitmap;
        this.legAngle = legAngle;
        this.uppderBodyAngle = uppderBodyAngle;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public double getLegAngle() {
        return legAngle;
    }

    public void setLegAngle(double legAngle) {
        this.legAngle = legAngle;
    }

    public double getUppderBodyAngle() {
        return uppderBodyAngle;
    }

    public void setUppderBodyAngle(double uppderBodyAngle) {
        this.uppderBodyAngle = uppderBodyAngle;
    }
}
