package com.example.warantee;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WarantyAdapter extends ArrayAdapter<Waranty> {
    private Context mContext;
    private List<Waranty> warantyList = new ArrayList<>();

    public WarantyAdapter(Context context, ArrayList<Waranty> list) {
        super(context, 0 , list);
        mContext = context;
        warantyList = list;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) listItem = LayoutInflater.from(mContext).inflate(R.layout.activity_warantee_list_item, parent, false);
        Waranty currentWaranty = warantyList.get(position);
        TextView id = (TextView) listItem.findViewById(R.id.warantyItemID); id.setText(currentWaranty.getId());
        TextView seller = (TextView) listItem.findViewById(R.id.warantyItemSeller); seller.setText(currentWaranty.getSellerName());
        TextView days = (TextView) listItem.findViewById(R.id.warantyItemDays); days.setText(currentWaranty.getWarantyPeriod() + "");
        ImageView image = (ImageView) listItem.findViewById(R.id.warantyItemImage);
        ImageView category = (ImageView) listItem.findViewById(R.id.warantyItemCategory);
        return listItem;
    }
}
