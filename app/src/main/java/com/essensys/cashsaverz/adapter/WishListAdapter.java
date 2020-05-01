package com.essensys.cashsaverz.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.BookDetailsActivity;
import com.essensys.cashsaverz.activities.BookListActivity;
import com.essensys.cashsaverz.activities.CheckOutActivity;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.localDatabases.DatabaseHandler;
import com.essensys.cashsaverz.model.Product;
import com.essensys.cashsaverz.networkManager.AddToCartManager;
import com.essensys.cashsaverz.networkManager.WishlistManager;

import org.json.JSONException;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> implements
        View.OnClickListener, WishlistManager.StatusListener, AddToCartManager.StatusListener {
    private List<Product> mItems;
    private Context mContext;
    private WishlistManager wishlistManager;
    private DatabaseHandler databaseHandler;
    private AddToCartManager addToCartManager;

    @Override
    public void onDataFetchSuccess(String successMessage, String total_cart_items) {
        HomeActivity.setCartCount(Integer.parseInt(total_cart_items));
        ((HomeActivity) mContext).setupBadge(HomeActivity.getCartCount());
        Toast.makeText(mContext, successMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataFetchFailure(String errorMessage, String total_cart_items) {
        HomeActivity.setCartCount(Integer.parseInt(total_cart_items));
        ((HomeActivity) mContext).setupBadge(HomeActivity.getCartCount());
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProdName, tvPrice, tvMrpPrice;
        private Button btAddToCart, btRentNow, btNotAvailable;
        private ImageView ivBookImage;
        private LikeButton btLike;
        private CardView product_card;

        private ViewHolder(View itemView) {
            super(itemView);
            btAddToCart = itemView.findViewById(R.id.btAddToCart);
            btRentNow = itemView.findViewById(R.id.btRentNow);
            tvProdName = itemView.findViewById(R.id.tvProdNameWish);
            tvPrice = itemView.findViewById(R.id.tv_priceWish);
            tvMrpPrice = itemView.findViewById(R.id.tv_mrpWish);
            ivBookImage = itemView.findViewById(R.id.ivBookImage);
            btLike = itemView.findViewById(R.id.btLike);
            product_card = itemView.findViewById(R.id.product_card);
            btNotAvailable = itemView.findViewById(R.id.btNotAvailable);
        }

    }

    public WishListAdapter(Context context, List<Product> productList) {
        mItems = productList;
        mContext = context;
        wishlistManager = new WishlistManager(mContext, this);
        databaseHandler = new DatabaseHandler(mContext);
        addToCartManager = new AddToCartManager(mContext, this);
    }

    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_wish_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView);
        postView.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WishListAdapter.ViewHolder holder,
                                 final int position) {
        Product item = mItems.get(position);
        Glide.with(mContext)
                .load(item.getImageUrl())
                .into(holder.ivBookImage);

        holder.tvProdName.setText(item.getProductName());
        holder.tvPrice.setText(mContext.getString(R.string.Rs)+" "+ item.getOffered_price());
        holder.tvMrpPrice.setText(mContext.getString(R.string.Rs)+" " + item.getMrp());
        holder.tvMrpPrice.setPaintFlags(holder.tvMrpPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        if (item.getAvilableBookCount().equals("0")) {
            holder.btNotAvailable.setVisibility(View.VISIBLE);
            holder.btRentNow.setVisibility(View.GONE);
            holder.btAddToCart.setVisibility(View.GONE);
        } else {
            holder.btRentNow.setVisibility(View.VISIBLE);
            holder.btAddToCart.setVisibility(View.VISIBLE);
        }

        holder.btRentNow.setOnClickListener(this);
        holder.btAddToCart.setOnClickListener(this);
        holder.product_card.setOnClickListener(this);
        holder.btAddToCart.setTag(item);
        holder.btRentNow.setTag(item);
        holder.product_card.setTag(item);
        holder.btLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                removeAt(position);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Product itemProduct = (Product) view.getTag();
        switch (view.getId()) {
            case R.id.product_card:
                BookDetailsActivity.setProductDetails(itemProduct);
                Intent intent = new Intent(mContext, BookDetailsActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.btAddToCart:
                if (!App.isUserLoggedIn(mContext)) {
                    CommonUtilities.openFragmentContainerActivityWithDelay(mContext,
                            R.id.nav_login, 6);
                } else {
                    /*databaseHandler.insertCartItemToLocalDb(App.getCurrentUser(
                            mContext).getCustomerId(),
                            itemProduct.getProductId(), itemProduct.getProductName(),
                            itemProduct.getAuthor(), itemProduct.getImageUrl());
                    try {
                        ((HomeActivity) mContext).setupBadge(databaseHandler.getCartItems(App.getCurrentUser(mContext).getCustomerId()).length());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    addToCartManager.addToCart(itemProduct.getProductId());
                }
                break;
            case R.id.btRentNow:
                if (!App.isUserLoggedIn(mContext)) {
                    CommonUtilities.openFragmentContainerActivityWithDelay(mContext,
                            R.id.nav_login, 6);
                } else {
                    if (App.getCurrentUser(mContext).getUserCurrentMembershipPlan() != null) {
                        CheckOutActivity.setRentBookID(itemProduct.getProductId());
                        mContext.startActivity(new Intent(mContext, CheckOutActivity.class));
                    } else {
                        CommonUtilities.openFragmentContainerActivityWithDelay(mContext, R.id.nav_browse_plans,
                                0);
                    }

                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void removeAt(int position) {
        wishlistManager.updateWishlist(mItems.get(position).getProductId());
        mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItems.size());
    }

    @Override
    public void onDataFetchSuccess(String successMessage) {

    }

    @Override
    public void onDataFetchFailure(String errorMessage) {

    }
}
