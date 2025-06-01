package nlu.fit.studyappr.activity.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.activity.grammar.GrammarTopicLearn;
import nlu.fit.studyappr.activity.grammar.GrammarTopicReview;
import nlu.fit.studyappr.activity.practice.PracticeList;
import nlu.fit.studyappr.activity.progress.ProgressSetup;
import nlu.fit.studyappr.activity.vocabulary.VocabularyLearnSetupActivity;
import nlu.fit.studyappr.activity.vocabulary.VocabularyReviewList;
import nlu.fit.studyappr.adapter.home.LearningOptionAdapter;
import nlu.fit.studyappr.model.LearningOption;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvLearningOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Gắn layout ở đây

        
        rvLearningOptions = findViewById(R.id.rvLearningOptions);

        List<LearningOption> optionList = new ArrayList<LearningOption>();
        optionList.add(new LearningOption("Học Ngữ Pháp", R.drawable.book));
        optionList.add(new LearningOption("Ôn Tập Ngữ Pháp", R.drawable.ic_review));
        optionList.add(new LearningOption("Học Từ Vựng", R.drawable.ic_vocabulary));
        optionList.add(new LearningOption("Ôn tập Từ Vựng", R.drawable.ic_vocabulary));
        optionList.add(new LearningOption("Luyện Theo Dạng Bài", R.drawable.ic_test));
        optionList.add(new LearningOption("Thi Thử", R.drawable.ic_test));
        optionList.add(new LearningOption("Lộ Trình Học", R.drawable.ic_journey));

        LearningOptionAdapter adapter = new LearningOptionAdapter(this, optionList);
        rvLearningOptions.setLayoutManager(new GridLayoutManager(this, 2));
        rvLearningOptions.setAdapter(adapter);

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
                    startActivity(new Intent(this, ProgressSetup.class));
                    break;
            }
        });
    }
}