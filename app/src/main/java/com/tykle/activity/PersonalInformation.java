package com.tykle.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tykle.Classess.HelperClass;
import com.tykle.R;

public class PersonalInformation extends AppCompatActivity implements View.OnClickListener
{
    ImageView next;
    private TextView title, language, looking_for, profession, age, gender;
    private Activity activity;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_personal_information );
        activity = PersonalInformation.this;

        next = findViewById( R.id.next );
        next.setOnClickListener( this );

        title = findViewById( R.id.title );
        title.setText( "Personal Information" );

        language = findViewById( R.id.language );
        language.setText( HelperClass.getProfileQuestionLanguage( activity ) );


        looking_for = findViewById( R.id.looking_for );
        if(HelperClass.getProfileQuestionLooking_for( activity ).equalsIgnoreCase( "" )) {
            looking_for.setText( "Female" );
        } else {
            looking_for.setText( HelperClass.getProfileQuestionLooking_for( activity ) );
        }
        profession = findViewById( R.id.profession );
        profession.setText( HelperClass.getProfileQuestionProfession( activity ) );

        age = findViewById( R.id.age );
        age.setText( HelperClass.getProfileQuestionAge( activity ) );

        gender = findViewById( R.id.gender );
        gender.setText( HelperClass.getProfileQuestionIdentity( activity ) );
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()) {
            case R.id.next:
                onBackPressed();
                break;
        }
    }
}
