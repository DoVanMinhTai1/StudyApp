package nlu.fit.studyappr.activity.grammar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // For toolbar back button
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
// Import MaterialButton if you are using it and need to set listeners
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;


import nlu.fit.studyappr.R;
import nlu.fit.studyappr.activity.home.HomeActivity; // Assuming you have a MainActivity
import nlu.fit.studyappr.model.grammar.ExerciseResult;
import nlu.fit.studyappr.model.grammar.GrammarLesson;
import nlu.fit.studyappr.model.grammar.GrammarReviewResult;

public class GrammarExerciseResult extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView topicNameTextView;
    private TextView congratsMessageTextView; // You might not need this if the message is static
    private TextView scoreTextView;
    private TextView saveConfirmationTextView; // You might not need this if the message is static
    private MaterialButton buttonBackToList;
    private MaterialButton buttonBackToHome;

    private GrammarLesson mLesson; // To store the received lesson
    private ExerciseResult mExerciseResult; // To store the received result

    private GrammarReviewResult grammarReviewResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_review_results); // Make sure this is your results XML file name

        // Initialize Views
        toolbar = findViewById(R.id.toolbar_results);
        topicNameTextView = findViewById(R.id.topicNameTextView);
        congratsMessageTextView = findViewById(R.id.congratsMessageTextView11); // Using ID from your XML
        scoreTextView = findViewById(R.id.scoreTextView1);                     // Using ID from your XML
        saveConfirmationTextView = findViewById(R.id.saveConfirmationTextView1); // Using ID from your XML
        buttonBackToList = findViewById(R.id.buttonBackToList1);               // Using ID from your XML
        buttonBackToHome = findViewById(R.id.buttonBackToHome1);                 // Using ID from your XML

        // Setup Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back arrow
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // If your toolbar's navigationIcon is ic_search, you'll need to change it to
            // ic_arrow_back in XML or programmatically for this to show a back arrow.
            // Or handle R.id.home differently if ic_search is intentional.
        }

        // Get data from Intent
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("lesson")) {
                mLesson = (GrammarLesson) intent.getSerializableExtra("lesson");
            }
            if (intent.hasExtra("exerciseResult")) {
                mExerciseResult = (ExerciseResult) intent.getSerializableExtra("exerciseResult");
            }
        }
        grammarReviewResult = (GrammarReviewResult) intent.getSerializableExtra("grammarReviewResult");

        // Render data
        if (mLesson != null && mExerciseResult != null) {
            displayResults();
        } else {
            // Handle error: Data not received
            Toast.makeText(this, "Lỗi: Không nhận được dữ liệu kết quả.", Toast.LENGTH_LONG).show();
            topicNameTextView.setText("Lỗi");
            scoreTextView.setText("N/A");
            // You might want to disable buttons or finish the activity
            buttonBackToList.setEnabled(false);
            buttonBackToHome.setEnabled(false);
        }

        // Setup Button Click Listeners
        buttonBackToList.setOnClickListener(v -> {
            // Navigate back to the list of grammar topics/lessons
            // This might involve finishing this activity and the previous quiz activity
            // Or starting a new Intent to your topic list screen.
            // For simplicity, let's assume it goes to a new instance of a fragment or activity
            // that shows the list of grammar exercises/topics.
            Intent listIntent = new Intent(GrammarExerciseResult.this, GrammarTopicReview.class); // Replace GrammarActivity with your actual list screen
            listIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(listIntent);
            finishAffinity(); // Finishes this and all parent activities in the task
        });

        buttonBackToHome.setOnClickListener(v -> {
            // Navigate to the main home screen of your app
            Intent homeIntent = new Intent(GrammarExerciseResult.this, HomeActivity.class); // Replace MainActivity with your actual home screen
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finishAffinity(); // Finishes this and all parent activities in the task
        });
    }

    private void displayResults() {
        // Set Topic Name
        // This assumes GrammarLesson has a way to get its associated GrammarTopic's name
        // or the lesson itself has a title that can be used.
        String topicDisplay = "N/A";
        if (mLesson.getGrammarTopicId() != null && grammarReviewResult.getGrammarTopic().getTitle() != null) {
            topicDisplay = "Chủ điểm: " + grammarReviewResult.getGrammarTopic().getTitle();
        } else if (grammarReviewResult.getGrammarTopic().getTitle() != null) { // Fallback to lesson title
            topicDisplay = "Bài học: " + grammarReviewResult.getGrammarTopic().getTitle();
        } else {
            topicDisplay = "Kết quả ôn tập";
        }
        topicNameTextView.setText(topicDisplay);

        // Set Score
        scoreTextView.setText("Điểm của bạn: " + mExerciseResult.getCorrect() + " / " + mExerciseResult.getTotal());

        // Optional: Customize congrats message based on score
        if ((double) mExerciseResult.getCorrect() / mExerciseResult.getTotal() >= 0.7) { // Example: 70% or more
            congratsMessageTextView.setText("Tuyệt vời! Bạn đã làm rất tốt!");
        } else if ((double) mExerciseResult.getCorrect() / mExerciseResult.getTotal() >= 0.5) { // Example: 50% or more
            congratsMessageTextView.setText("Làm tốt lắm! Cố gắng hơn ở lần sau nhé.");
        } else {
            congratsMessageTextView.setText("Chúc mừng bạn đã hoàn thành phần ôn tập!");
        }

        // The "saveConfirmationTextView" text is static in your XML, so no need to set it here unless it's dynamic.
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle toolbar item clicks here.
        if (item.getItemId() == android.R.id.home) {
            // This ID refers to the Home button in the Action Bar/Toolbar (often the back arrow)
            // Navigate back to where the user came from before the quiz,
            // or to a defined "up" destination.
            // Since the quiz activity (GrammarExerciseActivity) likely called finish(),
            // simply calling finish() here might take the user too far back.
            // It's often better to navigate to a specific screen, like the list of exercises.

            Intent listIntent = new Intent(GrammarExerciseResult.this, GrammarTopicReview.class); // Replace with your exercise list screen
            listIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(listIntent);
            finish(); // Finish this activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}