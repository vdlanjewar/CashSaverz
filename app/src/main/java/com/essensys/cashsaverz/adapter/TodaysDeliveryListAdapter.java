package com.essensys.cashsaverz.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.model.UserDeliveryBookList;
import com.essensys.cashsaverz.networkManager.BookDeliveryOtpManager;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;


public class TodaysDeliveryListAdapter extends RecyclerView.Adapter<TodaysDeliveryListAdapter.ViewHolder> implements
        BookDeliveryOtpManager.StatusListener {
    private List<UserDeliveryBookList> mItems;
    private Context mContext;
    private BookDeliveryOtpManager bookDeliveryOtpManager;
    private int pos = -1;

    @Override
    public void onDataFetchSuccess(String successMessage) {
        Toasty.success(mContext, successMessage,
                Toast.LENGTH_SHORT, true).show();
        removeAt(pos);
    }

    @Override
    public void onDataFetchFailure(String errorMessage) {
        Toasty.error(mContext, errorMessage,
                Toast.LENGTH_SHORT, true).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCustomerName, tvBookName, tvIsbn, tvAddress, tvOrderId;
        private Button btProceed;

        private ViewHolder(View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvIsbn = itemView.findViewById(R.id.tvIsbn);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            btProceed = itemView.findViewById(R.id.btProceed);
        }
    }

    public TodaysDeliveryListAdapter(Context context, List<UserDeliveryBookList> userDeliveryBookLists) {
        mItems = userDeliveryBookLists;
        mContext = context;
        bookDeliveryOtpManager = new BookDeliveryOtpManager(context, this);
    }

    @Override
    public TodaysDeliveryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_delivery_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView);
        postView.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TodaysDeliveryListAdapter.ViewHolder holder,
                                 final int position) {
        final UserDeliveryBookList item = mItems.get(position);
        holder.tvCustomerName.setText(item.getDeliveryName());
        holder.tvBookName.setText(item.getProductName());
        holder.tvIsbn.setText("ISBN: " + item.getIsbn());
        holder.tvOrderId.setText("Order Id: " + item.getOrderId());
        holder.tvAddress.setText(item.getDeliveryAddress() + ", " + item.getDeliveryLocality()
                + ", " + item.getDeliveryLandmark() + ", " + item.getDeliveryCityName()
                + ", " + item.getDeliveryPincode() + "\nContact: " + item.getDeliveryMobile()
                + ", " + item.getDeliveryAlternateMobileNumber()
                + "\nAddress Type: " + item.getDeliveryAddressType()
                + "\nDelivery Time Slot: " + item.getDeliveryTimeSlot());
        holder.btProceed.setTag(item);
        holder.btProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;
                showOtpDialog(item.getRentedBookId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public void showOtpDialog(final String rentedBookId) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogview = inflater.inflate(R.layout.otp_dialog, null);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setView(dialogview);
        final EditText etOtp = (EditText) dialogview.findViewById(R.id.etOtp);
        Button btDeliverBook = (Button) dialogview.findViewById(R.id.btDeliverBook);
        final AlertDialog ad = alertDialog.show();

        btDeliverBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etOtp.getText().toString().trim().equals("")) {
                    Toasty.error(mContext, "Plese enter OTP",
                            Toast.LENGTH_SHORT, true).show();
                } else if (etOtp.getText().toString().trim().length() < 4) {
                    Toasty.error(mContext, "Enter valid OTP",
                            Toast.LENGTH_SHORT, true).show();
                } else {
                    bookDeliveryOtpManager.deliverBook(rentedBookId, etOtp.getText().toString().trim());
                    ad.cancel();
                }
            }
        });
    }

    public void removeAt(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItems.size());
    }
}
