package com.example.runningcoach_new;

import java.util.ArrayList;

public class FeedbackResult {
    private String resultDate;
    private float resultLegAngle;
    private float resultUpperBodyAngle;
    private String resultLegAngleTxt;
    private String resultUpperBodyAngleTxt;


    public FeedbackResult(String resultDate, float resultLegAngle, float resultUpperBodyAngle, String resultLegAngleTxt, String resultUpperBodyAngleTxt) {
        this.resultDate = resultDate;
        this.resultLegAngle = resultLegAngle;
        this.resultUpperBodyAngle = resultUpperBodyAngle;
        this.resultLegAngleTxt = resultLegAngleTxt;
        this.resultUpperBodyAngleTxt = resultUpperBodyAngleTxt;
    }

    public String getResultDate() {
        return resultDate;
    }
    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    public float getResultLegAngle() {
        return resultLegAngle;
    }
    public void setResultLegAngle(float resultLegAngle) {
        this.resultLegAngle = resultLegAngle;
    }

    public float getResultUpperBodyAngle() {
        return resultUpperBodyAngle;
    }
    public void setResultUpperBodyAngle(float resultUpperBodyAngle) {
        this.resultUpperBodyAngle = resultUpperBodyAngle;
    }

    public String getResultLegAngleTxt() {
        return resultLegAngleTxt;
    }
    public void setResultLegAngleTxt(String resultLegAngleTxt) {
        this.resultLegAngleTxt = resultLegAngleTxt;
    }

    public String getResultUpperBodyAngleTxt() {
        return resultUpperBodyAngleTxt;
    }
    public void setResultUpperBodyAngleTxt(String resultUpperBodyAngleTxt) {
        this.resultUpperBodyAngleTxt = resultUpperBodyAngleTxt;
    }

}
