package com.tykle.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tykle.Classess.HelperClass;
import com.tykle.R;
import com.tykle.activity.PeopleActivity;


/**
 * Created by Naveen Devrani on 31-03-2018.
 */

public class CustomHomeAdapter extends RecyclerView.Adapter<CustomHomeAdapter.ViewHolder> {
    Activity activity;
    String list[] = {"Dinner", "Movies", "Drinks", "Smokes", "club", "Coffee"};
    int image[] = {R.drawable.dinner, R.drawable.movies, R.drawable.drinks, R.drawable.smokes, R.drawable.club, R.drawable.coffee};

    public CustomHomeAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.custom_adapter_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomHomeAdapter.ViewHolder holder, final int position) {
        holder.img.setImageResource(image[position]);
        holder.text.setText(list[position].toString());

        holder.cardVieww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.setInterst(list[position].toString(), activity);
                Intent intent = new Intent(activity.getApplicationContext(), PeopleActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView text;
        private CardView cardVieww;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            text = itemView.findViewById(R.id.text);
            cardVieww = itemView.findViewById(R.id.cardVieww);
        }
    }
}
