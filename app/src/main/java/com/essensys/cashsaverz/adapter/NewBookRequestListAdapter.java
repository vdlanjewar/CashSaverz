package com.essensys.cashsaverz.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.model.NewBookRequestList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class NewBookRequestListAdapter extends RecyclerView.Adapter<NewBookRequestListAdapter.ViewHolder> {
    private List<NewBookRequestList> mItems;
    private Context mContext;



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBookName, tvByAuthorName, tvName, tvEmail, tvPhone, tvStatus, tvOnDate, tvMessage, tvLink;
        private ImageView ivBookImage;


        private ViewHolder(View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvByAuthorName = itemView.findViewById(R.id.tvByAuthorName);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOnDate = itemView.findViewById(R.id.tvOnDate);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvLink = itemView.findViewById(R.id.tvLink);
            ivBookImage = itemView.findViewById(R.id.ivBookImage);

        }
    }

    public NewBookRequestListAdapter(Context context, List<NewBookRequestList> productList) {
        mItems = productList;
        mContext = context;
    }

    @Override
    public NewBookRequestListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_new_requested_books, parent, false);

        NewBookRequestListAdapter.ViewHolder viewHolder = new NewBookRequestListAdapter.ViewHolder(postView);
        postView.setTag(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,
                                 final int position) {
        NewBookRequestList item = mItems.get(position);
        //Glide.with(mContext).load(item.getImageUrl()).into(holder.ivBookImage);
        holder.tvBookName.setText(item.getBookName());
        holder.tvByAuthorName.setText("By " + item.getAuthorName());
        holder.tvName.setText(": " + item.getName());
        holder.tvEmail.setText(": " + item.getEmail());
        holder.tvPhone.setText(": " + item.getPhone());
        holder.tvStatus.setText(": " + item.getStatus());
        holder.tvOnDate.setText(": "+item.getOndate());
        if (item.getMessage()!=null && item.getMessage().length() > 0) {
            holder.tvMessage.setText(": " +item.getMessage());
        } else {
            holder.tvMessage.setText(": " +"NA");
        }
        if (item.getBookExternalLink()!=null && item.getBookExternalLink().length() > 0) {
            holder.tvLink.setClickable(true);
            holder.tvLink.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href=\'"+item.getBookExternalLink()+"\'>"+ item.getBookExternalLink()+"</a>";
            holder.tvLink.setText(Html.fromHtml(text));
            //holder.tvLink.setText(": " +item.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}

