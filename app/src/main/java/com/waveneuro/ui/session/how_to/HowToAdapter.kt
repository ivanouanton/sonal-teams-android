package com.waveneuro.ui.session.how_to

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.waveneuro.R

class HowToAdapter(private val mContext: Context) : PagerAdapter() {
    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val modelObject = HowToModel.values()[position]
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.item_how_to, collection, false) as ViewGroup
        (layout.findViewById<View>(R.id.tv_name) as TextView).setText(modelObject.titleRes)
        Glide.with(mContext).load(modelObject.drawableRes)
            .into((layout.findViewById<View>(R.id.iv_illustration) as ImageView))
        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return HowToModel.values().size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int): CharSequence {
        val customPagerEnum = HowToModel.values()[position]
        return mContext.getString(customPagerEnum.titleRes)
    }
}