package com.essensys.cashsaverz.model;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Created by root on 21/12/17.
 */

public abstract class CustomFragment extends Fragment {

    public CustomFragment() {
    }

    public abstract void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                    @NonNull int[] grantResults);

    public abstract void onBackPressed();
}
