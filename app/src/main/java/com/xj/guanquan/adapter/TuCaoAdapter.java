package com.xj.guanquan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.roast.QPublishAct;
import com.xj.guanquan.fragment.roast.Photo9Layout;
import com.xj.guanquan.model.NoteInfo;

import java.util.ArrayList;

import common.eric.com.ebaselibrary.util.ScreenUtils;

public class TuCaoAdapter extends RecyclerView.Adapter<ViewHolder> {

	Activity act;
	ArrayList<NoteInfo> datalist = new ArrayList<NoteInfo>();
	int width = 0;
	TextView footer;
	int PageType=0;

	public int getItemCount() {
		return datalist.size() + 1;
	}

	public int getItemViewType(int position) {
		return position == this.getItemCount() - 1 ? -1 : position;
	}

	LayoutInflater listInflater;

	public TuCaoAdapter(Activity act, ArrayList<NoteInfo> datalist, int PageType) {
		this.act = act;
		this.PageType=PageType;
		listInflater = LayoutInflater.from(act);
		this.datalist = datalist;

		WindowManager wm = act.getWindowManager();

		width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
//        this.imgWidth=(ConstantUtil.getWidth(act)-ImageUtil.dip2px(act, 30))/2;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		ViewHolder holder = null;
		if (position == -1) {
			footer = new TextView(act);
			footer.setGravity(Gravity.CENTER_HORIZONTAL);
			footer.setPadding(0, 20, 0, 0);
//			footer.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ImageUtil.dip2px(act,90)));
			holder = new FooterViewHolder(footer);
		} else {
			View v = listInflater.from(act).inflate(R.layout.tucao_item_detail, null);
			v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
			holder = new NoteHolder(v);
		}
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		if (viewHolder instanceof FooterViewHolder) {
			return;
		} else {
			NoteHolder vh = (NoteHolder) viewHolder;
			vh.createTime.setText(datalist.get(position).getCreateTime());
			vh.userAge.setText(datalist.get(position).getUserAge());
			vh.usrComment.setText(datalist.get(position).getUsrComment());
			vh.favorBtn.setText(datalist.get(position).getFavorBtn());
			vh.replyNums.setText(datalist.get(position).getReplyNums());
			vh.userName.setText(datalist.get(position).getUserName());
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int i = 0; i < position; i++) {
				list.add(R.mipmap.testphoto);
			}
			vh.photoLayout.removeAllViews();
			vh.photoLayout.addView(new Photo9Layout(act, (int) (width - ScreenUtils.dpToPxInt(act, 90)), list));
			vh.bookBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

					Intent intent=new Intent(act, QPublishAct.class);
					intent.putExtra("PageType", QPublishAct.TypeJoin);
					act.startActivity(intent);
				}
			});
		}
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {

		}
	};

	public class FooterViewHolder extends RecyclerView.ViewHolder {
		public FooterViewHolder(View v) {
			super(v);
		}
	}

	public class NoteHolder extends RecyclerView.ViewHolder {
		ImageView userImg;
		LinearLayout photoLayout;
		View bookBtn, dateExtraLayout;
		TextView userName, userAge, createTime, usrComment, favorBtn, replyNums, shareBtn;

		public NoteHolder(View itemView) {
			super(itemView);
			userImg = (SimpleDraweeView) itemView.findViewById(R.id.headImage);
			userName = (TextView) itemView.findViewById(R.id.userName);
			userAge = (TextView) itemView.findViewById(R.id.userAge);
			createTime = (TextView) itemView.findViewById(R.id.createTime);
			usrComment = (TextView) itemView.findViewById(R.id.usrComment);
			favorBtn = (TextView) itemView.findViewById(R.id.favorBtn);
			replyNums = (TextView) itemView.findViewById(R.id.replyNums);
			shareBtn = (TextView) itemView.findViewById(R.id.shareBtn);
			photoLayout = (LinearLayout) itemView.findViewById(R.id.photoLayout);
			bookBtn = itemView.findViewById(R.id.bookBtn);
			if(PageType==1)
				itemView.findViewById(R.id.dateExtraLayout).setVisibility(View.VISIBLE);
		}
	}

	public void isLoadMore(boolean isMore) {
		if (this.footer != null) {
			if (isMore) {
				this.footer.setText("正在加载中");
			} else {
				this.footer.setText("全部加载完成");
			}
		}

	}


}
