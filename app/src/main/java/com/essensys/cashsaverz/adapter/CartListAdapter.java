package com.essensys.cashsaverz.adapter;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.Interface.InterfaceClickListener;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.model.CartItems;
import com.essensys.cashsaverz.networkManager.AddToCartManager;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> implements
        View.OnClickListener, Callback<ResponseBody> {

    private List<CartItems> mItems;
    private Context mContext;
    private PostItemListener mItemListener;
    private AddToCartManager addToCartManager;
    private InterfaceClickListener interfaceClickListener;
    private CartItems item;
    private ViewHolder viewHolder;
    private String flgFrom = "";
    private boolean isSelected;

    public class ViewHolder extends RecyclerView.ViewHolder {
        PostItemListener mItemListener;
        private TextView tvProdName, tvPrice,tvMrpPrice,tvsize,tvBrand,tvqty;
        private ImageView ivRemove, ivBookImage;
        private Spinner mSpnrQty;
        private RelativeLayout mRelQty;
        private LinearLayout mLinearSpnr;

        private ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            tvProdName = itemView.findViewById(R.id.tv_prodName);
            tvPrice = itemView.findViewById(R.id.tv_priceMycart);
            tvMrpPrice = itemView.findViewById(R.id.tv_mrpMycart);
            tvsize = itemView.findViewById(R.id.tv_itemsizeMyCart);
            tvBrand=itemView.findViewById(R.id.tv_BrandMyCart);
            mSpnrQty=itemView.findViewById(R.id.spnr_qtyMycart);
            ivRemove = itemView.findViewById(R.id.ivRemove);
            ivBookImage = itemView.findViewById(R.id.ivBookImage);
            mRelQty = itemView.findViewById(R.id.rel_qty);
            mLinearSpnr = itemView.findViewById(R.id.linear_spnr);
            this.mItemListener = postItemListener;
        }
    }

    public CartListAdapter(Context context, ArrayList<CartItems> posts,
                           AddToCartManager addToCartManager, InterfaceClickListener interfaceClickListener, String flagFrom, PostItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
        this.addToCartManager = addToCartManager;
        this.interfaceClickListener=interfaceClickListener;
        this.flgFrom = flagFrom;
    }

    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_cart, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CartListAdapter.ViewHolder holder, final int position) {
        String[] Itemqty = {"Select Quantity", "1", "2", "3", "4", "5"};
         viewHolder=holder;
        item = mItems.get(position);
        viewHolder.tvProdName.setText(item.getProduct_name());
        viewHolder.tvPrice.setText(mContext.getString(R.string.Rs)+" "+item.getTotalPrice());
        viewHolder.tvMrpPrice.setText(mContext.getString(R.string.Rs)+" "+item.getMrp());
        viewHolder.tvMrpPrice.setPaintFlags(holder.tvMrpPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        viewHolder.tvsize.setText(item.getItem_size()+" "+item.getUnit());
        viewHolder.tvBrand.setText(item.getBrandName());
        Glide.with(mContext).load(item.getImageUrl())
                .into(viewHolder.ivBookImage);

        if (flgFrom.equalsIgnoreCase("1")) {
            viewHolder.mLinearSpnr.setVisibility(View.GONE);
            viewHolder.ivRemove.setVisibility(View.GONE);
            viewHolder.mRelQty.setVisibility(View.VISIBLE);
            viewHolder.tvqty.setText(this.item.getQty());
        } else {
            viewHolder.mLinearSpnr.setVisibility(View.VISIBLE);
            viewHolder.ivRemove.setVisibility(View.VISIBLE);
            viewHolder.mRelQty.setVisibility(View.GONE);
        }
//        viewHolder.mSpnrQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
//                if(i!=0)
//             {
////                 quantity=holder.mSpnrQty.getAdapter().getItem(i).toString();
//                 String cartId=mItems.get(position).getCartId();
////                  interfaceClickListener.Onclick(cartId,parent.getItemAtPosition(i).toString());
//
//                 updatequantity(cartId,parent.getItemAtPosition(i).toString());
//
//             }
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
////        holder.mSpnrQty.setTag(position);
//        //Creating the ArrayAdapter instance having the country list
//        ArrayAdapter aa = new ArrayAdapter(mContext,android.R.layout.simple_spinner_item,Itemqty);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //Setting the ArrayAdapter data on the Spinner
//        viewHolder.mSpnrQty.setAdapter(aa);
////        holder.mSpnrQty.setSelection(position,false);
//
//      //to set the value coming from webservice
//        int spinnerPosition = aa.getPosition(item.getQty());
//        //set the default according to value
//
//        viewHolder.mSpnrQty.setSelection(spinnerPosition);
//        viewHolder.ivRemove.setOnClickListener(this);
//        viewHolder.ivRemove.setTag(item);
        ArrayAdapter aa = new ArrayAdapter(mContext,android.R.layout.simple_spinner_item,Itemqty);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.viewHolder.mSpnrQty.setAdapter(aa);
        this.viewHolder.mSpnrQty.setSelection(aa.getPosition(this.item.getQty()));
        this.viewHolder.ivRemove.setOnClickListener(this);
        this.viewHolder.ivRemove.setTag(this.item);
        this.isSelected = false;
        this.viewHolder.mSpnrQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                if (CartListAdapter.this.isSelected && i != 0) {
                    String cartId = ((CartItems) CartListAdapter.this.mItems.get(position)).getCartId();
                    if (cartId == null) {
                        CartListAdapter.this.interfaceClickListener.Onclick(((CartItems) CartListAdapter.this.mItems.get(position)).getProductId(), parent.getItemAtPosition(i).toString());
                    } else {
                        CartListAdapter.this.interfaceClickListener.Onclick(cartId, parent.getItemAtPosition(i).toString());
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                CartListAdapter.this.isSelected = false;
            }
        });
        this.viewHolder.mSpnrQty.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                CartListAdapter.this.isSelected = true;
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        CartItems items = (CartItems) view.getTag();
        switch (view.getId()) {
            case R.id.ivRemove:
                addToCartManager.removeFromCart(items.getCartId(), false);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateProducts(List<CartItems> items) {
        mItems = items;
    }

    public interface PostItemListener {
        void onPostClick(int id, int viewid, int adapterpos);
    }

    public void setItems(String mrp,String totalPrice) {
        viewHolder.tvMrpPrice.setText(mContext.getString(R.string.Rs)+" "+mrp);
        viewHolder.tvMrpPrice.setPaintFlags(viewHolder.tvMrpPrice.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.tvPrice.setText(mContext.getString(R.string.Rs)+" "+totalPrice);
    }

    //webservice for update quantity
    private void updatequantity(String cartId,String value)
    {
        RetrofitClient
                .getClient(Constant.ServerEndpoint.UPDATE_QTY)
                .create(RetrofitInterfaces.UpdateQty.class)
                .post(App.getCurrentUser(mContext).getUserAuth(),App.getCurrentUser(mContext).getCustomerId(),cartId,value)
                .enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            String stringResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(stringResponse);
            JSONObject result = jsonObject.getJSONObject("result");
            String msg = result.getString("msg");
            if(msg.equalsIgnoreCase("1")) {
               JSONObject jsonCart=result.getJSONObject("cartItem");
                viewHolder.tvPrice.setText(mContext.getString(R.string.Rs)+" "+jsonCart.getString("totalPrice"));
                viewHolder.tvMrpPrice.setText(mContext.getString(R.string.Rs)+" "+jsonCart.getString("mrp"));
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
    }
}
