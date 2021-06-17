package com.tykle.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tykle.R;


public class WalkThroughFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View rootView;
    private LinearLayout linear_layout;
    private TextView tv_title, tv_description;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public WalkThroughFragment() {
        // Required empty public constructor
    }


    public static WalkThroughFragment newInstance(String param1, String param2) {
        WalkThroughFragment fragment = new WalkThroughFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_walk_through, container, false);
        initControl(rootView);


        return rootView;

    }

    private void initControl(View rootView) {
        linear_layout = (LinearLayout) rootView.findViewById(R.id.linear_layout);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_description = (TextView) rootView.findViewById(R.id.tv_description);

//        if (mParam1.equals("0")) {
//            Log.e("WalkThrough", "Frragment in if condition>>>>>>");
//            linear_layout.setBackgroundResource(R.drawable.walk_one);
//            tv_title.setText(getString(R.string.short_message));
//            tv_description.setText(getString(R.string.message));
//        } else if (mParam1.equals("1")) {
//            linear_layout.setBackgroundResource(R.drawable.walk_two);
//            tv_title.setText(getString(R.string.short_message));
//            tv_description.setText(getString(R.string.message));
//        } else if (mParam1.equals("2")) {
//            linear_layout.setBackgroundResource(R.drawable.walk_three);
//            tv_title.setText(getString(R.string.short_message));
//            tv_description.setText(getString(R.string.message));
//        }
    }


}
