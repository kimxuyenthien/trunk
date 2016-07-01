package de.gimik.app.allpresanapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.Utils.AlertUtils;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.DateTimeUtility;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.database.EventDbAdapter;
import de.gimik.app.allpresanapp.model.Event;
import de.gimik.app.allpresanapp.networking.AllpresansAPI;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Quyendt on 9/22/2015.
 */
public class EventDetailsActivity extends BaseActivity {
    public static final String TAG = EventDetailsActivity.class.getSimpleName();
    @Bind(R.id.txtName)
    TextView tvName;

    @Bind(R.id.txtPlace)
    TextView tvPlace;

    @Bind(R.id.txtDate)
    TextView tvDate;

    @Bind(R.id.txtSummary)
    TextView tvDescription;

    @Bind(R.id.txtAkademieRecommend)
    TextView txtAkademieRecommend;

    @Bind(R.id.llContact)
    LinearLayout llContact;

    @Bind(R.id.btn_registration)
    Button btnRegistration;

    @Bind(R.id.imageView)
    ImageView imageView;

    // Contact form
    @Bind(R.id.btn_send_contact)
    Button btn_send_contact;

    @Bind(R.id.et_company)
    EditText et_company;

    @Bind(R.id.et_surname)
    EditText et_surname;

    @Bind(R.id.et_name)
    EditText et_name;

    @Bind(R.id.et_birthdate)
    EditText et_birthdate;

    @Bind(R.id.et_street_house_number)
    EditText et_street_house_number;

    @Bind(R.id.et_ort)
    EditText et_ort;

    @Bind(R.id.et_plz)
    EditText et_plz;

    @Bind(R.id.et_phone)
    EditText et_phone;

    @Bind(R.id.et_email)
    EditText et_email;

    @Bind(R.id.cb_contact2)
    CheckBox cb_contact2;

    @Bind(R.id.llContact2)
    LinearLayout llContact2;

    @Bind(R.id.et_surname2)
    EditText et_surname2;

    @Bind(R.id.et_name2)
    EditText et_name2;

    @Bind(R.id.et_birthdate2)
    EditText et_birthdate2;

    @Bind(R.id.cb_copy)
    CheckBox cb_copy;

    private ProgressDialog progressDialog;

    private EventDbAdapter eventDbAdapter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public void onResume() {
        super.onResume();
//        sMoreetTitle(R.string.event_training);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        long eventId;
        if (bundle != null) {
            eventId = bundle.getLong(Constant.EVENT_ID);
            eventDbAdapter = new EventDbAdapter(this);
            final Event event = eventDbAdapter.queryForId(eventId);
            tvName.setText(event.getName());
            tvDate.setText(event.getDate());
            tvPlace.setText(event.getPlace());
            tvDescription.setText(Html.fromHtml(event.getDescription()));

            setTitle(event.getName());

            File imgFile = new File(getDownloadBasePath(), event.getLocalImage());

            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(myBitmap);
            }else {
                imageView.setImageBitmap(null);
                imageView.setVisibility(View.GONE);
            }

            final EditText[] requiredContact1Texts = new EditText[] { et_name, et_surname, et_birthdate, et_street_house_number, et_plz, et_ort, et_email };
            if (event.getNews() == 1) {
                txtAkademieRecommend.setVisibility(View.VISIBLE);
                btnRegistration.setVisibility(View.VISIBLE);
                btnRegistration.setText(R.string.register_now);
                btnRegistration.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isHiddenToVisible = llContact.getVisibility() != View.VISIBLE;
                        llContact.setVisibility(View.VISIBLE);
                        if (isHiddenToVisible) {
                            btnRegistration.setText(R.string.registration);
                            et_company.requestFocus();
                            displayDateTimePicker(et_birthdate);
                            displayDateTimePicker(et_birthdate2);

                            TextWatcher contact1InfoWatcher = (new TextWatcher() {

                                public void afterTextChanged(Editable s) {

                                }

                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    boolean isFillAll = true;
                                    for(EditText et : requiredContact1Texts) {
                                        if (et.getText().toString().trim().equals("")) {
                                            isFillAll = false;
                                            break;
                                        }
                                    }
                                    if (isFillAll)
                                        llContact2.setVisibility(View.VISIBLE);
                                }
                            });

                            for(EditText et : requiredContact1Texts) {
                                et.addTextChangedListener(contact1InfoWatcher);
                            }

                            btn_send_contact.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (validateData()) {
                                        String company, name, surname, birthdate, street, houseNumber, ort, plz, email, telephone, name2, surname2, birthdate2, message;
                                        company = et_company.getText().toString();
                                        name = et_name.getText().toString();
                                        surname = et_surname.getText().toString();
                                        birthdate = et_birthdate.getText().toString();
                                        street = et_street_house_number.getText().toString();
                                        ort = et_ort.getText().toString();
                                        plz = et_plz.getText().toString();
                                        telephone = et_phone.getText().toString();
                                        email = et_email.getText().toString();
                                        int contact2 = cb_contact2.isChecked() ? 1 : 0;
                                        int copy = cb_copy.isChecked() ? 1 : 0;

                                        name2 = cb_contact2.isChecked() ? et_name2.getText().toString() : "";
                                        surname2 = cb_contact2.isChecked() ? et_surname2.getText().toString() : "";
                                        birthdate2 = cb_contact2.isChecked() ? et_birthdate2.getText().toString() : "";

                                        closeKeyBoard();
                                        progressDialog = new ProgressDialog(EventDetailsActivity.this);
                                        progressDialog.setMessage(getString(R.string.sending));
                                        progressDialog.show();

                                        AllpresansAPI.getService().sendEventRegistration(new AllpresansAPI.AllpresanService.EventRegistrationInfo(
                                                event.getName(), company, name, surname, DateTimeUtility.formatDate(DateTimeUtility.parseGermanShortDate(birthdate)), street, "", plz, ort, email, telephone,
                                                contact2, name2, surname2, DateTimeUtility.formatDate(DateTimeUtility.parseGermanShortDate(birthdate2)), copy, "Allpresan Akademie Anmeldung"))
                                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(
                                                        // on success
                                                        new Action1<AllpresansAPI.AllpresanService.Result>() {
                                                            @Override
                                                            public void call(AllpresansAPI.AllpresanService.Result result) {
                                                                resetContact();

                                                            }
                                                        },

                                                        // on error
                                                        new Action1<Throwable>() {
                                                            @Override
                                                            public void call(Throwable throwable) {
                                                                progressDialog.dismiss();
                                                                AlertUtils.showErrorAlertDialog(EventDetailsActivity.this, R.string.send_contact_failed);
                                                                throwable.printStackTrace();
                                                            }
                                                        },

                                                        // on completed

                                                        new Action0() {
                                                            @Override
                                                            public void call() {
                                                                progressDialog.dismiss();
                                                                llContact.setVisibility(View.GONE);
                                                                btnRegistration.setVisibility(View.VISIBLE);
                                                                btnRegistration.setText(R.string.register_now);

                                                            }
                                                        }
                                                );

                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private void displayDateTimePicker(final TextView tvDate) {
        Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                tvDate.setText(DateTimeUtility.formatGermanShortDate(calendar.getTime()));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerDialog.show();
            }
        });

    }

