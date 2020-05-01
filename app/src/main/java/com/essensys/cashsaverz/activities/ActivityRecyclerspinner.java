package com.essensys.cashsaverz.activities;
import android.content.Context;
import android.os.Bundle;

import com.essensys.cashsaverz.adapter.UserAdapter;
import com.essensys.cashsaverz.model.User;
import com.essensys.cashsaverz.model.User1;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Spinner;

import com.essensys.cashsaverz.R;
public class ActivityRecyclerspinner extends AppCompatActivity {
    Spinner spinner;
    private Context mContext;
    private RecyclerView recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerspinner);
        mContext = this;

        recycler = (RecyclerView)findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        UserAdapter userAdapter = new UserAdapter(mContext, User1.getUserList());
        recycler.setAdapter(userAdapter);
    }
}
