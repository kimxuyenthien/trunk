package de.gimik.app.allpresanapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.AlertUtils;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.networking.AllpresansAPI;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class ContactFragment extends BaseFragment {
    public static final String TAG = ContactFragment.class.getSimpleName();

    @Bind(R.id.et_company)
    EditText et_company;

    @Bind(R.id.et_surname)
    EditText et_surname;

    @Bind(R.id.et_name)
    EditText et_name;

    @Bind(R.id.et_street)
    EditText et_street;

//    @Bind(R.id.et_house_number)
//    EditText et_house_number;

    @Bind(R.id.et_ort)
    EditText et_ort;

    @Bind(R.id.et_plz)
    EditText et_plz;

    @Bind(R.id.et_phone)
    EditText et_phone;

    @Bind(R.id.et_email)
    EditText et_email;

    @Bind(R.id.et_message)
    EditText et_message;

    @Bind(R.id.cb_call_phone)
    CheckBox cb_call_phone;

    @Bind(R.id.cb_copy)
    CheckBox cb_copy;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.contact);
    }

    @Override
    public void onStart() {
        super.onStart();
        et_phone.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_phone.getText().toString().trim().equals(""))
                    cb_call_phone.setVisibility(View.GONE);
                else
                    cb_call_phone.setVisibility(View.VISIBLE);
            }
        });

    }

    @OnClick(R.id.btn_send_contact)
    public void sendContact() {
        if (validateData()) {
            String company, name, surname, street, houseNumber, ort, plz, email, telephone, message;
            company = et_company.getText().toString();
            name = et_name.getText().toString();
            surname = et_surname.getText().toString();
            street = et_street.getText().toString();
            houseNumber = "";//et_house_number.getText().toString();
            ort = et_ort.getText().toString();
            plz = et_plz.getText().toString();
            telephone = et_phone.getText().toString();
            message = et_message.getText().toString();
            email = et_email.getText().toString();

            int recall = telephone.trim().equals("") ? 0 : cb_call_phone.isChecked() ? 1 : 0;
            int copy = cb_copy.isChecked() ? 1 : 0;

            closeKeyBoard();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.sending));
            progressDialog.show();

            AllpresansAPI.getService().sendContact(new AllpresansAPI.AllpresanService.ContactEmailInfo(company, name, surname, street, houseNumber, plz, ort, email, telephone, recall, message, copy, ""))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            // on success
                            new Action1<AllpresansAPI.AllpresanService.Result>() {
                                @Override
                                public void call(AllpresansAPI.AllpresanService.Result result) {
                                    resetContact();
                                    progressDialog.dismiss();
                                    AlertUtils.showAlertDialog(getActivity(), "", getString(R.string.send_success));
                                    switchMenuItem(0);
                                }
                            },

                            // on error
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    progressDialog.dismiss();
                                    AlertUtils.showErrorAlertDialog(getActivity(), R.string.send_contact_failed);
                                    throwable.printStackTrace();
                                }
                            },

                            // on completed

                            new Action0() {
                                @Override
                                public void call() {


                                }
                            }
                    );

        }

    }

    private void resetContact() {
        et_company.setText("");
        et_name.setText("");
        et_surname.setText("");
        et_street.setText("");
//        et_house_number.setText("");
        et_ort.setText("");
        et_plz.setText("");
        et_phone.setText("");
        et_message.setText("");
        cb_call_phone.setVisibility(View.GONE);
        et_email.setText("");

    }

    private boolean validateData() {
        if (Utils.isNullOrEmpty(et_name.getText().toString())) {
            et_name.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        if (Utils.isNullOrEmpty(et_surname.getText().toString())) {
            et_surname.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        if (Utils.isNullOrEmpty(et_street.getText().toString())) {
            et_street.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        if (Utils.isNullOrEmpty(et_plz.getText().toString())) {
            et_plz.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        if (Utils.isNullOrEmpty(et_ort.getText().toString())) {
            et_ort.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        if (!isValidEmail(et_email.getText().toString())) {
            et_email.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        if (Utils.isNullOrEmpty(et_message.getText().toString())) {
            et_message.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        return true;
    }


}
