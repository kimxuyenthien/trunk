package de.gimik.app.allpresanapp.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.AlertUtils;
import de.gimik.app.allpresanapp.Utils.Utils;
import de.gimik.app.allpresanapp.viewmodel.SurveyInfo;
import de.gimik.app.allpresanapp.networking.AllpresansAPI;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by QuyenDT on 9/22/2015.
 */
public class TrainingToolFragment extends BaseFragment {
    public static final String TAG = TrainingToolFragment.class.getSimpleName();

    @Bind(R.id.question1)
    TextView question1;

    @Bind(R.id.question2)
    TextView question2;

    @Bind(R.id.mote_schulungTool)
    TextView mote_schulungTool;

    @Bind(R.id.anwser1_1)
    RadioButton anwser1_1;

    @Bind(R.id.anwser1_2)
    RadioButton anwser1_2;

    @Bind(R.id.anwser1_3)
    RadioButton anwser1_3;

    @Bind(R.id.anwser2_1)
    RadioButton anwser2_1;

    @Bind(R.id.anwser2_2)
    RadioButton anwser2_2;

    @Bind(R.id.anwser2_3)
    RadioButton anwser2_3;

    @Bind(R.id.iv_training_tool)
    ImageView iv_training_tool;

    @Bind(R.id.bt_training_tool_previous)
    Button bt_training_tool_previous;

    @Bind(R.id.bt_training_tool_next)
    Button bt_training_tool_next;

    @Bind(R.id.et_training_company)
    EditText et_training_company;

    @Bind(R.id.et_training_surname)
    EditText et_training_surname;

    @Bind(R.id.et_training_name)
    EditText et_training_name;

    @Bind(R.id.et_training_street)
    EditText et_training_street;

    @Bind(R.id.et_training_ort)
    EditText et_training_ort;

    @Bind(R.id.et_training_plz)
    EditText et_training_plz;

    @Bind(R.id.et_training_email)
    EditText et_training_email;

    @Bind(R.id.cb_copy)
    CheckBox cb_copy;

    private List<SurveyInfo.AnswerInfo> listAnswerinfo;

    @Bind(R.id.layout_training_sending)
    LinearLayout layout_training_sending;

    @Bind(R.id.radioTraining1)
    RadioGroup radioTraining1;

    @Bind(R.id.radioTraining2)
    RadioGroup radioTraining2;

    @Bind(R.id.rlNavigation)
    RelativeLayout rlNavigation;

    @Bind(R.id.llEvaluationResult)
    LinearLayout llEvaluationResult;

    @Bind(R.id.tvCorrectAnswer)
    TextView tvCorrectAnswer;

    @Bind(R.id.tvIncorrectAnswer)
    TextView tvIncorrectAnswer;

    @Bind(R.id.tvSendInfo)
    TextView tvSendInfo;

    @Bind(R.id.btnFinish)
    Button btnFinish;

