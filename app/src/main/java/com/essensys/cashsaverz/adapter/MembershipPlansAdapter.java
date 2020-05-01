package com.essensys.cashsaverz.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.CheckOutActivity;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.model.MembershipPlans;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MembershipPlansAdapter extends RecyclerView.Adapter<MembershipPlansAdapter
        .ViewHolder> implements View.OnClickListener {

    private List<MembershipPlans> mItems;
    private Context mContext;

    public interface PostItemListener {
        void onPostClick(int id, int viewid, int adapterpos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPlanName, tvPlanPrice, tvPlanDescription, tvPlanOffer, tvPlanExpire,
                tvPlanDescription2, tvPlanDescription3, tvSecurityDepositInfo;
        private Button btSelectPlan;
        private View vDivider;
        private LinearLayout llTitleBackground, llOffer, llPlanExpire, llSecurityDepositInfo;
        private ImageView ivActivePlan;
        private CardView cvMembership;

        private ViewHolder(View itemView) {
            super(itemView);
            tvPlanName = itemView.findViewById(R.id.tvPlanName);
            tvPlanPrice = itemView.findViewById(R.id.tvPlanPrice);
            tvPlanDescription = itemView.findViewById(R.id.tvPlanDescription);
            tvPlanDescription2 = itemView.findViewById(R.id.tvPlanDescription2);
            tvPlanDescription3 = itemView.findViewById(R.id.tvPlanDescription3);
            tvSecurityDepositInfo = itemView.findViewById(R.id.tvSecurityDepositInfo); // Added by SM

            tvPlanOffer = itemView.findViewById(R.id.tvPlanOffer);
            tvPlanExpire = itemView.findViewById(R.id.tvPlanExpire);

            btSelectPlan = itemView.findViewById(R.id.btSelectPlan);
            vDivider = itemView.findViewById(R.id.vDivider);
            llTitleBackground = itemView.findViewById(R.id.llTitleBackground);
            llOffer = itemView.findViewById(R.id.llOffer);
            llPlanExpire = itemView.findViewById(R.id.llPlanExpire);
            llSecurityDepositInfo = itemView.findViewById(R.id.llSecurityDepositInfo); // Added by SM

            ivActivePlan = itemView.findViewById(R.id.ivActivePlan);
            cvMembership = itemView.findViewById(R.id.cvMembership);
        }
    }

    public MembershipPlansAdapter(Context context, ArrayList<MembershipPlans> posts,
                                  PostItemListener postItemListener) {
        mItems = posts;
        mContext = context;
    }

    @Override
    public MembershipPlansAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_membership_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MembershipPlansAdapter.ViewHolder holder, final int position) {
        MembershipPlans item = mItems.get(position);
        setCardTheme(position, holder.llTitleBackground, holder.tvPlanName, holder.btSelectPlan, holder.vDivider);
        holder.tvPlanName.setText(item.getTitle());
        holder.tvPlanPrice.setText(item.getPrice());
        holder.tvPlanDescription.setText(item.getDescription());
        holder.tvPlanDescription2.setText(item.getDescription2());
        holder.tvPlanDescription3.setText(item.getDescription3());
        holder.btSelectPlan.setOnClickListener(this);
        holder.btSelectPlan.setTag(item);

        if (!item.getOfferText().isEmpty()) {
            holder.tvPlanOffer.setText(item.getOfferText());
        } else {
            holder.llOffer.setVisibility(View.GONE);
        }

        if (!item.getMembershipExpierText().isEmpty()) {
            holder.tvPlanExpire.setText(item.getMembershipExpierText());
        } else {
            holder.llPlanExpire.setVisibility(View.GONE);
        }

        if (!item.getSecurityDepositText().isEmpty()) {
            if (!item.isSecurityDepositRecived()) {
                holder.llSecurityDepositInfo.setVisibility(View.VISIBLE);
                holder.tvSecurityDepositInfo.setText(item.getSecurityDepositText());
            }
        } else {
            holder.llSecurityDepositInfo.setVisibility(View.GONE);
        }

        setActiveMembershipPlan(holder.ivActivePlan, holder.btSelectPlan, holder.cvMembership, item);
    }

    @Override
    public void onClick(View view) {
        MembershipPlans item = (MembershipPlans) view.getTag();
        switch (view.getId()) {
            case R.id.btSelectPlan:
                if (!App.isUserLoggedIn(mContext)) {
                    CommonUtilities.openFragmentContainerActivityWithDelay(mContext, R.id.nav_login,
                            0);
                } else {
                    CheckOutActivity.setItemDetails(item);
                    Intent intent = new Intent(mContext, CheckOutActivity.class);
                    mContext.startActivity(intent);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updatePlans(List<MembershipPlans> items) {
        mItems = items;
    }

    private MembershipPlans getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    private void setCardTheme(int position, LinearLayout llTitleBackground, TextView tvPlanName,
                              Button btSelectPlan, View vDivider) {
        switch (position) {
            case 0:
                llTitleBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorLightBlue));
                tvPlanName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                btSelectPlan.setBackground(mContext.getResources().getDrawable(R.drawable.button_yellow));
                vDivider.setBackgroundColor(mContext.getResources().getColor(R.color.colorLightBlue));
                break;
            case 1:
                llTitleBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorYellow));
                tvPlanName.setTextColor(mContext.getResources().getColor(R.color.colorBlack));
                btSelectPlan.setBackground(mContext.getResources().getDrawable(R.drawable.button_blue));
                vDivider.setBackgroundColor(mContext.getResources().getColor(R.color.colorYellow));
                break;
            case 2:
                llTitleBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorBrown));
                tvPlanName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                btSelectPlan.setBackground(mContext.getResources().getDrawable(R.drawable.button_yellow));
                vDivider.setBackgroundColor(mContext.getResources().getColor(R.color.colorBrown));
                break;
            default:
                llTitleBackground.setBackgroundColor(mContext.getResources().getColor(R.color.colorLightBlue));
                tvPlanName.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                btSelectPlan.setBackground(mContext.getResources().getDrawable(R.drawable.button_yellow));
                vDivider.setBackgroundColor(mContext.getResources().getColor(R.color.colorLightBlue));
        }
    }

    public void setActiveMembershipPlan(ImageView ivActivePlan, Button btSelectPlan,
                                        CardView cvMembership, MembershipPlans item) {
        if (App.isUserLoggedIn(mContext)) {
            if (App.getCurrentUser(mContext).getUserCurrentMembershipPlan() != null) {
                if (item.getActivePlan()) {
                    ivActivePlan.setVisibility(View.VISIBLE);
                    btSelectPlan.setText("Activated");
                    btSelectPlan.setEnabled(false);
                } else if (item.getChoosePlanbtn()) {
                    ivActivePlan.setVisibility(View.GONE);
                    btSelectPlan.setText("Select Plan");
                    btSelectPlan.setEnabled(true);
                } else {
                    ivActivePlan.setVisibility(View.GONE);
                    btSelectPlan.setText("Select Plan");
                    btSelectPlan.setEnabled(false);
                }
            } else {
                if (item.getChoosePlanbtn()) {
                    btSelectPlan.setVisibility(View.VISIBLE);
                } else {
                    btSelectPlan.setVisibility(View.GONE);
                }
                ivActivePlan.setVisibility(View.GONE);
            }
        } else {
            btSelectPlan.setVisibility(View.VISIBLE);
            ivActivePlan.setVisibility(View.GONE);
        }
    }

}
