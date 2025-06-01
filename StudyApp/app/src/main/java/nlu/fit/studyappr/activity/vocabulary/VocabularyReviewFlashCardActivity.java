package nlu.fit.studyappr.activity.vocabulary; // Or your actual package

import android.content.Intent;
import android.media.MediaPlayer;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.api.vocabulary.VocabularyApiService;
import nlu.fit.studyappr.model.StartLearningReviewResponse;
import nlu.fit.studyappr.model.VocabularyReviewEndRequest;
import nlu.fit.studyappr.model.VocabularyReviewEndResponse;
import nlu.fit.studyappr.model.VocabularyWordViewModel;
import nlu.fit.studyappr.model.WordAnswer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabularyReviewFlashCardActivity extends AppCompatActivity {

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
    Long reviewScheduleId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_learn_flashcard);

        initializeViews();

        // Get data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("startLearning")) {
                StartLearningReviewResponse reviewResponse = (StartLearningReviewResponse) intent.getSerializableExtra("startLearning");
//                StartLearningResponse reviewResponse = (StartLearningResponse) intent.getSerializableExtra("startLearning");
                if (reviewResponse != null && reviewResponse.getWords() != null && !reviewResponse.getWords().isEmpty()) {
                    wordList = reviewResponse.getWords();
                    reviewScheduleId = reviewResponse.getReviewScheduleId(); // hoặc reviewResponse.getActiveSession().getId()
                    displayCurrentWord();
                } else {
                    handleError("Không có dữ liệu từ vựng từ startReview.");
                }

            } else {
                handleError("Không có dữ liệu truyền vào.");
            }
        }


        setupListeners(reviewScheduleId);
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

    private void setupListeners(Long reviewScheduleId) {
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

        sessionWordInteractions = new ArrayList<>();

        buttonKnown.setOnClickListener(v -> {
            if (currentDisplayedWord != null && currentDisplayedWord.getId() != null) { // Check ID for null
                addOrUpdateInteraction(currentDisplayedWord.getId(), true);
                goToNextWord();
            } else {
                Log.e(TAG, "Cannot record 'Known': currentDisplayedWord or its ID is null.");
                // Optionally show a generic error to the user
            }
        });

        buttonUnknown.setOnClickListener(v -> {
            // TODO: Record that user doesn't know this word (API call or local tracking)
            if (currentDisplayedWord != null && currentDisplayedWord.getId() != null) { // Check ID for null
                Log.d(TAG, "Word unknown: " + currentDisplayedWord.getWord() + " Session ID: " + currentSessionId);
                addOrUpdateInteraction(currentDisplayedWord.getId(), false);
                // ...
                goToNextWord();
            } else {
                Log.e(TAG, "Cannot record 'Unknown': currentDisplayedWord or its ID is null.");
            }
            if (!isMeaningShown) {
                isMeaningShown = true;
                textMeaning.setVisibility(View.VISIBLE);
                buttonShowMeaning.setText("Ẩn nghĩa / Đáp án");
            }
            // You might want to keep the card or move to next after a delay
        });

        buttonEndEarly.setOnClickListener(v -> {
            showEndEarlyConfirmationDialog(reviewScheduleId);

        });

    }

    private void addOrUpdateInteraction(Long wordId, Boolean status) {
        if (sessionWordInteractions == null) {
            sessionWordInteractions = new ArrayList<>();
        }
        sessionWordInteractions.removeIf(interaction -> interaction.getWordId().equals(wordId));
        sessionWordInteractions.add(new WordAnswer(wordId, status, "-1"));
    }

    private void showEndEarlyConfirmationDialog(Long reviewScheduleId) {
        new AlertDialog.Builder(this)
                .setTitle("Kết thúc sớm?")
                .setMessage("Bạn có chắc muốn kết thúc buổi học này không? Tiến trình sẽ được lưu.")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    handleEndEarly(reviewScheduleId);
                })
                .setNegativeButton("Ở lại", null)
                .show();
    }

    private void handleEndEarly(Long reviewScheduleId) {
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

        request.setReviewScheduleId(reviewScheduleId);
        request.setAnswers(sessionWordInteractions); // Pass the collected list of WordAnswer

        int knownCount = 0;
        for (WordAnswer answer : sessionWordInteractions) {
            if (answer.getStatus().equals(Boolean.TRUE)) { // Check the boolean status
                knownCount++;
            }
        }
        request.setWordsMarkedKnown(knownCount);
        request.setTotalWordsInSession(wordList != null ? wordList.size() : 0);

        vocabularyApiService = InitializeRetrofit.getInstance().create(VocabularyApiService.class);
        vocabularyApiService.endReviewSessionEarly(request).enqueue(new Callback<VocabularyReviewEndResponse>() {
            @Override
            public void onResponse(@NonNull Call<VocabularyReviewEndResponse> call, @NonNull Response<VocabularyReviewEndResponse> response) {
                // ... (hide loading indicator) ...
                if (response.isSuccessful() && response.body() != null) {
                    VocabularyReviewEndResponse summaryResponseData = response.body(); // This is the data for your summary screen
                    Toast.makeText(VocabularyReviewFlashCardActivity.this, "Buổi ôn tập kết thúc. Tiến trình đã lưu.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(VocabularyReviewFlashCardActivity.this, VocabularyReviewSummaryActivity.class); // Your summary activity
                    intent.putExtra("REVIEW_SESSION_SUMMARY", summaryResponseData); // Pass the whole object
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(VocabularyReviewFlashCardActivity.this, "Lỗi khi kết thúc buổi ôn tập: Code " + response.code(), Toast.LENGTH_LONG).show();
                    // ... (log error body) ...
                }
            }

            @Override
            public void onFailure(@NonNull Call<VocabularyReviewEndResponse> call, @NonNull Throwable t) {
                // ... (hide loading indicator, log error) ...
                Toast.makeText(VocabularyReviewFlashCardActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void displayCurrentWord() {
        if (wordList == null || wordList.isEmpty() || currentWordIndex >= wordList.size()) {
            // All words finished or error
            Toast.makeText(this, "Hoàn thành tất cả các từ!", Toast.LENGTH_LONG).show();
            // TODO: Navigate to a results screen or back
            finish();
            return;
        }

        currentDisplayedWord = wordList.get(currentWordIndex);

        textWord.setText(currentDisplayedWord.getWord());
        textMeaning.setText(currentDisplayedWord.getMeaning());
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
        if (!TextUtils.isEmpty(currentDisplayedWord.getImageUrl())) {
            // Example with Glide:
            // Glide.with(this).load(currentWord.getImageUrl()).placeholder(R.drawable.ic_image_placeholder).into(imageIllustration);
            imageIllustration.setImageResource(R.drawable.ic_journey); // Placeholder for now
            imageIllustration.setVisibility(View.VISIBLE);
        } else {
            imageIllustration.setVisibility(View.GONE); // Or set a default placeholder
        }

        // Enable/disable pronounce button
        buttonPronounce.setEnabled(!TextUtils.isEmpty(currentDisplayedWord.getAudioUrl()));
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
                Toast.makeText(VocabularyReviewFlashCardActivity.this, "Lỗi phát âm thanh.", Toast.LENGTH_SHORT).show();
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