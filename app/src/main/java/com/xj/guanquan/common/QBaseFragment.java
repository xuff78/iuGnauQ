package com.xj.guanquan.common;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.xj.guanquan.views.CustomProgressDialog;

import common.eric.com.ebaselibrary.common.EBaseApplication;

public class QBaseFragment extends Fragment implements Response.Listener, Response.ErrorListener {
    private CustomProgressDialog progressDialog;

    private OnFragmentListener mListener;

    // TODO: Rename method, update argument and hook method into UI event
    public void loadFragment(Fragment frg) {
        if (mListener != null) {
            mListener.onLoad(frg);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentListener {
        // TODO: Update argument type and name
        public void onLoad(Fragment frg);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        getProgressDialog().dismiss();
        ((QBaseActivity) getActivity()).alertDialog(error.toString(), null);
    }

    @Override
    public void onResponse(Object response) {
        VolleyLog.d("onResponse", response.toString());
        if (getProgressDialog().isShowing())
            getProgressDialog().dismiss();
        Log.i("Net", response.toString());
    }

    private CustomProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressUtil.getProgressDialog(getActivity());
        }
        return progressDialog;
    }

    public <T> void addToRequestQueue(Request<T> req, Boolean isShowDialog) {
        if ((!getProgressDialog().isShowing()) && isShowDialog)
            getProgressDialog().show();
        req.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        ((EBaseApplication) getActivity().getApplication()).addToRequestQueue(req);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag, Boolean isShowDialog) {
        if (!getProgressDialog().isShowing() && isShowDialog)
            getProgressDialog().show();
        req.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 1, 1.0f));
        ((EBaseApplication) getActivity().getApplication()).addToRequestQueue(req, tag);
    }

}
