package nlu.fit.studyappr.activity.progress; // Your package

import android.content.SharedPreferences;
import android.os.Bundle;
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
    // private LinearLayout dailyDetailsContentLayout; // Optional: to group RecyclerView for visibility

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
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String userId = prefs.getString("user_id", null);

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
                    List<DailyProgressEntry> dailyEntries = response.body();
                    if (dailyEntries.isEmpty()) {
                        showErrorState("Không có hoạt động nào được ghi nhận.");
                    } else {
                        adapter = new DailyDetailsAdapter(DailyDetailsActivity.this, dailyEntries);
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
        }
        return super.onOptionsItemSelected(item);
    }
}