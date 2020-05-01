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
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.BookDetailsActivity;
import com.essensys.cashsaverz.activities.BookListActivity;
import com.essensys.cashsaverz.activities.CartActivity;
import com.essensys.cashsaverz.activities.CheckOutActivity;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.localDatabases.DatabaseHandler;
import com.essensys.cashsaverz.model.Product;
import com.essensys.cashsaverz.networkManager.AddToCartManager;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BookProductListAdapter extends RecyclerView.Adapter<BookProductListAdapter.ViewHolder> implements View.OnClickListener, AddToCartManager.StatusListener {
    private List<Product> mItems;
    private Context mContext;
    private DatabaseHandler databaseHandler;
    private AddToCartManager addToCartManager;

    @Override
    public void onDataFetchSuccess(String successMessage, String total_cart_items) {
        HomeActivity.setCartCount(Integer.parseInt(total_cart_items));
        ((BookListActivity) mContext).setupBadge(HomeActivity.getCartCount());
        Toast.makeText(mContext, successMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataFetchFailure(String errorMessage, String total_cart_items) {
        HomeActivity.setCartCount(Integer.parseInt(total_cart_items));
        ((BookListActivity) mContext).setupBadge(HomeActivity.getCartCount());
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBookName, tvByAuthorName, tvPublisher,tvByMrpPrice;
        private Button btAddToCart, btRentNow, btNotAvailable,btnGoto;
        private CardView product_card;
        private ImageView ivBookImage;

        private ViewHolder(View itemView) {
            super(itemView);
            btAddToCart = itemView.findViewById(R.id.btAddToCart);
            btRentNow = itemView.findViewById(R.id.btRentNow);
            btnGoto=itemView.findViewById(R.id.btGoToMYcart);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvByAuthorName = itemView.findViewById(R.id.tvByAuthorName);
//            tvPublisher = itemView.findViewById(R.id.tvPublisher);
            tvByMrpPrice = itemView.findViewById(R.id.tvMrpPrice);

            product_card = itemView.findViewById(R.id.product_card);
            ivBookImage = itemView.findViewById(R.id.ivBookImage);
            btNotAvailable = itemView.findViewById(R.id.btNotAvailable);

        }

    }

    public BookProductListAdapter(Context context, List<Product> productList) {
        mItems = productList;
        mContext = context;
        databaseHandler = new DatabaseHandler(mContext);
        addToCartManager = new AddToCartManager(mContext, this);
    }

    @Override
    public BookProductListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_book_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView);
        postView.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BookProductListAdapter.ViewHolder holder,
                                 final int position) {

        Product item = mItems.get(position);
        Glide.with(mContext)
                .load(item.getImageUrl())
                .into(holder.ivBookImage);
        holder.tvBookName.setText(item.getProductName());
        holder.tvByAuthorName.setText(mContext.getString(R.string.Rs)+" "+item.getOffered_price());

        holder.tvByMrpPrice.setText(item.getMrp());
        holder.tvByMrpPrice.setPaintFlags(holder.tvByMrpPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);


//        holder.tvPublisher.setText("Publisher: " + item.getPublisher());

        if (item.getAvilableBookCount().equals("0")) {
            holder.btNotAvailable.setVisibility(View.VISIBLE);
            holder.btRentNow.setVisibility(View.GONE);
            holder.btAddToCart.setVisibility(View.GONE);
            holder.btnGoto.setVisibility(View.GONE);
        } else {
            holder.btNotAvailable.setVisibility(View.GONE);
            holder.btRentNow.setVisibility(View.VISIBLE);
            holder.btAddToCart.setVisibility(View.VISIBLE);
            holder.btnGoto.setVisibility(View.GONE);

        }
        if(item.getIsProductInCart()=="true")
        {
            holder.btAddToCart.setVisibility(View.GONE);
            holder.btnGoto.setVisibility(View.VISIBLE);

        }else
        {
            holder.btAddToCart.setVisibility(View.VISIBLE);
            holder.btnGoto.setVisibility(View.GONE);

        }

        holder.product_card.setOnClickListener(this);
        holder.btRentNow.setOnClickListener(this);
        holder.btAddToCart.setOnClickListener(this);
        holder.btAddToCart.setTag(item);
        holder.btnGoto.setOnClickListener(this);
        holder.btnGoto.setTag(item);
        holder.btRentNow.setTag(item);
        holder.product_card.setTag(item);
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
                            */
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
            case R.id.btGoToMYcart:
                if (!App.isUserLoggedIn(mContext)) {
                    CommonUtilities.openFragmentContainerActivityWithDelay(mContext,
                            R.id.nav_login, 6);
                } else {
//                    if (App.getCurrentUser(mContext).getUserCurrentMembershipPlan() != null) {
//                        CheckOutActivity.setRentBookID(itemProduct.getProductId());
//                        mContext.startActivity(new Intent(mContext, CheckOutActivity.class));
//                    } else {
//                        CommonUtilities.openFragmentContainerActivityWithDelay(mContext, R.id.nav_browse_plans,
//                                0);
//                    }
                    intent=new Intent(mContext, CartActivity.class);
                    mContext.startActivity(intent);


                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setData(List<Product> mArraylistFinalProd)
    {
        this.mItems = mArraylistFinalProd;
        this.notifyDataSetChanged();
    }
}
