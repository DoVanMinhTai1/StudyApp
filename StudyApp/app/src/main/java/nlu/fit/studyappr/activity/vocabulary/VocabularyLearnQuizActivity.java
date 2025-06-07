package nlu.fit.studyappr.activity.vocabulary;

import android.content.Intent;
import android.graphics.Color; // For feedback text color
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import java.util.ArrayList;
import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.api.vocabulary.VocabularyApiService;
import nlu.fit.studyappr.model.vocabulary.StartLearningResponse;
import nlu.fit.studyappr.model.vocabulary.VocabularyEndRequest;
import nlu.fit.studyappr.model.vocabulary.VocabularyEndResponse;
import nlu.fit.studyappr.model.vocabulary.VocabularyWordViewModel;
import nlu.fit.studyappr.model.vocabulary.WordAnswer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;

public class VocabularyLearnQuizActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "VocabQuizActivity";

    // UI Elements
    private ImageButton buttonBack;
    private TextView textToolbarTitle; // Assuming the title needs to be set
    private ProgressBar progressBar;
    private TextView textProgress;
    private TextView textQuestion;
    private Button option1Button, option2Button, option3Button, option4Button;
    private List<Button> optionButtons; // To easily manage them
    private TextView textFeedback;
    private Button buttonNext;
    private Button buttonEndEarly;

    private List<VocabularyWordViewModel> wordList;
    private int currentWordIndex = 0;
    private Long currentSessionId;
    private VocabularyWordViewModel currentQuizWord;
    private boolean answerChecked = false;
    private String selectedOptionTag = null; // To store which option (A, B, C, D) was clicked

    private long sessionStartTimeMillis;
    private VocabularyApiService sessionApiService;
    private String currentUserId; // Get this from SharedPreferences or auth manager
    private Long currentTopicId; // ID of the topic being learned
    private String currentTopicType; // "VOCABULARY" or "GRAMMAR"

    private HashMap<String, String> sessionUserAnswers = new HashMap<>();
    // To track if the answer was correct for the current session
    private HashMap<String, Boolean> sessionAnswerCorrectness = new HashMap<>();
    // To track all words encountered in the quiz part of the session
    private List<VocabularyWordViewModel> quizWordsEncountered = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_learn_quiz);

        initializeViews();

        sessionApiService = InitializeRetrofit.getInstance().create(VocabularyApiService.class);


        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("startLearning")) {
            StartLearningResponse response = (StartLearningResponse) intent.getSerializableExtra("startLearning");
            if (response != null && response.getWords() != null && !response.getWords().isEmpty()) {
                wordList = response.getWords();
                currentSessionId = response.getSessionId();
                currentTopicId = response.getTopicId();     // <<<< GETTING topicId HERE
                currentTopicType = response.getTitle();
                // textToolbarTitle.setText("Trắc nghiệm: " + response.getTopicName()); // If available
                displayCurrentQuestion();
            } else {
                handleError("Không có dữ liệu câu hỏi.");
            }
        } else {
            handleError("Lỗi tải dữ liệu học.");
        }

        setupListeners();
    }

    private void initializeViews() {
        buttonBack = findViewById(R.id.button_back);
        // textToolbarTitle = findViewById(R.id.your_toolbar_title_id); // If you give the title TextView an ID
        progressBar = findViewById(R.id.progress_bar);
        textProgress = findViewById(R.id.text_progress);
        textQuestion = findViewById(R.id.text_question);

        option1Button = findViewById(R.id.option_1);
        option2Button = findViewById(R.id.option_2);
        option3Button = findViewById(R.id.option_3);
        option4Button = findViewById(R.id.option_4);

        optionButtons = new ArrayList<>();
        optionButtons.add(option1Button);
        optionButtons.add(option2Button);
        optionButtons.add(option3Button);
        optionButtons.add(option4Button);

        textFeedback = findViewById(R.id.text_feedback);
        buttonNext = findViewById(R.id.button_next);
        buttonEndEarly = findViewById(R.id.button_end_early);

        sessionStartTimeMillis = System.currentTimeMillis();
    }

    private void setupListeners() {
        buttonBack.setOnClickListener(v -> {
            // TODO: Confirmation, save progress
            finish();
        });

        option1Button.setOnClickListener(this);
        option2Button.setOnClickListener(this);
        option3Button.setOnClickListener(this);
        option4Button.setOnClickListener(this);

        // Assign tags to easily identify which option was clicked
        option1Button.setTag("A");
        option2Button.setTag("B");
        option3Button.setTag("C");
        option4Button.setTag("D");


        buttonNext.setOnClickListener(v -> {
            if (answerChecked) { // Only go to next if an answer was checked
                goToNextQuestion();
            } else {
                Toast.makeText(this, "Vui lòng chọn một đáp án.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonEndEarly.setOnClickListener(v -> {
            showEndEarlyConfirmationDialog();

        });
    }

    private void showEndEarlyConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Kết thúc sớm?")
                .setMessage("Bạn có chắc muốn kết thúc buổi học này không? Tiến trình sẽ được lưu.")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    handleEndEarly();
                })
                .setNegativeButton("Ở lại", null)
                .show();
    }

    private void handleEndEarly() {
        int timeSpentSeconds = (int) ((System.currentTimeMillis() - sessionStartTimeMillis) / 1000);
        currentUserId =  "2";
        VocabularyEndRequest request = new VocabularyEndRequest(
                currentUserId,
                currentTopicId,
                currentTopicType,
                timeSpentSeconds,
                currentSessionId);

        List<WordAnswer> userAnswers = new ArrayList<>();
        int correctAnswerCount = 0;

        for(VocabularyWordViewModel v : wordList) {
            String wordId = String.valueOf(v.getId());
            boolean status = sessionAnswerCorrectness.get(wordId);
            String userAnswer = sessionUserAnswers.get(wordId);
            userAnswers.add(new WordAnswer(v.getId(),status,userAnswer));
        }
        request.setWordsLearnedInSession(userAnswers);


        sessionApiService.endLearningSessionEarly(request).enqueue(new Callback<VocabularyEndResponse>() {
            @Override
            public void onResponse(@NonNull Call<VocabularyEndResponse> call, @NonNull Response<VocabularyEndResponse> response) {
                // progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() ) {
                    VocabularyEndResponse summary = response.body();
                    Toast.makeText(VocabularyLearnQuizActivity.this, "Tiến trình đã được lưu.", Toast.LENGTH_SHORT).show();

                    // Navigate to the Summary Screen
                    Intent intent = new Intent(VocabularyLearnQuizActivity.this, VocabularyLearnSummaryActivity.class); // Your new summary activity
                    intent.putExtra("SESSION_SUMMARY", summary); // Pass the summary object
                    startActivity(intent);
                    finish(); // Finish the current learning activity
                } else {
                    // Handle API error
                    Toast.makeText(VocabularyLearnQuizActivity.this, "Lỗi khi lưu tiến trình: " + response.code(), Toast.LENGTH_LONG).show();
                    // You might want to parse response.errorBody()
                }
            }

            @Override
            public void onFailure(@NonNull Call<VocabularyEndResponse> call, @NonNull Throwable t) {
                // progressBar.setVisibility(View.GONE);
                Toast.makeText(VocabularyLearnQuizActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (answerChecked) return; // Don't allow changing answer after checking

        // Get the tag of the clicked button (A, B, C, or D)
        selectedOptionTag = (String) v.getTag();
        if (selectedOptionTag == null) return;

        checkAnswer(selectedOptionTag);
    }


    private void displayCurrentQuestion() {
        answerChecked = false;
        selectedOptionTag = null;
        enableOptionButtons(true);
        resetOptionButtonStyles();
        textFeedback.setVisibility(View.GONE);
        buttonNext.setText("Câu tiếp theo"); // Or "Hoàn thành" if last question

        if (wordList == null || wordList.isEmpty() || currentWordIndex >= wordList.size()) {
            Toast.makeText(this, "Hoàn thành tất cả câu hỏi!", Toast.LENGTH_LONG).show();
            // TODO: Navigate to results screen, pass session ID and user answers
            // For now, just finish
            new Handler(Looper.getMainLooper()).postDelayed(this::finish, 1500);
            return;
        }

        currentQuizWord = wordList.get(currentWordIndex);

        if (!quizWordsEncountered.contains(currentQuizWord)) {
            quizWordsEncountered.add(currentQuizWord);
        }


        textQuestion.setText("Nghĩa của từ " + currentQuizWord.getWord() + "là gì");

        // Shuffle options for display if they are not already shuffled, or set them directly
        // For this example, assume options A, B, C, D are already prepared in the model
        option1Button.setText(currentQuizWord.getOptionA());
        option2Button.setText(currentQuizWord.getOptionB());
        option3Button.setText(currentQuizWord.getOptionC());
        option4Button.setText(currentQuizWord.getOptionD());


        // Update progress
        progressBar.setMax(wordList.size());
        progressBar.setProgress(currentWordIndex + 1);
        textProgress.setText("Từ " + (currentWordIndex + 1) + " / " + wordList.size());

        if (currentWordIndex == wordList.size() - 1) {
            buttonNext.setText("Hoàn thành");
//            buttonNext.setOnClickListener();
        }
    }

    private void checkAnswer(String selectedOptTag) {
        if (currentQuizWord == null) return;
        answerChecked = true;
        enableOptionButtons(false); // Disable options after an answer is selected

        String correctAnswerTag = currentQuizWord.getCorrectOption();
        boolean isCorrect = selectedOptTag.equals(correctAnswerTag);

        sessionUserAnswers.put(String.valueOf(currentQuizWord.getId()), selectedOptTag);
        sessionAnswerCorrectness.put(String.valueOf(currentQuizWord.getId()), isCorrect);


        for (Button btn : optionButtons) {
            String btnTag = (String) btn.getTag();
            if (btnTag.equals(correctAnswerTag)) {
                // Highlight correct answer
                btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSuccess)); // Green
                btn.setTextColor(Color.WHITE);
            }
            if (btnTag.equals(selectedOptTag) && !selectedOptTag.equals(correctAnswerTag)) {
                // User selected this, and it's wrong
                btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorError)); // Red
                btn.setTextColor(Color.WHITE);
            } else if (btnTag.equals(selectedOptTag) && selectedOptTag.equals(correctAnswerTag)) {
                // User selected this, and it's correct (already handled by correct answer highlighting)
                // but ensure it's styled as correct
                btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSuccess));
                btn.setTextColor(Color.WHITE);
            }
        }


        if (selectedOptTag.equals(correctAnswerTag)) {
            textFeedback.setText("Chính xác!");
            textFeedback.setTextColor(ContextCompat.getColor(this, R.color.colorSuccess));
        } else {
            textFeedback.setText("Chưa đúng. Đáp án là: " + getOptionTextByTag(correctAnswerTag));
            textFeedback.setTextColor(ContextCompat.getColor(this, R.color.colorError));
        }
        textFeedback.setVisibility(View.VISIBLE);

        // Auto-move to next question after a delay or wait for "Next" button
        // For this example, we rely on the "Next" button.
    }

    private String getOptionTextByTag(String tag) {
        if (currentQuizWord == null) return "";
        switch (tag) {
            case "A":
                return currentQuizWord.getOptionA();
            case "B":
                return currentQuizWord.getOptionB();
            case "C":
                return currentQuizWord.getOptionC();
            case "D":
                return currentQuizWord.getOptionD();
            default:
                return "";
        }
    }


    private void enableOptionButtons(boolean enabled) {
        for (Button btn : optionButtons) {
            btn.setEnabled(enabled);
        }
    }

    private void resetOptionButtonStyles() {
        for (Button btn : optionButtons) {
            // Reset to default style (you might need to define a default button background drawable)
            // For MaterialButtons, resetting backgroundTint might be enough
            btn.setBackgroundColor(ContextCompat.getColor(this, R.color.activityItemBackground)); // Or your default button color
            btn.setTextColor(ContextCompat.getColor(this, R.color.activityItemTitleText)); // Or your default button text color
        }
    }


    private void goToNextQuestion() {
        currentWordIndex++;
        displayCurrentQuestion();
    }

    private void handleError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e(TAG, message);
        buttonNext.setEnabled(false);
        enableOptionButtons(false);
        // finish(); // Optionally finish
    }

}