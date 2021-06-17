package com.tykle.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.tykle.ModelClassess.TykleListModel;
import com.tykle.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by omninossolutions on 06/10/18.
 */

public class UserRequestAdaptor extends RecyclerView.Adapter<UserRequestAdaptor.ViewHolder> {

    private Activity activity;
    private MyClickListner myClickListner;
    private List<TykleListModel.Detail> user_requsts = new ArrayList<>();


    public UserRequestAdaptor(Activity activity, List<TykleListModel.Detail> matchRequestList, MyClickListner myClickListner) {

        this.activity = activity;
        this.user_requsts = matchRequestList;
        this.myClickListner = myClickListner;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.custom_adapter_matches_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (!user_requsts.get(position).getImage1().toString().equalsIgnoreCase("")) {

            Picasso.with(activity).load(user_requsts.get(position).getImage1()).into(holder.icon_avata);
        }


    }


    @Override
    public int getItemCount() {
        return user_requsts.size();
    }

    public interface MyClickListner {
        void myClick(int position, int view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView icon_avata;
        private RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            icon_avata = itemView.findViewById(R.id.img);
            relativeLayout = itemView.findViewById(R.id.linear_layout);

            relativeLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            myClickListner.myClick(getLayoutPosition(), v.getId());
        }
    }


}
