package com.waveneuro.ui.dashboard.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.waveneuro.R;
import com.waveneuro.data.model.response.patient.PatientListResponse;

import java.util.List;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(PatientListResponse.Patient patient);
        void onStartSessionClick(PatientListResponse.Patient patient);
    }


    private List<PatientListResponse.Patient> patients;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OnItemClickListener listener;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvOrganization;
        private final ImageView ivStartSession;

        public ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            tvOrganization = view.findViewById(R.id.tvOrganization);
            ivStartSession = view.findViewById(R.id.ivStartSession);
        }

    }

    public ClientListAdapter(List<PatientListResponse.Patient> pt) {
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
        viewHolder.ivStartSession.setOnClickListener(view -> listener.onStartSessionClick(patients.get(position)));
        viewHolder.itemView.setOnClickListener(view -> listener.onItemClick(patients.get(position)));

    }

    public int getItemCount() {
        return patients.size();
    }
}
