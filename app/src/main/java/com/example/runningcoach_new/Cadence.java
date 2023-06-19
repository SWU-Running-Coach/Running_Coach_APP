package com.example.runningcoach_new;

import android.content.res.AssetManager;
import java.io.IOException;
import java.util.ArrayList;

public class Cadence {
    private MoveNet movenet;
    private final int FRAME_RATE = 30;
    private final int DURATION_SECONDS = 15;

    public Cadence() {
        movenet = new MoveNet();
    }

    public int calculateCrossCount(AssetManager assetManager, ArrayList<VideoFrame> videoFrames) {
        ArrayList<AnalyzeResult> analyzeResults = new ArrayList<>();
        try {
            analyzeResults = movenet.MoveNetClass(assetManager, "lite-model_movenet_singlepose_thunder_3.tflite", videoFrames);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int crossCount = 0;
        int frameCount = 0;

        boolean isLeftFootCrossed = false;
        boolean isRightFootCrossed = false;

        for (AnalyzeResult result : analyzeResults) {
            ArrayList<Joint> joints = result.getJoints();
            Joint leftFoot = getJointByName(joints, "left ankle");
            Joint rightFoot = getJointByName(joints, "right ankle");

            if (leftFoot != null && rightFoot != null) {
                if (leftFoot.getX() < rightFoot.getX() && !isLeftFootCrossed) {
                    crossCount++;
                    isLeftFootCrossed = true;
                    isRightFootCrossed = false;
                } else if (rightFoot.getX() < leftFoot.getX() && !isRightFootCrossed) {
                    crossCount++;
                    isRightFootCrossed = true;
                    isLeftFootCrossed = false;
                }
            }

            frameCount++;
            if (frameCount >= FRAME_RATE * DURATION_SECONDS) {
                break;
            }
        }

        return crossCount;
    }

    private Joint getJointByName(ArrayList<Joint> joints, String name) {
        for (Joint joint : joints) {
            if (joint.getName().equals(name)) {
                return joint;
            }
        }
        return null;
    }

}
