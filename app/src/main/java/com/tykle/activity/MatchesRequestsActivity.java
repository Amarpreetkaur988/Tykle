package com.tykle.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.HelperClass;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.ModelClassess.FrindIDListModel;
import com.tykle.ModelClassess.TykleListModel;
import com.tykle.R;
import com.tykle.adapter.UserFriendListAdaptor;
import com.tykle.adapter.UserRequestAdaptor;
import com.tykle.managers.DialogsManager;
import com.tykle.util.AppConstants;
import com.tykle.util.ShowSnackBar;
import com.tykle.util.chat.ChatHelper;
import com.tykle.util.qb.QbDialogHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchesRequestsActivity extends AppCompatActivity {


    private static final long CLICK_DELAY = TimeUnit.SECONDS.toMillis(2);
    private static final String EXTRA_QB_DIALOG = "qb_dialog";
    private static final int REQUEST_DIALOG_ID_FOR_UPDATE = 165;
    String frindImg;
    private List<Integer> ids = new ArrayList<>();
    private Context context;
    private LinearLayout ll_tyklerequests, ll_matches;
    private RecyclerView rv_tyklerqsts, rv_matches;
    private Activity activity;
    private RelativeLayout parentPanel;
    private UserRequestAdaptor adaptor;
    private UserFriendListAdaptor friendListAdaptor;
    private List<TykleListModel.Detail> user_requsts = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private String Name = "", imageUrl = "";
    private AlertDialog dialog;
    private ProgressBar progress_dialog;
    private DialogsManager dialogsManager = new DialogsManager();
    private NestedScrollView nested_ScrollView;
    private TextView title, status;
    private ImageView backButtom;
    private QBSystemMessagesManager systemMessagesManager;
    private ImageView iv_show;

    private void SetAdaptor() {

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_tyklerqsts.setLayoutManager(linearLayoutManager);
        adaptor = new UserRequestAdaptor(activity, user_requsts, new UserRequestAdaptor.MyClickListner() {
            @Override
            public void myClick(int position, int view) {

                frindImg = user_requsts.get(position).getImage1();
                Name = user_requsts.get(position).getUserName();
                imageUrl = user_requsts.get(position).getImage1();
                AcceptRejectAlertMethod(user_requsts.get(position).getUserId());


            }
        });

        rv_tyklerqsts.setAdapter(adaptor);
        rv_tyklerqsts.setNestedScrollingEnabled(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        activity = MatchesRequestsActivity.this;
        systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
        findId();
        setUptoolbar();
        GetRequestList();
        getFriendsIdList();


    }


    private void setUptoolbar() {

        title = findViewById(R.id.title);
        backButtom = findViewById(R.id.next);
        title.setText("Requests");
        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void findId() {

        context = this;

        rv_matches = findViewById(R.id.rv_matches);

        progress_dialog = findViewById(R.id.progress_dialog);

        ll_tyklerequests = findViewById(R.id.ll_tyklerequests);

        parentPanel = findViewById(R.id.parentPanel);

        iv_show = findViewById(R.id.iv_show);

        status = findViewById(R.id.status);

        ll_matches = findViewById(R.id.ll_matches);

        rv_tyklerqsts = findViewById(R.id.rv_tyklerqsts);


    }

    private void GetRequestList() {

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();


            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.TykleFriendList(HelperClass.getID(activity)).enqueue(new Callback<TykleListModel>() {
                @Override
                public void onResponse(Call<TykleListModel> call, Response<TykleListModel> response) {


                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {
                            user_requsts = response.body().getDetails();
                            SetAdaptor();
                        } else {

                            ll_tyklerequests.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<TykleListModel> call, Throwable t) {


                    ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.Servererror);


                }
            });

        } else {


            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.checknetwork);
        }

    }

    private void PerformActionOnUser(final String action, String friendid) {


        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);
            apiService.UserAction(HelperClass.getID(activity), friendid, action).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if (response.isSuccessful()) {
                        if (response.body().get("success").toString().equalsIgnoreCase("1")) {


                            String status = response.body().get("status").toString();
                            if (status.equalsIgnoreCase("3")) {


                                Intent match = new Intent(activity, MatchActivity.class);
                                match.putExtra("frindImg", frindImg);
                                startActivity(match);

                            }

                            GetRequestList();


                        } else {

                            Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {

                    Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.checknetwork);

        }

    }

    private void AcceptRejectAlertMethod(final String friendid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.accept_reject_alert, null);
        TextView tv_name = view.findViewById(R.id.tv_name);
        iv_show = view.findViewById(R.id.iv_show);
        tv_name.setText(Name);
        CircleImageView accetp_reject_circularImage = view.findViewById(R.id.accetp_reject_circularImage);

        if (imageUrl != "")
            Picasso.with(activity).load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(accetp_reject_circularImage);

        Button Accept = view.findViewById(R.id.acceptBtn);
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PerformActionOnUser("2", friendid);
                dialog.dismiss();


            }
        });
        Button Reject = view.findViewById(R.id.rejectBtn);
        Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PerformActionOnUser("0", friendid);
                dialog.dismiss();

            }
        });

        iv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent profile = new Intent(activity, UserProfileActivity.class);
                profile.putExtra("id", friendid);

                startActivity(profile);
            }
        });


        dialog = builder.create();
        dialog.setView(view);
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void getFriendsIdList() {

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.FrindIdList(HelperClass.getID(activity)).enqueue(new Callback<FrindIDListModel>() {

                @Override
                public void onResponse(Call<FrindIDListModel> call, Response<FrindIDListModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {

                            ids = response.body().getDetails();

                            retrieveAllUsersFromPage(1);
                        } else {

                            status.setText("You have no matches");
                            progress_dialog.setVisibility(View.GONE);


                        }
                    }
                }


                @Override
                public void onFailure(Call<FrindIDListModel> call, Throwable t) {

                    status.setText("Server error");
                    progress_dialog.setVisibility(View.GONE);


                }
            });

        } else {

            status.setText("Network error");
            progress_dialog.setVisibility(View.GONE);


        }
    }


    private void retrieveAllUsersFromPage(final int page) {


        QBPagedRequestBuilder pagedRequestBuilder = new QBPagedRequestBuilder();
        pagedRequestBuilder.setPage(page);
        pagedRequestBuilder.setPerPage(100);
        QBUsers.getUsersByIDs(ids, pagedRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {


                QBChatDialog dialog = (QBChatDialog) getIntent().getSerializableExtra(EXTRA_QB_DIALOG);

                linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                rv_matches.setLayoutManager(linearLayoutManager);

                friendListAdaptor = new UserFriendListAdaptor(activity, qbUsers, new UserFriendListAdaptor.MyClickListner() {
                    @Override

                    public void myClick(int position, ArrayList<QBUser> users) {


                        if (isPrivateDialogExist(users)) {
                            users.remove(ChatHelper.getCurrentUser());
                            QBChatDialog existingPrivateDialog = QbDialogHolder.getInstance().getPrivateDialogWithUser(users.get(0));

                            ChatActivity.startForResult(activity, REQUEST_DIALOG_ID_FOR_UPDATE, existingPrivateDialog);
                            users.clear();
                        } else {
                            ProgressDialogFragment.show(getSupportFragmentManager(), R.string.loading_chat);
                            createDialog(users);
                            users.clear();
                        }

                    }
                });

                rv_matches.setAdapter(friendListAdaptor);
                progress_dialog.setVisibility(View.GONE);


            }


            @SuppressLint("SetTextI18n")
            @Override
            public void onError(QBResponseException e) {

                status.setText("Server error");
                progress_dialog.setVisibility(View.GONE);

            }
        });
    }

    private boolean isPrivateDialogExist(ArrayList<QBUser> allSelectedUsers) {
        ArrayList<QBUser> selectedUsers = new ArrayList<>();
        selectedUsers.addAll(allSelectedUsers);
        selectedUsers.remove(ChatHelper.getCurrentUser());
        return selectedUsers.size() == 1 && QbDialogHolder.getInstance().hasPrivateDialogWithUser(selectedUsers.get(0));
    }


    private void createDialog(final ArrayList<QBUser> selectedUsers) {
        ChatHelper.getInstance().createDialogWithSelectedUsers(selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {

                        try {

                            dialogsManager.sendSystemMessageAboutCreatingDialog(systemMessagesManager, dialog);
                            ChatActivity.startForResult(activity, REQUEST_DIALOG_ID_FOR_UPDATE, dialog);
                            ProgressDialogFragment.hide(getSupportFragmentManager());

                        } catch (Exception e) {
                            ProgressDialogFragment.hide(getSupportFragmentManager());
                            Toast.makeText(context, "Some network problem occurs", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(QBResponseException e) {

                        ProgressDialogFragment.hide(getSupportFragmentManager());

                    }
                }
        );
    }

}







