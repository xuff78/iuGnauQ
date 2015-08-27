package com.xj.guanquan.common;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class QBaseFragment extends Fragment {


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

}
