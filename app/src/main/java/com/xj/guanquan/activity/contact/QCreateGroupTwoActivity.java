package com.xj.guanquan.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.Utils.ImageUtils;
import com.xj.guanquan.common.QBaseActivity;
import com.xj.guanquan.model.CircleInfo;

import java.util.ArrayList;
import java.util.List;

import common.eric.com.ebaselibrary.util.PreferencesUtils;
import common.eric.com.ebaselibrary.util.StringUtils;

public class QCreateGroupTwoActivity extends QBaseActivity implements View.OnClickListener {
    private CircleInfo circleInfo;
    private SimpleDraweeView groupImage;
    private LinearLayout addGroupIcon;
    private TextView village;
    private TextView merchantHourse;
    private TextView school;
    private ImageView createGroup;
    private RelativeLayout selectGroupAddress;
    private ListView searchPoiList;
    private TextView address;
    private Double lat;
    private Double lng;
    private PoiSearch.Query query;
    private List<PoiItem> poiItemList;
    private SearchPoiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qcreate_two_group);

    }

    @Override
    protected void initView() {
        _setHeaderTitle(getString(R.string.title_activity_qcreate_group));
        _setRightHomeGone();
        initialize();
        circleInfo = (CircleInfo) getIntent().getExtras().getSerializable("circleInfo");

        _setRightHomeText("下一步", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(circleInfo.getAddress())) {
                    showToastShort("请选择创建地点!");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("circleInfo", circleInfo);
                toActivity(QCreateGroupThreeActivity.class, bundle);
                QCreateGroupTwoActivity.this.finish();
            }
        });

        Bitmap bmp = ImageUtils.getSmallBitmap(circleInfo.getFile_logo());
        groupImage.setImageBitmap(bmp);
        poiItemList = new ArrayList<PoiItem>();
        adapter = new SearchPoiAdapter(poiItemList, this);
        searchPoiList.setAdapter(adapter);
        searchPoiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PoiItem poiItem = (PoiItem) parent.getAdapter().getItem(position);
                circleInfo.setAddress(poiItem.getTitle());
                address.setText(poiItem.getTitle());
            }
        });
    }

    @Override
    protected void initHandler() {

    }

    @Override
    public void onClick(View v) {
        if (v == selectGroupAddress) {
            Intent intent = new Intent(this, QMapSelectActivity.class);
            startActivityForResult(intent, 999);
        } else if (v == village) {
            village.setSelected(true);
            merchantHourse.setSelected(false);
            school.setSelected(false);
            initSearch("小区");
        } else if (v == merchantHourse) {
            merchantHourse.setSelected(true);
            village.setSelected(false);
            school.setSelected(false);
            initSearch("商用楼");
        } else if (v == school) {
            school.setSelected(true);
            village.setSelected(false);
            merchantHourse.setSelected(false);
            initSearch("学校");
        }
    }

    private void initialize() {

        groupImage = (SimpleDraweeView) findViewById(R.id.groupImage);
        addGroupIcon = (LinearLayout) findViewById(R.id.addGroupIcon);
        village = (TextView) findViewById(R.id.village);
        merchantHourse = (TextView) findViewById(R.id.merchantHourse);
        school = (TextView) findViewById(R.id.school);
        createGroup = (ImageView) findViewById(R.id.createGroup);
        selectGroupAddress = (RelativeLayout) findViewById(R.id.selectGroupAddress);
        searchPoiList = (ListView) findViewById(R.id.searchPoiList);
        address = (TextView) findViewById(R.id.address);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 999) {
                lat = data.getDoubleExtra("lat", 0.0);
                lng = data.getDoubleExtra("lng", 0.0);
                village.performClick();
            }
        }
    }

    private void initSearch(String keyWord) {
        query = new PoiSearch.Query(keyWord, "", PreferencesUtils.getString(this, "city_code"));
        // keyWord表示搜索字符串，第二个参数表示POI搜索类型，默认为：生活服务、餐饮服务、商务住宅
        //共分为以下20种：汽车服务|汽车销售|
        //汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
        //住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
        //金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
        //cityCode表示POI搜索区域，（这里可以传空字符串，空字符串代表全国在全国范围内进行搜索）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查第一页
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lng), 1000));//设置周边搜索的中心点以及区域
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                poiItemList = poiResult.getPois();
                adapter = new SearchPoiAdapter(poiItemList, QCreateGroupTwoActivity.this);
                searchPoiList.setAdapter(adapter);
            }

            @Override
            public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {

            }
        });//设置数据返回的监听器
        poiSearch.searchPOIAsyn();//开始搜索
    }

    class SearchPoiAdapter extends BaseAdapter {
        private List<PoiItem> poiItems;
        private Context context;

        public SearchPoiAdapter(List<PoiItem> poiItems, Context context) {
            this.poiItems = poiItems;
            this.context = context;
        }

        @Override
        public int getCount() {
            return poiItems.size();
        }

        @Override
        public Object getItem(int position) {
            return poiItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(context).inflate(R.layout.list_search_poi_item, null);
            TextView areaName = (TextView) convertView.findViewById(R.id.areaName);
            TextView distance = (TextView) convertView.findViewById(R.id.distance);
            PoiItem poiItem = poiItems.get(position);
            areaName.setText(poiItem.getTitle());
            distance.setText(poiItem.getDistance() + "m");
            return convertView;
        }
    }

}
