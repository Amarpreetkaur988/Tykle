package com.tykle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tykle.Classess.HelperClass;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tykle.Classess.ApiClient.getApiClient;


public class ContactUS extends AppCompatActivity
{
    ImageView next;
    EditText et_user_name, et_email, message_box;
    Button btn_send;
    String username, email, message;
    int error = 0;
    Activity activity;
    ApiService apiService;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        activity = ContactUS.this;

        next = findViewById(R.id.next);

        title = findViewById(R.id.title);
        title.setText("Contact Us");

        //all id find
        et_user_name = findViewById(R.id.et_user_name);
        et_user_name.setText(HelperClass.getUserName(activity));
        et_email = findViewById(R.id.et_email);
        et_email.setText(HelperClass.getEmail(activity));
        message_box = findViewById(R.id.message_box);


    }
}
