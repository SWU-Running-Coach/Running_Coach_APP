package com.example.runningcoach_new;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runningcoach_new.data.FeedbackData;
import com.example.runningcoach_new.network.RetrofitClient;
import com.example.runningcoach_new.network.ServiceApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RunningFeedbackActivity extends AppCompatActivity {

    //현재 날짜 띄우기
    private TextView dateText;
    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_feedback);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        //뒤로가기 버튼
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        Intent intent=getIntent();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //홈 버튼
        ImageButton btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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
            textView3.setText("4˚~10˚가 되도록 상체를 더 굽혀서 달리세요");
        } else if (upperBodyAngle >= 4 && upperBodyAngle <= 10) {
            textView3.setText("상체의 각도가 4˚~10˚ 사이에 있어 좋은 자세입니다.");
        } else if (upperBodyAngle > 10) {
            textView3.setText("4˚~10˚가 되도록 상체를 더 펴서 달리세요");
        }
        //textView3.setText("상체각도에 따른 피드백");

        //케이던스 intent로 받기
        int receivedCadenceCount = getIntent().getIntExtra("cadenceCount", 0);
        // 케이던스 피드백 설정
        TextView txt_cadencefeedback = findViewById(R.id.txt_cadencefeedback);
        TextView txt_cadenceup = findViewById(R.id.txt_cadenceup);
        TextView txt_after = findViewById(R.id.txt_after);
        TextView txt_cadencegoal = findViewById(R.id.txt_cadencegoal);

        txt_after.setText(String.valueOf(receivedCadenceCount));

        //추후에 data 저장하면 코드 변경
        txt_cadencefeedback.setText("이전에 저장된 케이던스 데이터가 없습니다.");

        //5% 증가된 케이던스 수치 피드백
        int CadenceUp = (int) (receivedCadenceCount * 1.05); // 5% 증가된 수치 계산
        txt_cadenceup.setText("5% 향상된 수치는 " + CadenceUp + "입니다.");
        txt_cadencegoal.setText("목표 케이던스까지 " + (CadenceUp-receivedCadenceCount) + "남았습니다.");

        //달리기 자세 평가
        // 피드백 제시
        ImageView runningScore = findViewById(R.id.score_image);
        TextView txtRunningScore = findViewById(R.id.txt_running_score);
        //나쁜점수
        if (upperBodyAngle < 4 && legAngle < 150) {
            runningScore.setImageResource(R.drawable.badscore);
            txtRunningScore.setText("분발하세요!");
        } else if (upperBodyAngle < 4 && legAngle > 160) {
            runningScore.setImageResource(R.drawable.badscore);
            txtRunningScore.setText("분발하세요!");
        } else if (upperBodyAngle > 10 && legAngle < 150) {
            runningScore.setImageResource(R.drawable.badscore);
            txtRunningScore.setText("분발하세요!");
        }else if (upperBodyAngle > 10 && legAngle > 160) {
            runningScore.setImageResource(R.drawable.badscore);
            txtRunningScore.setText("분발하세요!");
        } else if (upperBodyAngle >= 4 && upperBodyAngle <= 10 && legAngle < 150) {
            runningScore.setImageResource(R.drawable.neutralscore);
            txtRunningScore.setText("약간 아쉽군요!");
        } else if (upperBodyAngle >= 4 && upperBodyAngle <= 10 && legAngle > 160) {
            runningScore.setImageResource(R.drawable.neutralscore);
            txtRunningScore.setText("약간 아쉽군요!");
        } else if (legAngle >= 150 && legAngle <= 160 && upperBodyAngle < 4) {
            runningScore.setImageResource(R.drawable.neutralscore);
            txtRunningScore.setText("약간 아쉽군요!");
        } else if (legAngle >= 150 && legAngle <= 160 && upperBodyAngle > 10) {
            runningScore.setImageResource(R.drawable.neutralscore);
            txtRunningScore.setText("약간 아쉽군요!");
        } else if (legAngle >= 150 && legAngle <= 160 && upperBodyAngle >= 4 && upperBodyAngle <= 10) {
            runningScore.setImageResource(R.drawable.goodscore);
            txtRunningScore.setText("완벽합니다!");
        }


        //저장하기
        ImageButton saveData = (ImageButton) findViewById(R.id.btnSave);
        //intent로 데이터 전달 (임시)
        float resultLegAngle = legAngle;
        float resultUpperBodyAngle = upperBodyAngle;
        String resultDate = dateText.getText().toString();
        String resultLegAngleTxt = textView2.getText().toString();
        String resultUpperBodyAngleTxt = textView3.getText().toString();
        ArrayList<FeedbackResult> feedbackResults = new ArrayList<>();

        saveData.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                //서버 post
//                FeedbackData fbdata = new FeedbackData();
//                fbdata.setText(dateText.getText().toString());
//                service.postData(fbdata).enqueue(new Callback<FeedbackData>() {
//                    @Override
//                    public void onResponse(Call<FeedbackData> call, Response<FeedbackData> response) {
//                        if(response.isSuccessful()){
//                            Toast.makeText(RunningFeedbackActivity.this, "저장 성공", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<FeedbackData> call, Throwable t) {
//                    }
//
//                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
//                startActivity(intent);
//                });

                //intent로 데이터 전달(임시)
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                //피드백 데이터를 Calendar액티비티로 intent 전달
                intent.putExtra("date", resultDate);
                intent.putExtra("LegAngle", resultLegAngle);
                intent.putExtra("UpdderBodyAngle", resultUpperBodyAngle);
                intent.putExtra("LegAngleTxt", resultLegAngleTxt);
                intent.putExtra("UpdderBodyAngleTxt", resultUpperBodyAngleTxt);
                startActivity(intent);

            }
        });






    }
}