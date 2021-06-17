package com.tykle.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.HelperClass;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.ModelClassess.UserProfileUpdate;
import com.tykle.R;
import com.tykle.util.AppConstants;
import com.tykle.util.ShowSnackBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements OnClickListener {

    //request code
    public static final int PICK_FROM_GALLERY = 100;
    public static final int CAMERA_REQUEST = 200;
    private ImageView next, profile_image_choose;
    private ImageView iv_menu;
    private Button btn_save;
    private String username = "", phone = "", age = "", interst = "", relationType = "", about = "";
    private EditText et_name, et_mobile, et_age, et_about;
    private Boolean sentToSettings = false;
    private Spinner sp_interests, sp_relationType;
    private CircleImageView profile_image;
    private Activity activity;
    private DialogPlus dialog;
    private TextView title;
    private TextView camera, galery, cancel;
    private String userChoosenTask = "", imagepath = null, path;
    private Bitmap bitmap;
    private Uri uri;
    private LinearLayout parentPanel;
    private List<String> list_interests, list_relationType;
    private RecyclerView images_recycler;
    private Button upload_images;
    private int index = 0;
    private List<String> imagesPathList = new ArrayList<>(4);
    private CircleImageView image1, image2, image3;
    private ImageView upload_img1, upload_img2, upload_img3;
    private String img1 = "", img2 = "", img3 = "", img4 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        activity = EditProfileActivity.this;
        list_interests = new ArrayList<>();
        list_relationType = new ArrayList<>();

        initViews();
        setUpToolbar();
        getData();

        imagesPathList.add("");
        imagesPathList.add("");
        imagesPathList.add("");
        imagesPathList.add("");


        //listners
        profile_image.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        upload_img1.setOnClickListener(this);
        upload_img2.setOnClickListener(this);
        upload_img3.setOnClickListener(this);
        next.setOnClickListener(this);


        setUpList();

    }

    private void getData() {

        img1 = getIntent().getStringExtra("img1");
        img2 = getIntent().getStringExtra("img2");
        img3 = getIntent().getStringExtra("img3");
        img4 = getIntent().getStringExtra("img4");

        if (!img1.isEmpty()) {

            Glide.with(activity).load(img1).into(profile_image);
        }


        if (!img2.isEmpty()) {
            Glide.with(activity).load(img2).into(image1);
        }
        if (!img3.isEmpty()) {

            Glide.with(activity).load(img3).into(image2);
        }

        if (!img4.isEmpty()) {

            Glide.with(activity).load(img4).into(image3);
        }

    }


    private void initViews() {

        sp_relationType = findViewById(R.id.sp_relationType);
        next = findViewById(R.id.next);
        iv_menu = findViewById(R.id.iv_menu);
        iv_menu.setVisibility(View.VISIBLE);
        et_about = findViewById(R.id.et_about);
        parentPanel = findViewById(R.id.parentPanel);
        profile_image = findViewById(R.id.profile_image);
        sp_interests = findViewById(R.id.sp_interests);
        btn_save = findViewById(R.id.btn_save);
        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
        et_age = findViewById(R.id.et_age);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        upload_img1 = findViewById(R.id.upload_img1);
        upload_img2 = findViewById(R.id.upload_img2);
        upload_img3 = findViewById(R.id.upload_img3);
        title = findViewById(R.id.title);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image:
                index = 0;
                CustomDialog();
                break;

            case R.id.tv_open_camera:
                userChoosenTask = "Take Photo";
                cameraIntent();
                dialog.dismiss();
                break;

            case R.id.tv_open_gallery:
                userChoosenTask = "Choose from Library";
                galleryIntent();
                dialog.dismiss();
                break;

            case R.id.tv_Cancel:
                dialog.dismiss();
                break;

            case R.id.btn_save:
                Validate();
                break;


            case R.id.upload_img1:
                index = 1;
                CustomDialog();
                break;

            case R.id.upload_img2:
                index = 2;
                CustomDialog();
                break;

            case R.id.upload_img3:
                index = 3;
                CustomDialog();
                break;

            case R.id.next:
                onBackPressed();
                break;

        }

    }

    private void Validate() {

        if (imagesPathList.get(0).isEmpty()) {

            if (img1.isEmpty()) {


            } else {

                imagesPathList.set(0, "");
            }

        }
        if (imagesPathList.get(1).isEmpty()) {

            if (img2.isEmpty()) {


            } else {

                imagesPathList.set(1, "");
            }
        }
        if (imagesPathList.get(2).isEmpty()) {

            if (img3.isEmpty()) {


            } else {

                imagesPathList.set(2, "");
            }
        }
        if (imagesPathList.get(3).isEmpty()) {

            if (img4.isEmpty()) {


            } else {

                imagesPathList.set(3, "");
            }

        }

        Update();

    }

    private void Update() {

        username = et_name.getText().toString();

        if (username.equalsIgnoreCase("")) {
            username = HelperClass.getUserName(activity);
        }

        about = et_about.getText().toString();


        if (about.equalsIgnoreCase("")) {
            about = HelperClass.getAbout(activity);
        }

        phone = et_mobile.getText().toString();

        if (phone.equalsIgnoreCase("")) {
            phone = HelperClass.getPhone(activity);
        }

        age = et_age.getText().toString();

        if (age.equalsIgnoreCase("")) {
            age = HelperClass.getAge(activity);
        }

        interst = sp_interests.getSelectedItem().toString();

        relationType = sp_relationType.getSelectedItem().toString();


        MultipartBody.Part image1;
        MultipartBody.Part image2;
        MultipartBody.Part image3;
        MultipartBody.Part image4;

//
        String path1 = imagesPathList.get(0);
        if (path1.isEmpty()) {

            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
//
            image1 = MultipartBody.Part.createFormData("image1", "", attachmentEmpty);

        } else {

            File file = new File(path1);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            image1 = MultipartBody.Part.createFormData("image1", file.getName(), requestFile);
        }


        String path2 = imagesPathList.get(1);

        if (path2.isEmpty()) {

            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
//
            image2 = MultipartBody.Part.createFormData("image2", "", attachmentEmpty);

        } else {
            File file2 = new File(path2);
            RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
            image2 = MultipartBody.Part.createFormData("image2", file2.getName(), requestFile2);
        }


        String path3 = imagesPathList.get(2);

        if (path3.isEmpty()) {

            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
//
            image3 = MultipartBody.Part.createFormData("image3", "", attachmentEmpty);


        } else {

            File file3 = new File(path3);
            RequestBody requestFile3 = RequestBody.create(MediaType.parse("multipart/form-data"), file3);
            image3 = MultipartBody.Part.createFormData("image3", file3.getName(), requestFile3);

        }

        String path4 = imagesPathList.get(3);

        if (path4.isEmpty()) {

            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
//
            image4 = MultipartBody.Part.createFormData("image4", "", attachmentEmpty);


        } else {

            File file4 = new File(path4);
            RequestBody requestFile4 = RequestBody.create(MediaType.parse("multipart/form-data"), file4);
            image4 = MultipartBody.Part.createFormData("image4", file4.getName(), requestFile4);
        }


        RequestBody userNameBody = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody UidBody = RequestBody.create(MediaType.parse("text/plain"), HelperClass.getID(activity));
        RequestBody ageBody = RequestBody.create(MediaType.parse("text/plain"), age);
        RequestBody aboutBody = RequestBody.create(MediaType.parse("text/plain"), about);
        RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody InterstBody = RequestBody.create(MediaType.parse("text/plain"), interst);
        RequestBody relationTypeBody = RequestBody.create(MediaType.parse("text/plain"), relationType);

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            HelperClass.showProgressDialog(activity);

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.UpdateProfile(UidBody, userNameBody, phoneBody, ageBody, InterstBody, relationTypeBody, aboutBody, image1, image2, image3, image4).enqueue(new Callback<UserProfileUpdate>() {
                @Override
                public void onResponse(Call<UserProfileUpdate> call, Response<UserProfileUpdate> response) {

                    HelperClass.hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {

                            String userName = response.body().getDetails().getUserName();
                            String email = response.body().getDetails().getEmail();
                            String number = response.body().getDetails().getPhone();
                            String interst = response.body().getDetails().getInterested();
                            String age = response.body().getDetails().getAge();
                            String image = response.body().getDetails().getImage1();

                            HelperClass.setUsername(userName, activity);
                            HelperClass.setEmail(email, activity);
                            HelperClass.setPhone(number, activity);
                            HelperClass.setuserInterst(interst, activity);
                            HelperClass.setAge(age, activity);
                            HelperClass.setAbout(response.body().getDetails().getAbout(), activity);
                            HelperClass.setProfileImage(image, activity);

                            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.updated);

                            imagesPathList.clear();

                            imagesPathList.add("");
                            imagesPathList.add("");
                            imagesPathList.add("");
                            imagesPathList.add("");


                        }
                    }
                }

                @Override
                public void onFailure(Call<UserProfileUpdate> call, Throwable t) {

                    HelperClass.hideProgressDialog();

                }
            });


        } else {

        }
    }

    private void CustomDialog() {

        dialog = DialogPlus.newDialog(this)

                .setContentHolder(new ViewHolder(R.layout.gallery_picker_dialog))
                .setMargin(20, 0, 20, 0)
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .create();

        camera = (TextView) dialog.findViewById(R.id.tv_open_camera);

        galery = (TextView) dialog.findViewById(R.id.tv_open_gallery);

        cancel = (TextView) dialog.findViewById(R.id.tv_Cancel);

        camera.setOnClickListener(this);

        galery.setOnClickListener(this);

        cancel.setOnClickListener(this);

        dialog.show();
    }

    private void galleryIntent() {

        Intent intent = new Intent();

        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);//

        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_GALLERY);
    }

    @SuppressLint("SetTextI18n")
    private void setUpToolbar() {

        title.setText("Edit Profile");
        //check the user login with facebook or normal
        if (HelperClass.getLoginToken(activity).equalsIgnoreCase("1")) {
            iv_menu.setVisibility(View.INVISIBLE);
        } else {
            iv_menu.setVisibility(View.VISIBLE);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());


            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        uri = getImageUri(this, bitmap);

        imagepath = getRealPathFromUri(uri);

        imagesPathList.set(index, imagepath);

        if (index == 0) {
            profile_image.setImageBitmap(bitmap);

        } else if (index == 1) {
            image1.setImageBitmap(bitmap);

        } else if (index == 2) {

            image2.setImageBitmap(bitmap);
        } else if (index == 3) {

            image3.setImageBitmap(bitmap);
        }


    }

    private void onCaptureImageResult(Intent data) {

        try {

            bitmap = (Bitmap) data.getExtras().get("data");

            uri = getImageUri(EditProfileActivity.this, bitmap);

            imagepath = getRealPathFromUri(uri);

            imagesPathList.set(index, imagepath);

            if (index == 0) {
                profile_image.setImageBitmap(bitmap);

            } else if (index == 1) {
                image1.setImageBitmap(bitmap);

            } else if (index == 2) {

                image2.setImageBitmap(bitmap);
            } else if (index == 3) {

                image3.setImageBitmap(bitmap);
            }


        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private Uri getImageUri(EditProfileActivity youractivity, Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        path = MediaStore.Images.Media.insertImage(youractivity.getContentResolver(), bitmap, "Title", null);

        return Uri.parse(path);
    }

    private String getRealPathFromUri(Uri tempUri) {

        Cursor cursor = null;
        try {

            String[] proj = {MediaStore.Images.Media.DATA};

            cursor = this.getContentResolver().query(tempUri, proj, null, null, null);

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            return cursor.getString(column_index);

        } finally {

            if (cursor != null) {

                cursor.close();
            }
        }
    }


    private void cameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        PackageManager packageManager = this.getPackageManager();

        List<ResolveInfo> listcam = packageManager.queryIntentActivities(intent, 0);

        intent.setPackage(listcam.get(0).activityInfo.packageName);

        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_FROM_GALLERY)

                onSelectFromGalleryResult(data);

            else if (requestCode == CAMERA_REQUEST)

                onCaptureImageResult(data);
        }
    }

    private void setUpList() {

        list_interests.add("Dinner");
        list_interests.add("Movies");
        list_interests.add("Drinks");
        list_interests.add("Smoke");
        list_interests.add("Club");
        list_interests.add("Coffee");

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, R.layout.spinner_item, list_interests); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_interests.setAdapter(spinnerArrayAdapter);

        list_relationType.add("Serious RelationShip");
        list_relationType.add("Social Dating");

        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>
                (this, R.layout.spinner_item, list_relationType); //selected item will look like a spinner set from XML
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_relationType.setAdapter(spinnerArrayAdapter1);


    }


}