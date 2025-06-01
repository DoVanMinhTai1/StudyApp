package nlu.fit.studyappr.activity.vocabulary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.api.vocabulary.VocabularyApiService;
import nlu.fit.studyappr.model.ActiveSessionInfo;
import nlu.fit.studyappr.model.ReviewableWord;
import nlu.fit.studyappr.model.StartLearningResponse;
import nlu.fit.studyappr.model.StartLearningRevewRequest;
import nlu.fit.studyappr.model.StartLearningReviewRequest;
import nlu.fit.studyappr.model.StartLearningReviewResponse;
import nlu.fit.studyappr.model.VocabularyReviewListResponse;
import nlu.fit.studyappr.model.VocabularyWordViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabularyReviewList extends AppCompatActivity {
    StartLearningReviewResponse startLearningResponse;

    private static final String TAG = "VocabReviewList";

    private VocabularyApiService vocabularyApiService;
    private String currentUserId;

    private View resumeSessionBanner; // The root of the included layout
    private TextView textResumeSessionTitle, textResumeSessionDetails;
    private Button buttonResumeSession;

    private TextView textWordsToReviewCount;
    private LinearLayout sampleWordsContainer;
    private TextView textMoreWords; // To show "... (and X more words)"

    private RadioGroup reviewMethodGroup;
    private Button buttonStartReview;

    private List<ReviewableWord> wordsAvailableForReview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_review_list);

        initializeViews();

        vocabularyApiService = InitializeRetrofit.getInstance().create(VocabularyApiService.class);

