package nlu.fit.studyappr.activity.progress;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem; // For toolbar back button
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter; // If not using android:entries directly
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import nlu.fit.studyappr.R;
// Assuming you have a LearningPathProposalActivity
// import nlu.fit.studyappr.activity.path.LearningPathProposalActivity;

public class ProgressSetup extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private Spinner spinnerTargetScore;
    private Spinner spinnerStudyDuration;
    private SeekBar seekBarHoursPerWeek;
    private TextView textViewHoursValue;
    private MaterialButton buttonSubmitPathRequest;

    private final int MIN_HOURS_PER_WEEK = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_select_learning_path); // Ensure this is the correct layout file

        toolbar = findViewById(R.id.toolbar_select_path);
        spinnerTargetScore = findViewById(R.id.spinnerTargetScore);
        spinnerStudyDuration = findViewById(R.id.spinnerStudyDuration);
        seekBarHoursPerWeek = findViewById(R.id.seekBarHoursPerWeek);
        textViewHoursValue = findViewById(R.id.textViewHoursValue);
        buttonSubmitPathRequest = findViewById(R.id.buttonSubmitPathRequest);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Setup SeekBar
        // Initialize text based on initial progress (progress + MIN_HOURS_PER_WEEK)
        textViewHoursValue.setText((seekBarHoursPerWeek.getProgress() + MIN_HOURS_PER_WEEK) + " giờ / tuần");

        seekBarHoursPerWeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int actualHours = progress + MIN_HOURS_PER_WEEK;
                textViewHoursValue.setText(actualHours + " giờ / tuần");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }
        });

        // Setup Submit Button
        buttonSubmitPathRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });

        // Optional: Add listeners to Spinners if you need to react to immediate changes,
        // but for submission, getting selected item on button click is usually enough.
    }

    private void handleSubmit() {
        String targetScore = spinnerTargetScore.getSelectedItem().toString();
        String studyDuration = spinnerStudyDuration.getSelectedItem().toString();
        int hoursPerWeek = seekBarHoursPerWeek.getProgress() + MIN_HOURS_PER_WEEK;

        if (spinnerTargetScore.getSelectedItemPosition() == 0) {
            showMissingInfoDialog("Vui lòng chọn mục tiêu điểm số.");
            return;
        }
        if (spinnerStudyDuration.getSelectedItemPosition() == 0) {
            showMissingInfoDialog("Vui lòng chọn thời gian học dự kiến.");
            return;
        }

        Toast.makeText(this, "Đang xử lý...", Toast.LENGTH_SHORT).show();

        Log.d("ProgressSetup", "Target Score: " + targetScore);
        Log.d("ProgressSetup", "Study Duration: " + studyDuration);
        Log.d("ProgressSetup", "Hours per Week: " + hoursPerWeek);

        // Example: Navigate to LearningPathProposalActivity
         Intent intent = new Intent(ProgressSetup.this, LearningPathProposalActivity.class);
         intent.putExtra("TARGET_SCORE", targetScore);
         intent.putExtra("STUDY_DURATION", studyDuration);
         intent.putExtra("HOURS_PER_WEEK", hoursPerWeek);
         startActivity(intent);

        // For now, let's just show a Toast with the selection
//        String message = "Mục tiêu: " + targetScore +
//                "\nThời gian: " + studyDuration +
//                "\nGiờ/tuần: " + hoursPerWeek;
//        new AlertDialog.Builder(this)
//                .setTitle("Lựa chọn của bạn")
//                .setMessage(message)
//                .setPositiveButton("OK", null)
//                .show();
    }

    private void showMissingInfoDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Thông tin chưa đủ")
                .setMessage(message)
                .setPositiveButton("Đã hiểu", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to previous one
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}