package com.essensys.cashsaverz.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.model.Customer;
import com.essensys.cashsaverz.model.User;
import com.essensys.cashsaverz.model.User1;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context mContext;
    private List<User1> userList;
    private ArrayAdapter<Customer> dataAdapter;
    public UserAdapter(Context context, List<User1> userList){
        this.mContext = context;
        this.userList = userList;

        dataAdapter = new ArrayAdapter<Customer>(mContext,
                android.R.layout.simple_spinner_item, Customer.fillCustomer());
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }
    @NonNull

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.tv_1.setText(""+userList.get(position).getUserId());
        holder.tv_2.setText(userList.get(position).getUserName());
        holder.spinner.setAdapter(dataAdapter);
    }
    class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_1;
        public TextView tv_2;
        public Spinner spinner;
        public UserViewHolder(View itemView) {
            super(itemView);
            tv_1 = (TextView) itemView.findViewById(R.id.tv_1);
            tv_2 = (TextView) itemView.findViewById(R.id.tv_2);
            spinner = (Spinner) itemView.findViewById(R.id.spinner);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // On selecting a spinner item

                        String item = parent.getItemAtPosition(position).toString();



                    // Showing selected spinner item
                    //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }

}
