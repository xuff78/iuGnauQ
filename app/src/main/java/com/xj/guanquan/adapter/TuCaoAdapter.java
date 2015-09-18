package com.xj.guanquan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.roast.QPublishAct;
import com.xj.guanquan.activity.roast.TucaoDetailAct;
import com.xj.guanquan.activity.roast.ViewPagerExampleActivity;
import com.xj.guanquan.fragment.roast.Photo9Layout;
import com.xj.guanquan.model.DateInfo;
import com.xj.guanquan.model.NoteInfo;

import java.util.ArrayList;

import common.eric.com.ebaselibrary.util.ScreenUtils;

public class TuCaoAdapter extends RecyclerView.Adapter<ViewHolder> {

	Activity act;
	ArrayList<NoteInfo> datalist = new ArrayList<NoteInfo>();
	int width = 0;
	TextView footer;
	int PageType=0;
	private View.OnClickListener listener;

	public int getItemCount() {
		return datalist.size() + 1;
	}

	public int getItemViewType(int position) {
		return position == this.getItemCount() - 1 ? -1 : position;
	}

	LayoutInflater listInflater;

	public TuCaoAdapter(Activity act, ArrayList<NoteInfo> datalist, int PageType, View.OnClickListener listener) {
		this.act = act;
		this.PageType=PageType;
		listInflater = LayoutInflater.from(act);
		this.datalist = datalist;
		this.listener=listener;
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
			footer.setTextColor(Color.WHITE);
			footer.setGravity(Gravity.CENTER);
			footer.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, (int)(ScreenUtils.dpToPxInt(act, 30))));
			holder = new FooterViewHolder(footer);
		} else {
			View v = listInflater.from(act).inflate(R.layout.tucao_item_detail, null);
			v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
			holder = new NoteHolder(v, position);
		}
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		if (viewHolder instanceof FooterViewHolder) {
			return;
		} else {
			NoteHolder vh = (NoteHolder) viewHolder;
			vh.userImg.setImageURI(Uri.parse(datalist.get(position).getAvatar()));
			vh.createTime.setText(datalist.get(position).getTime());
			String sex="♂ ";
			if(datalist.get(position).getSex()!=1){
				sex="♀ ";
			}
			vh.userAge.setText(sex+datalist.get(position).getAge());
			vh.usrComment.setText(datalist.get(position).getContent());
			vh.favorBtn.setText(datalist.get(position).getIsLike()+"");
			vh.replyNums.setText(datalist.get(position).getCommentNum()+"");
			vh.userName.setText(datalist.get(position).getNickName());
			if(datalist.get(position).getPicture().length()>0) {
				final String[] urls = datalist.get(position).getPicture().split(",");
				vh.photoLayout.removeAllViews();
				Photo9Layout photo9Layout=new Photo9Layout(act, (int) (width - ScreenUtils.dpToPxInt(act, 90)), urls);
				vh.photoLayout.addView(photo9Layout);
				photo9Layout.setImgCallback(new Photo9Layout.ClickListener(){

					@Override
					public void onClick(View v, int position) {
						Intent intent=new Intent(act, ViewPagerExampleActivity.class);
						intent.putExtra("Images", urls);
						intent.putExtra("pos", position);
						act.startActivity(intent);
					}
				});
			}
			if(PageType==QPublishAct.TypeDate){
				DateInfo dateinfo= (DateInfo) datalist.get(position);
				vh.dateTime.setText(dateinfo.getBeginTime() + "");
				vh.dateAddr.setText(dateinfo.getAddress());
			}
			vh.bookBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

					Intent intent=new Intent(act, QPublishAct.class);
					intent.putExtra("NoteInfo", datalist.get(position));
					intent.putExtra("RequestType", QPublishAct.RequestJoin);
					intent.putExtra("PageType", QPublishAct.TypeDate);
					act.startActivity(intent);
				}
			});
		}
	}

	public class FooterViewHolder extends RecyclerView.ViewHolder {
		public FooterViewHolder(View v) {
			super(v);
		}
	}

	public class NoteHolder extends RecyclerView.ViewHolder {

		SimpleDraweeView userImg;
		LinearLayout photoLayout, hideMenuLayout;
		View bookBtn, dateExtraLayout, moreBtn;
		TextView userName, userAge, createTime, usrComment, favorBtn, replyNums, shareBtn, dateTime, dateAddr;

		public NoteHolder(View itemView, final int position) {
			super(itemView);
			moreBtn=itemView.findViewById(R.id.moreBtn);
			hideMenuLayout = (LinearLayout)itemView.findViewById(R.id.hideMenuLayout);
			userImg = (SimpleDraweeView) itemView.findViewById(R.id.userImg);
			dateTime = (TextView) itemView.findViewById(R.id.dateTime);
			dateAddr = (TextView) itemView.findViewById(R.id.dateAddr);
			userName = (TextView) itemView.findViewById(R.id.userName);
			userAge = (TextView) itemView.findViewById(R.id.userAge);
			createTime = (TextView) itemView.findViewById(R.id.createTime);
			usrComment = (TextView) itemView.findViewById(R.id.usrComment);
			favorBtn = (TextView) itemView.findViewById(R.id.favorBtn);
			favorBtn.setTag(position);
			favorBtn.setOnClickListener(listener);
			replyNums = (TextView) itemView.findViewById(R.id.replyNums);
			replyNums.setTag(position);
			replyNums.setOnClickListener(listener);
			shareBtn = (TextView) itemView.findViewById(R.id.shareBtn);
			shareBtn.setTag(position);
			shareBtn.setOnClickListener(listener);
			photoLayout = (LinearLayout) itemView.findViewById(R.id.photoLayout);
			bookBtn = itemView.findViewById(R.id.bookBtn);
			bookBtn.setTag(position);
			bookBtn.setOnClickListener(listener);
			View complainBtn=itemView.findViewById(R.id.complainBtn);
			complainBtn.setTag(position);
			complainBtn.setOnClickListener(listener);
			View deleteBtn=itemView.findViewById(R.id.deleteBtn);
			deleteBtn.setTag(position);
			deleteBtn.setOnClickListener(listener);
			if(PageType==1)
				itemView.findViewById(R.id.dateExtraLayout).setVisibility(View.VISIBLE);
			itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

					Intent intent=new Intent(act, TucaoDetailAct.class);
					intent.putExtra("PageType", PageType);
					intent.putExtra("NoteInfo", datalist.get(position));
					act.startActivity(intent);
				}
			});
			moreBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					startAnimation(hideMenuLayout);
				}
			});
		}
	}

	private void startAnimation(final View view){
		float fromAlpha=0, toAlpha=1;
		float fromScaleX=0.1f, toScaleX=1f;
		final boolean isShown=view.isShown();
		if(isShown){
			fromAlpha=1; toAlpha=0;
			fromScaleX=1f; toScaleX=0.1f;
		}
		final AnimationSet anim=new AnimationSet(true);
		AlphaAnimation alpha=new AlphaAnimation(fromAlpha,toAlpha);
		ScaleAnimation ra=new ScaleAnimation(fromScaleX, toScaleX, 1, 1, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,0f);
		anim.addAnimation(alpha);
		anim.setInterpolator(act, android.R.anim.decelerate_interpolator);
		anim.addAnimation(ra);
		anim.setDuration(300);
		anim.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(isShown){
					view.setVisibility(View.GONE);
				}
				view.clearAnimation();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
		if(!isShown){
			view.setVisibility(View.VISIBLE);
		}
		view.startAnimation(anim);
		view.invalidate();
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
