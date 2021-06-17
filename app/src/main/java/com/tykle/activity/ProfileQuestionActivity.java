package com.tykle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tykle.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileQuestionActivity extends AppCompatActivity {
    private TextView title;
    private Button btn_next;
    private Spinner sp_interests;
    private ImageView next;
    private String user_id;
    private List<String> list_interests = new ArrayList<>();
    private String selcted_interest;
    private RadioGroup age, identify, looking_for, language, profession, relationshiptype;
    private String sage = "", sIdentify = null, sLooking_for = null, slanguage = null, sProfession = null, srelationship = null;
    private int error = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_question);
        title = findViewById(R.id.title);
        title.setText("Profile Question");
        btn_next = findViewById(R.id.btn_next);
        sp_interests = findViewById(R.id.sp_interests);
        next = findViewById(R.id.next);
        next.setVisibility(View.GONE);

        //Radio group find id
        identify = findViewById(R.id.identify);
        looking_for = findViewById(R.id.looking_for);
        language = findViewById(R.id.language);
        relationshiptype = findViewById(R.id.relationshiptype);
        profession = findViewById(R.id.profession);
        age = findViewById(R.id.age);

        user_id = getIntent().getStringExtra("user_id");

        setUpInterestsList();


        sp_interests.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selcted_interest = list_interests.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        //Validation of Identify  Radio group
        age.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Above_18:
                        sage = "Above 18";
                        break;
                    case R.id.age20_30:
                        sage = "20-30";
                        break;
                    case R.id.age30_40:
                        sage = "30-40";
                        break;
                    case R.id.age40_30:
                        sage = "30-40";
                        break;
                    case R.id.older_wiser:
                        sage = "older and wiser";
                        break;
                }


            }
        });


        identify.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        sIdentify = "Male";
                        break;
                    case R.id.female:
                        sIdentify = "Female";
                        break;
                }

                Log.e("ProfiteQuestion", "value of ssssssssssIdentify  " + sIdentify);
                if (sIdentify.equalsIgnoreCase("")) {
                    error = 1;
                    Log.e("ProfiteQuestion", "value of sIdentify" + sIdentify);
                } else {
                    error = 0;
                    Log.e("ProfiteQuestion", "value of sIdentify" + sIdentify);
                }
            }
        });

        //choose language radio button
        language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.english:
                        slanguage = "English";
                        break;
                    case R.id.spanish:
                        slanguage = "Spanish";
                        break;
                    case R.id.russian:
                        slanguage = "Russian";
                        break;
                    case R.id.french:
                        slanguage = "French";
                        break;
                }


            }
        });


        relationshiptype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.seriousrelationship:
                        srelationship = "   Serious Relationship";
                        break;

                    case R.id.socialdating:
                        srelationship = "Social Dating";
                        break;

                }


            }
        });

        //Looking for radion group
        looking_for.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.looking_male:
                        sLooking_for = "Male";
                        break;
                    case R.id.looking_female:
                        sLooking_for = "Female";
                        break;
                    case R.id.looking_both:
                        sLooking_for = "Both";
                        break;
                }


            }
        });

        profession.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bussion:
                        sProfession = "Bussion";
                        break;
                    case R.id.job:
                        sProfession = "Job";
                        break;
                    case R.id.self_employee:
                        sProfession = "Self Employee";
                        break;
                    case R.id.other:
                        sProfession = "Others";
                        break;
                }


            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Age radio group
                if (age.getCheckedRadioButtonId() == -1) {
                    error = 1;
                } else {
                    error = 0;
                }

                if (relationshiptype.getCheckedRadioButtonId() == -1) {
                    error = 1;
                } else {
                    error = 0;
                }

                //identify radio group
                if (identify.getCheckedRadioButtonId() == -1) {
                    error = 1;
                } else {
                    error = 0;
                }

                //language radio group
                if (language.getCheckedRadioButtonId() == -1) {
                    error = 1;
                } else {
                    error = 0;
                }
                //looking for radio group
                if (looking_for.getCheckedRadioButtonId() == -1) {
                    error = 1;
                } else {
                    error = 0;
                }

                if (selcted_interest == null) {
                    error = 1;
                } else {
                    error = 0;
                }

                if (profession.getCheckedRadioButtonId() == -1) {
                    error = 1;
                } else {
                    error = 0;
                }

                if (error == 1) {
                    Toast.makeText(ProfileQuestionActivity.this, "Please select all profile Question", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ProfileQuestionActivity.this, AboutMeActivity.class);
                    intent.putExtra("Age", sage);
                    intent.putExtra("Identity", sIdentify);
                    intent.putExtra("Looking_for", sLooking_for);
                    intent.putExtra("Language", slanguage);
                    intent.putExtra("Profession", sProfession);
                    intent.putExtra("interest", selcted_interest);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("relationshipStatus", srelationship);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setUpInterestsList() {

        list_interests.add("Dinner");
        list_interests.add("Movies");
        list_interests.add("Drinks");
        list_interests.add("Smoke");
        list_interests.add("club");
        list_interests.add("Coffee");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_item, list_interests); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_interests.setAdapter(spinnerArrayAdapter);


    }
}
