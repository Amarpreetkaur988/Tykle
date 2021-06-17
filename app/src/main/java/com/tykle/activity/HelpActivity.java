package com.tykle.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tykle.R;
import com.tykle.adapter.HelpAdapter;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener
{
RecyclerView recyclerView;
    private TextView title;
    ImageView next;
    List<String> list=new ArrayList<>(  );
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_help );
        title=findViewById( R.id.title );
        title.setText("Help");
        next=findViewById(R.id.next);
        next.setOnClickListener( this );
        list.add("fahjkdgjdgdjgdskjjk dhddskhglsdajgdsgdshgdsakhgdskhgkdsgkjsdgwdsdsguiwajdhjdiwfdsfjdsgfhfdshk");
        recyclerView=findViewById( R.id.recyclerView );
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager( getApplicationContext(),LinearLayoutManager.VERTICAL,false );
        recyclerView.setLayoutManager( linearLayoutManager );
        HelpAdapter  adapter=new HelpAdapter(getApplicationContext(),list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.next:
                onBackPressed();
                break;
        }
    }
}