    private int page = 1;

    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_training_tool, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.training_tool);
    }

    @Override
    public void onStart() {
        super.onStart();
        setDisplayButton();

    }

    @OnClick(R.id.bt_training_tool_next)
    public void goNextPage() {
        if (saveAnwser()) {
            radioTraining1.clearCheck();
            radioTraining2.clearCheck();

            page++;
            setDisplayButton();

            bindAnswerData();
        }
    }

    @OnClick(R.id.bt_training_tool_previous)
    public void goPreviousPage() {
        page--;
        setDisplayButton();
        radioTraining1.clearCheck();
        radioTraining2.clearCheck();

        bindAnswerData();
    }

    private void bindAnswerData() {
        if (listAnswerinfo != null) {
            for (SurveyInfo.AnswerInfo answerInfo : listAnswerinfo) {
                if (answerInfo.getQuestion().equalsIgnoreCase(question1.getText().toString())) {
                    if (answerInfo.getAnswer().equalsIgnoreCase(anwser1_1.getText().toString()))
                        anwser1_1.setChecked(true);
                    else if (answerInfo.getAnswer().equalsIgnoreCase(anwser1_2.getText().toString()))
                        anwser1_2.setChecked(true);
                    else if (answerInfo.getAnswer().equalsIgnoreCase(anwser1_3.getText().toString()))
                        anwser1_3.setChecked(true);
                }
                if (answerInfo.getQuestion().equalsIgnoreCase(question2.getText().toString())) {
                    if (answerInfo.getAnswer().equalsIgnoreCase(anwser2_1.getText().toString()))
                        anwser2_1.setChecked(true);
                    else if (answerInfo.getAnswer().equalsIgnoreCase(anwser2_2.getText().toString()))
                        anwser2_2.setChecked(true);
                    else if (answerInfo.getAnswer().equalsIgnoreCase(anwser2_3.getText().toString()))
                        anwser2_3.setChecked(true);
                }
            }
        }
    }

    @OnClick(R.id.btnFinish)
    public void finishTraining() {
        switchMenuItem(0);
    }

    @OnClick(R.id.btn_send_training)
    public void sendTraining() {
        /*if (!Utils.isConnectingToInternet(mContext)) {
            AlertUtils.showAlertDialog(getActivity(), getString(R.string.error), getString(R.string.sending_error));
            return;
        }*/
        if (validateData()) {
            closeKeyBoard();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.sending));
            progressDialog.show();

            AllpresansAPI.getService().sendSurvey(getDataServey())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            // on success
                            new Action1<JsonObject>() {
                                @Override
                                public void call(JsonObject jsonElement) {
                                    AlertUtils.showAlertDialog(getActivity(),"", getString(R.string.sending_ok));
                                }
                            },

                            // on error
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    progressDialog.dismiss();
                                    AlertUtils.showAlertDialog(getActivity(), getString(R.string.error), getString(R.string.sending_error));
                                    throwable.printStackTrace();
                                }
                            },

                            // on completed
                            new Action0() {
                                @Override
                                public void call() {
                                    progressDialog.dismiss();
                                }
                            }

                    );
        }
    }

    private SurveyInfo getDataServey() {
        SurveyInfo surveyInfo = new SurveyInfo();
        surveyInfo.setFirm(et_training_company.getText().toString());
        surveyInfo.setForename(et_training_name.getText().toString());
        surveyInfo.setSurname(et_training_surname.getText().toString());
        surveyInfo.setHouseNumber(et_training_street.getText().toString());
        surveyInfo.setCity(et_training_ort.getText().toString());
        surveyInfo.setZipCode(et_training_plz.getText().toString());
        surveyInfo.setEmail(et_training_email.getText().toString());
        surveyInfo.setAnswers(listAnswerinfo);
        surveyInfo.setCopy(cb_copy.isChecked() ? 1 : 0);
        return surveyInfo;

    }

    private boolean validateData() {
        if (Utils.isNullOrEmpty(et_training_company.getText().toString())) {
            et_training_company.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        if (Utils.isNullOrEmpty(et_training_name.getText().toString())) {
            et_training_name.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        if (Utils.isNullOrEmpty(et_training_surname.getText().toString())) {
            et_training_surname.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }

        if (Utils.isNullOrEmpty(et_training_street.getText().toString())) {
            et_training_street.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        if (Utils.isNullOrEmpty(et_training_plz.getText().toString())) {
            et_training_plz.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        if (Utils.isNullOrEmpty(et_training_ort.getText().toString())) {
            et_training_ort.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        if (!isValidEmail(et_training_email.getText().toString())) {
            et_training_email.requestFocus();
            AlertUtils.showErrorAlertDialog(getContext(), R.string.error_title_contact_data, R.string.error_contact_data);
            return false;
        }
        return true;
    }


    private boolean saveAnwser() {
        String answer1 = "", answer2 = "";
        boolean correct1 = false, correct2 = false;
        SurveyInfo.AnswerInfo answerInfo1 = new SurveyInfo.AnswerInfo();
        boolean isSaved = false;
        if (listAnswerinfo == null) {
            listAnswerinfo = new ArrayList<>();
        } else {
            //case back to change answser
            for (SurveyInfo.AnswerInfo answerInfo : listAnswerinfo) {
                if (answerInfo.getQuestion().equalsIgnoreCase(question1.getText().toString())){
                    if (anwser1_1.isChecked()) {
                        answer1 = anwser1_1.getText().toString();
                        correct1 = (boolean) anwser1_1.getTag();
                    } else if (anwser1_2.isChecked()) {
                        answer1 = anwser1_2.getText().toString();
                        correct1 = (boolean) anwser1_2.getTag();
                    } else if (anwser1_3.isChecked()) {
                        answer1 = anwser1_3.getText().toString();
                        correct1 = (boolean) anwser1_2.getTag();
                    }
                    answerInfo.setAnswer(answer1);
                    answerInfo.setCorrect(correct1);
                    isSaved= true;
                }
                if ( answerInfo.getQuestion().equalsIgnoreCase(question2.getText().toString())) {
                    if (anwser2_1.isChecked()) {
                        answer2 = anwser2_1.getText().toString();
                        correct2 = (boolean) anwser2_1.getTag();
                    } else if (anwser2_2.isChecked()) {
                        answer2 = anwser2_2.getText().toString();
                        correct2 = (boolean) anwser2_2.getTag();
                    } else if (anwser1_3.isChecked()) {
                        answer2 = anwser2_3.getText().toString();
                        correct2 = (boolean) anwser2_3.getTag();
                    }
                    answerInfo.setAnswer(answer2);
                    answerInfo.setCorrect(correct2);
                    isSaved= true;
                }
            }
        }
        if(!isSaved) {
            answerInfo1.setQuestion(question1.getText().toString());
            if (anwser1_1.isChecked()) {
                answer1 = anwser1_1.getText().toString();
                correct1 = (boolean) anwser1_1.getTag();
            } else if (anwser1_2.isChecked()) {
                answer1 = anwser1_2.getText().toString();
                correct1 = (boolean) anwser1_2.getTag();
            } else if (anwser1_3.isChecked()) {
                answer1 = anwser1_3.getText().toString();
                correct1 = (boolean) anwser1_3.getTag();
            }
            answerInfo1.setAnswer(answer1);
            answerInfo1.setCorrect(correct1);

            SurveyInfo.AnswerInfo answerInfo2 = new SurveyInfo.AnswerInfo();
            if (page != 5) {

                answerInfo2.setQuestion(question2.getText().toString());
                if (anwser2_1.isChecked()) {
                    answer2 = anwser2_1.getText().toString();
                    correct2 = (boolean) anwser2_1.getTag();
                } else if (anwser2_2.isChecked()) {
                    answer2 = anwser2_2.getText().toString();
                    correct2 = (boolean) anwser2_2.getTag();
                } else if (anwser2_3.isChecked()) {
                    answer2 = anwser2_3.getText().toString();
                    correct2 = (boolean) anwser2_3.getTag();
                }
                answerInfo2.setAnswer(answer2);
                answerInfo2.setCorrect(correct2);
            }

            boolean isAnswered = page != 5 ? (answer1.equals("") || answer2.equals("")) : (answer1.equals(""));
            if (isAnswered) {
                AlertUtils.showErrorAlertDialog(getActivity(), R.string.training_tool_not_answered);
                return false;
            }
            listAnswerinfo.add(answerInfo1);
            if (page != 5)
                listAnswerinfo.add(answerInfo2);
        }

        return true;
    }

    private void setDisplayButton() {
        bt_training_tool_next.setText(">>");
        if (page == 1) {
            bt_training_tool_previous.setVisibility(View.GONE);
            question2.setVisibility(View.VISIBLE);
            anwser2_1.setVisibility(View.VISIBLE);
            anwser2_2.setVisibility(View.VISIBLE);
            anwser2_3.setVisibility(View.VISIBLE);
            mote_schulungTool.setVisibility(View.VISIBLE);
            layout_training_sending.setVisibility(View.GONE);
            setTextQuestion();

        } else if (page == 5) {
            question2.setVisibility(View.GONE);
            anwser2_1.setVisibility(View.GONE);
            anwser2_2.setVisibility(View.GONE);
            anwser2_3.setVisibility(View.GONE);
            mote_schulungTool.setVisibility(View.GONE);
            iv_training_tool.setVisibility(View.VISIBLE);
            layout_training_sending.setVisibility(View.GONE);

            bt_training_tool_next.setText(R.string.evalutaion);
            setTextQuestion();
        } else if (page == 6) {
            question1.setVisibility(View.GONE);
            anwser1_1.setVisibility(View.GONE);
            anwser1_2.setVisibility(View.GONE);
            anwser1_3.setVisibility(View.GONE);
            question2.setVisibility(View.GONE);
            anwser2_1.setVisibility(View.GONE);
            anwser2_2.setVisibility(View.GONE);
            anwser2_3.setVisibility(View.GONE);
            mote_schulungTool.setVisibility(View.GONE);

            rlNavigation.setVisibility(View.GONE);
            llEvaluationResult.setVisibility(View.VISIBLE);

            int correctAnswerNumber = 0;
            int wrongAnswerNumber = 0;
            for(SurveyInfo.AnswerInfo answerInfo : listAnswerinfo) {
                if (!answerInfo.isCorrect())
                    wrongAnswerNumber++;
                else
                    correctAnswerNumber++;
            }

            if (wrongAnswerNumber > 0) {
                iv_training_tool.setVisibility(View.VISIBLE);
                tvSendInfo.setVisibility(View.GONE);
                btnFinish.setVisibility(View.VISIBLE);
            } else {
                iv_training_tool.setVisibility(View.GONE);
                tvSendInfo.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.GONE);
                layout_training_sending.setVisibility(View.VISIBLE);
            }

            tvCorrectAnswer.setText(correctAnswerNumber + " " + getActivity().getText(R.string.training_tool_correct));
            tvIncorrectAnswer.setText(wrongAnswerNumber + " " + getActivity().getText(R.string.training_tool_incorrect));

        } else {
            bt_training_tool_previous.setVisibility(View.VISIBLE);
            bt_training_tool_next.setVisibility(View.VISIBLE);
            question2.setVisibility(View.VISIBLE);
            anwser2_1.setVisibility(View.VISIBLE);
            anwser2_2.setVisibility(View.VISIBLE);
            anwser2_3.setVisibility(View.VISIBLE);
            mote_schulungTool.setVisibility(View.GONE);
            layout_training_sending.setVisibility(View.GONE);
            setTextQuestion();
        }

    }

    private void setTextQuestion() {
        switch (page) {
            case 1:
                question1.setText(getActivity().getText(R.string.question1));
                question2.setText(getActivity().getText(R.string.question2));
                anwser1_1.setText(getActivity().getText(R.string.anwser1_1));
                anwser1_1.setTag(false);
                anwser1_2.setText(getActivity().getText(R.string.anwser1_2));
                anwser1_2.setTag(true);
                anwser1_3.setText(getActivity().getText(R.string.anwser1_3));
                anwser1_3.setTag(false);
                anwser2_1.setText(getActivity().getText(R.string.anwser2_1));
                anwser2_1.setTag(true);
                anwser2_2.setText(getActivity().getText(R.string.anwser2_2));
                anwser2_2.setTag(false);
                anwser2_3.setText(getActivity().getText(R.string.anwser2_3));
                anwser2_3.setTag(false);
                break;
            case 2:
                question1.setText(getActivity().getText(R.string.question3));
                question2.setText(getActivity().getText(R.string.question4));
                anwser1_1.setText(getActivity().getText(R.string.anwser3_1));
                anwser1_1.setTag(false);
                anwser1_2.setText(getActivity().getText(R.string.anwser3_2));
                anwser1_2.setTag(true);
                anwser1_3.setText(getActivity().getText(R.string.anwser3_3));
                anwser1_3.setTag(false);
                anwser2_1.setText(getActivity().getText(R.string.anwser4_1));
                anwser2_1.setTag(false);
                anwser2_2.setText(getActivity().getText(R.string.anwser4_2));
                anwser2_2.setTag(false);
                anwser2_3.setText(getActivity().getText(R.string.anwser4_3));
                anwser2_3.setTag(true);
                break;
            case 3:
                question1.setText(getActivity().getText(R.string.question5));
                question2.setText(getActivity().getText(R.string.question6));
                anwser1_1.setText(getActivity().getText(R.string.anwser5_1));
                anwser1_1.setTag(true);
                anwser1_2.setText(getActivity().getText(R.string.anwser5_2));
                anwser1_2.setTag(false);
                anwser1_3.setText(getActivity().getText(R.string.anwser5_3));
                anwser1_3.setTag(false);
                anwser2_1.setText(getActivity().getText(R.string.anwser6_1));
                anwser2_1.setTag(false);
                anwser2_2.setText(getActivity().getText(R.string.anwser6_2));
                anwser2_2.setTag(false);
                anwser2_3.setText(getActivity().getText(R.string.anwser6_3));
                anwser2_3.setTag(true);
                break;
            case 4:
                question1.setText(getActivity().getText(R.string.question7));
                question2.setText(getActivity().getText(R.string.question8));
                anwser1_1.setText(getActivity().getText(R.string.anwser7_1));
                anwser1_1.setTag(false);
                anwser1_2.setText(getActivity().getText(R.string.anwser7_2));
                anwser1_2.setTag(true);
                anwser1_3.setText(getActivity().getText(R.string.anwser7_3));
                anwser1_3.setTag(false);
                anwser2_1.setText(getActivity().getText(R.string.anwser8_1));
                anwser2_1.setTag(true);
                anwser2_2.setText(getActivity().getText(R.string.anwser8_2));
                anwser2_2.setTag(false);
                anwser2_3.setText(getActivity().getText(R.string.anwser8_3));
                anwser2_3.setTag(false);
                break;
            case 5:
                question1.setText(getActivity().getText(R.string.question9));
                anwser1_1.setText(getActivity().getText(R.string.anwser9_1));
                anwser1_1.setTag(true);
                anwser1_2.setText(getActivity().getText(R.string.anwser9_2));
                anwser1_2.setTag(false);
                anwser1_3.setText(getActivity().getText(R.string.anwser9_3));
                anwser1_3.setTag(false);
                question2.setText("");
                break;
        }
    }
}
