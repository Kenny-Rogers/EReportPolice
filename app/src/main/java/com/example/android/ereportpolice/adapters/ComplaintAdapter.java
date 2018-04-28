package com.example.android.ereportpolice.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.ereportpolice.R;
import com.example.android.ereportpolice.models.Complaint;

import java.util.ArrayList;

/**
 * Created by krogers on 3/1/18.
 */

public class ComplaintAdapter extends ArrayAdapter<Complaint> {
    public ComplaintAdapter(@NonNull Context context, ArrayList<Complaint> complaints) {
        super(context, 0, complaints);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View complaint_list_view = convertView;

        if (complaint_list_view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            complaint_list_view = inflater.inflate(R.layout.complaint_list_info_view, null);
        }


        TextView title = complaint_list_view.findViewById(R.id.tv_title);
        TextView description = complaint_list_view.findViewById(R.id.tv_description);

        Complaint complaint = getItem(position);

        title.setText(complaint.getNature_of_issue());
        description.setText(complaint.getDate_time_of_report());

        return complaint_list_view;
    }
}
