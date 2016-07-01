package de.gimik.app.allpresanapp.fragment;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.AlertUtils;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.database.MemberDbAdapter;
import de.gimik.app.allpresanapp.model.Member;
import de.gimik.app.allpresanapp.networking.AllpresansAPI;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dang on 25.03.2015.
 */
public class LoginFragment extends BaseFragment {
    public static final String TAG = LoginFragment.class.getSimpleName();
    private Account account;
    // Activity result key for camera
    private static final int RESULT_LOAD_IMG = 1;

    // UI components
    @Bind(R.id.et_customer_number)
    EditText et_customer_number;
    @Bind(R.id.et_postcode)
    EditText et_postcode;

    @Bind(R.id.btn_login)
    Button btn_login;

    @Bind(R.id.btn_cancel_login)
    Button btn_cancel_login;


    @Bind(R.id.cb_save_login)
    CheckBox cb_save_login;

    @Bind(R.id.tvPasswordForgot)
    TextView tvPasswordForgot;

    private String original;
    private ProgressDialog progressDialog;

    private Member member=null;
    private boolean resultLogin=false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container,
                false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.login));

        MemberDbAdapter memberDbAdapter = new MemberDbAdapter(getBaseActivity());
        member = memberDbAdapter.queryForFirst();

        if(member!=null && member.isSavedLogin()) {
            et_customer_number.setText(member.getMemberNumber());
            et_postcode.setText(member.getPostCode());
            cb_save_login.setChecked(true);

        }else {
            et_customer_number.setText("");
            et_postcode.setText("");
            cb_save_login.setChecked(false);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.show();
                if (cb_save_login.isChecked()) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    prefs.edit().putLong(Constant.ExtraKey.PREF_KEEP_LOGIN, 1);
                }
                AllpresansAPI.getService().login(et_customer_number.getText().toString(), et_postcode.getText().toString())
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                // on success
                                new Action1<JsonObject>() {
                                    @Override
                                    public void call(JsonObject jsonElement) {
                                        Gson gson = new GsonBuilder().create();

                                        member = gson.fromJson(jsonElement, Member.class);
                                        if (member != null && member.isActive()) {
                                            getMemberDbAdapter().clMember();
                                            if (cb_save_login.isChecked())
                                                member.setSavedLogin(true);
                                            else
                                                member.setSavedLogin(false);
                                            getMemberDbAdapter().saveOrUpdate(member);
                                            resultLogin = true;
                                        } else {
                                            resultLogin = false;
                                        }

                                    }
                                },

                                // on error
                                new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        progressDialog.dismiss();
                                        AlertUtils.showErrorAlertDialog(getActivity(), R.string.login_failed);
                                        throwable.printStackTrace();
                                    }
                                },

                                // on completed

                                new Action0() {
                                    @Override
                                    public void call() {
                                        progressDialog.dismiss();

                                        if (!resultLogin) {
                                            AlertUtils.showErrorAlertDialog(getActivity(), R.string.login_failed);
                                        } else {
//                                            replaceFragment(new ProductWorldsFragment(), ProductWorldsFragment.TAG, false, true, null);
                                            getMainActivity().setupNavigationMenu(true);
                                            switchMenuItem(0);
                                        }
                                    }
                                }
                        );
            }
        });

        btn_cancel_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenuItem(0);
            }
        });

        tvPasswordForgot.setTextColor(cb_save_login.getCurrentHintTextColor());
        tvPasswordForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMenuItem(3);
            }
        });
    }
}
