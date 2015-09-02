package com.xj.guanquan.fragment.roast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xj.guanquan.R;

import com.xj.guanquan.activity.roast.QPublishAct;
import com.xj.guanquan.common.QBaseFragment;

import java.util.ArrayList;

import common.eric.com.ebaselibrary.adapter.RecyclerViewAdapter;

public class TucaoMianFrg extends QBaseFragment {

    int PageType=0;

    public static TucaoMianFrg newInstance() {
        TucaoMianFrg fragment = new TucaoMianFrg();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private ViewPager vPager;
    private View selectedView=null;
    private ArrayList<TextView> menu=new ArrayList<TextView>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tucao_mian_frg, container, false);

        TextView leftBtn= (TextView) v.findViewById(R.id.leftBtn);
        selectedView=leftBtn;
        leftBtn.setOnClickListener(listener);
        TextView midBtn= (TextView) v.findViewById(R.id.midBtn);
        midBtn.setOnClickListener(listener);
        TextView rightBtn= (TextView) v.findViewById(R.id.rightBtn);
        rightBtn.setOnClickListener(listener);
        leftBtn.setSelected(true);
        menu.add(leftBtn);
        menu.add(midBtn);
        menu.add(rightBtn);
        vPager=(ViewPager)v.findViewById(R.id.mViewPager);
        vPager.setAdapter(new TCMainAdapter(getActivity().getSupportFragmentManager()));
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PageType=position;
                selectedView.setSelected(false);
                selectedView = menu.get(position);
                menu.get(position).setSelected(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        v.findViewById(R.id.createNew).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), QPublishAct.class);
                intent.putExtra("PageType", PageType);
                startActivity(intent);
            }
        });
        return v;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(selectedView.getId()==v.getId())
                return;
            selectedView.setSelected(false);
            selectedView=v;
            v.setSelected(true);
            switch (v.getId()){
                case R.id.leftBtn:
                    vPager.setCurrentItem(0);
                    break;
                case R.id.midBtn:
                    vPager.setCurrentItem(1);
                    break;
                case R.id.rightBtn:
                    vPager.setCurrentItem(2);
                    break;
            }
        }
    };

    public static class TCMainAdapter extends FragmentStatePagerAdapter {
        public TCMainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        //得到每个item
        @Override
        public Fragment getItem(int position) {
            Fragment frg=TucaoListFrg.newInstance(position);
//            switch (position){
//                case 0:
//                    frg=TucaoListFrg.newInstance(position);
//                    break;
//                case 1:
//                    frg=TucaoListFrg.newInstance(position);
//                    break;
//                case 2:
//                    frg=TucaoListFrg.newInstance(position);
//                    break;
//            }
            return frg;
        }


        // 初始化每个页卡选项
        @Override
        public Object instantiateItem(ViewGroup arg0, int arg1) {
            // TODO Auto-generated method stub
            return super.instantiateItem(arg0, arg1);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

    }

}