//        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
//        currentUserId = prefs.getString("user_id", null);
//
//        if (currentUserId == null) {
//            Toast.makeText(this, "Lỗi người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
//            // TODO: Navigate to login
//            finish();
//            return;
//        }

        fetchReviewData();

        buttonStartReview.setOnClickListener(v -> startNewReviewSession());
    }

    private void initializeViews() {
        resumeSessionBanner = findViewById(R.id.resumeSessionBanner);
        textResumeSessionTitle = resumeSessionBanner.findViewById(R.id.textResumeSessionTitle); // Find within banner
        textResumeSessionDetails = resumeSessionBanner.findViewById(R.id.textResumeSessionDetails);
        buttonResumeSession = resumeSessionBanner.findViewById(R.id.buttonResumeSession);

        textWordsToReviewCount = findViewById(R.id.textWordsToReviewCount);
        sampleWordsContainer = findViewById(R.id.sampleWordsContainer);
        textMoreWords = findViewById(R.id.textMoreWords);

        reviewMethodGroup = findViewById(R.id.reviewMethodGroup);
        buttonStartReview = findViewById(R.id.buttonStartReview);
    }

    private String getCurrentIsoTime() {
        TimeZone tz = TimeZone.getTimeZone("UTC"); // Or your server's expected timezone
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    private void fetchReviewData() {
        // Show loading indicator
        Toast.makeText(this, "Đang tải danh sách ôn tập...", Toast.LENGTH_SHORT).show();

        currentUserId = "2";
        vocabularyApiService.getVocabularyReviewList(currentUserId, getCurrentIsoTime())
                .enqueue(new Callback<VocabularyReviewListResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<VocabularyReviewListResponse> call, @NonNull Response<VocabularyReviewListResponse> response) {
                        // Hide loading indicator
                        if (response.isSuccessful() && response.body() != null) {
                            VocabularyReviewListResponse data = response.body();
                            updateUiWithReviewData(data);
                        } else {
                            Toast.makeText(VocabularyReviewList.this, "Lỗi tải danh sách: " + response.code(), Toast.LENGTH_LONG).show();
                            textWordsToReviewCount.setText("Không có từ nào cần ôn tập.");
                            buttonStartReview.setEnabled(false);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VocabularyReviewListResponse> call, @NonNull Throwable t) {
                        // Hide loading indicator
                        Toast.makeText(VocabularyReviewList.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "API Failure: getVocabularyReviewList", t);
                        textWordsToReviewCount.setText("Lỗi tải danh sách.");
                        buttonStartReview.setEnabled(false);
                    }
                });
    }

    private void updateUiWithReviewData(VocabularyReviewListResponse data) {
        // Handle Active Session Banner
//        if (data.getActiveSession() != null) {
//            ActiveSessionInfo sessionInfo = data.getActiveSession();
//            resumeSessionBanner.setVisibility(View.VISIBLE);
//            // textResumeSessionTitle can remain static or be updated if needed
//            textResumeSessionDetails.setText(sessionInfo.getTopicName() + " (" +
//                    sessionInfo.getWordsCompletedInSession() + "/" + sessionInfo.getTotalWordsInSession() + " từ)");
//            buttonResumeSession.setOnClickListener(v -> resumeSession(sessionInfo));
//        } else {
//            resumeSessionBanner.setVisibility(View.GONE);
//        }

        // Handle Words to Review
        wordsAvailableForReview = data.getWordsToReview(); // Store for starting session
        if (wordsAvailableForReview != null && !wordsAvailableForReview.isEmpty()) {
            textWordsToReviewCount.setText("Cần ôn tập (" + data.getTotalWordsToReviewCount() + " từ)");
            displaySampleWords(wordsAvailableForReview);
            buttonStartReview.setEnabled(true);
        } else {
            textWordsToReviewCount.setText("Tuyệt vời! Không có từ nào cần ôn tập lúc này.");
            sampleWordsContainer.setVisibility(View.GONE); // Hide sample words area
            buttonStartReview.setEnabled(false);
        }
    }

    private void displaySampleWords(List<ReviewableWord> words) {
        sampleWordsContainer.removeAllViews(); // Clear previous samples
        int count = 0;
        for (ReviewableWord word : words) {
            if (count < 5) { // Show up to 5 sample words
                TextView wordView = new TextView(this);
                wordView.setText(word.getWord() + " - " + word.getMeaning());
                wordView.setTextSize(15f); // sp
                wordView.setPadding(0, (int) (4 * getResources().getDisplayMetrics().density), 0, (int) (4 * getResources().getDisplayMetrics().density));
                sampleWordsContainer.addView(wordView);
                count++;
            } else {
                break;
            }
        }
        if (words.size() > 5) {
            textMoreWords.setText("... (và " + (words.size() - 5) + " từ khác)");
            textMoreWords.setVisibility(View.VISIBLE);
            sampleWordsContainer.addView(textMoreWords); // Add it after clearing and adding new samples
        } else {
            textMoreWords.setVisibility(View.GONE);
        }
    }


    private void resumeSession(ActiveSessionInfo sessionInfo) {
        Toast.makeText(this, "Tiếp tục buổi học: " + sessionInfo.getTopicName(), Toast.LENGTH_SHORT).show();
        // TODO: Make an API call to get the words for this sessionInfo.sessionId
        // Or if sessionInfo itself contains enough data, or if your learning activity can handle resuming
        // based on lastSeenWordIndex and topicId.
        // This typically involves starting VocabularyLearnActivity or VocabularyLearnQuizActivity
        // with specific "resume" parameters.

        // Example: If your StartLearningResponse is what the quiz activity expects
        // you might need to fetch a new StartLearningResponse for resuming.
        // For now, let's assume you'd go to a learning screen.

        // This is a placeholder - you'll need a proper way to get words for the resumed session
        // StartLearningResponse resumeData = new StartLearningResponse();
        // resumeData.setSessionId(sessionInfo.getSessionId());
        // resumeData.setTopicId(Long.parseLong(sessionInfo.getTopicId())); // Assuming topicId is Long
        // resumeData.setTopicName(sessionInfo.getTopicName());
        // resumeData.setTopicType("VOCABULARY"); // Or from sessionInfo
        // resumeData.setWords(... words for this session ...); // <<<< THIS IS THE MISSING PIECE

        // Intent intent = new Intent(this, VocabularyLearnQuizActivity.class);
        // intent.putExtra("startLearning", resumeData);
        // intent.putExtra("isResuming", true);
        // intent.putExtra("lastSeenIndex", sessionInfo.getLastSeenWordIndex());
        // startActivity(intent);
    }

    private void startNewReviewSession() {
        if (wordsAvailableForReview == null || wordsAvailableForReview.isEmpty()) {
            Toast.makeText(this, "Không có từ nào để ôn tập.", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedMethodId = reviewMethodGroup.getCheckedRadioButtonId();
        String reviewMethod;
        if (selectedMethodId == R.id.radioFlashcard) {
            reviewMethod = "FLASHCARD";
        } else if (selectedMethodId == R.id.radioQuiz) {
            reviewMethod = "QUIZ";
        } else if (selectedMethodId == R.id.radioRewrite) {
            reviewMethod = "REWRITE";
        } else {
            Toast.makeText(this, "Vui lòng chọn phương pháp ôn tập.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Bắt đầu ôn tập với: " + reviewMethod, Toast.LENGTH_SHORT).show();

        // TODO: 1. Call an API to "start" a new review session.
        // This API might take userId, list of word IDs from wordsAvailableForReview, and selected reviewMethod.
        // It should return a StartLearningResponse (or similar) containing the words formatted
        // for the chosen review method and a new sessionId.

        // Mocking the StartLearningResponse structure for navigation
        currentUserId = "2";
        StartLearningRevewRequest startLearningRevewRequest = new StartLearningRevewRequest();
        startLearningRevewRequest.setMethod(reviewMethod);
        startLearningRevewRequest.setUserId(currentUserId);
        StartLearningReviewRequest request = new StartLearningReviewRequest(reviewMethod, currentUserId);
        vocabularyApiService.getStartReview(request).enqueue(new Callback<StartLearningReviewResponse>() {
            @Override
            public void onResponse(Call<StartLearningReviewResponse> call, Response<StartLearningReviewResponse> response) {
                buttonStartReview.setEnabled(true);


                if (response.isSuccessful()) {
                    startLearningResponse = response.body();

                    // startResponse.setSessionId(... from new session API response ...);
                    // startResponse.setTopicId(... usually null for a general review list, or a special review topic ID ...);
                    // startResponse.setTopicName("Ôn tập từ vựng");
                    StartLearningReviewResponse startResponse = new StartLearningReviewResponse();
//        startResponse.set("VOCABULARY_REVIEW");

                    // Convert ReviewableWord to VocabularyWordViewModel if your learning activities expect that
                    List<VocabularyWordViewModel> wordsForSession = new ArrayList<>();
                    for (ReviewableWord rw : wordsAvailableForReview) {
                        VocabularyWordViewModel vm = new VocabularyWordViewModel(
                        );

                        vm.setId(rw.getId()); // Assuming constructor takes ID
                        vm.setWord(rw.getWord()); // Assuming word is the question prompt for flashcard/rewrite
                        // For quiz, you'd need to generate options or fetch them.
                        vm.setMeaning(rw.getMeaning()); // Option A or part of flashcard
                        vm.setWord(rw.getWord());
                        vm.setMeaning(rw.getMeaning());
                        wordsForSession.add(vm);
                    }
                    startResponse.setReviewScheduleId(startLearningResponse.getReviewScheduleId());
                    startResponse.setWords(wordsForSession);


                    // Navigate to the appropriate learning/review activity based on reviewMethod
                    Intent intent;
                    if ("QUIZ".equals(reviewMethod)) {
                        intent = new Intent(VocabularyReviewList.this, VocabularyLearnQuizActivity.class);
                        intent.putExtra("startLearning", startResponse);
                        startActivity(intent);
                    } else if ("FLASHCARD".equals(reviewMethod)) {
                        intent = new Intent(VocabularyReviewList.this, VocabularyReviewFlashCardActivity.class);
                        intent.putExtra("startLearning", startResponse);
                        startActivity(intent);
                    } else { // REWRITE
                        intent = new Intent(VocabularyReviewList.this, VocabularyLearnRewriteActivity.class);
                        intent.putExtra("startLearning", startResponse);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<StartLearningReviewResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}