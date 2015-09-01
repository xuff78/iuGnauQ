package com.xj.guanquan.fragment.contact;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xj.guanquan.R;
import com.xj.guanquan.activity.contact.QAddFriendActivity;
import com.xj.guanquan.activity.contact.QAddGroupActivity;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.common.QBaseFragment;
import com.xj.guanquan.fragment.found.QFindCircleFragment;

import java.util.ArrayList;

public class QContactFragment extends QBaseFragment {

    public static QContactFragment newInstance() {
        QContactFragment fragment = new QContactFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private ViewPager vPager;
    private View selectedView = null;
    private ArrayList<TextView> menu = new ArrayList<TextView>();
    private ImageView createNew;
    private static int selectIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_qcontact, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        TextView firend = (TextView) v.findViewById(R.id.firend);
        createNew = (ImageView) v.findViewById(R.id.createNew);
        createNew.setOnClickListener(listener);
        selectedView = firend;
        firend.setOnClickListener(listener);
        TextView group = (TextView) v.findViewById(R.id.group);
        group.setOnClickListener(listener);
        TextView attention = (TextView) v.findViewById(R.id.attention);
        attention.setOnClickListener(listener);
        TextView fans = (TextView) v.findViewById(R.id.fans);
        fans.setOnClickListener(listener);
        firend.setSelected(true);
        menu.add(firend);
        menu.add(group);
        menu.add(attention);
        menu.add(fans);
        vPager = (ViewPager) v.findViewById(R.id.mViewPager);
        vPager.setAdapter(new TCMainAdapter(getActivity().getSupportFragmentManager()));
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedView.setSelected(false);
                selectedView = menu.get(position);
                menu.get(position).setSelected(true);
                selectIndex = position;
                switch (position) {
                    case 0:
                        createNew.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        createNew.setVisibility(View.VISIBLE);
                        break;
                    default:
                        createNew.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (selectedView.getId() == v.getId())
                return;
            if (v == createNew) {
                if (selectIndex == 0) {
                    ((QBaseActivity) getActivity()).toActivity(QAddFriendActivity.class);
                } else if (selectIndex == 1) {
                    ((QBaseActivity) getActivity()).toActivity(QAddGroupActivity.class);
                }
            } else {
                selectedView.setSelected(false);
                selectedView = v;
                v.setSelected(true);
                switch (v.getId()) {
                    case R.id.firend:
                        vPager.setCurrentItem(0);
                        createNew.setVisibility(View.VISIBLE);
                        break;
                    case R.id.group:
                        vPager.setCurrentItem(1);
                        createNew.setVisibility(View.VISIBLE);
                        break;
                    case R.id.attention:
                        vPager.setCurrentItem(2);
                        break;
                    case R.id.fans:
                        vPager.setCurrentItem(3);
                        break;
                }
            }
        }
    };

    public static class TCMainAdapter extends FragmentStatePagerAdapter {
        public TCMainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 4;
        }

        //得到每个item
        @Override
        public Fragment getItem(int position) {
            Fragment frg = null;
            switch (position) {
                case 0:
                    frg = QFriendFragment.newInstance();
                    break;
                case 1:
                    frg = QFindCircleFragment.newInstance("group", null);
                    break;
                case 2:
                    frg = QUserFragment.newInstance(2);
                    break;
                case 3:
                    frg = QUserFragment.newInstance(3);
                    break;
            }
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
