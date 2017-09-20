package com.macate.minitpay.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.macate.minitpay.R;
import com.macate.minitpay.helpers.SessionManager;

import java.util.ArrayList;

/**
 * Created by Neerajakshi.Daggubat on 9/30/2016.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private SessionManager session;
    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private ArrayList<String> items;

    public CustomSpinnerAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        session = new SessionManager(getContext());
        String orders = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_custom_spinner, parent, false);
            viewHolder.tvText = (TextView) convertView.findViewById(R.id.tvText);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvText.setText(orders);
        return convertView;
    }


    public int getCount(){
        return items.size();
    }

    public String getItem(int position){
        return items.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    private static class ViewHolder {
        TextView tvText;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_custom_spinner_without_dropdown, parent, false);
        TextView tvText = (TextView) view.findViewById(R.id.tvText);
        tvText.setText(items.get(position));
        return view;
    }
}
