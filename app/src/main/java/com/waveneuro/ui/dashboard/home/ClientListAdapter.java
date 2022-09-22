package com.waveneuro.ui.dashboard.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.waveneuro.R;
import com.waveneuro.data.model.response.patient.PatientResponse;

import java.util.List;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ViewHolder> {

    private List<PatientResponse.Patient> patients;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvOrganization;

        public ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            tvOrganization = view.findViewById(R.id.tvOrganization);
        }

    }

    public ClientListAdapter(List<PatientResponse.Patient> pt) {
        patients = pt;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_client, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.tvName.setText(patients.get(position).getFirstName() + " " + patients.get(position).getLastName());
        viewHolder.tvOrganization.setText(patients.get(position).getOrganizationName());
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }
}
