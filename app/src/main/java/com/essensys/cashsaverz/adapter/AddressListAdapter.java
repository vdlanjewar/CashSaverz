package com.essensys.cashsaverz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.fragments.MyProfileFragment;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.UserAddressList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder>{
    private List<UserAddressList> userAddressLists;
    private Context mContext;
    private MyProfileFragment myProfileFragment;

    public AddressListAdapter(Context mContext, List<UserAddressList> userAddressLists,
                              MyProfileFragment myProfileFragment) {
        this.mContext = mContext;
        this.userAddressLists = userAddressLists;
        this.myProfileFragment = myProfileFragment;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAddress, tvEditButton;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvEditButton = itemView.findViewById(R.id.tvEditButton);
        }

    }

    @Override
    public AddressListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_edit_address, parent, false);

        AddressListAdapter.ViewHolder viewHolder = new AddressListAdapter.ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final UserAddressList item = userAddressLists.get(position);
        holder.tvAddress.setText(CommonUtilities.addressFormatter(item.getName(),
                item.getAddress(), item.getLandmark(), item.getLocality(), item.getCSCName(),
                item.getPincode(), item.getMobileNumber(), item.getAlternateMobileNumber()));
        holder.tvEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myProfileFragment.showEditAddressView(true,true,item.getId(),item.getName(),
                        item.getAddress(), item.getLandmark(), item.getLocality(), item.getCSCName(),
                        item.getPincode(), item.getMobileNumber(), item.getAlternateMobileNumber(), item.getCityId(), item.getAddressType());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userAddressLists.size();
    }

    private UserAddressList getItem(int adapterPosition) {
        return userAddressLists.get(adapterPosition);
    }
}

