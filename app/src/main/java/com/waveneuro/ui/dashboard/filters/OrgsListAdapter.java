package com.waveneuro.ui.dashboard.filters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.waveneuro.R;
import com.waveneuro.data.model.response.patient.PatientListResponse;

import java.util.List;

public class OrgsListAdapter extends RecyclerView.Adapter<OrgsListAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onSelected(int id);
        void onDeselected(int id);
    }


    private List<PatientListResponse.Patient.Organization> orgs;

    private List<Integer> selected;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public OnItemClickListener listener;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final CheckBox cbOrganization;

        public ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            cbOrganization = view.findViewById(R.id.checkBox);

        }

    }

    public OrgsListAdapter(List<PatientListResponse.Patient.Organization> pt, List<Integer> sel) {
        orgs = pt;
        selected = sel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_organization, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.tvName.setText(orgs.get(position).getName());

        viewHolder.cbOrganization.setChecked(selected.contains(orgs.get(position).getId()));
        viewHolder.cbOrganization.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                listener.onSelected(orgs.get(position).getId());
            } else {
                listener.onDeselected(orgs.get(position).getId());
            }
        });

    }

    public int getItemCount() {
        return orgs.size();
    }
}
