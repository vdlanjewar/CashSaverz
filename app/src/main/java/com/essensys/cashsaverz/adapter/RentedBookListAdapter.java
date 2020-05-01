package com.essensys.cashsaverz.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.model.UserRentedBookList;
import com.essensys.cashsaverz.networkManager.ReturnAndReIssueBookManager;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RentedBookListAdapter extends RecyclerView.Adapter<RentedBookListAdapter.ViewHolder> implements View.OnClickListener, ReturnAndReIssueBookManager.StatusListener {
    private List<UserRentedBookList> mItems;
    private Context mContext;
    private ReturnAndReIssueBookManager returnAndReIssueBookManager;

    @Override
    public void onClick(View view) {
        UserRentedBookList itemProduct = (UserRentedBookList) view.getTag();
        switch (view.getId()) {
            case R.id.btReissueBook:
                returnAndReIssueBookManager.reIssueBook(itemProduct.getRentedBookId());
                break;
            case R.id.btReturnBook:
                returnAndReIssueBookManager.returnBook(itemProduct.getRentedBookId());
                break;
        }
    }

    @Override
    public void onDataFetchSuccess(String successMessage) {
        Toast.makeText(mContext, successMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDataFetchFailure(String errorMessage) {
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBookName, tvByAuthorName, tvStatus, tvIssuedDate, tvReturnDate, tvOtp;
        private Button btReissueBook, btReturnBook;
        private ImageView ivBookImage;
        private LinearLayout llOtp;

        private ViewHolder(View itemView) {
            super(itemView);
            btReissueBook = itemView.findViewById(R.id.btReissueBook);
            btReturnBook = itemView.findViewById(R.id.btReturnBook);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvByAuthorName = itemView.findViewById(R.id.tvByAuthorName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvIssuedDate = itemView.findViewById(R.id.tvIssuedDate);
            tvReturnDate = itemView.findViewById(R.id.tvReturnDate);
            tvOtp = itemView.findViewById(R.id.tvOtp);
            ivBookImage = itemView.findViewById(R.id.ivBookImage);
            llOtp = itemView.findViewById(R.id.llOtp);
        }
    }

    public RentedBookListAdapter(Context context, List<UserRentedBookList> productList) {
        mItems = productList;
        mContext = context;
        returnAndReIssueBookManager = new ReturnAndReIssueBookManager(context, this);
    }

    @Override
    public RentedBookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_rented_book_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView);
        postView.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RentedBookListAdapter.ViewHolder holder,
                                 final int position) {
        UserRentedBookList item = mItems.get(position);
        Glide.with(mContext)
                .load(item.getImageUrl())
                .into(holder.ivBookImage);
        holder.tvBookName.setText(item.getProductName());
        holder.tvByAuthorName.setText("By " + item.getAuthor());
        holder.tvStatus.setText(": " + item.getStatus());
        holder.tvOtp.setText("OTP: "+item.getOtp());
        if (item.getBookDeliveredToCustomerOndateTime().length() > 0) {
            holder.tvIssuedDate.setText(": " + item.getBookDeliveredToCustomerOndateTime());
        } else {
            holder.tvIssuedDate.setText(": NA");

        }
        if (item.getReturnedDateTime().length() > 0) {
            holder.tvReturnDate.setText(": " + item.getReturnedDateTime());
        } else {
            holder.tvReturnDate.setText(": NA");

        }
        holder.btReturnBook.setTag(item);
        holder.btReissueBook.setTag(item);
        holder.btReissueBook.setOnClickListener(this);
        holder.btReturnBook.setOnClickListener(this);
        manageButtons(item.getStatus(), holder.btReissueBook, holder.btReturnBook, holder.tvStatus, holder.llOtp);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void manageButtons(String status, Button btReissueBook, Button btReturnBook, TextView tvStatus, LinearLayout llOtp) {
        switch (status) {
            case "Pending":
                llOtp.setVisibility(View.VISIBLE);
                btReissueBook.setVisibility(View.GONE);
                btReturnBook.setVisibility(View.GONE);
                break;
            case "Rented":
                llOtp.setVisibility(View.GONE);
                btReissueBook.setVisibility(View.VISIBLE);
                btReturnBook.setVisibility(View.VISIBLE);
                break;
            case "Return_Schedule":
                llOtp.setVisibility(View.GONE);
                btReissueBook.setVisibility(View.GONE);
                btReturnBook.setVisibility(View.GONE);
                break;
            case "Reissue":
                llOtp.setVisibility(View.GONE);
                btReissueBook.setVisibility(View.VISIBLE);
                btReturnBook.setVisibility(View.VISIBLE);
                break;
            case "Returned":
                llOtp.setVisibility(View.GONE);
                btReissueBook.setVisibility(View.GONE);
                btReturnBook.setVisibility(View.GONE);
                break;
        }
    }
}
