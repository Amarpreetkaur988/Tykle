package com.tykle.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tykle.ModelClassess.FilterUserModel;
import com.tykle.ModelClassess.GetUsers;
import com.tykle.R;

import java.util.List;

/**
 * Created by omninossolutions on 06/10/18.
 */

public class CardStackViewAdaptor extends BaseAdapter {

    private List<GetUsers.Detail> data;

    private Activity context;

    private ImageView tykle_friend;

    private BtnClickListener mClickListener = null;

    private FrameLayout parentPanel;

    private String type = "";


    private List<FilterUserModel.Detail> filtered_list;

    public CardStackViewAdaptor(List<GetUsers.Detail> data, Activity context, BtnClickListener listener) {
        this.data = data;
        this.context = context;
        this.mClickListener = listener;
    }

    public CardStackViewAdaptor(List<FilterUserModel.Detail> data, Activity context, String type, BtnClickListener listener) {
        this.filtered_list = data;
        this.context = context;
        this.mClickListener = listener;
        this.type = type;
    }


    @Override
    public int getCount() {

        if (type.equalsIgnoreCase("filter")) {

            return filtered_list.size();

        } else {

            return data.size();
        }


    }

    @Override
    public Object getItem(int position) {


        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            v = inflater.inflate(R.layout.swipping_layout, parent, false);
        }


        ImageView imageView = v.findViewById(R.id.profileImageView);
        tykle_friend = v.findViewById(R.id.tykle_friend);

        TextView name = v.findViewById(R.id.name);
        TextView age = v.findViewById(R.id.age);


        tykle_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha);

                if (animation == null) {
                    return; // here, we don't care
                }
                // reset initialization state
                animation.reset();
                // find View by its id attribute in the XML

                // cancel any pending animation and start this one
                if (v != null) {
                    tykle_friend.clearAnimation();
                    tykle_friend.startAnimation(animation);
                }

                if (mClickListener != null)
                    mClickListener.onBtnClick(position);
            }
        });

        if (type.equalsIgnoreCase("filter")) {

            name.setText(filtered_list.get(position).getUserName());
            age.setText(filtered_list.get(position).getAge());
            Glide.with(context).load(filtered_list.get(position).getImage1()).into(imageView);


        } else {

            name.setText(data.get(position).getUserName());
            age.setText(data.get(position).getAge());
            Glide.with(context).load(data.get(position).getImage1()).into(imageView);


        }


        return v;
    }


    public interface BtnClickListener {
        void onBtnClick(int position);
    }

}
