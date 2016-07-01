package de.gimik.app.allpresanapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.Constant;

/**
 * Created by Mr.Binh on 9/22/2015.
 */
public class FeaturesFragment extends BaseFragment {
    public static final String TAG = FeaturesFragment.class.getSimpleName();
    @Bind(R.id.btn_send_feedback)
    Button btn_send_feedback;

    @Bind(R.id.et_message_feedback)
    EditText et_message_feedback;

    @Bind(R.id.et_email_feedback)
    EditText et_email_feedback;

    @Bind(R.id.et_telephone_number_feedback)
    EditText et_telephone_number_feedback;
    @Bind(R.id.et_name_feedback)
    EditText et_name_feedback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_features, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.features);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_send_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidEmail(et_email_feedback.getText().toString())) {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    String receiver = Constant.EMAIL_SEND_FEEDBACK;
                    String message = et_email_feedback.getText().toString() + "\n" +
                            et_name_feedback.getText().toString() + "\n" +
                            et_telephone_number_feedback.getText().toString() + "\n" +
                            et_message_feedback.getText().toString();

                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{receiver});
                    email.putExtra(Intent.EXTRA_SUBJECT, Constant.SUBJECT_EMAIL_FEEDBACK);
                    email.putExtra(Intent.EXTRA_TEXT, message);
                    email.setType("message/rfc822");

                    startActivity(Intent.createChooser(email, getText(R.string.choosen_email_client)));

                }

            }
        });

    }



}