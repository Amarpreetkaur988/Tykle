package com.tykle.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tykle.R;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener
{
    private ImageView next;
    private TextView title;
    private Spinner year_spinner, month_spinner;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_payment );
        next = findViewById( R.id.next );
        next.setOnClickListener( this );
        title = findViewById( R.id.title );
        title.setText( "Payment" );
        String monthArray[]={"01","02","03","04","05","06","07","08","09","10","11","12"};
        String yearArray[]={"19","20","21","22"};
         ArrayList<String> montharrayList = new ArrayList<>();
         ArrayList<String> yearArryList=new ArrayList<>(  );
        for(int i=0;i<monthArray.length;i++)
        {
            montharrayList.add(monthArray[i] );
        }
    for(int i=0;i<yearArray.length;i++)
    {
        yearArryList.add(yearArray[i]);
    }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PaymentActivity.this, android.R.layout.simple_list_item_1, montharrayList );
        month_spinner=findViewById( R.id.month_spinner );
        month_spinner.setAdapter( arrayAdapter );

        ArrayAdapter<String> arrayAdapteryear=new ArrayAdapter<String>(PaymentActivity.this,android.R.layout.simple_list_item_1,yearArryList);
        year_spinner=findViewById(R.id.year_spinner);
        year_spinner.setAdapter( arrayAdapteryear );


//      //  month_spinner.setSelection(0, true);
//        View v = month_spinner.getSelectedView();
//        ((TextView)v).setTextColor( ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
//String ss=year_spinner.getSelectedItem().toString();
//        Toast.makeText( this, ""+ss, Toast.LENGTH_SHORT ).show();

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
