package com.essensys.cashsaverz.fragments;

import android.app.MediaRouteButton;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.essensys.cashsaverz.R;
import com.essensys.cashsaverz.activities.HomeActivity;
import com.essensys.cashsaverz.helper.CommonUtilities;
import com.essensys.cashsaverz.helper.Constant;
import com.essensys.cashsaverz.helper.HelloWebViewClient;
import com.essensys.cashsaverz.model.CustomFragment;

import androidx.annotation.NonNull;

public class TermsConditionFragment extends CustomFragment {
    private WebView wvAboutUs;
    private HelloWebViewClient helloWebViewClient;
    private ProgressBar mProgress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        mProgress=(ProgressBar)view.findViewById(R.id.progressBarAbt);

//        helloWebViewClient = new HelloWebViewClient();
        wvAboutUs = view.findViewById(R.id.wvAboutUs);
//        wvAboutUs.loadUrl(Constant.ServerEndpoint.TERMS_AND_CONDITIONS);
//        wvAboutUs.setWebViewClient(helloWebViewClient);

        GetWebView();
        return view;
    }
    //method to get mWebAbout
    private void GetWebView()
    {
        wvAboutUs.getSettings().setJavaScriptEnabled(true);
        wvAboutUs.getSettings().setDomStorageEnabled(true);
        wvAboutUs.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                wvAboutUs.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
//                Toast.makeText(getActivity(),"page started",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                wvAboutUs.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
//                Toast.makeText(getActivity(),"page finished",Toast.LENGTH_LONG).show();

            }

        });

        wvAboutUs.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                if (progress < 100 && mProgress.getVisibility() == View.GONE) {
                    mProgress.setVisibility(View.VISIBLE);
                }
                mProgress.setProgress(progress);
                if (progress == 100) {
                    mProgress.setVisibility(View.GONE);
                }
            }

        });
        if (!TextUtils.isEmpty(Constant.ServerEndpoint.TERMS_AND_CONDITIONS)) {
            String newUA = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36";
            wvAboutUs.getSettings().setUserAgentString(newUA);
            wvAboutUs.loadUrl(Constant.ServerEndpoint.ABOUT_US);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

    }

    @Override
    public void onBackPressed() {
        CommonUtilities.openFragmentContainerActivityWithDelay(getActivity(), R.id.nav_home,
                0);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setupBadge(HomeActivity.getCartCount());
    }
}
