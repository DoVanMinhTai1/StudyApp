package nlu.fit.studyappr.activity.progress; // Your package

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import nlu.fit.studyappr.R;
//import nlu.fit.studyappr.activity.MainActivity; // Example for "Go Home"
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit; // Your Retrofit client
import nlu.fit.studyappr.api.learningpath.LearningPathApiService;
import nlu.fit.studyappr.model.ConfirmPathRequest;
import nlu.fit.studyappr.model.LearningPathProposal;
import nlu.fit.studyappr.model.LearningPathRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LearningPathProposalActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private FrameLayout pathProposalContentFrame;
    private ProgressBar progressBarPathProposal;
    private TextView textViewPathError;
    private ScrollView scrollViewPathContent;

    private TextView summaryTargetTextView;
    private TextView summaryDurationTextView;
    private TextView summaryLessonsPerDayTextView;
    private TextView summaryTimePerDayTextView;
    private LinearLayout topicListContainer;

    private MaterialButton buttonAdjustInputs; // ID from XML is buttonAdjustInputs1
    private MaterialButton buttonConfirmAndStartPath; // ID from XML is buttonConfirmAndStartPath1

    private LearningPathApiService learningPathService;
    private String currentPathId; // To store the ID of the proposed path

    private LearningPathProposal learningPathProposal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_path_proposal);

        // Initialize Views
        toolbar = findViewById(R.id.toolbar_path_proposal);
        pathProposalContentFrame = findViewById(R.id.pathProposalContentFrame);
        progressBarPathProposal = findViewById(R.id.progressBarPathProposal);
        textViewPathError = findViewById(R.id.textViewPathError);
        scrollViewPathContent = findViewById(R.id.scrollViewPathContent);

        summaryTargetTextView = findViewById(R.id.summaryTargetTextView);
        summaryDurationTextView = findViewById(R.id.summaryDurationTextView);
        summaryLessonsPerDayTextView = findViewById(R.id.summaryLessonsPerDayTextView);
        summaryTimePerDayTextView = findViewById(R.id.summaryTimePerDayTextView);
        topicListContainer = findViewById(R.id.topicListContainer1); // Correct ID from your XML

        buttonAdjustInputs = findViewById(R.id.buttonAdjustInputs1); // Correct ID
        buttonConfirmAndStartPath = findViewById(R.id.buttonConfirmAndStartPath1); // Correct ID

        // Setup Toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // If using ic_arrow_back, enable it:
             getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize Retrofit Service
        learningPathService = InitializeRetrofit.getInstance().create(LearningPathApiService.class);

        // Get data from previous Activity (ProgressSetup)
        Intent intent = getIntent();
        String targetScore = intent.getStringExtra("TARGET_SCORE");
        String studyDuration = intent.getStringExtra("STUDY_DURATION");
        int hoursPerWeek = intent.getIntExtra("HOURS_PER_WEEK", 5); // Default value if not found

        if (targetScore == null || studyDuration == null) {
            showErrorState("Lỗi: Dữ liệu đầu vào không hợp lệ.");
            buttonConfirmAndStartPath.setEnabled(false);
            buttonAdjustInputs.setOnClickListener(v -> finish()); // Just go back
            return;
        }

        // Make API call to get path proposal
        fetchPathProposal(targetScore, studyDuration, hoursPerWeek);

        buttonAdjustInputs.setOnClickListener(v -> {
            // Go back to ProgressSetup activity to adjust inputs
            // You might want to pass current selections back or just let user re-enter
            finish(); // Closes this activity, user will be back at ProgressSetup
        });

        buttonConfirmAndStartPath.setOnClickListener(v -> {
            if (currentPathId != null) {
                confirmAndStartLearningPath(currentPathId, learningPathProposal);
            } else {
                Toast.makeText(this, "Chưa có lộ trình nào được đề xuất.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoadingState(boolean isLoading) {
        if (isLoading) {
            progressBarPathProposal.setVisibility(View.VISIBLE);
            textViewPathError.setVisibility(View.GONE);
            scrollViewPathContent.setVisibility(View.GONE);
        } else {
            progressBarPathProposal.setVisibility(View.GONE);
        }
    }

    private void showErrorState(String message) {
        showLoadingState(false);
        textViewPathError.setText(message);
        textViewPathError.setVisibility(View.VISIBLE);
        scrollViewPathContent.setVisibility(View.GONE);
        buttonConfirmAndStartPath.setEnabled(false); // Disable start button on error
    }

    private void showContentState(LearningPathProposal proposal) {
        showLoadingState(false);
        textViewPathError.setVisibility(View.GONE);
        scrollViewPathContent.setVisibility(View.VISIBLE);
        buttonConfirmAndStartPath.setEnabled(true);

        // Populate Summary
        // Using Html.fromHtml for bolding
        summaryTargetTextView.setText(Html.fromHtml("<b>Mục tiêu:</b> " + proposal.getTargetAchieved(), Html.FROM_HTML_MODE_LEGACY));
        summaryDurationTextView.setText(Html.fromHtml("<b>Thời gian:</b> " + proposal.getDurationForTarget(), Html.FROM_HTML_MODE_LEGACY));
        summaryLessonsPerDayTextView.setText(Html.fromHtml("<b>Số bài học mỗi ngày (dự kiến):</b> " + proposal.getLessonsPerDay() + " bài", Html.FROM_HTML_MODE_LEGACY));
        summaryTimePerDayTextView.setText(Html.fromHtml("<b>Thời lượng học mỗi ngày (dự kiến):</b> " + proposal.getStudyTimePerDay(), Html.FROM_HTML_MODE_LEGACY));

        // Populate Topic List
        topicListContainer.removeAllViews(); // Clear previous topics if any (e.g., on retry)
        // Add the title "Các chủ đề/bài học chính:" back if it was part of the static layout
        // Or add it dynamically here:
        TextView topicsTitle = new TextView(this);
        topicsTitle.setText("Các chủ đề/bài học chính:");
        // You might want to apply R.style.TopicListContainerTitle style programmatically or define it
        topicsTitle.setTextAppearance(R.style.TopicListContainerTitle); // If TopicListContainerTitle is a TextAppearance
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, (int) (10 * getResources().getDisplayMetrics().density)); // 10dp margin bottom
        topicsTitle.setLayoutParams(titleParams);
        topicListContainer.addView(topicsTitle);


        if (proposal.getTopics() != null && !proposal.getTopics().isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            for (String topic : proposal.getTopics()) {
                // Inflate suggested_topic_item.xml or create TextView programmatically
                // For simplicity, creating TextView programmatically:
                TextView topicView = new TextView(this);
                topicView.setText(topic);
                // Apply R.style.SuggestedTopicText programmatically
                // This is harder for complex styles. Inflating a layout is usually better.
                topicView.setTextAppearance(R.style.SuggestedTopicText); // If SuggestedTopicText is a TextAppearance
                topicView.setBackgroundResource(R.drawable.suggested_topic_background);
                topicView.setPadding((int) (10 * getResources().getDisplayMetrics().density),
                        (int) (10 * getResources().getDisplayMetrics().density),
                        (int) (10 * getResources().getDisplayMetrics().density),
                        (int) (10 * getResources().getDisplayMetrics().density));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, (int) (8 * getResources().getDisplayMetrics().density)); // 8dp margin bottom
                topicView.setLayoutParams(params);
                topicListContainer.addView(topicView);
            }
        } else {
            TextView noTopicsView = new TextView(this);
            noTopicsView.setText("Không có chủ đề cụ thể nào được đề xuất.");
            topicListContainer.addView(noTopicsView);
        }

        currentPathId = proposal.getPathId(); // Store the path ID
    }


    private void fetchPathProposal(String targetScore, String studyDuration, int hoursPerWeek) {
        showLoadingState(true);
        LearningPathRequest request = new LearningPathRequest(targetScore, studyDuration, hoursPerWeek);

        learningPathService.getLearningPathProposal(request).enqueue(new Callback<LearningPathProposal>() {
            @Override
            public void onResponse(@NonNull Call<LearningPathProposal> call, @NonNull Response<LearningPathProposal> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showContentState(response.body());
                } else {
                    // Handle API error (e.g., no path found, server error)
                    // You might want to parse an error body if your API provides one
                    showNoPathFoundDialog(); // Show specific dialog for this case
                }
            }

            @Override
            public void onFailure(@NonNull Call<LearningPathProposal> call, @NonNull Throwable t) {
                showErrorState("Lỗi mạng: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void showNoPathFoundDialog() {
        showLoadingState(false); // Hide progress bar
        scrollViewPathContent.setVisibility(View.GONE); // Hide content scroll view
        textViewPathError.setVisibility(View.GONE); // Hide general error text view

        // Show specific dialog for "No Path Found"
        new AlertDialog.Builder(this)
                .setTitle("Không tìm thấy lộ trình phù hợp")
                .setMessage("Với mục tiêu và thời gian bạn chọn, hệ thống chưa thể tạo lộ trình tối ưu. Vui lòng thử:\n\n" +
                        "• Điều chỉnh mục tiêu điểm số thấp hơn.\n" +
                        "• Tăng thời gian học hoặc số giờ học mỗi tuần.")
                .setPositiveButton("Điều chỉnh lại", (dialog, which) -> {
                    finish(); // Go back to ProgressSetup to adjust inputs
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    dialog.dismiss();
                    // Optional: Navigate to a different screen or just stay
                })
                .setCancelable(false) // User must interact with dialog
                .show();
        buttonConfirmAndStartPath.setEnabled(false);
    }

    private void confirmAndStartLearningPath(String pathIdToConfirm, LearningPathProposal learningPathProposal) {
        showLoadingState(true); // Show loading indicator

        // 1. Get User ID (from SharedPreferences or your auth manager)
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE); // Use a consistent name
        String userId = prefs.getString("user_id", null);

        if (userId == null) {
            showLoadingState(false);
            Toast.makeText(this, "Lỗi: Không thể xác định người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            // Optionally, navigate to login screen
            return;
        }

        ConfirmPathRequest confirmRequest = new ConfirmPathRequest(userId, pathIdToConfirm);

        learningPathService.confirmLearningPath(confirmRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                showLoadingState(false);
                if (response.isSuccessful()) {
                    // Path confirmed and saved successfully on the backend
                    Toast.makeText(LearningPathProposalActivity.this, "Lộ trình học đã được lưu!", Toast.LENGTH_LONG).show();

                    // TODO: Save essential path info LOCALLY (SharedPreferences or local DB)
                    // This helps the TrackProgressActivity load the current path info quickly
                    // without always needing an internet connection for basic display.
                    saveCurrentPathInfoLocally(pathIdToConfirm, learningPathProposal);


                    // Navigate to the next screen (TrackProgressActivity)
                    Intent intent = new Intent(LearningPathProposalActivity.this, TrackProgressActivity.class);
                    // Pass data that TrackProgressActivity might need immediately
                    intent.putExtra("ACTIVE_PATH_ID", pathIdToConfirm);
                    // You could also pass the entire 'proposalDetails' if it's Serializable/Parcelable
                    // and TrackProgressActivity can use it to display initial data while fetching fresh data.
                    // intent.putExtra("INITIAL_PATH_PROPOSAL", proposalDetails); // Make LearningPathProposal Serializable

                    startActivity(intent);
                    finishAffinity(); // Finish this and previous path setup activities
                } else {
                    // Handle API error for confirming the path
                    Toast.makeText(LearningPathProposalActivity.this, "Lỗi khi lưu lộ trình. Mã: " + response.code(), Toast.LENGTH_LONG).show();
                    // You might want to parse an error body from response.errorBody()
                }
            }



            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                showLoadingState(false);
                Toast.makeText(LearningPathProposalActivity.this, "Lỗi mạng khi lưu lộ trình: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
    private void saveCurrentPathInfoLocally(String pathId, LearningPathProposal proposal) {
        SharedPreferences prefs = getSharedPreferences("user_active_path", MODE_PRIVATE); // Specific prefs file for active path
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("current_path_id", pathId);
        editor.putString("path_target", proposal.getTargetAchieved());
        editor.putString("path_duration", proposal.getDurationForTarget());
        editor.putInt("path_lessons_per_day", proposal.getLessonsPerDay());
        editor.putString("path_time_per_day", proposal.getStudyTimePerDay());
        // You might want to save the topics list as a JSON string if needed for offline display
        // Gson gson = new Gson();
        // String topicsJson = gson.toJson(proposal.getTopics());
        // editor.putString("path_topics_json", topicsJson);
        editor.apply();
        Log.d("PathSave", "Current path info saved locally. ID: " + pathId);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle toolbar back arrow click
            // If this screen was launched after selections, going back might mean re-selecting
            // So, finish() might be appropriate, or navigate to a specific screen
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}