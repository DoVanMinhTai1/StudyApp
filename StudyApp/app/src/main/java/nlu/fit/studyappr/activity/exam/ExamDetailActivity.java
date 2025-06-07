package nlu.fit.studyappr.activity.exam;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.os.CountDownTimer;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.api.exam.ExamApiService;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.model.exam.Answer;
import nlu.fit.studyappr.model.exam.Exam;
import nlu.fit.studyappr.model.exam.ExamResult;
import nlu.fit.studyappr.model.exam.ExamSubmited;
import nlu.fit.studyappr.model.exam.Question;
import nlu.fit.studyappr.model.exam.SubQuestion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamDetailActivity extends AppCompatActivity {
    private TextView examTitleTextView, examTimeTextView, exit_btn;
    private Button preButton, nextButton, submitButton;
    private static final String TAG = "ExamDetailActivity";
    private CountDownTimer countDownTimer;
    private List<Question> questionList;
    private int currentIndex = 0;

    private final List<Answer> answerList = new ArrayList<>();
    private long startTimeMillis;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_take_exam);

        examTitleTextView = findViewById(R.id.exam_title);
        examTimeTextView = findViewById(R.id.exam_time);
        preButton=findViewById(R.id.prevExamQuestionButton);
        nextButton=findViewById(R.id.nextExamQuestionButton);
        submitButton=findViewById(R.id.submitExamButton);
        exit_btn=findViewById(R.id.toolbarExamExitButton);

        exit_btn.setOnClickListener(v->{
            showConfirmExitDialog();
        });


        String examId = getIntent().getStringExtra("examId");
        if (examId != null) {
            fetchExamDetail(examId);
        }

        setupNavigationButtons();

        submitButton.setOnClickListener(v -> showConfirmSubmitDialog());

        FloatingActionButton fab = findViewById(R.id.fab_toggle_question_list);
        fab.setOnClickListener(v -> showQuestionListDialog());


    }

    private void fetchExamDetail(String examId) {
        ExamApiService apiService = InitializeRetrofit.getNodeApiInstance().create(ExamApiService.class);
        Call<Exam> call = apiService.getExamById(examId);

        call.enqueue(new Callback<Exam>() {
            @Override
            public void onResponse(Call<Exam> call, Response<Exam> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Exam exam = response.body();

                    examTitleTextView.setText(exam.getTitle());


                    int minutes = exam.getDuration();
                    examTimeTextView.setText(String.format("%02d:00", minutes));
                    startTimeMillis = System.currentTimeMillis();
                    startCountDown(minutes * 60 * 1000);

                    questionList = exam.getQuestions();
                    renderQuestion(questionList.get(currentIndex));



                } else {
                    Log.e(TAG, "Không tìm thấy đề thi hoặc phản hồi lỗi");
                }
            }

            @Override
            public void onFailure(Call<Exam> call, Throwable t) {
                Log.e(TAG, "Lỗi gọi API", t);
            }
        });
    }

    private void saveCurrentQuestionState() {
        if (questionList == null || currentIndex >= questionList.size()) return;

        Question currentQuestion = questionList.get(currentIndex);
        LinearLayout scrollContainer = findViewById(R.id.exam_content_container);

        if (currentQuestion.getType().equals("single")) {
            // Xử lý câu hỏi đơn
            View questionView = scrollContainer.getChildAt(0);
            if (questionView != null) {
                RadioGroup radioGroup = questionView.findViewById(R.id.optionRadioGroup);
                if (radioGroup != null) {
                    int checkedId = radioGroup.getCheckedRadioButtonId();
                    if (checkedId != -1) {
                        RadioButton selected = questionView.findViewById(checkedId);
                        if (selected != null) {
                            String selectedAns = selected.getText().toString().substring(0, 1);
                            Answer a = new Answer();
                            a.setQuestionNumber(currentQuestion.getQuestionNumber());
                            a.setAnswer(selectedAns);
                            updateAnswerList(a);
                        }
                    }
                }
            }
        } else if (currentQuestion.getType().equals("group")) {
            // Xử lý nhóm câu hỏi
            View groupView = scrollContainer.getChildAt(0);
            if (groupView != null) {
                LinearLayout subContainer = groupView.findViewById(R.id.subQuestionsContainer);
                if (subContainer != null) {
                    for (int i = 0; i < subContainer.getChildCount(); i++) {
                        View subView = subContainer.getChildAt(i);
                        RadioGroup radioGroup = subView.findViewById(R.id.optionRadioGroup);
                        if (radioGroup != null) {
                            int checkedId = radioGroup.getCheckedRadioButtonId();
                            if (checkedId != -1) {
                                RadioButton selected = subView.findViewById(checkedId);
                                if (selected != null) {
                                    String selectedAns = selected.getText().toString().substring(0, 1);
                                    SubQuestion sq = currentQuestion.getSubQuestions().get(i);
                                    Answer a = new Answer();
                                    a.setQuestionNumber(sq.getQuestionNumber());
                                    a.setAnswer(selectedAns);
                                    updateAnswerList(a);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Method để khôi phục trạng thái RadioButton
    private void restoreRadioButtonState(RadioGroup radioGroup, int questionNumber) {
        // Tìm đáp án đã lưu
        String savedAnswer = null;
        for (Answer ans : answerList) {
            if (ans.getQuestionNumber() == questionNumber) {
                savedAnswer = ans.getAnswer();
                break;
            }
        }

        // Nếu có đáp án đã lưu, check RadioButton tương ứng
        if (savedAnswer != null) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                View child = radioGroup.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton rb = (RadioButton) child;
                    String optionLetter = rb.getText().toString().substring(0, 1);
                    if (optionLetter.equals(savedAnswer)) {
                        // Tạm thời disable listener để tránh trigger event
                        radioGroup.setOnCheckedChangeListener(null);
                        rb.setChecked(true);
                        // Restore listener
                        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                            updateRadioTick(group);
                            if (checkedId != -1) {
                                RadioButton selected = group.findViewById(checkedId);
                                String selectedAns = selected.getText().toString().substring(0, 1);
                                Answer a = new Answer();
                                a.setQuestionNumber(questionNumber);
                                a.setAnswer(selectedAns);
                                updateAnswerList(a);
                            }
                        });
                        updateRadioTick(radioGroup);
                        break;
                    }
                }
            }
        }
    }

    // Cập nhật button click listeners
    private void setupNavigationButtons() {
        preButton.setOnClickListener(v -> {
            if (currentIndex > 0) {
                saveCurrentQuestionState(); // Lưu trạng thái trước khi chuyển
                currentIndex--;
                renderQuestion(questionList.get(currentIndex));
            }
        });

        nextButton.setOnClickListener(v -> {
            if (currentIndex < questionList.size() - 1) {
                saveCurrentQuestionState(); // Lưu trạng thái trước khi chuyển
                currentIndex++;
                renderQuestion(questionList.get(currentIndex));
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }

    private void startCountDown(long millisInFuture) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                examTimeTextView.setText(timeFormatted);
            }

            @Override
            public void onFinish() {
                examTimeTextView.setText("00:00");
                // TODO: nộp bài tự động tại đây nếu cần
            }
        };

        countDownTimer.start();
    }

    private void renderQuestion(Question q) {

        LinearLayout scrollContainer = findViewById(R.id.exam_content_container);
        scrollContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (q.getType().equals("single")) {
            View view = inflater.inflate(R.layout.item_question_sub, scrollContainer, false);

            TextView questionTextView = view.findViewById(R.id.questionTextView);
            RadioButton optionA = view.findViewById(R.id.optionA);
            RadioButton optionB = view.findViewById(R.id.optionB);
            RadioButton optionC = view.findViewById(R.id.optionC);
            RadioButton optionD = view.findViewById(R.id.optionD);

            RadioGroup radioGroup = view.findViewById(R.id.optionRadioGroup);
            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                updateRadioTick(group);
                if (checkedId != -1) {
                    RadioButton selected = view.findViewById(checkedId);
                    String selectedAns = selected.getText().toString().substring(0, 1);

                    // Lưu lại vào danh sách
                    Answer a = new Answer();
                    a.setQuestionNumber(q.getQuestionNumber());
                    a.setAnswer(selectedAns);
                    updateAnswerList(a);
                }
            });



            questionTextView.setVisibility(View.VISIBLE);
            String numberPart = "Câu " + q.getQuestionNumber();
            String fullText = numberPart + ": " + q.getQuestionText();

            SpannableString spannable = new SpannableString(fullText);
            spannable.setSpan(
                    new ForegroundColorSpan(Color.RED),
                    0,
                    numberPart.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            questionTextView.setText(spannable);


            optionA.setText("A. " + q.getOptions().get("A"));
            optionB.setText("B. " + q.getOptions().get("B"));
            optionC.setText("C. " + q.getOptions().get("C"));
            optionD.setText("D. " + q.getOptions().get("D"));

            restoreRadioButtonState(radioGroup, q.getQuestionNumber());

            scrollContainer.addView(view);

        } else if (q.getType().equals("group")) {
            View groupView = inflater.inflate(R.layout.item_question_group, scrollContainer, false);

            TextView passageTextView = groupView.findViewById(R.id.passageTextView);
            LinearLayout subContainer = groupView.findViewById(R.id.subQuestionsContainer);

            passageTextView.setText(q.getPassage());
            passageTextView.setVisibility(View.VISIBLE);

            for (SubQuestion sq : q.getSubQuestions()) {
                View subView = inflater.inflate(R.layout.item_question_sub, subContainer, false);

                TextView questionTextView = subView.findViewById(R.id.questionTextView);
                RadioButton optionA = subView.findViewById(R.id.optionA);
                RadioButton optionB = subView.findViewById(R.id.optionB);
                RadioButton optionC = subView.findViewById(R.id.optionC);
                RadioButton optionD = subView.findViewById(R.id.optionD);

                RadioGroup radioGroup = subView.findViewById(R.id.optionRadioGroup);
                radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    updateRadioTick(group);
                    if (checkedId != -1) {
                        RadioButton selected = subView.findViewById(checkedId);
                        String selectedAns = selected.getText().toString().substring(0, 1);

                        Answer a = new Answer();
                        a.setQuestionNumber(sq.getQuestionNumber());
                        a.setAnswer(selectedAns);
                        updateAnswerList(a);
                    }
                });

                questionTextView.setVisibility(View.VISIBLE);
                String numberPart = "Câu " + sq.getQuestionNumber();
                String fullText = numberPart + ": " + sq.getQuestionText();

                SpannableString spannable = new SpannableString(fullText);
                spannable.setSpan(
                        new ForegroundColorSpan(Color.RED),
                        0,
                        numberPart.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                questionTextView.setText(spannable);


                optionA.setText("A. " + sq.getOptions().get("A"));
                optionB.setText("B. " + sq.getOptions().get("B"));
                optionC.setText("C. " + sq.getOptions().get("C"));
                optionD.setText("D. " + sq.getOptions().get("D"));

                subContainer.addView(subView);
                restoreRadioButtonState(radioGroup, sq.getQuestionNumber());
            }


            scrollContainer.addView(groupView);


        }
    }

    private void submitExam() {
        String examId = getIntent().getStringExtra("examId");

        long endTimeMillis = System.currentTimeMillis();
        long durationMillis = endTimeMillis - startTimeMillis;
        long durationSeconds = durationMillis / 1000;

        ExamSubmited submited = new ExamSubmited();
        submited.setExamId(examId);
        submited.setAnswers(answerList);
        submited.setTime(durationSeconds);

        Log.d(TAG, "Answers size: " + answerList.size());
        for (Answer a : answerList) {
            Log.d(TAG, "Answer questionNumber: " + a.getQuestionNumber() + ", answer: " + a.getAnswer());
        }



        ExamApiService apiService = InitializeRetrofit.getNodeApiInstance().create(ExamApiService.class);
        Call<ExamResult> call = apiService.submitExam(submited);
        call.enqueue(new Callback<ExamResult>() {
            @Override
            public void onResponse(Call<ExamResult> call, Response<ExamResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ExamResult result = response.body();

                    Intent intent = new Intent(ExamDetailActivity.this, ExamResultActivity.class);
                    intent.putExtra("score", result.getScore());
                    intent.putExtra("correct", result.getCorrect());
                    intent.putExtra("incorrect", result.getIncorrect());
                    intent.putExtra("total", result.getTotal());
                    intent.putExtra("time", result.getTime());
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorMsg = response.errorBody() != null ? response.errorBody().string() : "Không rõ lỗi";
                        Log.e(TAG, "Nộp bài thất bại: " + errorMsg);
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi khi đọc errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ExamResult> call, Throwable t) {
                Log.e(TAG, "Lỗi kết nối khi nộp bài", t);
            }
        });
    }

    private void showConfirmSubmitDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận nộp bài")
                .setMessage("Bạn có chắc chắn muốn nộp bài không?")
                .setPositiveButton("Nộp", (dialog, which) -> {
                    submitExam();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showConfirmExitDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận thoát bài làm")
                .setMessage("Bạn có chắc chắn muốn thoát không?")
                .setPositiveButton("Thoát", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showQuestionListDialog() {
        // Inflate layout cho bottom sheet dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_question_list, null);
        GridLayout questionGrid = dialogView.findViewById(R.id.question_grid);

        // Tạo dialog trước để dùng trong lambda
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(dialogView);

        for (int i = 0; i < 40; i++) {
            final int index = i;

            TextView textView = new TextView(this);

            // Lấy đáp án nếu có
            int questionNumber = i + 1;
            String answer = null;

            for (Answer ans : answerList) {
                if (ans.getQuestionNumber() == questionNumber) {
                    answer = ans.getAnswer();
                    break;
                }
            }

            String displayText = questionNumber + (answer != null ? answer : "");

            // Thiết lập thuộc tính cho TextView
            textView.setText(displayText);
            textView.setTextSize(18); // Set text size
            textView.setGravity(Gravity.CENTER); // Center text

            if (answer != null) {
                // Có đáp án - tô màu xanh và in đậm
                textView.setTextColor(Color.WHITE);
                textView.setTypeface(null, Typeface.BOLD); // In đậm
                textView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_dark)); // Nền xanh
            } else {
                // Chưa có đáp án - màu mặc định
                textView.setTextColor(Color.BLACK);
                textView.setTypeface(null, Typeface.NORMAL); // Chữ thường
                textView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray)); // Nền xám
            }


            // Thiết lập padding
            int paddingPx = 16; // hoặc dùng giá trị cố định như 16
            textView.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);

            // Tạo LayoutParams cho GridLayout
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            // Thiết lập kích thước
            params.width = 0; // sử dụng weight
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;

            // Thiết lập weight để các item có kích thước đều nhau
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED);

            // Thiết lập margin
            int marginPx = 10;// hoặc 8
            params.setMargins(marginPx, marginPx, marginPx, marginPx);

            textView.setLayoutParams(params);


            questionGrid.addView(textView);
        }

        dialog.show();
    }

    private void updateRadioTick(RadioGroup group) {
        Drawable tick = ContextCompat.getDrawable(this, R.drawable.radio_button);
        if (tick != null) {
            int sizeInPx = (int) getResources().getDimension(R.dimen.tick_icon_size); // ví dụ 24dp
            tick.setBounds(0, 0, sizeInPx, sizeInPx); // Set kích thước mong muốn
        }

        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child instanceof RadioButton) {
                RadioButton rb = (RadioButton) child;
                if (rb.isChecked()) {
                    rb.setCompoundDrawables(null, null, tick, null);
                } else {
                    rb.setCompoundDrawables(null, null, null, null);
                }
            }
        }
    }
    private void updateAnswerList(Answer newAnswer) {
        for (int i = 0; i < answerList.size(); i++) {
            if (answerList.get(i).getQuestionNumber()==(newAnswer.getQuestionNumber())) {
                answerList.set(i, newAnswer);
                return;
            }
        }
        answerList.add(newAnswer);
    }


}
