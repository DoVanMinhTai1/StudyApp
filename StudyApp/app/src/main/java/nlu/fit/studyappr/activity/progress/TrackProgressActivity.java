package nlu.fit.studyappr.activity.progress;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;

import nlu.fit.studyappr.R;

public class TrackProgressActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView statLearnedVocabValue, statExercisesDoneValue, /* ... other stat TextViews ... */ overallProgressBarText;
    private ProgressBar overallProgressBar;
    // ... Other UI elements like chart container, buttons ...

    private String activePathId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_progress);

        toolbar = findViewById(R.id.toolbar_track_progress);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize other UI elements from activity_track_progress.xml
        statLearnedVocabValue = findViewById(R.id.statLearnedVocabValue);
        // ... findViewById for all other TextViews, ProgressBar, Buttons ...
        overallProgressBar = findViewById(R.id.overallProgressBar);
        overallProgressBarText = findViewById(R.id.overallProgressBarText);


        // Get active path ID (from Intent or SharedPreferences)
        activePathId = getIntent().getStringExtra("ACTIVE_PATH_ID");
        if (activePathId == null) {
            // Try loading from SharedPreferences if not passed via Intent
            SharedPreferences prefs = getSharedPreferences("user_active_path", MODE_PRIVATE);
            activePathId = prefs.getString("current_path_id", null);
        }

        if (activePathId != null) {
            Log.d("TrackProgress", "Active Path ID: " + activePathId);
            // TODO: Load path details and progress from API using activePathId and userId
            // For now, display placeholder or locally saved info
            loadAndDisplayProgressData(activePathId);
        } else {
            // Handle case: No active learning path found.
            // Show a message, or navigate user to select a path.
            // For example, update a TextView to say "No active path"
            statLearnedVocabValue.setText("N/A");
            // ... set other fields to N/A or show an empty state view
            Toast.makeText(this, "Không có lộ trình học nào đang hoạt động.", Toast.LENGTH_LONG).show();
        }
    }

    private void loadAndDisplayProgressData(String pathId) {
        // 1. Display any locally saved quick-view data from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_active_path", MODE_PRIVATE);
        String pathTarget = prefs.getString("path_target", "N/A");
        // You can display this somewhere if needed, e.g., a subtitle

        // TODO: 2. Make an API call to get full progress for this pathId and userId
        // This API would return data like:
        // {
        //   "learnedVocab": 150,
        //   "exercisesDone": 35,
        //   "exercisesCompleted": 30,
        //   "pathCompletionPercentage": 45,
        //   "weeklyProgressDataForChart": [ ... ]
        // }

        // For now, using mock data as in your HTML's JS
        int learnedVocab = 125;
        int exercisesDone = 30;
        int exercisesCompleted = 25;
        int pathCompletionPercentage = 40; // Example

        statLearnedVocabValue.setText(String.valueOf(learnedVocab));
        // ... set text for other stat TextViews ...
//        findViewById(R.id.statExercisesDoneValue).setText(String.valueOf(exercisesDone));
//        findViewById(R.id.statExercisesCompletedValue).setText(String.valueOf(exercisesCompleted));
//        findViewById(R.id.statPathCompletionValue).setText(pathCompletionPercentage + "%");


        overallProgressBar.setProgress(pathCompletionPercentage);
        overallProgressBarText.setText(pathCompletionPercentage + "%");

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Navigate back or to a main screen
            // finish(); // Simple back
            // Or:
            // Intent intent = new Intent(this, MainActivity.class);
            // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            // startActivity(intent);
            // finish();
            onBackPressed(); // Standard back behavior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}