package nlu.fit.studyappr.activity.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.activity.home.HomeActivity;
import nlu.fit.studyappr.model.vocabulary.VocabularyEndResponse;

public class VocabularyLearnSummaryActivity extends AppCompatActivity {


    private TextView itemTopicValue, itemLevelValue, itemMethodValue,
            itemTotalWordsValue, itemCorrectValue, itemIncorrectValue,
            suggestionText;
    private Button buttonHome, buttonNewSession;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_learn_summary); // Your provided XML layout file

        // Find views for summary items (using the IDs from your include tags)
        // We need to find the TextViews *inside* the included layouts.
        View itemTopicLayout = findViewById(R.id.item_topic);
        itemTopicValue = itemTopicLayout.findViewById(R.id.value); // Assuming item_summary_value is the ID in activity_vocabulary_learn_item_summary.xml
        ((TextView) itemTopicLayout.findViewById(R.id.label)).setText("Chủ đề"); // Assuming item_summary_label is the ID

        View itemLevelLayout = findViewById(R.id.item_level);
        itemLevelValue = itemLevelLayout.findViewById(R.id.value);
        ((TextView) itemLevelLayout.findViewById(R.id.label)).setText("Cấp độ");

        View itemMethodLayout = findViewById(R.id.item_method);
        itemMethodValue = itemMethodLayout.findViewById(R.id.value);
        ((TextView) itemMethodLayout.findViewById(R.id.label)).setText("Phương pháp");

        View itemTotalWordsLayout = findViewById(R.id.item_total_words);
        itemTotalWordsValue = itemTotalWordsLayout.findViewById(R.id.value);
        ((TextView) itemTotalWordsLayout.findViewById(R.id.label)).setText("Tổng số từ trong buổi");

        View itemCorrectLayout = findViewById(R.id.item_correct);
        itemCorrectValue = itemCorrectLayout.findViewById(R.id.value);
        ((TextView) itemCorrectLayout.findViewById(R.id.label)).setText("Số từ đã học/đúng");

        View itemIncorrectLayout = findViewById(R.id.item_incorrect);
        itemIncorrectValue = itemIncorrectLayout.findViewById(R.id.value);
        ((TextView) itemIncorrectLayout.findViewById(R.id.label)).setText("Số từ chưa thuộc/sai");


        // Find other views
        // Assuming your suggestion TextView has an ID, e.g., R.id.text_suggestion
        suggestionText = findViewById(R.id.text_suggestion); // You need to add an ID to this TextView in your XML
        buttonHome = findViewById(R.id.button_home);
        buttonNewSession = findViewById(R.id.button_new_session);


        // Get the summary data from the Intent
        VocabularyEndResponse summary = (VocabularyEndResponse) getIntent().getSerializableExtra("SESSION_SUMMARY");

        if (summary != null) {
            populateSummaryData(summary);
        } else {
            // Handle error - summary data not found
            Toast.makeText(this, "Lỗi hiển thị tổng kết.", Toast.LENGTH_SHORT).show();
            // You might want to set default text or finish the activity
            itemTopicValue.setText("N/A");
            itemLevelValue.setText("N/A");
            // ... and so on for other fields
            suggestionText.setText("Không có dữ liệu tổng kết.");
        }

        buttonHome.setOnClickListener(v -> {
            // Navigate to Home Screen
            Intent intent = new Intent(VocabularyLearnSummaryActivity.this, HomeActivity.class); // Replace MainActivity with your actual home screen
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        buttonNewSession.setOnClickListener(v -> {
            // Navigate to a screen to start a new learning session
            // This might be your topic selection screen
             Intent intent = new Intent(VocabularyLearnSummaryActivity.this, VocabularyLearnSetupActivity.class);
             startActivity(intent);
            Toast.makeText(this, "Bắt đầu buổi học mới (chưa triển khai)", Toast.LENGTH_SHORT).show();
            finish(); // Or navigate appropriately
        });
    }

    private void populateSummaryData(VocabularyEndResponse summary) {
        itemTopicValue.setText(summary.getTopicTitle() != null ? summary.getTopicTitle() : "N/A");
        itemLevelValue.setText(summary.getLevel() != null ? summary.getLevel() : "N/A");
        itemMethodValue.setText(summary.getMethod() != null ? summary.getMethod() : "N/A");
        itemTotalWordsValue.setText(summary.getTotalWordsInSession() + " từ");
        itemCorrectValue.setText(summary.getWordsCorrectOrLearned() + " từ");
        itemIncorrectValue.setText(summary.getWordsIncorrectOrPending() + " từ");

        if (suggestionText != null && summary.getSuggestion() != null) {
            suggestionText.setText(summary.getSuggestion());
        } else if (suggestionText != null) {
            suggestionText.setText("Hãy tiếp tục cố gắng nhé!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Navigate to Home Screen or previous meaningful screen, not just finish()
            // as finish() might take user back to the learning session they just ended.
            Intent intent = new Intent(VocabularyLearnSummaryActivity.this, HomeActivity.class); // Replace MainActivity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
