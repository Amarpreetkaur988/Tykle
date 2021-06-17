package com.tykle.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tykle.InterfaceClass.ApiService;
import com.tykle.R;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextView title;
    ImageView next;
    private Button save;
    EditText newpassword,currentpassword, confirmpassword;
    private String snewpassword,scurrentpassword,sconfirmpassword;
    private int error=0;
    private ApiService apiService;
    private Activity activity;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_change_password );
        title=findViewById( R.id.title );
        title.setText("Change Password");
        next=findViewById(R.id.next);

        activity=ChangePasswordActivity.this;


        //edit fields find id
        newpassword=findViewById( R.id.newpassword );
        currentpassword=findViewById( R.id.currentpassword );
        confirmpassword=findViewById( R.id.confirmpassword );
    }


}
