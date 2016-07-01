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
public class AboutFragment extends BaseFragment{
    public static final String TAG = AboutFragment.class.getSimpleName();

	@Bind(R.id.wvAboutUs)
    WebView wvAboutUs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
		
        setTitle(R.string.about_us);
		
		wvAboutUs.loadUrl("file:///android_asset/about_us.html");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
           }


}