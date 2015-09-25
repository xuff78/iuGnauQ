//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xj.guanquan.Utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCount extends CountDownTimer {
    private Button codeBtn;
    private Context context;
    private long millisInFuture;
    private long countDownInterval;
    private static TimeCount timeCount = null;

    private TimeCount(long millisInFuture, long countDownInterval, Button view, Context context) {
        super(millisInFuture, countDownInterval);
        this.codeBtn = view;
        this.context = context;
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
    }

    public static TimeCount getInstance(long millisInFuture, long countDownInterval, Button view, Context context) {
        if (view != null || timeCount == null) {
            timeCount = new TimeCount(millisInFuture, countDownInterval, view, context);
        }

        return timeCount;
    }

    public void onTick(long millisUntilFinished) {
        if (this.codeBtn != null) {
            this.codeBtn.setClickable(false);
            this.codeBtn.setText(millisUntilFinished / 1000L + "秒");
        }

    }

    public void onFinish() {
        this.shutBtn();
    }

    public void onCover() {
        this.shutBtn();
    }

    public void onError() {
        if (timeCount != null) {
            timeCount.cancel();
            timeCount.onCover();
            timeCount = null;
        }

    }

    private void shutBtn() {
        if (this.codeBtn != null) {
            this.codeBtn.setText("重新获取");
            this.codeBtn.setClickable(true);
        }

    }
}
