package com.example.runningcoach_new;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RunningFeedbackActivity extends AppCompatActivity {

    //현재 날짜 띄우기
    private TextView dateText;

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

        // 레이아웃에서 날짜를 표시할 텍스트뷰 가져오기
        dateText = findViewById(R.id.dateText);

        // 현재 날짜 가져오기
        Date currentDate = new Date();

        // 날짜 형식 설정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // 형식화된 날짜 문자열 생성
        String formattedDate = dateFormat.format(currentDate);

        // 텍스트뷰에 날짜 설정
        dateText.setText(formattedDate);

        //worst 이미지 띄우기
        Bitmap receivedImage;
        ImageView feedbackview = findViewById(R.id.feedbackview);
        // Intent에서 이미지 데이터 가져오기
        if (intent != null) {
            receivedImage = intent.getParcelableExtra("image");
            feedbackview.setImageBitmap(receivedImage);
        }


        //intent로 return값 받아오기
        //Bitmap image = intentdata.getParcelableExtra("image");
        double legAngleDouble  = intent.getDoubleExtra("LegAngle", 0.0f);
        float legAngle = (float) legAngleDouble;
        double upperBodyAngleDouble = intent.getDoubleExtra("UpdderBodyAngle", 0.0f);
        float upperBodyAngle = (float) upperBodyAngleDouble;

        //하체각도 text
        //피드백 제시
        TextView txtKneeAngle = findViewById(R.id.txt_kneeangle);
        TextView textView2 = findViewById(R.id.textView2);
        txtKneeAngle.setText(legAngle + "˚");
        if (legAngle < 150) {
            textView2.setText("150˚~160˚가 되도록 무릎을 더 펴서 달리세요");
        } else if (legAngle >= 150 && legAngle <= 160) {
            textView2.setText("무릎의 각도가 150˚~160˚ 사이에 있어 좋은 자세입니다.");
        } else if (legAngle > 160) {
            textView2.setText("150˚~160˚가 되도록 무릎을 더 굽혀서 달리세요");
        }
        //textView2.setText("무릎각도에 따른 피드백");

        //상체각도 text
        // 피드백 제시
        TextView txtUpperAngle = findViewById(R.id.txt_upperangle);
        TextView textView3 = findViewById(R.id.textView3);
        txtUpperAngle.setText(upperBodyAngle + "˚");
        if (upperBodyAngle < 4) {
            textView3.setText("4˚~10˚가 되도록 상체를 더 펴서 달리세요");
        } else if (upperBodyAngle >= 4 && upperBodyAngle <= 10) {
            textView3.setText("상체의 각도가 4˚~10˚ 사이에 있어 좋은 자세입니다.");
        } else if (upperBodyAngle > 10) {
            textView3.setText("4˚~10˚가 되도록 상체를 더 굽혀서 달리세요");
        }
        //textView3.setText("상체각도에 따른 피드백");

        //케이던스 intent로 받기
        int receivedCadenceCount = getIntent().getIntExtra("cadenceCount", 0);
        // 케이던스 피드백 설정
        TextView txt_cadencefeedback = findViewById(R.id.txt_cadencefeedback);
        TextView txt_cadenceup = findViewById(R.id.txt_cadenceup);
        TextView txt_after = findViewById(R.id.txt_after);

        txt_after.setText(String.valueOf(receivedCadenceCount));

        //추후에 data 저장하면 코드 변경
        txt_cadencefeedback.setText("이전에 저장된 케이던스 데이터가 없습니다.");

        //5% 증가된 케이던스 수치 피드백
        int CadenceUp = (int) (receivedCadenceCount * 1.05); // 5% 증가된 수치 계산
        txt_cadenceup.setText("5% 향상된 수치는 " + CadenceUp + "입니다.");






    }
}