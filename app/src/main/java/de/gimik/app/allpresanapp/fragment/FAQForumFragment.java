package de.gimik.app.allpresanapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.R;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class FAQForumFragment extends BaseFragment{
    public static final String TAG = FAQForumFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_faq_forum, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.faq_forum);
    }
}
