package com.macate.minitpay.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.macate.minitpay.R;
import com.macate.minitpay.models.Items;
import com.macate.minitpay.models.Transactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Neerajakshi.Daggubat on 1/25/2017.
 */
public class ItemsAdapter extends ArrayAdapter<Items> {
    public static  String selectedId;

    public ItemsAdapter(Context context, ArrayList<Items> item) {
        super(context, 0, item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        Items item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_items, parent, false);
            viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
            viewHolder.tvItemTotalPrice = (TextView) convertView.findViewById(R.id.tvItemTotalPrice);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvItemName.setText(item.getItemName());
        viewHolder.tvItemTotalPrice.setText(item.getTotalItemPrice());
        return convertView;
    }

    private static class ViewHolder {
        TextView tvItemName, tvItemTotalPrice;
    }

}

