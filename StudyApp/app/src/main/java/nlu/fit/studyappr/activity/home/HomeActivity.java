package nlu.fit.studyappr.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.activity.grammar.GrammarTopicLearn;
import nlu.fit.studyappr.activity.grammar.GrammarTopicReview;
import nlu.fit.studyappr.activity.practice.PracticeList;
import nlu.fit.studyappr.activity.profile.SettingsActivity;
import nlu.fit.studyappr.activity.progress.DailyDetailsActivity;
import nlu.fit.studyappr.activity.progress.ProgressSetup;
import nlu.fit.studyappr.activity.vocabulary.DictionaryLookupActivity;
import nlu.fit.studyappr.activity.vocabulary.VocabularyLearnSetupActivity;
import nlu.fit.studyappr.activity.vocabulary.VocabularyReviewList;
import nlu.fit.studyappr.adapter.home.LearningOptionAdapter;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.api.learningpath.LearningPathApiService;
import nlu.fit.studyappr.api.learningpath.ProgressApiService;
import nlu.fit.studyappr.model.learningProgress.LearningOption;
import nlu.fit.studyappr.model.learningProgress.UserLearningPath;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvLearningOptions;
private ImageView imageViewSettng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Gắn layout ở đây

        imageViewSettng=findViewById(R.id.ivProfileIcon);
        rvLearningOptions = findViewById(R.id.rvLearningOptions);
        TextView tvGreeting = findViewById(R.id.tvGreeting);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            if (displayName == null || displayName.isEmpty()) {
                displayName = "Người dùng"; // fallback nếu user không có tên hiển thị
            }
            tvGreeting.setText("Xin chào, " + displayName + "!");
        } else {
            tvGreeting.setText("Xin chào!");
        }

        List<LearningOption> optionList = new ArrayList<LearningOption>();
        optionList.add(new LearningOption("Học Ngữ Pháp", R.drawable.book));
        optionList.add(new LearningOption("Ôn Tập Ngữ Pháp", R.drawable.ic_review));
        optionList.add(new LearningOption("Học Từ Vựng", R.drawable.ic_vocabulary));
        optionList.add(new LearningOption("Ôn tập Từ Vựng", R.drawable.ic_vocabulary));
        optionList.add(new LearningOption("Luyện Theo Dạng Bài", R.drawable.ic_test));
        optionList.add(new LearningOption("Thi Thử", R.drawable.ic_test));
        optionList.add(new LearningOption("Lộ Trình Học", R.drawable.ic_journey));
        optionList.add(new LearningOption("Tra cứu từ vựng", R.drawable.ic_vocabulary)); // Bổ sung dòng này

        LearningOptionAdapter adapter = new LearningOptionAdapter(this, optionList);
        rvLearningOptions.setLayoutManager(new GridLayoutManager(this, 2));
        rvLearningOptions.setAdapter(adapter);
        imageViewSettng.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class); // hoặc HomeActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        adapter.setOnItemClickListener(option -> {
            switch (option.getTitle()) {
                case "Học Ngữ Pháp":
                    startActivity(new Intent(this, GrammarTopicLearn.class));
                    break;
                case "Ôn Tập Ngữ Pháp":
                    startActivity(new Intent(this, GrammarTopicReview.class));
                    break;
                case "Học Từ Vựng":
                    startActivity(new Intent(this, VocabularyLearnSetupActivity.class));
                    break;
                case "Ôn tập Từ Vựng":
                    startActivity(new Intent(this, VocabularyReviewList.class));
                    break;
                case "Thi Thử":
                    startActivity(new Intent(this, PracticeList.class));
                    break;
                case "Lộ Trình Học":
                    checkLearningPathAndNavigate();
                    break;
                case "Tra cứu từ vựng":
                    startActivity(new Intent(this, DictionaryLookupActivity.class));
                    break;

            }
        });
    }
    private void checkLearningPathAndNavigate() {

        String userId = "2";

        LearningPathApiService api = InitializeRetrofit.getInstance().create(LearningPathApiService.class);
        api.getUserLearningPathActive(userId).enqueue(new Callback<UserLearningPath>() {
            @Override
            public void onResponse(Call<UserLearningPath> call, Response<UserLearningPath> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Có lộ trình active
                    Intent intent = new Intent(HomeActivity.this, DailyDetailsActivity.class);
                    startActivity(intent);
                } else {
                    // Không có lộ trình active
                    Intent intent = new Intent(HomeActivity.this, ProgressSetup.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<UserLearningPath> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Không thể kiểm tra trạng thái lộ trình", Toast.LENGTH_SHORT).show();
            }
        });
    }

}