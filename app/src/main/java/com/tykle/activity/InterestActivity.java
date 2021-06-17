package com.tykle.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tykle.R;
import com.tykle.adapter.CustomHomeAdapter;

public class InterestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Activity activity;
    private DrawerLayout drawer;
    private TextView title;
    private ImageView next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        activity = InterestActivity.this;
        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        CustomHomeAdapter customHomeAdapter = new CustomHomeAdapter(activity);
        recyclerView.setAdapter(customHomeAdapter);

        title = findViewById(R.id.title);
        title.setText("Home");
        next = findViewById(R.id.next);
        next.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
