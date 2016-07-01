package de.gimik.app.allpresanapp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.MainActivity;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.Utils;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class MainFragment extends BaseFragment{
    public static final String TAG = MainFragment.class.getSimpleName();

    @Bind(R.id.btn_go_main)
    Button btnGoMain;

    @Bind(R.id.btn_login_main)
    Button btnLoginMain;

    @Bind(R.id.ivMain)
    ImageView ivMain;

    @Bind(R.id.txtConfirmFristTime)
    TextView txtConfirmFristTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);
        Boolean isNotFirstTime =  Utils.getBooleanForKey(
                mContext,
                Constant.NOT_FRIST_TIME);
        if(isNotFirstTime) {
            txtConfirmFristTime.setVisibility(View.GONE);
            btnLoginMain.setVisibility(View.GONE);
            btnGoMain.setVisibility(View.GONE);
//            ivMain.setImageDrawable(getResources().getDrawable(R.drawable.splash));
        }else {
            txtConfirmFristTime.setVisibility(View.VISIBLE);
            btnLoginMain.setVisibility(View.VISIBLE);
            btnGoMain.setVisibility(View.VISIBLE);
//            ivMain.setImageDrawable(getResources().getDrawable(R.drawable.splash_3));
        }

        int[] images = {R.drawable.splash_1, R.drawable.splash_2, R.drawable.splash_3, R.drawable.splash_4, R.drawable.splash_5};
        int posOfImage = Utils.getIntForKey(getActivity(), Constant.SPLASH_IMAGE_POSITION);
        ivMain.setBackgroundResource(images[posOfImage]);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.home);

    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onStart() {
        super.onStart();
        btnLoginMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveBooleanForKey(
                        mContext,
                        Constant.NOT_FRIST_TIME,true);
                replaceFragment(new LoginFragment(), LoginFragment.TAG, true, true, null);

            }
        });

        btnGoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.saveBooleanForKey(
                        mContext,
                        Constant.NOT_FRIST_TIME, true);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

}
