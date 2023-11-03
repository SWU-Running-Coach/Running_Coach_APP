package com.example.runningcoach_new;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{
    SurfaceHolder holder;
    Camera camera = null;
    public CameraSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        //초기화를 위한 메소드
        holder = getHolder();
        holder.addCallback(this);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        //만들어지는시점
        camera  = Camera.open();//카메라 객체 참조
        try{
            camera.setPreviewDisplay(holder);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //변경
        camera.startPreview(); //렌즈로 부터 들어오는 영상을 뿌려줌
        camera.stopPreview();
        camera.setDisplayOrientation(90);//카메라 미리보기 오른쪽 으로 90 도회전
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        //소멸
        camera.stopPreview();//미리보기중지
        camera.release();
        camera = null;
    }
}
