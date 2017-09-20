package com.macate.minitpay.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.macate.minitpay.R;
import com.macate.minitpay.helpers.SessionManager;
import com.macate.minitpay.models.PaymentSources;

import java.util.ArrayList;

/**
 * Created by Neerajakshi.Daggubat on 9/23/2016.
 */
public class PaymentSourcesListAdapter extends ArrayAdapter<PaymentSources> {
    private SessionManager session;
    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<PaymentSources> items;

    public PaymentSourcesListAdapter(Context context, ArrayList<PaymentSources> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        session = new SessionManager(getContext());
        PaymentSources orders = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_payment_source_withour_drop_down_icon, parent, false);
            viewHolder.tvCardType = (TextView) convertView.findViewById(R.id.tvCardType);
            viewHolder.tvCardNo = (TextView) convertView.findViewById(R.id.tvCardNo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvCardType.setText(orders.getCardType());
        viewHolder.tvCardNo.setText(orders.getCardNo());
        return convertView;
    }


    public int getCount(){
        return items.size();
    }

    public PaymentSources getItem(int position){
        return items.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    private static class ViewHolder {
        TextView tvCardType, tvCardNo;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_payment_source_withour_drop_down_icon, parent, false);
        TextView tvCardType = (TextView) view.findViewById(R.id.tvCardType);
        TextView tvCardNo = (TextView) view.findViewById(R.id.tvCardNo);
        tvCardType.setText(items.get(position).getCardType());
        tvCardNo.setText(items.get(position).getCardNo());
        return view;
    }
}

