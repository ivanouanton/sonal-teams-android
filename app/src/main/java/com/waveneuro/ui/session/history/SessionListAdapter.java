package com.waveneuro.ui.session.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import androidx.recyclerview.widget.RecyclerView;

import com.waveneuro.R;

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

            tvName = view.findViewById(R.id.tv_name);
            tvRd = view.findViewById(R.id.tv_rd);
            tvSd = view.findViewById(R.id.tv_sd);
            tvStatus = view.findViewById(R.id.tv_status);
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
        viewHolder.tvRd.setText(sessions.get(position).rd==null?"n/a":sessions.get(position).rd);
        viewHolder.tvSd.setText(sessions.get(position).sd);

        Context context = viewHolder.itemView.getContext();

        String completed = context.getString(R.string.completed);
        String terminated = context.getString(R.string.terminated);
        viewHolder.tvStatus.setText(sessions.get(position).isCompleted?completed:terminated);

        int color = ContextCompat.getColor(context, sessions.get(position).isCompleted?R.color.aqua: R.color.gray_dim_dark);
        viewHolder.tvStatus.setTextColor(color);
    }

    public int getItemCount() {
        return sessions.size();
    }
}
