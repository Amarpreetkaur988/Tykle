package com.tykle.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.quickblox.sample.core.utils.StorageUtils;
import com.tykle.BuildConfig;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.HelperClass;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.R;
import com.tykle.adapter.CustomPagerAdapter;
import com.tykle.adapter.SignUp_pagerAdaptor;
import com.tykle.util.App;
import com.tykle.util.AppConstants;
import com.tykle.util.ShowSnackBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 200;
    private static final int PICK_FROM_GALLERY = 100;
    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

    private CircleImageView profile_image;

    private TextView camera, galery, cancel;
    private String userChoosenTask = "", imagepath = "", path;
    private TextView tv_login, termData, privacyData;
    private DialogPlus dialog;
    private Activity activity;
    private EditText et_user_name, et_email, et_mobile, et_password;
    private Bitmap bitmap;
    private NestedScrollView parentPanel;
    private Uri uri;
    private Boolean verfied = false;
    private ViewPager viewPager;
    private CircleIndicator indicator;
    private Button btn_signup;
    private int Position;
    private List<String> product_images = new ArrayList<>();
    private static final String CAMERA_FILE_NAME_PREFIX = "CAMERA_";
    private CheckBox checkPrivacy, checkTerm;


    Uri outPutfileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        activity = SignupActivity.this;
        initControls();

        setviewpager();

        //clickListners
        btn_signup.setOnClickListener(this);
