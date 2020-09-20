package com.codebase.inmateapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.codebase.inmateapp.R;
import com.codebase.inmateapp.utils.AppGlobals;
import com.codebase.inmateapp.utils.Helpers;

public class GeneralSettings extends Fragment implements View.OnClickListener {

    LinearLayout llUniqueID;
    TextView tvUniqueId;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_general_settings, container, false);

        llUniqueID = view.findViewById(R.id.ll_unique_id);
        llUniqueID.setOnClickListener(this);

        tvUniqueId = view.findViewById(R.id.tv_uid_current_value);

        setViews();

        return view;
    }

    private void setViews() {
        tvUniqueId.setText(AppGlobals.getUniqueId());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_unique_id) {
            Helpers.showFloatingInputDialog(requireContext(),
                    getString(R.string.label_enter_unique_id), AppGlobals.getUniqueId(), value -> {
                AppGlobals.setUniqueID(value);
                tvUniqueId.setText(value);
            });
        }
    }
}
