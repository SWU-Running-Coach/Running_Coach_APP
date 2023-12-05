package com.example.runningcoach_new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.runningcoach_new.data.FeedbackData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {
    private TextView monthText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //뒤로가기 버튼
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        Intent intent = getIntent();
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

        //오늘 날짜에 컬러 표시
        MaterialCalendarView materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());

        //@@월 기록 텍스트
        monthText = findViewById(R.id.monthTxt);
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String formattedDate = dateFormat.format(currentDate);
        monthText.setText(formattedDate);

        //===== 테스트를 위한 더미 데이터 생성 ===================
        ArrayList<FeedbackResult> feedbackResults = new ArrayList<>();

        String resultDate = intent.getStringExtra("date");
        float resultLegAngle = intent.getFloatExtra("LegAngle", 0.0f); // 기본값은 0.0f로 설정하거나 적절한 기본값으로 변경하세요.
        String resultUpperBodyAngle = intent.getStringExtra("LegAngleTxt");
        float resultLegAngleTxt = intent.getFloatExtra("UpdderBodyAngle", 0.0f); // 기본값은 0.0f로 설정하거나 적절한 기본값으로 변경하세요.
        String resultUpperBodyAngleTxt = intent.getStringExtra("UpdderBodyAngleTxt");

        FeedbackResult feedbackResult = new FeedbackResult(
                resultDate,
                resultLegAngle,
                resultLegAngleTxt,
                resultUpperBodyAngle,
                resultUpperBodyAngleTxt
        );
        feedbackResults.add(feedbackResult);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);  // LayoutManager 설정

        CustomAdapter customAdapter = new CustomAdapter(feedbackResults);
        recyclerView.setAdapter(customAdapter); // 어댑터 설정




    }
}