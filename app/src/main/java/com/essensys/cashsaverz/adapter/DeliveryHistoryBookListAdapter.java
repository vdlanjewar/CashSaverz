package com.essensys.cashsaverz.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.model.UserDeliveryBookList;
import com.essensys.cashsaverz.model.UserDeliveryHistoryBookList;
import com.essensys.cashsaverz.networkManager.BookDeliveryOtpManager;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;


public class DeliveryHistoryBookListAdapter extends RecyclerView.Adapter<DeliveryHistoryBookListAdapter.ViewHolder>{
    private List<UserDeliveryHistoryBookList> mItems;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerName, tvBookName, tvIsbn, tvAddress, tvOrderId, tvDeliveryDate;

        private ViewHolder(View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvIsbn = itemView.findViewById(R.id.tvIsbn);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvDeliveryDate = itemView.findViewById(R.id.tvDeliveryDate);
        }
    }

    public DeliveryHistoryBookListAdapter(Context context, List<UserDeliveryHistoryBookList> userDeliveryBookLists) {
        mItems = userDeliveryBookLists;
        mContext = context;
    }

    @Override
    public DeliveryHistoryBookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_delivery_history_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView);
        postView.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DeliveryHistoryBookListAdapter.ViewHolder holder,
                                 final int position) {
        final UserDeliveryHistoryBookList item = mItems.get(position);
        holder.tvCustomerName.setText(item.getDeliveryName());
        holder.tvBookName.setText(item.getProductName());
        holder.tvIsbn.setText("ISBN: " + item.getIsbn());
        holder.tvOrderId.setText("Order Id: " + item.getOrderId());
        holder.tvAddress.setText(item.getDeliveryAddress() + ", " + item.getDeliveryLocality()
                + ", " + item.getDeliveryLandmark() + ", " + item.getDeliveryCityName()
                + ", " + item.getDeliveryPincode() + "\nContact: " + item.getDeliveryMobile()
                + ", " + item.getDeliveryAlternateMobileNumber()
                + "\nAddress Type: " + item.getDeliveryAddressType());
        holder.tvDeliveryDate.setText("Delivered On: "+item.getBookDeliveredToCustomerOndateTime());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
