package nlu.fit.studyappr.activity.progress;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.appbar.MaterialToolbar;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.api.learningpath.LearningPathApiService;
import nlu.fit.studyappr.model.learningProgress.ChartDataPoint;
import nlu.fit.studyappr.model.learningProgress.UserLearningPath;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackProgressActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView statLearnedVocabValue, overallProgressBarText;
    private ProgressBar overallProgressBar;

    private String activePathId;

    private LearningPathApiService learningPathApiService;

    private UserLearningPath userLearningPath;

    private LineChart weeklyProgressChart;

    private TextView chartPlaceHolderText;

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
        weeklyProgressChart = findViewById(R.id.weeklyProgressChart);
        chartPlaceHolderText = findViewById(R.id.chartPlaceholderText);

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

        FrameLayout chartContainer = findViewById(R.id.chartContainer);
        // You might need to inflate or add the LineChart programmatically if it's not directly in XML,
        // or ensure your XML has <com.github.mikephil.charting.charts.LineChart .../>
        // For now, let's assume it's directly in the XML:
    }

    private void loadAndDisplayProgressData(String pathId) {
        // 1. Display any locally saved quick-view data from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_active_path", MODE_PRIVATE);
        String pathTarget = prefs.getString("path_target", "N/A");
        // You can display this somewhere if needed, e.g., a subtitle

        // This API would return data like:
        // {
        //   "learnedVocab": 150,
        //   "exercisesDone": 35,
        //   "exercisesCompleted": 30,
        //   "pathCompletionPercentage": 45,
        //   "weeklyProgressDataForChart": [ ... ]
        // }

        String userId = "2";
        learningPathApiService = InitializeRetrofit.getInstance().create(LearningPathApiService.class);
        learningPathApiService.getUserLearningPathActive(userId).enqueue(new Callback<UserLearningPath>() {
            @Override
            public void onResponse(Call<UserLearningPath> call, Response<UserLearningPath> response) {
                if (response.isSuccessful()) {
                    userLearningPath = response.body();
                    // For now, using mock data as in your HTML's JS
                    int learnedVocab = 125;
                    int exercisesDone = 30;
                    int exercisesCompleted = 25;
                    int pathCompletionPercentage = 40; // Example
                    // ... set text for other stat TextViews ...
                    TextView statExercisesDone = findViewById(R.id.statExercisesDoneValue);
                    TextView statExercisesCompleted = findViewById(R.id.statExercisesCompletedValue);
                    TextView statPathCompletion = findViewById(R.id.statPathCompletionValue);

                    statLearnedVocabValue.setText(String.valueOf(userLearningPath.getProgressStats().getLearnedVocab()));
                    statExercisesDone.setText(String.valueOf(userLearningPath.getProgressStats().getTotalLessons()));
                    statExercisesCompleted.setText(String.valueOf(userLearningPath.getProgressStats().getLessonsCompleted()));
                    statPathCompletion.setText(userLearningPath.getProgressStats().getCompletionPercentage() + "%");
                    int progressPercent = (int) userLearningPath.getProgressStats().getCompletionPercentage();
                    overallProgressBar.setProgress(progressPercent);
                    overallProgressBarText.setText(userLearningPath.getProgressStats().getCompletionPercentage() + "%");

                    List<ChartDataPoint> chartDataPoints = new ArrayList<>();
                    if (weeklyProgressChart != null && chartDataPoints != null && !chartDataPoints.isEmpty()) {
                        chartPlaceHolderText.setVisibility(View.GONE);
                        weeklyProgressChart.setVisibility(View.VISIBLE);
                        setupLineChart(chartDataPoints);
                    }

                }
            }

            private void setupLineChart(List<ChartDataPoint> chartDataPoints) {
                Collections.sort(chartDataPoints, Comparator.comparing(ChartDataPoint::getDate));

                List<Entry> entries = new ArrayList<>();
                final List<String> xAxisLabels = new ArrayList<>();

                for (int i = 0; i < chartDataPoints.size(); i++) {
                    ChartDataPoint point = chartDataPoints.get(i);
                    // X value is the index, Y value is itemsCompletedToday
                    entries.add(new Entry(i, point.getItemsCompletedToday()));
                    // Format LocalDate to a string for X-axis label (e.g., "Oct 27")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        xAxisLabels.add(point.getDate().format(String.valueOf(DateTimeFormatter.ofPattern("MMM dd"))));
                    }
                }
                if (entries.isEmpty()) {
                    weeklyProgressChart.clear();
                    weeklyProgressChart.invalidate(); // Refresh chart
                    chartPlaceHolderText.setVisibility(View.VISIBLE);
                    weeklyProgressChart.setVisibility(View.GONE);
//                    chartPlaceHolderText.setText("Không có dữ liệu cho biểu đồ.");
                    return;
                }

                LineDataSet dataSet = new LineDataSet(entries, "Bài học hoàn thành"); // Label for the legend
                // --- Customize dataSet (colors, line thickness, circles, etc.) ---
                dataSet.setColor(getResources().getColor(R.color.chartLineColor)); // Define chartLineColor in colors.xml
                dataSet.setValueTextColor(getResources().getColor(R.color.chartValueColor)); // Define chartValueColor
                dataSet.setCircleColor(getResources().getColor(R.color.chartCircleColor)); // Define chartCircleColor
                dataSet.setCircleRadius(4f);
                dataSet.setDrawCircleHole(false);
                dataSet.setLineWidth(2f);
                dataSet.setValueTextSize(10f);
                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Makes the line smooth
                dataSet.setDrawValues(true); // Show values on data points

                LineData lineData = new LineData(dataSet);
                weeklyProgressChart.setData(lineData);

                // --- Customize Chart Appearance ---
                weeklyProgressChart.getDescription().setEnabled(false); // No description text
                weeklyProgressChart.setDrawGridBackground(false);
                weeklyProgressChart.setTouchEnabled(true);
                weeklyProgressChart.setDragEnabled(true);
                weeklyProgressChart.setScaleEnabled(true);
                weeklyProgressChart.setPinchZoom(true);

                // --- X-Axis Customization ---
                XAxis xAxis = weeklyProgressChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1f); // Minimum interval between axis values
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, com.github.mikephil.charting.components.AxisBase axis) {
                        int index = (int) value;
                        if (index >= 0 && index < xAxisLabels.size()) {
                            return xAxisLabels.get(index);
                        }
                        return "";
                    }
                });

                // --- Y-Axis Customization (Left) ---
                YAxis leftAxis = weeklyProgressChart.getAxisLeft();
                leftAxis.setDrawGridLines(true); // Horizontal grid lines
                leftAxis.setAxisMinimum(0f); // Start Y-axis from 0
                // Optional: Set a max value or let it be dynamic
                // leftAxis.setAxisMaximum(determineMaxYValue(entries) + buffer);
                leftAxis.setGranularity(1f); // Ensure Y-axis labels are integers if itemsCompletedToday is int

                // --- Y-Axis Customization (Right) ---
                weeklyProgressChart.getAxisRight().setEnabled(false); // Disable right Y-axis

                // --- Legend ---
                weeklyProgressChart.getLegend().setEnabled(true); // Show legend

                // Refresh the chart
                weeklyProgressChart.animateX(1000); // Animate X-axis drawing
                // weeklyProgressChart.invalidate(); // Or just invalidate if no animation needed
            }

            @Override
            public void onFailure(Call<UserLearningPath> call, Throwable t) {

            }
        });


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