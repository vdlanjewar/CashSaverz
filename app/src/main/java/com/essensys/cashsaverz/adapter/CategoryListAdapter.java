package com.essensys.cashsaverz.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.BookListActivity;
import com.essensys.cashsaverz.model.Category;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {
    private List<Category> categoryItemList;
    private Context mContext;

    public CategoryListAdapter(Context mContext, List<Category> categoryItemList) {
        this.mContext = mContext;
        this.categoryItemList = categoryItemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvCategorName;
        private ImageView image;
        private CardView cardCategoryDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCategorName = itemView.findViewById(R.id.tvCategorName);
            image = itemView.findViewById(R.id.image);
            cardCategoryDetails = itemView.findViewById(R.id.cardCategoryDetails);
            cardCategoryDetails.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.cardCategoryDetails) {
                Category item = getItem(getAdapterPosition());
                BookListActivity.setInputparameters(item.getCatId(),item.getCatName(),false,
                        "",item.getProductsList());
                Intent i = new Intent(mContext, BookListActivity.class);
                mContext.startActivity(i);
            }
        }
    }

    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.category_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CategoryListAdapter.ViewHolder holder, final int position) {

        final Category item = categoryItemList.get(position);
        holder.tvCategorName.setText(item.getCatName());
        Glide.with(mContext)
                .load(item.getImageUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return categoryItemList.size();
    }

    private Category getItem(int adapterPosition) {
        return categoryItemList.get(adapterPosition);
    }

}
