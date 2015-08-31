package com.xj.guanquan.common;

import android.content.Context;

import com.xj.guanquan.views.CustomProgressDialog;


public class ProgressUtil {

    public static CustomProgressDialog getProgressDialog(Context con) {
        CustomProgressDialog dialog = new CustomProgressDialog(con);
        return dialog;
    }

}
