package com.tykle.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tykle.InterfaceClass.ApiService;
import com.tykle.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tykle.Classess.ApiClient.getApiClient;

public class ForgotPassword extends AppCompatActivity {
    Button btn_send, btn_cancel;
    EditText et_email;
    String email;
    ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        et_email = findViewById(R.id.et_email);
        btn_send = findViewById(R.id.btn_send);


        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
