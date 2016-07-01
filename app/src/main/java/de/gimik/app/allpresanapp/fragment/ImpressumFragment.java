package de.gimik.app.allpresanapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.R;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class ImpressumFragment extends BaseFragment{
    public static final String TAG = ImpressumFragment.class.getSimpleName();

	@Bind(R.id.wvContent)
    WebView wvContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_impressum, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(R.string.impressum);

        wvContent.loadUrl("file:///android_asset/impressum.html");
    }
}
