package nlu.fit.studyappr.activity.progress; // Your package

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.adapter.progress.DailyDetailsAdapter;
import nlu.fit.studyappr.api.learningpath.ProgressApiService; // Your new API service
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.model.learningProgress.DailyProgressEntry;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyDetailsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView dailyDetailsRecyclerView;
    private DailyDetailsAdapter adapter;
    private ProgressApiService progressApiService;

    private ProgressBar progressBarDailyDetails;
    private TextView textViewDailyDetailsError;

    List<DailyProgressEntry> dailyEntries;
    private final String TOPIC_TYPE_DATE = "DATE";

    private final String TOPIC_TYPE_VOCABULARY = "VOCABULARY";

    private final String TOPIC_TYPE_GRAMMAR = "GRAMMAR";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_daily_details);

        toolbar = findViewById(R.id.toolbar_daily_details);
        dailyDetailsRecyclerView = findViewById(R.id.dailyDetailsRecyclerView);
        // Initialize ProgressBar and Error TextView if you add them to activity_daily_details.xml
        // progressBarDailyDetails = findViewById(R.id.progressBarDailyDetails);
        // textViewDailyDetailsError = findViewById(R.id.textViewDailyDetailsError);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // For back arrow
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // Change app:navigationIcon in XML to @drawable/ic_arrow_back for this to work
        }


        dailyDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressApiService = InitializeRetrofit.getInstance().create(ProgressApiService.class);

        // Get userId (from SharedPreferences or Intent)
//        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
//        String userId = prefs.getString("user_id", null);

        String userId = "2";
        if (userId != null) {
            fetchDailyDetails(userId);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy User ID.", Toast.LENGTH_LONG).show();
            // Handle error, maybe finish activity
            showErrorState("Không thể tải dữ liệu người dùng.");
        }
    }

    private void showLoadingState(boolean isLoading) {
        // if (progressBarDailyDetails != null) progressBarDailyDetails.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        // if (dailyDetailsRecyclerView != null) dailyDetailsRecyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        // if (textViewDailyDetailsError != null) textViewDailyDetailsError.setVisibility(View.GONE);
        Toast.makeText(this, isLoading ? "Đang tải chi tiết..." : "", Toast.LENGTH_SHORT).show(); // Simple loading
    }

    private void showErrorState(String message) {
        showLoadingState(false);
        // if (textViewDailyDetailsError != null) {
        //     textViewDailyDetailsError.setText(message);
        //     textViewDailyDetailsError.setVisibility(View.VISIBLE);
        // }
        // if (dailyDetailsRecyclerView != null) dailyDetailsRecyclerView.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void fetchDailyDetails(String userId) {
        showLoadingState(true);
        progressApiService.getDailyProgressDetails(userId).enqueue(new Callback<List<DailyProgressEntry>>() {
            @Override
            public void onResponse(@NonNull Call<List<DailyProgressEntry>> call, @NonNull Response<List<DailyProgressEntry>> response) {
                showLoadingState(false);
                if (response.isSuccessful() && response.body() != null) {
                   dailyEntries = response.body();
                    if (dailyEntries.isEmpty()) {
                        showErrorState("Không có hoạt động nào được ghi nhận.");
                    } else {
                        adapter = new DailyDetailsAdapter(DailyDetailsActivity.this, new ArrayList<>(dailyEntries), TOPIC_TYPE_DATE);
                        dailyDetailsRecyclerView.setAdapter(adapter);
                        // if (dailyDetailsRecyclerView != null) dailyDetailsRecyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    showErrorState("Lỗi tải dữ liệu chi tiết: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DailyProgressEntry>> call, @NonNull Throwable t) {
                showLoadingState(false);
                showErrorState("Lỗi mạng: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Go back to the previous activity
            return true;
        } else if (item.getItemId() == R.id.action_progress_setup) {
            Intent intent = new Intent(DailyDetailsActivity.this, ProgressSetup.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_filter_date) {
            applyTopicTypeFilter(TOPIC_TYPE_DATE);
            toolbar.setTitle("Chi tiết học theo ngày");
            return true;
        } else if (item.getItemId() == R.id.action_filter_topic_type_vocab) {
            toolbar.setTitle("Chi tiết học theo từ vựng");
            applyTopicTypeFilter(TOPIC_TYPE_VOCABULARY);
            return true;
        } else if (item.getItemId() == R.id.action_filter_topic_type_grammar) {
            toolbar.setTitle("Chi tiết học theo ngữ pháp");
            applyTopicTypeFilter(TOPIC_TYPE_GRAMMAR);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void applyTopicTypeFilter(String topicType) {
        if (adapter != null) {
            // The adapter needs to be aware of the master list and the filter
            adapter.updateData(new ArrayList<>(dailyEntries), topicType);
        } else if (!dailyEntries.isEmpty()){
            // If adapter was not yet created but data is loaded
            adapter = new DailyDetailsAdapter(DailyDetailsActivity.this, new ArrayList<>(dailyEntries), topicType);
            dailyDetailsRecyclerView.setAdapter(adapter);
        }
        Toast.makeText(this, "Lọc theo: " + topicType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_daily_details, menu);
        return true;
    }
}