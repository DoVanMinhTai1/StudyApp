package nlu.fit.studyappr.activity.grammar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.adapter.grammar.GrammarQuestionReviewAdapter;
import nlu.fit.studyappr.api.grammar.GrammarApiService;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.model.AnswerSubmissionRequest;
import nlu.fit.studyappr.model.ExerciseResult;
import nlu.fit.studyappr.model.GrammarExcerciseQuestion;
import nlu.fit.studyappr.model.GrammarLesson;
import nlu.fit.studyappr.model.GrammarReviewResult;
import nlu.fit.studyappr.model.GrammarTopic;
import nlu.fit.studyappr.model.UserAnswer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;


// ... other imports ...
import android.widget.LinearLayout; // For managing visibility of content/error/loading

public class GrammarExerciseActivity extends AppCompatActivity implements GrammarQuestionReviewAdapter.OnAnswerSelectedListener {
    List<GrammarExcerciseQuestion> questionList;
    ExerciseResult exerciseResult;
    Button buttonExit;

    Button buttonSubmit;
    private RecyclerView questionsRecyclerView;
    private GrammarQuestionReviewAdapter adapter;
    private TextView quizTopicTitleTextView;
    private TextView quizScoreTextView;
    GrammarLesson grammarLesson;
    private GrammarApiService grammarApiService;

    RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout contentLayout; // A layout group holding your RecyclerView and other content
    private TextView errorTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_review_quiz);

        // Initialize Views
        quizTopicTitleTextView = findViewById(R.id.quizTopicTitleTextView); // Uncommented and correct ID
        quizScoreTextView = findViewById(R.id.quizScoreTextView);         // Uncommented and correct ID
        questionsRecyclerView = findViewById(R.id.questionsRecyclerView);
        buttonSubmit = findViewById(R.id.quizSubmitButton1); // Assuming this is the correct ID from your XML
        buttonExit = findViewById(R.id.quizExitButton); // Assuming this is the correct ID

        // Assuming you have these in your XML for loading/error states
        progressBar = findViewById(R.id.progressBar); // Add a ProgressBar to your XML
        contentLayout = findViewById(R.id.contentLayout); // Wrap your RecyclerView and header_info_layout in this
        errorTextView = findViewById(R.id.errorTextView);   // Add a TextView for errors to your XML

        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        grammarApiService = InitializeRetrofit.getInstance().create(GrammarApiService.class);

        // Setup button listeners (can be done here or after data loads)
        Long grammarTopicId = getIntent().getLongExtra("grammarTopicId",-1);
        buttonSubmit.setOnClickListener(v -> handleSubmit(grammarTopicId));
        buttonExit.setOnClickListener(v -> finish()); // Or show a confirmation dialog

        // Fetch Lesson Data
        Long lessonId = getIntent().getLongExtra("grammarLessonId", -1);
        if (lessonId != -1) {
            fetchGrammarLesson(lessonId);
        } else {
            showError("Invalid Lesson ID.");
        }
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            contentLayout.setVisibility(View.GONE);
            errorTextView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        showLoading(false);
        contentLayout.setVisibility(View.GONE);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showContent() {
        showLoading(false);
        errorTextView.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
    }


    private void fetchGrammarLesson(Long id) {
        showLoading(true);
        Call<GrammarLesson> callLesson = grammarApiService.getGrammarLessonById(id);
        callLesson.enqueue(new Callback<GrammarLesson>() {
            @Override
            public void onResponse(Call<GrammarLesson> call, Response<GrammarLesson> response) {
                if (response.isSuccessful() && response.body() != null) {
                    grammarLesson = response.body();
                    // CRITICAL: Initialize questionList here
                    if (grammarLesson.getGrammarExcerciseQuestionList() != null) {
                        questionList = grammarLesson.getGrammarExcerciseQuestionList(); // Initialize class field

                        // Initialize adapter and set it to RecyclerView
                        adapter = new GrammarQuestionReviewAdapter(GrammarExerciseActivity.this, questionList, GrammarExerciseActivity.this);
                        questionsRecyclerView.setAdapter(adapter);

                        // Update UI elements that depend on grammarLesson
                        // Example: Assuming GrammarLesson has a 'topicName' field
                        if (grammarLesson.getId() != null) { // You need a getTopicName() or similar
//                            quizTopicTitleTextView.setText("Ôn tập: " + grammarLesson.getTopicName());
                        } else {
                            quizTopicTitleTextView.setText("Ôn tập Ngữ pháp");
                        }
                        // Score is usually calculated after submission, so quizScoreTextView might be initially empty or show "0/X"
                        if(quizScoreTextView != null) {

                            quizScoreTextView.setText("Trả lời các câu hỏi"); // Or "0/" + questionList.size()
                        }

                        showContent();
                    } else {
                        showError("Bài học không có câu hỏi nào.");
                    }
                } else {
                    showError("Không thể tải dữ liệu bài học. Mã lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GrammarLesson> call, Throwable t) {
                showError("Lỗi mạng: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void handleSubmit(Long grammarTopicId) {
        if (questionList == null || questionList.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi để nộp.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if adapter has collected all answers (depends on adapter's getUserAnswers() logic)
        // HashMap<Integer, String> userAnswersMap = adapter.getUserAnswers();
        // For now, we assume onAnswerSelected has updated the questionList's userAnswer field.

        List<AnswerSubmissionRequest> answerSubmissionRequests = new ArrayList<>();
        boolean allAnswered = true;
        for (GrammarExcerciseQuestion q : questionList) {
            if (q != null) {
                if (q.getUserAnswer() == null || q.getUserAnswer().isEmpty()) {
                    allAnswered = false;
                    break;
                }
                answerSubmissionRequests.add(new AnswerSubmissionRequest(q.getId(), q.getUserAnswer()));
            }
        }

        if (!allAnswered) {
            Toast.makeText(this, "Vui lòng trả lời tất cả các câu hỏi.", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true); // Show loading while submitting
        // Use the class field grammarApiService
        Call<ExerciseResult> callCheckAnswer = this.grammarApiService.checkAnswer(answerSubmissionRequests);
        callCheckAnswer.enqueue(new Callback<ExerciseResult>() {
            @Override
            public void onResponse(Call<ExerciseResult> call, Response<ExerciseResult> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    exerciseResult = response.body();
                    // Correct score calculation for display (e.g., number correct)
                    int scoreValue = exerciseResult.getCorrect(); // Assuming getCorrect() is the number of correct answers
                    int totalValue = exerciseResult.getTotal();

                    // Display score on the current screen or pass to results screen
                    quizScoreTextView.setText("Điểm: " + scoreValue + " / " + totalValue);
                    showSuccessNotification("Điểm của bạn: " + scoreValue + "/" + totalValue);

                    // Save result and navigate
                    saveAndNavigateToResults(scoreValue, totalValue,grammarTopicId);

                } else {
                    Toast.makeText(GrammarExerciseActivity.this, "Nộp bài thất bại! Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ExerciseResult> call, Throwable t) {
                showLoading(false);
                Toast.makeText(GrammarExerciseActivity.this, "Lỗi khi nộp bài: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAndNavigateToResults(int score, int total, Long grammarTopicId) {
//        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
//        String userId = preferences.getString("user_id", null);
//
//        if (userId == null) {
//            Toast.makeText(this, "Lỗi: Không tìm thấy User ID.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (grammarLesson == null || grammarLesson.getGrammarTopicId() == null) {
//            Toast.makeText(this, "Lỗi: Dữ liệu bài học không đầy đủ để lưu kết quả.", Toast.LENGTH_SHORT).show();
//            return;
//        }


        GrammarReviewResult grammarReviewResult = new GrammarReviewResult();
        GrammarTopic grammarTopic = new GrammarTopic();
        grammarTopic.setId(grammarTopicId);
        // Assuming score is number correct, not percentage for saving
        grammarReviewResult.setScore(score); // Use the integer score
        grammarReviewResult.setUserId("2");
        grammarReviewResult.setGrammarTopic(grammarTopic);


        this.grammarApiService.saveGrammarReviewResult(grammarReviewResult).enqueue(new Callback<GrammarReviewResult>() {
            @Override
            public void onResponse(Call<GrammarReviewResult> call, Response<GrammarReviewResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GrammarReviewResult savedResult = response.body();
                    // Now save individual user answers linked to this review result ID
                    saveUserAnswers(score, savedResult); // Pass the ID of the saved GrammarReviewResult
                } else {
                    Toast.makeText(GrammarExerciseActivity.this, "Lưu tổng kết quả thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GrammarReviewResult> call, Throwable t) {
                Toast.makeText(GrammarExerciseActivity.this, "Lỗi khi lưu tổng kết quả: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserAnswers(double score, GrammarReviewResult grammarReviewResultId) {
        if (questionList == null || questionList.isEmpty()) return;

        List<UserAnswer> userAnswers = new ArrayList<>();
        for (GrammarExcerciseQuestion q : questionList) {
            // Assuming UserAnswer needs questionId, score (perhaps overall score for context), and reviewResultId
            // The score here might be ambiguous. Is it score per question or overall?
            // If UserAnswer stores if *this specific answer* was correct, that's different.
            // For now, let's assume it links to the overall review.
            userAnswers.add(new UserAnswer(q.getId(), score, grammarReviewResultId.getId()));
        }

        this.grammarApiService.saveUserAnswer(userAnswers).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GrammarExerciseActivity.this, "Câu trả lời đã được lưu.", Toast.LENGTH_SHORT).show();
                    // Navigate to results screen, passing the grammarLesson (which now contains user answers)
                    // and potentially the exerciseResult
                    Intent intent = new Intent(GrammarExerciseActivity.this, GrammarExerciseResult.class); // Assuming this is your results activity
                    intent.putExtra("lesson", grammarLesson); // Pass the lesson with user answers
                    intent.putExtra("exerciseResult", exerciseResult); // Pass the API result
                    intent.putExtra("grammarReviewResult", grammarReviewResultId);
                    startActivity(intent);
                    finish(); // Finish this activity
                } else {
                    Toast.makeText(GrammarExerciseActivity.this, "Lưu chi tiết câu trả lời thất bại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(GrammarExerciseActivity.this, "Lỗi khi lưu chi tiết câu trả lời: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showSuccessNotification(String message) {
        // ... (your existing notification code is good) ...
        String channelId = "grammar_channel";
        String channelName = "Grammar Result";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_funtion) // Replace with your actual notification icon
                .setContentTitle("Nộp bài thành công")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }

    @Override
    public void onAnswerSelected(int questionPosition, String selectedOption) {
        if (questionList != null && questionPosition >= 0 && questionPosition < questionList.size()) {
            questionList.get(questionPosition).setUserAnswer(selectedOption);
            Log.d("QuizAnswer", "Q: " + (questionPosition + 1) + " Answered: " + selectedOption);
        }
    }
}