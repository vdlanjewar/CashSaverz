package com.essensys.cashsaverz.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.essensys.cashsaverz.App;
import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.model.CustomFragment;
import com.essensys.cashsaverz.remote.RetrofitClient;
import com.essensys.cashsaverz.remote.RetrofitInterfaces;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewBookRequestFragment extends CustomFragment implements Callback<ResponseBody> {

    public NewBookRequestFragment() {
        // Required empty public constructor
    }
    private EditText etBookName, etAuthorName, etBookExternalLink, etName, etEmail, etMobile, etMessage;
    private Button btSubmit;
    private TextView tvEmail, tvContact;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_book_request, container, false);

        etBookName = view.findViewById(R.id.etBookName);
        etAuthorName = view.findViewById(R.id.etAuthorName);
        etBookExternalLink = view.findViewById(R.id.etBookExternalLink);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvContact = view.findViewById(R.id.tvContact);
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etMobile = view.findViewById(R.id.etMobile);
        etMessage = view.findViewById(R.id.etMessage);
        btSubmit = view.findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMailBox(tvEmail.getText().toString());
            }
        });
        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialer(tvContact.getText().toString());
            }
        });

        if (!App.isUserLoggedIn(getActivity())) {
            CommonUtilities.openFragmentContainerActivityWithDelay(getActivity(), R.id.nav_login,
                    0);
        }

        return view;
    }

    private void sendData() {
        //etBookName, etAuthorName, etBookExternalLink,
        String bookName = etBookName.getText().toString();
        String authorName = etAuthorName.getText().toString();
        String bookExternalLink = etBookExternalLink.getText().toString();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String mobile = etMobile.getText().toString();
        String message = etMessage.getText().toString();

        if (etBookName.getText().toString().equalsIgnoreCase(""))
            Toasty.error(getActivity(), "Please enter book name", Toast.LENGTH_SHORT,
                    true).show();
        else if (etAuthorName.getText().toString().equalsIgnoreCase(""))
            Toasty.error(getActivity(), "Please enter author name", Toast.LENGTH_SHORT,
                    true).show();
        else if (etBookExternalLink.getText().toString().equalsIgnoreCase(""))
            Toasty.error(getActivity(), "Please enter book external link", Toast.LENGTH_SHORT,
                    true).show();
        else if (etName.getText().toString().equalsIgnoreCase(""))
            Toasty.error(getActivity(), "Please enter your name", Toast.LENGTH_SHORT,
                    true).show();
        else if (!CommonUtilities.isValidEmail(etEmail.getText().toString()))
            Toasty.error(getActivity(), "Please enter valid email id", Toast.LENGTH_SHORT,
                    true).show();
        else if (etMobile.getText().toString().equalsIgnoreCase(""))
            Toasty.error(getActivity(), "Please enter contact number", Toast.LENGTH_SHORT,
                    true).show();
        else if (etMessage.getText().toString().equalsIgnoreCase(""))
            Toasty.error(getActivity(), "Please enter message", Toast.LENGTH_SHORT,
                    true).show();
        else {
            RetrofitClient
                    .getClient(Constant.ServerEndpoint.NEW_BOOK_REQUEST)
                    .create(RetrofitInterfaces.NewBookRequest.class)
                    .post(App.getCurrentUser(getActivity()).getUserAuth(),App.getCurrentUser(getActivity()).getCustomerId(),bookName,authorName,bookExternalLink,name, email, mobile, message)
                    .enqueue(this);
        }

    }

    private void openMailBox(String toMail)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { toMail });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Enquiry for books");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(intent, ""));
    }
    private void openDialer(String number)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            String stringResponse = response.body().string();
            JSONObject jsonObject = new JSONObject(stringResponse);
            JSONObject result = jsonObject.getJSONObject("result");
            String msg = result.getString("msg");
            String msg_string = result.getString("msg_string");

            if (msg.contentEquals("1")) {
                Toast.makeText(getActivity(), msg_string, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }
    @Override
    public void onBackPressed() {
        CommonUtilities.openFragmentContainerActivityWithDelay(getActivity(), R.id.nav_home,
                0);
        getActivity().finish();
    }
}
