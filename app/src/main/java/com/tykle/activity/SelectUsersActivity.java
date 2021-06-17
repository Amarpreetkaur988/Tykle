package com.tykle.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.HelperClass;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.ModelClassess.FrindIDListModel;
import com.tykle.R;
import com.tykle.adapter.CheckboxUsersAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectUsersActivity extends BaseActivity {
    public static final String EXTRA_QB_USERS = "qb_users";
    public static final int MINIMUM_CHAT_OCCUPANTS_SIZE = 2;
    private static final long CLICK_DELAY = TimeUnit.SECONDS.toMillis(2);

    private static final String EXTRA_QB_DIALOG = "qb_dialog";

    private ListView usersListView;
    private ProgressBar progressBar;
    private CheckboxUsersAdapter usersAdapter;
    private long lastClickTime = 0l;

    private ImageView iv_firsticon, iv_lasticon;
    private TextView tv_title;
    private List<Integer> ids = new ArrayList<>();
    private TextView status;

    public static void start(Context context) {
        Intent intent = new Intent(context, SelectUsersActivity.class);
        context.startActivity(intent);
    }

    /**
     * Start activity for picking users
     *
     * @param activity activity to return result
     * @param code     request code for onActivityResult() method
     *                 <p>
     *                 in onActivityResult there will be 'ArrayList<QBUser>' in the intent extras
     *                 which can be obtained with SelectPeopleActivity.EXTRA_QB_USERS key
     */
    public static void startForResult(Activity activity, int code) {
        startForResult(activity, code, null);
    }

    public static void startForResult(Activity activity, int code, QBChatDialog dialog) {
        Intent intent = new Intent(activity, SelectUsersActivity.class);
        intent.putExtra(EXTRA_QB_DIALOG, dialog);
        activity.startActivityForResult(intent, code);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_users);

        progressBar = _findViewById(R.id.progress_select_users);
        iv_firsticon = findViewById(R.id.iv_firsticon);

        iv_lasticon = findViewById(R.id.iv_lasticon);
        tv_title = findViewById(R.id.tv_title);
        status = findViewById(R.id.status);
        usersListView = _findViewById(R.id.list_select_users);

        iv_firsticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_lasticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usersAdapter != null) {
                    List<QBUser> users = new ArrayList<>(usersAdapter.getSelectedUsers());
                    if (users.size() >= MINIMUM_CHAT_OCCUPANTS_SIZE) {
                        passResultToCallerActivity();
                    } else {
                        Toaster.shortToast(R.string.select_users_choose_users);
                    }
                }
            }
        });


        iv_lasticon.setImageResource(R.drawable.ic_checked);
        iv_lasticon.setVisibility(View.VISIBLE);


        TextView listHeader = (TextView) LayoutInflater.from(this)
                .inflate(R.layout.include_list_hint_header, usersListView, false);
        listHeader.setText(R.string.select_users_list_hint);
        usersListView.addHeaderView(listHeader, null, false);

        if (isEditingChat()) {
            setActionBarTitle(R.string.select_users_edit_chat);
        } else {
            setActionBarTitle(R.string.select_users_create_chat);
        }
        // actionBar.setDisplayHomeAsUpEnabled(true);

        getFriendsIdList();
    }

    @Override
    protected void initUI() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ((SystemClock.uptimeMillis() - lastClickTime) < CLICK_DELAY) {
            return super.onOptionsItemSelected(item);
        }
        lastClickTime = SystemClock.uptimeMillis();

        switch (item.getItemId()) {
            case R.id.menu_select_people_action_done:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected View getSnackbarAnchorView() {
        return findViewById(R.id.layout_root);
    }

    private void passResultToCallerActivity() {
        Intent result = new Intent();
        ArrayList<QBUser> selectedUsers = new ArrayList<>(usersAdapter.getSelectedUsers());
        result.putExtra(EXTRA_QB_USERS, selectedUsers);
        setResult(RESULT_OK, result);
        finish();
    }


    private boolean isEditingChat() {
        return getIntent().getSerializableExtra(EXTRA_QB_DIALOG) != null;
    }

    @SuppressLint("SetTextI18n")
    private void getFriendsIdList() {

        if (HelperClass.isNetworkConnected(SelectUsersActivity.this)) {

            ApiClient.getApiClient();

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);


            apiService.FrindIdList(HelperClass.getID(SelectUsersActivity.this)).enqueue(new Callback<FrindIDListModel>() {

                @Override
                public void onResponse(Call<FrindIDListModel> call, Response<FrindIDListModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {

                            ids = response.body().getDetails();

                            retrieveAllUsersFromPage(1);
                        } else {

                            status.setText("You have no matches");
                            progressBar.setVisibility(View.GONE);


                        }
                    }
                }


                @Override
                public void onFailure(Call<FrindIDListModel> call, Throwable t) {

                    status.setText("Server error");
                    progressBar.setVisibility(View.GONE);


                }
            });

        } else {

            status.setText("Network error");
            progressBar.setVisibility(View.GONE);

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

                usersAdapter = new CheckboxUsersAdapter(SelectUsersActivity.this, qbUsers);
                if (dialog != null) {
                    usersAdapter.addSelectedUsers(dialog.getOccupants());
                }
                usersListView.setAdapter(usersAdapter);

                progressBar.setVisibility(View.GONE);
            }


            @SuppressLint("SetTextI18n")
            @Override
            public void onError(QBResponseException e) {

                status.setText("Server error");

            }
        });
    }

}
