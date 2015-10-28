package com.xj.guanquan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xj.guanquan.R;
import com.xj.guanquan.activity.roast.QPublishAct;
import com.xj.guanquan.activity.roast.ViewPagerExampleActivity;
import com.xj.guanquan.common.ApiList;
import com.xj.guanquan.fragment.roast.Photo9Layout;
import com.xj.guanquan.model.NoteInfo;
import com.xj.guanquan.model.TucaoCommentInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import common.eric.com.ebaselibrary.util.ScreenUtils;
import common.eric.com.ebaselibrary.util.ToastUtils;

/**
 * Created by 可爱的蘑菇 on 2015/9/2.
 */
public class TucaoCommentAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity act;
    ArrayList<TucaoCommentInfo> datalist = new ArrayList<TucaoCommentInfo>();
    int width = 0;
    TextView footer;
    int PageType=0;
    NoteInfo note;
    TextView favorBtnSingle;
    View.OnClickListener listener;

    public int getItemCount() {
        return datalist.size() + 3;
    }

    public int getItemViewType(int position) {
        return position == this.getItemCount() - 1 ? -1 : position;
    }

    LayoutInflater listInflater;

    public TucaoCommentAdapter(Activity act, ArrayList<TucaoCommentInfo> datalist, NoteInfo note, int PageType, View.OnClickListener listener) {
        this.act = act;
        this.PageType=PageType;
        listInflater = LayoutInflater.from(act);
        this.datalist = datalist;
        this.note=note;
        WindowManager wm = act.getWindowManager();
        this.listener=listener;
        width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
//        this.imgWidth=(ConstantUtil.getWidth(act)-ImageUtil.dip2px(act, 30))/2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        RecyclerView.ViewHolder holder = null;
        if (position == -1) {
            footer = new TextView(act);
            footer.setTextColor(Color.WHITE);
            footer.setGravity(Gravity.CENTER);
            footer.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, (int)(ScreenUtils.dpToPxInt(act, 30))));
            holder = new FooterViewHolder(footer);
        } else if (position == 1) {
            View v = listInflater.from(act).inflate(R.layout.tucao_joinmember, null);
            holder=new JoinHolder(v);
        }else {
            View v = listInflater.from(act).inflate(R.layout.tucao_item_detail, null);
            v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
            holder = new NoteHolder(v, position);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof FooterViewHolder) {
            return;
        } else if (position == 1) {
            JoinHolder jh = (JoinHolder)viewHolder;
        } else {
            if (position == 0) {
                NoteHolder vh = (NoteHolder) viewHolder;
                vh.userImg.setImageURI(Uri.parse(note.getAvatar()));
                vh.createTime.setText(note.getTime());
                String sex="♂ ";
                if(note.getSex()!=1){
                    sex="♀ ";
                }
                vh.userAge.setText(sex+note.getAge());
                vh.usrComment.setText(note.getContent());
                vh.favorBtn.setText(note.getLikeNum() + "");
                vh.replyNums.setText(note.getCommentNum() + "");
                vh.userName.setText(note.getNickName());
                if (note.getIsLike() == 0) {
                    vh.favorBtn.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.zan1),null,null,null);
                }else
                    vh.favorBtn.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.zan2),null,null,null);
                if (note.getPicture().length() > 0) {
                    final String[] urls = note.getPicture().split(",");
                    vh.photoLayout.removeAllViews();
                    Photo9Layout photo9Layout=new Photo9Layout(act, (int) (width - ScreenUtils.dpToPxInt(act, 90)), urls);
                    vh.photoLayout.addView(photo9Layout);
                    photo9Layout.setImgCallback(new Photo9Layout.ClickListener() {

                        @Override
                        public void onClick(View v, int position) {
                            Intent intent = new Intent(act, ViewPagerExampleActivity.class);
                            intent.putExtra("Images", urls);
                            intent.putExtra("pos", position);
                            act.startActivity(intent);
                        }
                    });
                }
                vh.bookBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(act, QPublishAct.class);
                        intent.putExtra("RequestType", QPublishAct.RequestJoin);
                        intent.putExtra("PageType", QPublishAct.TypeDate);
                        act.startActivity(intent);
                    }
                });
            }else{
                TucaoCommentInfo info=datalist.get(position-2); //因为第一个是原帖
                NoteHolder vh = (NoteHolder) viewHolder;
                vh.userImg.setImageURI(Uri.parse(info.getAvatar()));
                vh.createTime.setText(info.getTime());
                String sex="♂ ";
                if(info.getSex()!=1){
                    sex="♀ ";
                }
                vh.userAge.setText(sex+info.getAge());
                vh.usrComment.setText(info.getContent());
                vh.userName.setText(info.getNickName());
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View v) {
            super(v);
        }
    }

    public class JoinHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView userImg;
        TextView joinNum;
        LinearLayout imgsLayout;

        public JoinHolder(View v) {
            super(v);
            joinNum=(TextView)v.findViewById(R.id.joinNum);
            imgsLayout=(LinearLayout)v.findViewById(R.id.imgsLayout);
        }
    }

    public class NoteHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView userImg;
        LinearLayout photoLayout, hideMenuLayout;
        View bookBtn, dateExtraLayout, moreBtn;
        TextView userName, userAge, createTime, usrComment, favorBtn, replyNums, shareBtn;

        public NoteHolder(View itemView, int positon) {
            super(itemView);
            moreBtn=itemView.findViewById(R.id.moreBtn);
            hideMenuLayout = (LinearLayout)itemView.findViewById(R.id.hideMenuLayout);
            userImg = (SimpleDraweeView) itemView.findViewById(R.id.userImg);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userAge = (TextView) itemView.findViewById(R.id.userAge);
            createTime = (TextView) itemView.findViewById(R.id.createTime);
            usrComment = (TextView) itemView.findViewById(R.id.usrComment);
            favorBtn = (TextView) itemView.findViewById(R.id.favorBtn);
            favorBtnSingle=favorBtn;

            if(positon==0) {
                View complainBtn=itemView.findViewById(R.id.complainBtn);
                complainBtn.setOnClickListener(listener);
                View deleteBtn=itemView.findViewById(R.id.deleteBtn);
                deleteBtn.setOnClickListener(listener);
                favorBtn.setTag(positon);
                favorBtn.setOnClickListener(listener);
                replyNums = (TextView) itemView.findViewById(R.id.replyNums);
                replyNums.setTag(positon);
                replyNums.setOnClickListener(listener);
                shareBtn = (TextView) itemView.findViewById(R.id.shareBtn);
                shareBtn.setTag(positon);
                shareBtn.setOnClickListener(listener);
                photoLayout = (LinearLayout) itemView.findViewById(R.id.photoLayout);
                bookBtn = itemView.findViewById(R.id.bookBtn);
                bookBtn.setTag(positon);
                bookBtn.setOnClickListener(listener);

                moreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startAnimation(hideMenuLayout);
                    }
                });
            }else{
//                itemView.findViewById(R.id.photoLayout).setVisibility(View.GONE);
                itemView.findViewById(R.id.actionLayout).setVisibility(View.GONE);
            }
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


}
