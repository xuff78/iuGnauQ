package com.xj.guanquan.common;

import com.easemob.chat.EMChat;

import common.eric.com.ebaselibrary.common.EBaseApplication;

/**
 * Created by eric on 2015/9/7.
 */
public class QBaseApplication extends EBaseApplication {
    private QBaseApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        EMChat.getInstance().init(this);
        /**
         * debugMode == true 时为打开，sdk 会在log里输入调试信息
         * @param debugMode
         * 在做代码混淆的时候需要设置成false
         */
        EMChat.getInstance().setDebugMode(true);//在做打包混淆时，要关闭debug模式，如果未被关闭，则会出现程序无法运行问题
    }
}
