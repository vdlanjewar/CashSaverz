package com.essensys.cashsaverz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.model.PaymentHistory;

import java.util.List;

import androidx.cardview.widget.CardView;

public class PaymentHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<PaymentHistory> paymentHistoryList;
    private LayoutInflater layoutInflater;

    public PaymentHistoryAdapter(Context mContext, List<PaymentHistory> paymentHistoryList) {
        this.mContext = mContext;
        this.paymentHistoryList = paymentHistoryList;
        this.layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return paymentHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return paymentHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CardView rootView = (CardView) convertView;
        ViewHolder holder;
        if (rootView == null) {
            rootView = (CardView) layoutInflater.inflate(R.layout.layout_payment_history_item, null);
            holder = new ViewHolder();
            holder.tvTransactionID = rootView.findViewById(R.id.tvTransactionID);
            holder.tvAmount = rootView.findViewById(R.id.tvAmount);
            holder.tvDate = rootView.findViewById(R.id.tvDate);
            holder.tvMemberShipPlan = rootView.findViewById(R.id.tvMemberShipPlan);
            holder.tvStatus = rootView.findViewById(R.id.tvStatus);
            holder.tvMemberShipPlanStartDate = rootView.findViewById(R.id.tvMemberShipPlanStartDate);
            holder.tvMemberShipPlanEnddate = rootView.findViewById(R.id.tvMemberShipPlanEnddate);

            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }

        PaymentHistory paymentHistory = (PaymentHistory) getItem(position);
        holder.tvTransactionID.setText(paymentHistory.getTransactionID());
        holder.tvAmount.setText(paymentHistory.getAmount());
        holder.tvDate.setText(paymentHistory.getAddedOn());
        holder.tvMemberShipPlan.setText(paymentHistory.getMembershipPlanTitle());
        holder.tvMemberShipPlanStartDate.setText(paymentHistory.getMembershipStartDate());
        holder.tvMemberShipPlanEnddate.setText(paymentHistory.getMembershipEndDate());
        holder.tvStatus.setText(paymentHistory.getStatus());

        return rootView;
    }

    private class ViewHolder {
        TextView tvTransactionID, tvAmount, tvDate, tvMemberShipPlan,
                tvStatus, tvMemberShipPlanStartDate, tvMemberShipPlanEnddate;
    }
}
