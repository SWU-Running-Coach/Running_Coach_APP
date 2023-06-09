package com.example.runningcoach_new;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RunningFeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_feedback);

        //뒤로가기 버튼
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        Intent intent=getIntent();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //착지하는 무릎각도 text
        //intent로 return값 받아오기
        //피드백 제시
        TextView textView2 = (TextView) findViewById(R.id.textView2) ;
//        if (각도<150){
//            textView2.setText("150˚~160˚가 되도록 무릎을 더 펴서 달리세요") ;
//        } else if (각도>=150 && 각도<=160) {
//            textView2.setText("무릎의 각도가 150˚~160˚ 사이에 있어 좋은 자세입니다.") ;
//        } else if (각도<160) {
//            textView2.setText("150˚~160˚가 되도록 무릎을 더 굽혀서 달리세요") ;
//        }
        textView2.setText("무릎각도에 따른 피드백") ;

        //상체각도 text
        //intent로 return값 받아오기
        //피드백 제시
        TextView textView3 = (TextView) findViewById(R.id.textView3) ;
//        if (각도<4){
//            textView2.setText("4˚~10˚가 되도록 상체를 더 펴서 달리세요") ;
//        } else if (각도>=4 && 각도<=10) {
//            textView2.setText("상체의 각도가 4˚~10˚ 사이에 있어 좋은 자세입니다.") ;
//        } else if (각도<10) {
//            textView2.setText("4˚~10˚가 되도록 상체를 더 굽혀서 달리세요") ;
//        }
        textView3.setText("상체각도에 따른 피드백") ;



    }
}