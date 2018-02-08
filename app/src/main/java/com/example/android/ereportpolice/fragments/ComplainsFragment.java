package com.example.android.ereportpolice.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.ereportpolice.R;
import com.example.android.ereportpolice.utils.Utils;

/**
 * Created by krogers on 2/1/18.
 */

public class ComplainsFragment extends Fragment {
    Button btn_stop_tracking;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_complain, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //equivalent to onCreate of an Activity

        Toast.makeText(getActivity(), "Complains Fragment Created", Toast.LENGTH_LONG).show();
        btn_stop_tracking = view.findViewById(R.id.btn_stop_tracking);

        //stop getting the location after five secs
        btn_stop_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.stopAlarm(getActivity().getApplicationContext());
            }
        });
    }
}
