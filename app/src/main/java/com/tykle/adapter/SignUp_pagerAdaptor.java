package com.tykle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import com.tykle.R;

import java.io.File;
import java.util.List;

public class SignUp_pagerAdaptor extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ButtonClickListner buttonClickListner;
    private List<String> product_images;

    public SignUp_pagerAdaptor(Context context, List<String> product_images, ButtonClickListner buttonClickListner) {
        mContext = context;
        this.product_images = product_images;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.buttonClickListner = buttonClickListner;
    }

    @Override
    public int getCount() {

        return product_images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.sign_up_pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);

        Button btn_upload = itemView.findViewById(R.id.btn_upload);

        if (product_images.get(position).equalsIgnoreCase("")) {
            imageView.setVisibility(View.GONE);
            btn_upload.setVisibility(View.VISIBLE);

        } else {

            if (product_images.get(position) != null) {
                File image = new File(product_images.get(position));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap mBitmapInsurance = BitmapFactory.decodeFile(image.getAbsolutePath(), options);
                imageView.setImageBitmap(mBitmapInsurance);
                imageView.setVisibility(View.VISIBLE);
            }
            btn_upload.setVisibility(View.GONE);
        }

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickListner.Click(position);
            }
        });

        container.addView(itemView);


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    public interface ButtonClickListner {

        void Click(int layoutPosition);

    }
}