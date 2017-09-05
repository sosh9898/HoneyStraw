package com.jyoung.honeystraw.ui.tabs.brand;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.model.Brand;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by jyoung on 2017. 8. 1..
 */

public class BrandRecyclerAdapter extends RecyclerView.Adapter {
    public List<Brand> brandList;
    public View.OnClickListener onClickListener;

    public BrandRecyclerAdapter(List<Brand> brandList, View.OnClickListener onClickListener) {
        this.brandList = brandList;
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_brand_item, parent, false);
                view.setOnClickListener(onClickListener);
                return new BrandViewHolder(view);

 }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BrandViewHolder)holder).bindView(brandList.get(position));
    }


    @Override
    public int getItemCount() {
        return brandList != null ? brandList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return  brandList.get(position).getViewType();
    }

    public class BrandViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.brandname_text)TextView brandNameText;
        @InjectView(R.id.brandname_image)ImageView brandNameImage;

        public BrandViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindView(Brand brandList){

            brandNameText.setText(brandList.getBrandName());
            Glide.with(brandNameImage.getContext()).load(brandList.getLogo()).into(brandNameImage);
        }
    }
}