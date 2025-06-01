package nlu.fit.studyappr.activity.vocabulary; // Your package

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar; // If you add a Toolbar to summary XML

import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.model.NextReviewTimeInfo;
import nlu.fit.studyappr.model.VocabularyReviewEndResponse;

public class VocabularyReviewSummaryActivity extends AppCompatActivity {

    private TextView headerTitle, sectionTitle, suggestionBox;
    private TextView valueMethod, valueTotalWords, valueRemembered, valueToReviewAgain;
    private LinearLayout nextReviewTimesContainer;
    private Button buttonHome, buttonReviewMore;
    private MaterialToolbar toolbar; // Optional

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_result_summary); // Your provided summary XML

        // Initialize Views from your summary XML
        // headerTitle = findViewById(R.id.header_title); // ID from your summary XML
        // sectionTitle = findViewById(R.id.section_title); // ID from your summary XML

        // For the label-value pairs, find the VALUE TextViews
        // Assuming the structure from your XML:
        // Phương pháp:
        valueMethod = findViewById(R.id.value_method);
        valueTotalWords = findViewById(R.id.value_total_words);
        valueRemembered = findViewById(R.id.value_remembered);
        valueToReviewAgain = findViewById(R.id.value_to_review_again);

        suggestionBox = findViewById(R.id.suggestion_box); // ID from your summary XML
        nextReviewTimesContainer = findViewById(R.id.next_review_times_list_container); // ADD ID to the LinearLayout holding next review times

        buttonHome = findViewById(R.id.button_home);
        buttonReviewMore = findViewById(R.id.button_review_more);

        // Optional Toolbar Setup
        // toolbar = findViewById(R.id.toolbar_summary); // Add Toolbar to summary XML with this ID
        // setSupportActionBar(toolbar);
        // if (getSupportActionBar() != null) {
        //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //     getSupportActionBar().setTitle("Tổng Kết Ôn Tập");
        // }

        VocabularyReviewEndResponse summaryData = (VocabularyReviewEndResponse) getIntent().getSerializableExtra("REVIEW_SESSION_SUMMARY");

        if (summaryData != null) {
            populateUI(summaryData);
        } else {
            Toast.makeText(this, "Không có dữ liệu tổng kết.", Toast.LENGTH_LONG).show();
            // Set default text or finish
        }

        buttonHome.setOnClickListener(v -> {
            // Intent to go to Home Activity
            // ...
            finishAffinity(); // Close this and related task activities
        });

        buttonReviewMore.setOnClickListener(v -> {
            // Intent to go back to VocabularyReviewList or similar
            Intent intent = new Intent(VocabularyReviewSummaryActivity.this, VocabularyReviewList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void populateUI(VocabularyReviewEndResponse summary) {
        // headerTitle.setText("Tổng Kết Buổi Ôn Tập: " + (summary.getTopicName() != null ? summary.getTopicName() : ""));
        // sectionTitle.setText("Kết quả ôn tập - " + (summary.getReviewMethod() != null ? summary.getReviewMethod() : ""));

        if (valueMethod != null)
            valueMethod.setText(summary.getReviewMethod() != null ? summary.getReviewMethod() : "N/A");
        if (valueTotalWords != null)
            valueTotalWords.setText(summary.getTotalWordsReviewed() + " từ");
        if (valueRemembered != null) valueRemembered.setText(summary.getWordsRemembered() + " từ");
        if (valueToReviewAgain != null)
            valueToReviewAgain.setText(summary.getWordsToReviewAgain() + " từ");

        if (suggestionBox != null)
            suggestionBox.setText(summary.getSuggestionText() != null ? summary.getSuggestionText() : "Hoàn thành tốt!");

        if (nextReviewTimesContainer != null && summary.getNextReviewTimes() != null && !summary.getNextReviewTimes().isEmpty()) {
            nextReviewTimesContainer.removeAllViews(); // Clear any previous (though unlikely for this screen)
            LayoutInflater inflater = LayoutInflater.from(this);
            for (NextReviewTimeInfo info : summary.getNextReviewTimes()) {
                // You'll need a small layout for each word-time pair, e.g., list_item_next_review.xml
                // <LinearLayout orientation="horizontal">
                //    <TextView android:id="@+id/text_next_review_word" style="bold"/>
                //    <TextView android:id="@+id/text_next_review_time" style="marginStart"/>
                // </LinearLayout>
                View itemView = inflater.inflate(R.layout.list_item_next_review, nextReviewTimesContainer, false);
                TextView wordText = itemView.findViewById(R.id.text_next_review_word);
                TextView timeText = itemView.findViewById(R.id.text_next_review_time);

                wordText.setText(info.getWord() + ":");
                timeText.setText(info.getNextReviewTimeText());
                nextReviewTimesContainer.addView(itemView);
            }
        } else if (nextReviewTimesContainer != null) {
            // Hide the "Thời gian ôn tập gợi ý tiếp theo:" title if no items
            // Or show a "No specific suggestions" message
            View parentOfNextReviewContainerTitle = (View) nextReviewTimesContainer.getParent();
            TextView nextReviewTitle = parentOfNextReviewContainerTitle.findViewById(R.id.next_review_times_title); // Give this title an ID
            if (nextReviewTitle != null) nextReviewTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Similar to buttonHome logic or just finish()
            finish(); // Or navigate to a specific screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}