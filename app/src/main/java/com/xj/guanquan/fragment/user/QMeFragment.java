package com.xj.guanquan.fragment.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.found.QUserDetailActivity;
import com.xj.guanquan.activity.user.QLoginActivity;
import com.xj.guanquan.activity.user.QSystemSetActivity;
import com.xj.guanquan.activity.user.QVisitorListActivity;
import com.xj.guanquan.common.QBaseActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link QMeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QMeFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SimpleDraweeView headImage;
    private TextView name;
    private TextView circleNum;
    private RelativeLayout selfDataArea;
    private RelativeLayout signArea;
    private RelativeLayout visitorArea;
    private RelativeLayout vipArea;
    private RelativeLayout mallArea;
    private RelativeLayout newerArea;
    private RelativeLayout systemArea;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QFindUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QMeFragment newInstance(String param1, String param2) {
        QMeFragment fragment = new QMeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public QMeFragment() {
        // Required empty public constructor
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qme, container, false);
    }

    private void initialize(View view) {
        headImage = (SimpleDraweeView) view.findViewById(R.id.avatar);
        name = (TextView) view.findViewById(R.id.name);
        circleNum = (TextView) view.findViewById(R.id.circleNum);
        selfDataArea = (RelativeLayout) view.findViewById(R.id.selfDataArea);
        signArea = (RelativeLayout) view.findViewById(R.id.signArea);
        visitorArea = (RelativeLayout) view.findViewById(R.id.visitorArea);
        vipArea = (RelativeLayout) view.findViewById(R.id.vipArea);
        mallArea = (RelativeLayout) view.findViewById(R.id.mallArea);
        newerArea = (RelativeLayout) view.findViewById(R.id.newerArea);
        systemArea = (RelativeLayout) view.findViewById(R.id.systemArea);


        newerArea.setOnClickListener(this);
        vipArea.setOnClickListener(this);
        visitorArea.setOnClickListener(this);
        signArea.setOnClickListener(this);
        selfDataArea.setOnClickListener(this);
        circleNum.setOnClickListener(this);
        name.setOnClickListener(this);
        headImage.setOnClickListener(this);
        mallArea.setOnClickListener(this);
        systemArea.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final View clickView = v;
        ((QBaseActivity) getActivity()).alertConfirmDialog("您还没有登录，是否先去登录呢?",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((QBaseActivity) getActivity()).toActivity(QLoginActivity.class);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickView == systemArea) {
                            ((QBaseActivity) getActivity()).toActivity(QSystemSetActivity.class);
                        } else if (clickView == visitorArea) {
                            ((QBaseActivity) getActivity()).toActivity(QVisitorListActivity.class);
                        } else if (clickView == selfDataArea) {
                            ((QBaseActivity) getActivity()).toActivity(QUserDetailActivity.class);
                        }
                    }
                });
    }
}
