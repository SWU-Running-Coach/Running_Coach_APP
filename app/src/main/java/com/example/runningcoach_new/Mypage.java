package com.example.runningcoach_new;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.runningcoach_new.data.LoginData;
import com.example.runningcoach_new.data.LoginResponse;
import com.example.runningcoach_new.data.UserDeleteData;
import com.example.runningcoach_new.data.UserDeleteResponse;
import com.example.runningcoach_new.network.RetrofitClient;
import com.example.runningcoach_new.network.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Mypage extends AppCompatActivity {

    private TextView nicknameView;
    private TextView emailView;
    private ImageButton btnUserinfoChange;
    private ImageButton btnUserDelete;
    private ImageButton btnLogout;

    private ServiceApi service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        //뒤로가기 버튼
        ImageButton btnBack = (ImageButton) findViewById(R.id.btnBack);
        Intent intent=getIntent();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nicknameView = (TextView) findViewById(R.id.nicknameView);
        emailView = (TextView) findViewById(R.id.emailView);
        btnUserinfoChange = (ImageButton) findViewById(R.id.btnUserinfoChange);
        btnUserDelete = (ImageButton) findViewById(R.id.btnUserDelete);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);

        service = RetrofitClient.getClient().create(ServiceApi.class);

        //회원탈퇴
        btnUserDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailView.getText().toString();
                EditText editText = new EditText(Mypage.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(Mypage.this);
                builder.setTitle("정말로 탈퇴하시겠습니까? \n" + "비밀번호를 입력해주세요.").setView(editText);
                builder.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String password = editText.getText().toString();
                        System.out.println(password);//잘 들어옴
                        startUserDelete(new UserDeleteData(email, password));
                    }
                });

            }
        });

        //로그아웃
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Mypage.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Mypage.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }

    private void startUserDelete(UserDeleteData data) {
        service.userDelete(data).enqueue(new Callback<UserDeleteResponse>() {
            @Override
            public void onResponse(Call<UserDeleteResponse> call, Response<UserDeleteResponse> response) {
                UserDeleteResponse result = response.body();
                Toast.makeText(Mypage.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserDeleteResponse> call, Throwable t) {
                Toast.makeText(Mypage.this, "회원탈퇴 에러 발생", Toast.LENGTH_SHORT).show();
                Log.e("회원탈퇴 에러 발생", t.getMessage());
            }
        });
    }
}