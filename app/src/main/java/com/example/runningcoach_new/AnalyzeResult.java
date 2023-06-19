package com.example.runningcoach_new;

import android.graphics.Bitmap;
import java.util.ArrayList;

public class AnalyzeResult {
    private Bitmap bitmap;
    private double legAngle;
    private double uppderBodyAngle;
    private ArrayList<Joint> joints;


    public AnalyzeResult() {

    }

    public AnalyzeResult(Bitmap bitmap, double legAngle, double uppderBodyAngle, ArrayList<Joint> joints) {
        this.bitmap = bitmap;
        this.legAngle = legAngle;
        this.uppderBodyAngle = uppderBodyAngle;
        this.joints = joints;
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

    public ArrayList<Joint> getJoints() {
        return joints;
    }
    public void setJoints(ArrayList<Joint> joints) {
        this.joints = joints;
    }

}
