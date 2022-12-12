package com.waveneuro.ui.dashboard.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.waveneuro.R
import com.waveneuro.data.model.response.patient.PatientListResponse.Patient
import timber.log.Timber

private const val PAGE_SIZE = 10

class ClientListAdapter(private val listener: ClientListAdapter.OnItemClickListener) :
    ListAdapter<Patient, ClientListAdapter.ViewHolder>(DiffCallback()){

    interface OnItemClickListener {
        fun onItemClick(patient: Patient?)
        fun onStartSessionClick(patient: Patient?)
        fun onListEnded()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView
        private val tvOrganization: TextView
        private val ivStartSession: ImageView

        init {
            tvName = view.findViewById(R.id.tv_name)
            tvOrganization = view.findViewById(R.id.tvOrganization)
            ivStartSession = view.findViewById(R.id.ivStartSession)
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Patient, position: Int) = with(itemView) {
            tvName.text =
                item.firstName + " " + item.lastName
            tvOrganization.text = item.organizationName
            ivStartSession.setOnClickListener { view: View? ->
                listener.onStartSessionClick(item)
            }
            itemView.setOnClickListener { view: View? ->
                listener.onItemClick(item)
            }
            if ((position + 1) % itemCount == 0 && (position + 1) % PAGE_SIZE == 0) {
                listener.onListEnded()
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_client, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ClientListAdapter.ViewHolder, position: Int) {
        viewHolder.bind(getItem(position), position)
    }

    class DiffCallback : DiffUtil.ItemCallback<Patient>() {
        override fun areItemsTheSame(oldItem: Patient, newItem: Patient): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Patient, newItem: Patient): Boolean = oldItem.id == newItem.id
    }

}