    private void resetContact() {
        et_company.setText("");
        et_name.setText("");
        et_surname.setText("");
        et_birthdate.setText("");
        et_name2.setText("");
        et_surname2.setText("");
        et_birthdate2.setText("");
        et_street_house_number.setText("");
        et_ort.setText("");
        et_plz.setText("");
        et_phone.setText("");
        et_email.setText("");

    }

    private boolean validateData() {
        if (Utils.isNullOrEmpty(et_name.getText().toString())) {
            et_name.requestFocus();
            AlertUtils.showErrorAlertDialog(EventDetailsActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        if (Utils.isNullOrEmpty(et_surname.getText().toString())) {
            et_surname.requestFocus();
            AlertUtils.showErrorAlertDialog(EventDetailsActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        if (Utils.isNullOrEmpty(et_birthdate.getText().toString())) {
            et_birthdate.requestFocus();
            AlertUtils.showErrorAlertDialog(EventDetailsActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        if (Utils.isNullOrEmpty(et_street_house_number.getText().toString())) {
            et_street_house_number.requestFocus();
            AlertUtils.showErrorAlertDialog(EventDetailsActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        if (Utils.isNullOrEmpty(et_plz.getText().toString())) {
            et_plz.requestFocus();
            AlertUtils.showErrorAlertDialog(EventDetailsActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        if (Utils.isNullOrEmpty(et_ort.getText().toString())) {
            et_ort.requestFocus();
            AlertUtils.showErrorAlertDialog(EventDetailsActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        if (!isValidEmail(et_email.getText().toString())) {
            et_email.requestFocus();
            AlertUtils.showErrorAlertDialog(EventDetailsActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        if (cb_contact2.isChecked()) {
            if (Utils.isNullOrEmpty(et_name2.getText().toString())) {
                et_name2.requestFocus();
                AlertUtils.showErrorAlertDialog(EventDetailsActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
                return false;
            }

            if (Utils.isNullOrEmpty(et_surname2.getText().toString())) {
                et_surname2.requestFocus();
                AlertUtils.showErrorAlertDialog(EventDetailsActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
                return false;
            }

            if (Utils.isNullOrEmpty(et_birthdate2.getText().toString())) {
                et_birthdate2.requestFocus();
                AlertUtils.showErrorAlertDialog(EventDetailsActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
                return false;
            }
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
