package com.tykle.ModelClassess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tykle.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CameraPicsAdaptor extends RecyclerView.Adapter<CameraPicsAdaptor.ViewHolder> {

    public Context activity;

    private List<String> listofpics = new ArrayList<>();

    private EditImages editImages;

    private String type;


    public CameraPicsAdaptor(Context activity, List<String> listofpics, String type, EditImages editImages) {


        this.activity = activity;

        this.listofpics = listofpics;

        this.editImages = editImages;

        this.type = type;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.camera_pics_item, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (type.equalsIgnoreCase("edit")) {

            holder.delete_pic.setVisibility(View.VISIBLE);
            File sd = Environment.getExternalStorageDirectory();
            if (listofpics.get(position) != null) {
                File image = new File(listofpics.get(position));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap mBitmapInsurance = BitmapFactory.decodeFile(image.getAbsolutePath(), options);
                holder.action_image.setImageBitmap(mBitmapInsurance);
            }

        } else {
            holder.delete_pic.setVisibility(View.GONE);

            Glide.with(activity).load(listofpics.get(position)).into(holder.action_image);
        }


    }


    @Override
    public int getItemCount() {

        return listofpics.size();
    }

    public void removeAt(int position) {

        listofpics.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listofpics.size());

        editImages.onRemove(position, listofpics);
    }

    public interface EditImages {

        void onRemove(int layoutposition, List<String> listofpics);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView action_image, delete_pic;

        private ViewHolder(View itemView) {

            super(itemView);

            delete_pic = itemView.findViewById(R.id.delete_pic);
            action_image = itemView.findViewById(R.id.action_image);

            delete_pic.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.delete_pic:
                    removeAt(getAdapterPosition());
                    break;


            }

        }
    }

}

