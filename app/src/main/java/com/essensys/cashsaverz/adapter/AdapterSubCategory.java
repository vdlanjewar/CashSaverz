package com.essensys.cashsaverz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.essensys.cashsaverz.Interface.InterfaceClickListener;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.model.SubCategory;
import java.util.List;

public class AdapterSubCategory extends Adapter<AdapterSubCategory.ViewHolder> implements OnClickListener {
    private InterfaceClickListener interfaceClickListener;
    private final Context mContext;
    private List<SubCategory> mItems;

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private LinearLayout mLinAll;
        /* access modifiers changed from: private */
        public LinearLayout mLinearSubcat;
        private View mView;
        /* access modifiers changed from: private */
        public TextView txtSubCat;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtSubCat = (TextView) itemView.findViewById(R.id.txt_subcat);
            this.mView = itemView.findViewById(R.id.view_subcat);
            this.mLinearSubcat = (LinearLayout) itemView.findViewById(R.id.linearSubCat);
        }
    }

    public AdapterSubCategory(Context context, List<SubCategory> subCategoryList, InterfaceClickListener interfaceClickListener2) {
        this.mItems = subCategoryList;
        this.mContext = context;
        this.interfaceClickListener = interfaceClickListener2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View postView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sub_cat, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);
        postView.setTag(viewHolder);
        return viewHolder;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtSubCat.setText(((SubCategory) this.mItems.get(position)).getSubCatName());
        holder.mLinearSubcat.setOnClickListener(this);
        holder.mLinearSubcat.setTag(Integer.valueOf(position));
    }

    public int getItemCount() {
        return this.mItems.size();
    }

    public void onClick(View v) {
        StringBuilder sb = new StringBuilder();
        String str = "";
        sb.append(str);
        sb.append(v.getTag());
        int pos = Integer.parseInt(sb.toString());
        if (v.getId() == R.id.linearSubCat) {
            this.interfaceClickListener.Onclick(((SubCategory) this.mItems.get(pos)).getSubCatId(), str);
        }
    }
}
