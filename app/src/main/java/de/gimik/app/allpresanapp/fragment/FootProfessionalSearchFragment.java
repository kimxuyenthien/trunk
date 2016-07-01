package de.gimik.app.allpresanapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.AlertUtils;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class FootProfessionalSearchFragment extends BaseFragment {
    public static final String TAG = FootProfessionalSearchFragment.class.getSimpleName();
    private NetworkChangeReceiver status = new NetworkChangeReceiver();

    @Bind(R.id.wvContent)
    WebView wvContent;

    @Bind(R.id.btnRefresh)
    Button btnRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_foot_professional_search, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.foot_prefessional_search);
        status.onReceive(mContext, getActivity().getIntent());

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage();
            }
        });
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(wvContent,true);
        }
        loadPage();
    }

    private void loadPage() {

        if (Utils.isNetworkAvailable(mContext)) {
            wvContent.setVisibility(View.VISIBLE);
            btnRefresh.setVisibility(View.GONE);
            wvContent.loadUrl(Constant.URL_SEARCH_FOOT);
            wvContent.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    wvContent.setVisibility(View.GONE);
                    btnRefresh.setVisibility(View.VISIBLE);
                    AlertUtils.showErrorAlertDialog(getActivity(), R.string.error_no_internet_for_foot_profi);

                }

            });
        } else {
            wvContent.setVisibility(View.GONE);
            btnRefresh.setVisibility(View.VISIBLE);
            AlertUtils.showErrorAlertDialog(getActivity(), R.string.error_no_internet_for_foot_profi);
        }
    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            final android.net.NetworkInfo wifi = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (!wifi.isAvailable()) {
                // Do something
                wvContent.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.VISIBLE);
                AlertUtils.showErrorAlertDialog(getActivity(), R.string.error_no_internet_for_foot_profi);
            }
        }
    }


}