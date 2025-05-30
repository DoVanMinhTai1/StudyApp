package nlu.fit.studyappr.activity.grammar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.api.grammar.GrammarApiService;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.model.AnswerSubmissionRequest;
import nlu.fit.studyappr.model.ExerciseResult;
import nlu.fit.studyappr.model.GrammarExcerciseQuestion;
import nlu.fit.studyappr.model.GrammarLesson;
import nlu.fit.studyappr.model.GrammarReviewResult;
import nlu.fit.studyappr.model.UserAnswer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.*;

public class GrammarExerciseActivity extends AppCompatActivity {
    private List<GrammarExcerciseQuestion> questionList;
    ExerciseResult exerciseResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grammar_learn_exercise);
        GrammarLesson grammarLesson = (GrammarLesson) getIntent().getSerializableExtra("lesson");
        Button button = findViewById(R.id.submitAll);
        ProgressBar progressBar = findViewById(R.id.submitProgress);
        progressBar.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            List<AnswerSubmissionRequest> answerSubmissionRequests = new ArrayList<>();
            for (GrammarExcerciseQuestion q : questionList) {
                if (q != null) {
                    answerSubmissionRequests.add(new AnswerSubmissionRequest(q.getId(), q.getUserAnswer()));
                }
            }

            if (answerSubmissionRequests.size() != questionList.size()) {
                Toast.makeText(this, "Please answer all questions first.", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.GONE);

            GrammarApiService grammarApiService = InitializeRetrofit.getInstance().create(GrammarApiService.class);
            Call<ExerciseResult> call = grammarApiService.checkAnswer(answerSubmissionRequests);
            call.enqueue(new Callback<ExerciseResult>() {
                @Override
                public void onResponse(Call<ExerciseResult> call, Response<ExerciseResult> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        exerciseResult = response.body();
                        int score = exerciseResult.getCorrect() / exerciseResult.getTotal();
                        showSuccessNotification("Your Score" + score);

                        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                        String userId = preferences.getString("user_id", null);


                        GrammarReviewResult grammarReviewResult = new GrammarReviewResult();
                        grammarReviewResult.setScore(score);
                        grammarReviewResult.setUserId(userId);
                        grammarReviewResult.setGrammarTopic(grammarLesson.getGrammarTopicId());

                        grammarApiService.saveGrammarReviewResult(grammarReviewResult).enqueue(new Callback<GrammarReviewResult>() {
                            @Override
                            public void onResponse(Call<GrammarReviewResult> call, Response<GrammarReviewResult> response) {
                                if (response.isSuccessful()) {
                                    grammarReviewResult.setId(response.body().getId());
                                }
                            }

                            @Override
                            public void onFailure(Call<GrammarReviewResult> call, Throwable t) {

                            }
                        });

                        List<UserAnswer> userAnswers = new ArrayList<>();
                        for (GrammarExcerciseQuestion grammarExcerciseQuestion : questionList) {
                            userAnswers.add(new UserAnswer(grammarExcerciseQuestion.getId(), (double) score, grammarReviewResult.getId()));
                        }

                        grammarApiService.saveUserAnswer(userAnswers).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(GrammarExerciseActivity.this, "Answers saved successfully", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                Toast.makeText(GrammarExerciseActivity.this, "Failed to save answers", Toast.LENGTH_SHORT).show();

                            }
                        });
                    } else {
                        Toast.makeText(GrammarExerciseActivity.this, "Failed to submit!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ExerciseResult> call, Throwable t) {
                    Toast.makeText(GrammarExerciseActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void showSuccessNotification(String message) {
        String channelId = "grammar_channel";
        String channelName = "Grammar Result";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId).setSmallIcon(R.drawable.download).setContentTitle("Exercise Submitted").setContentText(message).setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}
