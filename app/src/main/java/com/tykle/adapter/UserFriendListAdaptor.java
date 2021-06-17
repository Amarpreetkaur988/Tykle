package com.tykle.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quickblox.chat.QBChatService;
import com.quickblox.users.model.QBUser;
import com.tykle.R;
import com.tykle.activity.UserProfileActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by omninossolutions on 01/11/18.
 */

public class UserFriendListAdaptor extends RecyclerView.Adapter<UserFriendListAdaptor.ViewHolder> {

    public static final String EXTRA_QB_USERS = "qb_users";
    private static final String EXTRA_QB_DIALOG = "qb_dialog";
    ArrayList<QBUser> selectedUsers = new ArrayList<>();
    private Activity activity;
    private MyClickListner myClickListner;
    private ArrayList<QBUser> users = new ArrayList<>();


    public UserFriendListAdaptor(Activity activity, ArrayList<QBUser> users, MyClickListner myClickListner) {

        this.activity = activity;
        this.myClickListner = myClickListner;
        this.users = users;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.custom_adapter_requests, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.txtName.setText(users.get(position).getFullName());
        String url = users.get(position).getWebsite();
        Glide.with(activity).load(url).into(holder.icon_avata);

    }


    @Override
    public int getItemCount() {
        return users.size();
    }


    public interface MyClickListner {
        void myClick(int position, ArrayList<QBUser> users);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView icon_avata;
        private TextView txtName, txtTime;
        private RelativeLayout parentPanel;
        private ImageView iv_chat;


        public ViewHolder(View itemView) {
            super(itemView);
            icon_avata = itemView.findViewById(R.id.icon_avata);
            txtName = itemView.findViewById(R.id.txtName);
            txtTime = itemView.findViewById(R.id.txtTime);
            parentPanel = itemView.findViewById(R.id.parentPanel);
            iv_chat = itemView.findViewById(R.id.iv_chat);
            parentPanel.setOnClickListener(this);
            iv_chat.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.iv_chat:
                    selectedUsers.add(QBChatService.getInstance().getUser());
                    selectedUsers.add(users.get(getAdapterPosition()));
                    myClickListner.myClick(getAdapterPosition(), selectedUsers);
                    break;

                case R.id.parentPanel:
                    Intent intent = new Intent(activity, UserProfileActivity.class);
                    intent.putExtra("id", users.get(getAdapterPosition()).getExternalId());
                    activity.startActivity(intent);
                    break;


            }
        }
    }

}
