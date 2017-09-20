package com.macate.minitpay.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.macate.minitpay.R;
import com.macate.minitpay.models.Transactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Neerajakshi.Daggubat on 1/24/2017.
 */
public class TransactionsAdapter extends ArrayAdapter<Transactions> {
    public static  String selectedId;

    public TransactionsAdapter(Context context, ArrayList<Transactions> item) {
        super(context, 0, item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        Transactions item = getItem(position);
        Log.e("date", "before calling date -------- " + item.getCreatedDate());
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_transactions, parent, false);
            viewHolder.tvTransactionAge = (TextView) convertView.findViewById(R.id.tvTransactionAge);
            viewHolder.tvCompanyName = (TextView) convertView.findViewById(R.id.tvCompanyName);
            viewHolder.tvTotalAmount = (TextView) convertView.findViewById(R.id.tvTotalAmount);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.e("date", "before calling date -------- " + item.getCreatedDate());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        String formattedDate="";
        Log.e("date","date -------- "+item.getCreatedDate());
        try {
            SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            date = currentFormat.parse(item.getCreatedDate());
            Log.e("","date -------- "+date);
            formattedDate = formatter.format(date);
            Log.e("", "formattedDate -------- " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.tvTransactionAge.setText(formattedDate);
        viewHolder.tvCompanyName.setText(item.getMerchantName());
        viewHolder.tvTotalAmount.setText(item.getAmount());
        return convertView;
    }

    private static class ViewHolder {
        TextView tvTransactionAge, tvTotalAmount, tvCompanyName;
    }

}

