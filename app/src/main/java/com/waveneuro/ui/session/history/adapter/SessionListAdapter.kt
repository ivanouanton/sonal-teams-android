package com.waveneuro.ui.session.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.waveneuro.R
import com.waveneuro.databinding.ItemSessionBinding
import com.waveneuro.ui.model.Session

class SessionListAdapter(
    private val context: Context,
    private val sessions: List<Session>
) : RecyclerView.Adapter<SessionListAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemSessionBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            val session = sessions[absoluteAdapterPosition]
            with(binding) {
                tvName.text = session.name
                tvRd.text = session.rd ?: "n/a"
                tvSd.text = session.sd
                val text = context.getString(
                    if (session.isCompleted) R.string.completed else R.string.terminated)
                val color = ContextCompat.getColor(context,
                    if (session.isCompleted) R.color.aqua else R.color.gray_dim_dark)
                tvStatus.text = text
                tvStatus.setTextColor(color)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemSessionBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(holder: SessionListAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = sessions.size

}