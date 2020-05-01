package com.essensys.cashsaverz.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.BookDetailsActivity;
import com.essensys.cashsaverz.model.Product;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PopularBookListAdapter extends RecyclerView.Adapter<PopularBookListAdapter.ViewHolder>
        implements View.OnClickListener {

    List<Product> popularBooksList;
    private Context mContext;

    public PopularBookListAdapter(Context mContext, List<Product> popularBooksList) {
        this.mContext = mContext;
        this.popularBooksList = popularBooksList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivBookCover;
        public CardView cvBookCover;

        public ViewHolder(View itemView) {
            super(itemView);
            ivBookCover = itemView.findViewById(R.id.ivBookCover);
            cvBookCover = itemView.findViewById(R.id.cvBookCover);
        }
    }


    @Override
    public PopularBookListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_popular_book, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PopularBookListAdapter.ViewHolder holder, final int position) {

        Product item = popularBooksList.get(position);
        Glide.with(mContext).load(item.getImageUrl()).apply(new RequestOptions().centerInside())
                .into(holder.ivBookCover);

        holder.cvBookCover.setOnClickListener(this);
        holder.cvBookCover.setTag(item);
    }

    @Override
    public void onClick(View view) {
        Product itemProduct = (Product) view.getTag();

        switch (view.getId()){
            case R.id.cvBookCover:
                BookDetailsActivity.setProductDetails(itemProduct);
                Intent intent = new Intent(mContext, BookDetailsActivity.class);
                mContext.startActivity(intent);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return popularBooksList.size();
    }

    private Product getItem(int adapterPosition) {
        return popularBooksList.get(adapterPosition);
    }


}
