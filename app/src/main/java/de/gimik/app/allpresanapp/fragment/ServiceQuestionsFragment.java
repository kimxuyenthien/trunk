package de.gimik.app.allpresanapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.database.ServiceQuestionDbAdapter;
import de.gimik.app.allpresanapp.model.ServiceQuestion;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class ServiceQuestionsFragment extends BaseFragment {
    public static final String TAG = ServiceQuestionsFragment.class.getSimpleName();

    @Bind(R.id.wvServiceQuestion)
    WebView wvServiceQuestion;

    private ServiceQuestionDbAdapter serviceQuestionDbAdapter;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_questions, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.service_questions);
    }

    @Override
    public void onStart() {
        super.onStart();
        serviceQuestionDbAdapter = getServiceQuestionDbAdapter();
        ServiceQuestion serviceQuestion = serviceQuestionDbAdapter.queryFirstRecord();

        if (serviceQuestion != null && serviceQuestion.getLink() != null) {
            wvServiceQuestion.setWebViewClient(new ServiceQuestionWebview());
            wvServiceQuestion.getSettings().setJavaScriptEnabled(true);
            openURL(serviceQuestion.getLink());
        }
    }

    /**
     * Opens the URL in a browser
     */
    private void openURL(String url) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        wvServiceQuestion.loadUrl(url);
        wvServiceQuestion.requestFocus();
    }

    private class ServiceQuestionWebview extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            progressDialog.dismiss();
        }

        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            try {
                wvServiceQuestion.stopLoading();
            } catch (Exception e) {
            }
            wvServiceQuestion.loadUrl("about:blank");
        }
    }
}