//        profile_image_choose.setOnClickListener(this);

        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {

                } else {

                    String email = et_email.getText().toString();
                    CheckUserEmail(email);
                }
            }
        });
    }

    private void initControls() {

        //findId
        et_user_name = findViewById(R.id.et_user_name);
        et_email = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        et_password = findViewById(R.id.et_password);
        parentPanel = findViewById(R.id.parentPanel);
        viewPager = findViewById(R.id.viewPager);

        termData = findViewById(R.id.termData);
        termData.setOnClickListener(this);
        privacyData = findViewById(R.id.privacyData);
        privacyData.setOnClickListener(this);

        checkPrivacy = findViewById(R.id.checkPrivacy);
        checkTerm = findViewById(R.id.checkTerm);

        btn_signup = findViewById(R.id.btn_signup);
        indicator = findViewById(R.id.indicator);

        tv_login = (TextView) findViewById(R.id.tv_login);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        //chose image
        profile_image = findViewById(R.id.profile_image);


        product_images.add("");
        product_images.add("");
        product_images.add("");
        product_images.add("");

    }


    //Email validation code

    public boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.termData:
                startActivity(new Intent(activity, TermAndConditionActivity.class));
                break;

            case R.id.privacyData:
                startActivity(new Intent(activity, privacyPolicyActivity.class));
                break;

            case R.id.profile_image_choose:
                //    CustomDialog();
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

            case R.id.btn_signup:
                Validate();
                break;


        }

    }

    private void Validate() {

        String userName = et_user_name.getText().toString();

        String mobile = et_mobile.getText().toString();

        String password = et_password.getText().toString();

        String email = et_email.getText().toString();

        if (TextUtils.isEmpty(userName)) {

            et_user_name.setError(AppConstants.mandatoryField);

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.mandatoryField);

        } else if (TextUtils.isEmpty(email)) {

            et_email.setError(AppConstants.mandatoryField);

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.mandatoryField);

        } else if (!isValidEmail(email)) {

            et_email.setError(AppConstants.validemail);

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.validemail);

        } else if (TextUtils.isEmpty(mobile)) {

            et_mobile.setError(AppConstants.mandatoryField);

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.mandatoryField);


        } else if (TextUtils.isEmpty(password) || password.length() < 8) {

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.PASSWORD_PATTERN);
            et_password.setError(AppConstants.PASSWORD_PATTERN);


        } else if (product_images.size() == 0) {

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.uploadimage);

        } else if (!verfied) {

            et_email.setError(AppConstants.emailexist);

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.emailexist);

        } else if (!checkTerm.isChecked()) {
            ShowSnackBar.longSnackBar(parentPanel, activity, "Accept Term and conditions");
        } else if (!checkPrivacy.isChecked()) {
            ShowSnackBar.longSnackBar(parentPanel, activity, "Accept Privacy Policy");
        } else {
            App.getCommonInstances().setUserName(userName);
            App.getCommonInstances().setMobile(mobile);
            App.getCommonInstances().setImagesPathList(product_images);
            VerifyEmail(email, password, userName);

        }
    }

    private void VerifyEmail(final String email, final String password, final String userName) {

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            HelperClass.showProgressDialog(activity);

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.EmailVerification(email).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {

                    HelperClass.hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().get("success").toString().equalsIgnoreCase("1")) {

                            String otp = response.body().get("OTP").toString();

                            Toast.makeText(activity, "Please check your email", Toast.LENGTH_SHORT).show();

                            Intent otpActivity = new Intent(activity, OtpActivity.class);
                            otpActivity.putExtra("otp", otp);
                            otpActivity.putExtra("email", email);
                            otpActivity.putExtra("password", password);
                            otpActivity.putExtra("name", userName);
                            startActivity(otpActivity);


                        } else {

                            Toast.makeText(activity, "Server error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {

                    HelperClass.hideProgressDialog();

                    ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.Servererror);
                }
            });

        } else {

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.checknetwork);
        }


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

    private boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private void galleryIntent() {

        Intent intent = new Intent();

        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);//

        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_GALLERY);
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

        product_images.remove(Position);
        product_images.add(imagepath);


        Collections.reverse(product_images);

        setviewpager();


    }


    public File getTemporaryCameraFile() {
        File storageDir = StorageUtils.getAppExternalDataDirectoryFile();
        File file = new File(storageDir, getTemporaryCameraFileName());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imagepath = file.getAbsolutePath();


        return file;
    }

    private static String getTemporaryCameraFileName() {
        return CAMERA_FILE_NAME_PREFIX + System.currentTimeMillis() + ".jpg";
    }

    private void onCaptureImageResult(Intent data) {

        try {

            String path = compressImage(imagepath);


            product_images.remove(Position);
            product_images.add(path);

            Collections.reverse(product_images);

            setviewpager();


        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    private Uri getImageUri(SignupActivity youractivity, Bitmap bitmap) {

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

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        PackageManager packageManager = this.getPackageManager();
//
//        List<ResolveInfo> listcam = packageManager.queryIntentActivities(intent, 0);
//
//        intent.setPackage(listcam.get(0).activityInfo.packageName);
//
//        startActivityForResult(intent, CAMERA_REQUEST);

        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            photoFile = getTemporaryCameraFile();
            if (photoFile != null) {
                Uri uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", photoFile);

                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(pictureIntent,
                        CAMERA_REQUEST);
            }
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

    private void CheckUserEmail(String email) {

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.CheckUserEmail(email).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {


                    if (response.isSuccessful()) {
                        if (response.body().get("success").toString().equalsIgnoreCase("1")) {

                            verfied = true;

                        } else {
                            verfied = false;
                            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.emailexist);
                            et_email.setError(AppConstants.emailexist);
                        }

                    }

                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {


                    verfied = false;
                    ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.Servererror);

                }
            });

        } else {

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.checknetwork);
        }


    }

    private void setviewpager() {


        SignUp_pagerAdaptor mCustomPagerAdapter = new SignUp_pagerAdaptor(activity, product_images, new SignUp_pagerAdaptor.ButtonClickListner() {
            @Override
            public void Click(int layoutPosition) {

                Position = layoutPosition;
                CustomDialog();
            }
        });

        viewPager.setAdapter(mCustomPagerAdapter);

        indicator.setViewPager(viewPager);

        mCustomPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());

    }

    public String compressImage(String filePath) {


        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];


        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 40, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;


    }


}

