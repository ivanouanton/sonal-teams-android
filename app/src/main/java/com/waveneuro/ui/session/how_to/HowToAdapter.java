package com.waveneuro.ui.session.how_to;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.waveneuro.R;

public class HowToAdapter extends PagerAdapter {

    private Context mContext;

    public HowToAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        HowToModel modelObject = HowToModel.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_how_to, collection, false);
        ((TextView)layout.findViewById(R.id.tv_name)).setText(modelObject.getTitleRes());
        Glide.with(mContext).load(modelObject.getDrawableRes()).into((ImageView) layout.findViewById(R.id.iv_illustration));
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }



    @Override
    public int getCount() {
        return HowToModel.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        HowToModel customPagerEnum = HowToModel.values()[position];
        return mContext.getString(customPagerEnum.getTitleRes());
    }

}