package de.gimik.app.allpresanapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import de.gimik.app.allpresanapp.Utils.AlertUtils;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.Utils.DateTimeUtility;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.database.MemberDbAdapter;
import de.gimik.app.allpresanapp.model.Member;
import de.gimik.app.allpresanapp.networking.AllpresansAPI;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.io.File;
import java.util.Date;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class TreatmentContactActivity extends BaseActivity {
    public static final String TAG = TreatmentContactActivity.class.getSimpleName();

    @Bind(R.id.btn_send)
    Button btn_send;

    @Bind(R.id.tvAgreement)
    TextView tvAgreement;

    @Bind(R.id.et_message)
    EditText et_message;

    @Bind(R.id.et_email)
    EditText et_email;

    @Bind(R.id.et_subject)
    EditText et_subject;

    @Bind(R.id.iv_tip_detail)
    ImageView iv_tip_detail;

    @Bind(R.id.flImage)
    FrameLayout flImage;

    @Bind(R.id.ib_camera_contact)
    ImageButton ib_camera_contact;

    @Bind(R.id.cb_accept)
    CheckBox cb_accept;

    @Bind(R.id.cb_copy_email)
    CheckBox cb_copy_email;

    private ProgressDialog progressDialog;

    private int result = 1;
    String imagePath = null;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    int width;
    int height;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_treatment_contact);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        resetContact();

    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.send_picture);
    }


    @Override
    public void onStart() {
        super.onStart();
        setTitle(getString(R.string.treatment_tip));
        final ViewTreeObserver observer = iv_tip_detail.getViewTreeObserver();

        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                iv_tip_detail.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                width = iv_tip_detail.getMeasuredWidth();
                height = iv_tip_detail.getMeasuredHeight();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    String customerNumber, email, subject, message;
                    subject = et_subject.getText().toString();
                    message = et_message.getText().toString();
                    email = et_email.getText().toString();

                    int copy_email = cb_copy_email.isChecked() ? 1 : 0;

                    Member member = new MemberDbAdapter(TreatmentContactActivity.this).queryForFirst();
                    customerNumber = "";
                    if (member != null)
                        customerNumber = member.getMemberNumber();


                    closeKeyBoard();
                    progressDialog = new ProgressDialog(TreatmentContactActivity.this);
                    progressDialog.setMessage(getString(R.string.sending));
                    progressDialog.show();

                    RequestBody requestBody = RequestBody
                            .create(MediaType.parse("application/octet-stream"), imagePath);

                    AllpresansAPI.getService().sendPictureContact(requestBody, customerNumber, email, subject,
                            message, copy_email)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    // on success
                                    new Action1<AllpresansAPI.AllpresanService.Result>() {
                                        @Override
                                        public void call(AllpresansAPI.AllpresanService.Result result) {
                                            resetContact();
                                            progressDialog.dismiss();
                                            AlertUtils.showInfoAlertDialog(TreatmentContactActivity.this, R.string.send_success);
                                        }
                                    },

                                    // on error
                                    new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            progressDialog.dismiss();
                                            AlertUtils.showErrorAlertDialog(TreatmentContactActivity.this, R.string.send_contact_failed);
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
        });

        ib_camera_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assume thisActivity is the current activity
                int permissionCheck = ContextCompat.checkSelfPermission(TreatmentContactActivity.this,
                        Manifest.permission.CAMERA);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(TreatmentContactActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            Constant.CAMERA_REQUEST_CODE);

                } else {
                    startCameraActivity(DateTimeUtility.Format_yyyyMMdd_HHmmss.format(new Date()));
                }
            }
        });

        initAgreementUI();
    }

    private void resetContact() {
        et_email.setText("");
        et_email.clearFocus();
        et_subject.setText("");
        et_subject.clearFocus();

        et_message.setText("");
        et_message.clearFocus();

        cb_accept.setChecked(false);
        cb_accept.clearFocus();
        cb_copy_email.setChecked(false);
        cb_copy_email.clearFocus();
        imagePath = null;
        iv_tip_detail.setImageBitmap(null);
    }

    private boolean validateData() {
        if (!cb_accept.isChecked()) {
            AlertUtils.showErrorAlertDialog(TreatmentContactActivity.this, R.string.error, R.string.accept_foto_message);
            return false;
        }

        if (imagePath == null || !new File(imagePath).exists()) {
            AlertUtils.showErrorAlertDialog(TreatmentContactActivity.this, R.string.error_title_contact_data, R.string.error_picture_data);
            return false;
        }

        if (Utils.isNullOrEmpty(et_email.getText().toString())) {
            et_email.requestFocus();
            AlertUtils.showErrorAlertDialog(TreatmentContactActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        if (!isValidEmail(et_email.getText().toString())) {
            et_email.requestFocus();
            AlertUtils.showErrorAlertDialog(TreatmentContactActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        if (Utils.isNullOrEmpty(et_subject.getText().toString())) {
            et_subject.requestFocus();
            AlertUtils.showErrorAlertDialog(TreatmentContactActivity.this, R.string.error_title_contact_data, R.string.error_invalid_subject);
            return false;
        }
        if (Utils.isNullOrEmpty(et_message.getText().toString())) {
            et_message.requestFocus();
            AlertUtils.showErrorAlertDialog(TreatmentContactActivity.this, R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        return true;
    }


    private void initAgreementUI() {
        String agreementContent = getString(R.string.agreement_content);
        SpannableString ss = new SpannableString(agreementContent);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                AlertUtils.showAlertDialog(TreatmentContactActivity.this, R.string.the_agreement_title, R.string.the_agreement_content);
            }
        };

        String theAgreement = getString(R.string.the_agreement);
        int start = agreementContent.indexOf(theAgreement);
        int end = start + theAgreement.length();

        ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(ss);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        tvAgreement.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        try {
            if (requestCode == Constant.takePhoto) {
                if (resultCode == Activity.RESULT_OK) {
                    if (newImagePath != null) {
                        //Bitmap bmp = Utils.loadScaledBitmap(newImagePath, iv_tip_detail.getMeasuredWidth());
                        //iv_tip_detail.setImageBitmap(bmp);
                        Picasso.with(this).load(new File(newImagePath)).resize(width, height).into(iv_tip_detail);
                        imagePath = newImagePath;
                    }

                } else {
                    AlertUtils.showErrorAlertDialog(TreatmentContactActivity.this, R.string.take_photo_error);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // go back
        this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
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
