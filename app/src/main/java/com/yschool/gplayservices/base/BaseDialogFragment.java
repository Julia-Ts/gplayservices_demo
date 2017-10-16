package com.yschool.gplayservices.base;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;

public abstract class BaseDialogFragment extends AppCompatDialogFragment {

    public abstract String getFragmentTag();

    public void show(FragmentManager manager) {
        super.show(manager, getFragmentTag());
    }

}
