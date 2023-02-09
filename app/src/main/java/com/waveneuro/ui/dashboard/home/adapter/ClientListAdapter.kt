package com.waveneuro.ui.dashboard.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.waveneuro.R
import com.waveneuro.databinding.ItemClientBinding
import com.waveneuro.ui.base.recycler.BaseEntityListAdapter
import com.waveneuro.ui.base.recycler.EntityVH
import com.waveneuro.ui.dashboard.home.adapter.model.PatientItem
import com.waveneuro.utils.ext.px
import timber.log.Timber

class ClientListAdapter(
    context: Context,
    private val onItemClick: (PatientItem) -> Unit,
    private val onStartSessionClick: (PatientItem) -> Unit,
    private val onMoreRequested: () -> Unit,
) : BaseEntityListAdapter<PatientItem, ClientListAdapter.PatientVH>(
    context,
    PatientItem.DiffCallback
){

    var totalCurrentCount: Int = 0

    override fun getItemCount(): Int {
        val itemCount = super.getItemCount()
        totalCurrentCount = itemCount

        return itemCount
    }

    private val customTypeface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.resources.getFont(R.font.inter_bold)
    } else {
        ResourcesCompat.getFont(context, R.font.inter_bold)
    }


    inner class PatientVH(
        private val binding: ItemClientBinding
    ) : EntityVH<PatientItem>(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun bind(model: PatientItem) {
            super.bind(model)
            with(binding) {
                tvName.text = "${model.firstName} ${model.lastName}"
                tvOrganization.text = model.organization.name
                ivStartSession.setOnClickListener {
                    onStartSessionClick(model)
                }
                itemView.setOnClickListener {
                    onItemClick(model)
                }
                val startImage = if (model.isTosSigned) R.drawable.ic_start_session
                    else R.drawable.ic_start_session_disable
                ivStartSession.setImageResource(startImage)

                val initials = "${model.firstName[0]}${model.lastName[0]}".trim().uppercase()

                Glide.with(binding.root.context)
                    .load(model.imageURLString)
                    .error(getBitmapWithInitials(initials))
                    .into(ivProfileImage)
            }
        }

        private fun getBitmapWithInitials(initials: String) : Bitmap {
            val placeHolder: Bitmap = Bitmap.createBitmap(38.px.toInt(),
                38.px.toInt(), Bitmap.Config.ARGB_8888)

            val canvas = Canvas(placeHolder)
            val paint = Paint().apply {
                textSize = 16.px
                typeface = customTypeface
                color = Color.BLACK
            }
            val width = paint.measureText(initials)
            val fm = paint.fontMetrics
            val x = (38.px - width)/2
            val y = 38.px/2 + fm.bottom
            canvas.drawText(initials, x, y, paint)

            return placeHolder
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientVH =
        PatientVH(ItemClientBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: PatientVH, position: Int) {
        super.onBindViewHolder(holder, position)

        if (position == totalCurrentCount - 1) {
            onMoreRequested()
        }
    }

}