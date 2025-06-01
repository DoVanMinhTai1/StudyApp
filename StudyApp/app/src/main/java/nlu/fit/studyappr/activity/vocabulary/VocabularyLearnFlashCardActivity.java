package nlu.fit.studyappr.activity.vocabulary; // Or your actual package

import android.content.Intent;
import android.media.MediaPlayer; // For audio playback
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// Import an image loading library like Glide or Picasso
// import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nlu.fit.studyappr.R; // Your project's R class
import nlu.fit.studyappr.api.vocabulary.VocabularyApiService;
import nlu.fit.studyappr.model.StartLearningResponse;
import nlu.fit.studyappr.model.StartLearningReviewResponse;
import nlu.fit.studyappr.model.VocabularyEndResponse;
import nlu.fit.studyappr.model.VocabularyReviewEndRequest;
import nlu.fit.studyappr.model.VocabularyWordViewModel;
import nlu.fit.studyappr.model.WordAnswer;
import nlu.fit.studyappr.model.WordInteraction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabularyLearnFlashCardActivity extends AppCompatActivity {

    private static final String TAG = "FlashCardActivity";

    // UI Elements
    private ImageButton buttonBack;
    private TextView textToolbarTitle; // Assuming you want to set the title "Flashcard: Chủ đề Du lịch" dynamically
    private ProgressBar progressBar;
    private TextView textProgress;
    private TextView textWord;
    private TextView textMeaning;
    private TextView textPronunciation;
    private Button buttonPronounce;
    private ImageView imageIllustration;
    private TextView textExample;
    private Button buttonShowMeaning;
    private Button buttonUnknown;
    private Button buttonKnown;
    private Button buttonEndEarly;

    private List<VocabularyWordViewModel> wordList;
    private int currentWordIndex = 0;
    private Long currentSessionId;
    private boolean isMeaningShown = false;

    private MediaPlayer mediaPlayer;

    private List<WordAnswer> sessionWordInteractions;
    private VocabularyWordViewModel currentDisplayedWord; // To hold the current word object

    private long sessionStartTimeMillis;
    private Long currentTopicId;

    private HashMap<String, String> sessionUserAnswers = new HashMap<>();
    // To track if the answer was correct for the current session
    private HashMap<String, Boolean> sessionAnswerCorrectness = new HashMap<>();
    // To track all words encountered in the quiz part of the session
    private List<VocabularyWordViewModel> quizWordsEncountered = new ArrayList<>();

    private VocabularyApiService vocabularyApiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_learn_flashcard);

        initializeViews();

        // Get data from Intent
        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra("startLearning")) {
                StartLearningResponse response = (StartLearningResponse) intent.getSerializableExtra("startLearning");
                if (response != null && response.getWords() != null && !response.getWords().isEmpty()) {
                    wordList = response.getWords();
                    currentSessionId = response.getSessionId();
                    displayCurrentWord();
                } else {
                    handleError("Không có dữ liệu từ vựng từ startLearning.");
                }

            } else if (intent.hasExtra("startReview")) {
                StartLearningReviewResponse reviewResponse = (StartLearningReviewResponse) intent.getSerializableExtra("startReview");
                if (reviewResponse != null && reviewResponse.getWords() != null && !reviewResponse.getWords().isEmpty()) {
                    wordList = reviewResponse.getWords();
                    Long reviewScheduleId = reviewResponse.getReviewScheduleId(); // hoặc reviewResponse.getActiveSession().getId()
                    displayCurrentWord();
                } else {
                    handleError("Không có dữ liệu từ vựng từ startReview.");
                }

            } else {
                handleError("Không có dữ liệu truyền vào.");
            }
        }


        setupListeners();
    }

    private void initializeViews() {
        buttonBack = findViewById(R.id.button_back);
        // Assuming the TextView for the title is the one after the back button
        // If it's a Toolbar title, you'd get the Toolbar and set its title.
        // For this example, let's assume it's the TextView in your header LinearLayout
        View headerLayout = findViewById(android.R.id.content).getRootView(); // A way to get to the header if not using Toolbar
        // This is a bit hacky, ideally use a proper Toolbar or give the title TextView an ID
        // textToolbarTitle = headerLayout.findViewById(R.id.your_header_title_textview_id);


        progressBar = findViewById(R.id.progress_bar);
        textProgress = findViewById(R.id.text_progress);
        textWord = findViewById(R.id.text_word);
        textMeaning = findViewById(R.id.text_meaning);
        textPronunciation = findViewById(R.id.text_pronunciation);
        buttonPronounce = findViewById(R.id.button_pronounce);
        imageIllustration = findViewById(R.id.image_illustration);
        textExample = findViewById(R.id.text_example);
        buttonShowMeaning = findViewById(R.id.button_show_meaning);
        buttonUnknown = findViewById(R.id.button_unknown);
        buttonKnown = findViewById(R.id.button_known);
        buttonEndEarly = findViewById(R.id.button_end_early);
    }

    private void setupListeners() {
        buttonBack.setOnClickListener(v -> {
            // TODO: Implement back logic (e.g., show confirmation dialog, save progress)
            finish();
        });

        buttonShowMeaning.setOnClickListener(v -> {
            isMeaningShown = !isMeaningShown; // Toggle
            if (isMeaningShown) {
                textMeaning.setVisibility(View.VISIBLE);
                buttonShowMeaning.setText("Ẩn nghĩa / Đáp án");
            } else {
                textMeaning.setVisibility(View.INVISIBLE);
                buttonShowMeaning.setText("Hiện nghĩa / Đáp án");
            }
        });

        buttonPronounce.setOnClickListener(v -> {
            if (wordList != null && currentWordIndex < wordList.size()) {
                playAudio(wordList.get(currentWordIndex).getAudioUrl());
            }
        });

        buttonKnown.setOnClickListener(v -> {
            // TODO: Record that user knows this word (API call or local tracking)
            goToNextWord();
        });

        buttonUnknown.setOnClickListener(v -> {
            // TODO: Record that user doesn't know this word (API call or local tracking)
            Log.d(TAG, "Word unknown: " + wordList.get(currentWordIndex).getWord() + " Session ID: " + currentSessionId);

            if (!isMeaningShown) {
                isMeaningShown = true;
                textMeaning.setVisibility(View.VISIBLE);
                buttonShowMeaning.setText("Ẩn nghĩa / Đáp án");
            }
            // You might want to keep the card or move to next after a delay
            goToNextWord(); // Or implement different logic for unknown
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
        String currentUserId = "2";
        VocabularyReviewEndRequest request = new VocabularyReviewEndRequest(
                currentUserId,
                currentTopicId,
                "VOCABULARY",
                timeSpentSeconds,
                currentSessionId);


        List<WordAnswer> userAnswers = new ArrayList<>();
        int correctAnswerCount = 0;

//        request.setAnswers(sessionWordInteractions); // Pass the collected list of WordAnswer

//        int knownCount = 0;
//        for (WordAnswer answer : sessionWordInteractions) {
//            if (answer.getStatus().equals(Boolean.TRUE)) { // Check the boolean status
//                knownCount++;
//            }
//        }
//        request.setWordsMarkedKnown(knownCount);
//        request.setTotalWordsInSession(wordList != null ? wordList.size() : 0);

//
//        vocabularyApiService.endLearningSessionEarly(request).enqueue(new Callback<VocabularyEndResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<VocabularyEndResponse> call, @NonNull Response<VocabularyEndResponse> response) {
//                // progressBar.setVisibility(View.GONE);
//                if (response.isSuccessful()) {
//                    VocabularyEndResponse summary = response.body();
//                    Toast.makeText(VocabularyLearnFlashCardActivity.this, "Tiến trình đã được lưu.", Toast.LENGTH_SHORT).show();
//
//                    // Navigate to the Summary Screen
//                    Intent intent = new Intent(VocabularyLearnFlashCardActivity.this, VocabularyLearnSummaryActivity.class); // Your new summary activity
//                    intent.putExtra("SESSION_SUMMARY", summary); // Pass the summary object
//                    startActivity(intent);
//                    finish(); // Finish the current learning activity
//                } else {
//                    // Handle API error
//                    Toast.makeText(VocabularyLearnFlashCardActivity.this, "Lỗi khi lưu tiến trình: " + response.code(), Toast.LENGTH_LONG).show();
//                    // You might want to parse response.errorBody()
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<VocabularyEndResponse> call, @NonNull Throwable t) {
//                // progressBar.setVisibility(View.GONE);
//                Toast.makeText(VocabularyLearnFlashCardActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                t.printStackTrace();
//            }
//        });
//
//
    }


    private void displayCurrentWord() {
        if (wordList == null || wordList.isEmpty() || currentWordIndex >= wordList.size()) {
            // All words finished or error
            Toast.makeText(this, "Hoàn thành tất cả các từ!", Toast.LENGTH_LONG).show();
            // TODO: Navigate to a results screen or back
            finish();
            return;
        }

        VocabularyWordViewModel currentWord = wordList.get(currentWordIndex);

        textWord.setText(currentWord.getWord());
        textMeaning.setText(currentWord.getMeaning());
        // textPronunciation.setText(currentWord.getPronunciation()); // If you add pronunciation to model
        // textExample.setText("Ví dụ: " + currentWord.getExampleSentence()); // If you add example to model

        // Reset meaning visibility for new card
        isMeaningShown = false;
        textMeaning.setVisibility(View.INVISIBLE);
        buttonShowMeaning.setText("Hiện nghĩa / Đáp án");

        // Update progress
        progressBar.setMax(wordList.size());
        progressBar.setProgress(currentWordIndex + 1);
        textProgress.setText("Từ " + (currentWordIndex + 1) + " / " + wordList.size());

        // Load image (using a library like Glide is recommended)
        if (!TextUtils.isEmpty(currentWord.getImageUrl())) {
            // Example with Glide:
            // Glide.with(this).load(currentWord.getImageUrl()).placeholder(R.drawable.ic_image_placeholder).into(imageIllustration);
            imageIllustration.setImageResource(R.drawable.ic_journey); // Placeholder for now
            imageIllustration.setVisibility(View.VISIBLE);
        } else {
            imageIllustration.setVisibility(View.GONE); // Or set a default placeholder
        }

        // Enable/disable pronounce button
        buttonPronounce.setEnabled(!TextUtils.isEmpty(currentWord.getAudioUrl()));
    }

    private void goToNextWord() {
        currentWordIndex++;
        displayCurrentWord();
    }

    private void playAudio(String audioUrl) {
        if (TextUtils.isEmpty(audioUrl)) {
            Toast.makeText(this, "Không có file âm thanh.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl); // Needs to be a valid URL or local path
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer Error: what=" + what + ", extra=" + extra);
                Toast.makeText(VocabularyLearnFlashCardActivity.this, "Lỗi phát âm thanh.", Toast.LENGTH_SHORT).show();
                mp.release();
                mediaPlayer = null;
                return true;
            });
            mediaPlayer.prepareAsync(); // Use prepareAsync for network URLs
        } catch (IOException e) {
            Log.e(TAG, "Error setting data source for MediaPlayer", e);
            Toast.makeText(this, "Lỗi chuẩn bị âm thanh.", Toast.LENGTH_SHORT).show();
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }


    private void handleError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e(TAG, message);
        // Disable buttons or finish activity if data is crucial
        buttonKnown.setEnabled(false);
        buttonUnknown.setEnabled(false);
        buttonShowMeaning.setEnabled(false);
        // finish(); // Optionally finish if unusable
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}