package com.dealwala.main.dealwala.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dealwala.main.dealwala.R;
import com.dealwala.main.dealwala.model.MyLoyaltyModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jaimin Patel on 23-Jun-16.
 */
public class RecyclerViewLoyaltyAdapter extends RecyclerView.Adapter<RecyclerViewLoyaltyAdapter.MyViewHolder>  {

    private ArrayList<MyLoyaltyModel> dataList;
    private Activity context;

    HashMap<String,Handler> handlerHashMap = new HashMap<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtMerchantName, txtTotalPin, txtReedemedPin;
        LinearLayout rootLay;

        public MyViewHolder(View view) {
            super(view);
            txtMerchantName = (TextView) view.findViewById(R.id.txtMerchantName);
            txtTotalPin = (TextView) view.findViewById(R.id.txtTotalpin);
            txtReedemedPin = (TextView) view.findViewById(R.id.txtRedeem);
            rootLay = (LinearLayout) view.findViewById(R.id.rootLay);
        }
    }

    public RecyclerViewLoyaltyAdapter(Activity context, ArrayList<MyLoyaltyModel> dataList) {
        this.dataList = dataList;
        this.context = context;
    }



    @Override
    public RecyclerViewLoyaltyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_my_loyalty, parent, false);

        Log.v("Notification","On create View Holder");

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MyLoyaltyModel data = dataList.get(position);


        holder.txtMerchantName.setText(data.getMerchantname());
        holder.txtTotalPin.setText(data.getTotalpin());
        holder.txtReedemedPin.setText(data.getReedemedpin());

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
