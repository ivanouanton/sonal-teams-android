package com.waveneuro.ui.session.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.waveneuro.R;
import com.waveneuro.data.model.response.patient.PatientListResponse;

import java.util.List;

public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.ViewHolder> {


    private List<Session> sessions;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvRd;
        private final TextView tvSd;
        private final TextView tvStatus;

        public ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            tvRd = view.findViewById(R.id.tvRd);
            tvSd = view.findViewById(R.id.tvSd);
            tvStatus = view.findViewById(R.id.tvStatus);
        }

    }

    public SessionListAdapter(List<Session> s) {
        sessions = s;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_session, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.tvName.setText(sessions.get(position).name);
        viewHolder.tvRd.setText(sessions.get(position).rd);
        viewHolder.tvSd.setText(sessions.get(position).sd);
        viewHolder.tvStatus.setText(sessions.get(position).status);

    }

    public int getItemCount() {
        return sessions.size();
    }


}
