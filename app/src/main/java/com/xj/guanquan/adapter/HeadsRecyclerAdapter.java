package com.xj.guanquan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.roast.ViewPagerExampleActivity;

public class HeadsRecyclerAdapter extends RecyclerView.Adapter {
    //先定义两个ItemViewType，0代表头，1代表表格中间的部分
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    //数据源
    private String[] dataList;
    private Activity activity;

    //构造函数
    public HeadsRecyclerAdapter(String[] dataList, Activity act) {
        this.dataList = dataList;
        this.activity = act;
    }

    public void setDataList(String[] dataList) {
        this.dataList = dataList;
    }

    /**
     * 判断当前position是否处于第一个
     *
     * @param position
     * @return
     */
    public boolean isHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //在onCreateViewHolder方法中，我们要根据不同的ViewType来返回不同的ViewHolder
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            //对于Header，我们应该返回填充有Header对应布局文件的ViewHolder（再次我们返回的都是一个布局文件，请根据不同的需求做相应的改动）
            return new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_photos_head, null));
        } else {
            //对于Body中的item，我们也返回所对应的ViewHolder
            return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_photos_item, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HeaderViewHolder) {
            HeaderViewHolder hv = (HeaderViewHolder) viewHolder;
            hv.headImg.setImageURI(Uri.parse(dataList[position]));
        } else {
            ItemViewHolder iv = (ItemViewHolder) viewHolder;
            iv.picture.setImageURI(Uri.parse(dataList[position]));
        }
    }

    /**
     * 总条目数量是数据源数量+1，因为我们有个Header
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return dataList.length;
    }

    /**
     * 复用getItemViewType方法，根据位置返回相应的ViewType
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //如果是0，就是头，否则则是其他的item
        return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
    }

    /**
     * 给头部专用的ViewHolder，大家根据需求自行修改
     */
    public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private SimpleDraweeView headImg;
        private Intent intent = new Intent(activity, ViewPagerExampleActivity.class);

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headImg = (SimpleDraweeView) itemView.findViewById(R.id.picture);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        public SimpleDraweeView getHeadImg() {
            return headImg;
        }

        public void setHeadImg(SimpleDraweeView headImg) {
            this.headImg = headImg;
        }

        @Override
        public void onClick(View v) {
            intent.putExtra("Images", dataList);
            intent.putExtra("pos", getAdapterPosition());
            activity.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    /**
     * 给GridView中的条目用的ViewHolder
     */
    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private SimpleDraweeView picture;
        private Intent intent = new Intent(activity, ViewPagerExampleActivity.class);

        public ItemViewHolder(View itemView) {
            super(itemView);
            picture = (SimpleDraweeView) itemView.findViewById(R.id.picture);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public SimpleDraweeView getPicture() {
            return picture;
        }

        public void setPicture(SimpleDraweeView picture) {
            this.picture = picture;
        }


        @Override
        public void onClick(View v) {
            intent.putExtra("Images", dataList);
            intent.putExtra("pos", getAdapterPosition());
            activity.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}