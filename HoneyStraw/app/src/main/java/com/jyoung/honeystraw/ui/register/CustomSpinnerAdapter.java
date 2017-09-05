package com.jyoung.honeystraw.ui.register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.model.Brand;

import java.util.List;

/**
 * Created by jyoung on 2017. 8. 9..
 */

public class CustomSpinnerAdapter extends BaseAdapter{
    Context context;
    List<Brand> brandList;
    LayoutInflater inflater;


    public CustomSpinnerAdapter(Context context, List<Brand> brandList){
        this.context = context;
        this.brandList = brandList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return brandList != null ? brandList.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = inflater.inflate(R.layout.custom_spinner_normal, parent, false);
        }

        if(brandList!=null){
            String text =brandList.get(position).getBrandName();
            ((TextView)convertView.findViewById(R.id.spinner_normal_text)).setText(text);
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.custom_spinner_dropdown, parent, false);
        }
        String text = brandList.get(position).getBrandName();
        ((TextView)convertView.findViewById(R.id.spinner_drop_text)).setText(text);

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return brandList.get(position).getBrandName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
