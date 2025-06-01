package nlu.fit.studyappr.activity.vocabulary; // Or your actual package

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

// Import an image loading library like Glide or Picasso
// import com.bumptech.glide.Glide;

import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.model.StartLearningResponse;
import nlu.fit.studyappr.model.VocabularyWordViewModel;

public class VocabularyLearnRewriteActivity extends AppCompatActivity {

    private static final String TAG = "RewriteActivity";

    // UI Elements
    private ImageButton buttonBack;
    private TextView textToolbarTitle; // Assuming you might want to set this dynamically
    private ProgressBar progressBar;
    private TextView textProgress;
    private TextView textMeaningPrompt; // "Viết lại từ có nghĩa:"
    private TextView textPronunciationHint;
    private ImageView imageHint;
    private EditText editTextAnswer;
    private Button buttonCheck;
    private TextView textFeedback;
    private Button buttonEndEarly;

    private List<VocabularyWordViewModel> wordList;
    private int currentWordIndex = 0;
    private Long currentSessionId;
    private VocabularyWordViewModel currentWord;
    private boolean answereChecked = false; // To control flow after checking

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_learn_rewrite);

        initializeViews();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("startLearning")) {
            StartLearningResponse response = (StartLearningResponse) intent.getSerializableExtra("startLearning");
            if (response != null && response.getWords() != null && !response.getWords().isEmpty()) {
                wordList = response.getWords();
                currentSessionId = response.getSessionId();
                // textToolbarTitle.setText("Viết lại từ: " + response.getTopicName()); // If available
                displayCurrentWord();
            } else {
                handleError("Không có dữ liệu từ vựng.");
            }
        } else {
            handleError("Lỗi tải dữ liệu học.");
        }

        setupListeners();
    }

    private void initializeViews() {
        buttonBack = findViewById(R.id.button_back);
        // textToolbarTitle = findViewById(R.id.your_toolbar_title_id); // Give the title TextView an ID
        progressBar = findViewById(R.id.progress_bar);
        textProgress = findViewById(R.id.text_progress);
        textMeaningPrompt = findViewById(R.id.text_meaning); // This TextView shows the meaning
        textPronunciationHint = findViewById(R.id.text_pronunciation);
        imageHint = findViewById(R.id.image_hint);
        editTextAnswer = findViewById(R.id.edit_text_answer);
        buttonCheck = findViewById(R.id.button_check);
        textFeedback = findViewById(R.id.text_feedback);
        buttonEndEarly = findViewById(R.id.button_end_early);
    }

    private void setupListeners() {
        buttonBack.setOnClickListener(v -> {
            // TODO: Confirmation dialog, save progress
            finish();
        });

        buttonCheck.setOnClickListener(v -> {
            if (answereChecked) { // If already checked, this button acts as "Next"
                goToNextWord();
            } else {
                checkAnswer();
            }
        });

        buttonEndEarly.setOnClickListener(v -> {
            // TODO: Confirmation, save progress, go to results
            Toast.makeText(this, "Kết thúc sớm (chưa triển khai)", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void displayCurrentWord() {
        answereChecked = false; // Reset for the new word
        if (wordList == null || wordList.isEmpty() || currentWordIndex >= wordList.size()) {
            Toast.makeText(this, "Hoàn thành tất cả các từ!", Toast.LENGTH_LONG).show();
            // TODO: Navigate to results
            finish();
            return;
        }

        currentWord = wordList.get(currentWordIndex);

        textMeaningPrompt.setText("Viết lại từ có nghĩa: " + currentWord.getMeaning());
        // textPronunciationHint.setText(currentWord.getPronunciation()); // If model has pronunciation

        // Load hint image
        if (!TextUtils.isEmpty(currentWord.getImageUrl())) {
            // Glide.with(this).load(currentWord.getImageUrl()).placeholder(R.drawable.ic_image_placeholder).into(imageHint);
            imageHint.setImageResource(R.drawable.english); // Placeholder, replace with actual logic
            imageHint.setVisibility(View.VISIBLE);
        } else {
            imageHint.setVisibility(View.GONE);
        }

        editTextAnswer.setText(""); // Clear previous answer
        editTextAnswer.setEnabled(true);
        textFeedback.setVisibility(View.GONE); // Hide feedback
        buttonCheck.setText("Kiểm tra");

        // Update progress
        progressBar.setMax(wordList.size());
        progressBar.setProgress(currentWordIndex + 1);
        textProgress.setText("Từ " + (currentWordIndex + 1) + " / " + wordList.size());
    }

    private void checkAnswer() {
        if (currentWord == null) return;

        String userAnswer = editTextAnswer.getText().toString().trim();
        String correctAnswer = currentWord.getWord().trim(); // The actual word to be typed

        if (TextUtils.isEmpty(userAnswer)) {
            Toast.makeText(this, "Vui lòng nhập câu trả lời.", Toast.LENGTH_SHORT).show();
            return;
        }

        answereChecked = true;
        editTextAnswer.setEnabled(false); // Disable editing after checking

        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            textFeedback.setText("Chính xác! Từ đúng là: " + correctAnswer);
            textFeedback.setTextColor(ContextCompat.getColor(this, R.color.colorSuccess)); // Define R.color.colorSuccess (e.g., green)
            // TODO: Record correct answer (API call or local tracking)
            Log.d(TAG, "Correct: " + correctAnswer + " Session ID: " + currentSessionId);
        } else {
            textFeedback.setText("Chưa đúng. Từ đúng là: " + correctAnswer + "\nBạn đã viết: " + userAnswer);
            textFeedback.setTextColor(ContextCompat.getColor(this, R.color.colorError)); // Define R.color.colorError (e.g., red)
            // TODO: Record incorrect answer
            Log.d(TAG, "Incorrect. Correct: " + correctAnswer + ", User: " + userAnswer + " Session ID: " + currentSessionId);
        }
        textFeedback.setVisibility(View.VISIBLE);
        buttonCheck.setText("Tiếp theo"); // Change button text to "Next"
    }

    private void goToNextWord() {
        currentWordIndex++;
        displayCurrentWord();
    }

    private void handleError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e(TAG, message);
        buttonCheck.setEnabled(false);
        // finish(); // Optionally finish
    }

    // Remember to define these colors in your colors.xml:
    // <color name="colorSuccess">#27ae60</color>
    // <color name="colorError">#e74c3c</color>
}