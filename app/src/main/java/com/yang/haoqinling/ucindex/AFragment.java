package com.yang.haoqinling.ucindex;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_a, container, false);
        return view;
    }

    public void setFragmentPaddingTop(int top) {
        view.setPadding(0, top, 0, 0);
    }

}
