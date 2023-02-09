package com.waveneuro.ui.dashboard.filters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.databinding.ItemOrganizationBinding

class OrgsListAdapter(
    private val context: Context,
    private val orgs: List<OrganizationResponse>,
    private val selected: List<Int>,
    private val onSelected: (Int) -> Unit,
    private val onDeselected: (Int) -> Unit
) : RecyclerView.Adapter<OrgsListAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemOrganizationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                tvName.text = orgs[bindingAdapterPosition].name
                ivTick.isVisible = selected.contains(orgs[bindingAdapterPosition].id)
                root.setOnClickListener {
                    if (selected.contains(orgs[bindingAdapterPosition].id)) {
                        onDeselected(orgs[bindingAdapterPosition].id)
                        ivTick.visibility = View.INVISIBLE
                    } else {
                        onSelected(orgs[bindingAdapterPosition].id)
                        ivTick.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemOrganizationBinding.inflate(LayoutInflater.from(context), viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind()
    }

    override fun getItemCount(): Int {
        return orgs.size
    }

}