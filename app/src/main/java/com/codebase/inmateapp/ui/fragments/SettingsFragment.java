package com.codebase.inmateapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.codebase.inmateapp.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {


    private TextView tvGeneral, tvMessages, tvAutomation;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        tvGeneral = view.findViewById(R.id.tv_general);
        tvGeneral.setOnClickListener(this);

//        tvMessages = view.findViewById(R.id.tv_messages);
//        tvMessages.setOnClickListener(this);

        tvAutomation = view.findViewById(R.id.tv_automation);
        tvAutomation.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_general:
                Navigation.findNavController(view).navigate(R.id.navigation_general_settings);
                break;
//            case R.id.tv_messages:
//                Navigation.findNavController(view).navigate(R.id.navigation_messages_settings);
//                break;
            case R.id.tv_automation:
                Navigation.findNavController(view).navigate(R.id.navigation_automation_settings);
                break;
        }
    }
}