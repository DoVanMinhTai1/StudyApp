package nlu.fit.studyappr.activity.grammar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.adapter.grammar.GrammarTopicAdapter;
import nlu.fit.studyappr.api.grammar.GrammarApiService;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.model.grammar.GrammarTopic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrammarTopicReview extends AppCompatActivity {
    GrammarApiService grammarApiService;
    List<GrammarTopic> grammarTopics;

    GrammarTopicAdapter grammarTopicAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_review_select_topic);

        recyclerView = findViewById(R.id.reviewTopicsRecyclerView1);

        grammarApiService = InitializeRetrofit.getInstance().create(GrammarApiService.class);
        Call<List<GrammarTopic>> call = grammarApiService.getAllGrammarTopics();
        call.enqueue(new Callback<List<GrammarTopic>>() {
            @Override
            public void onResponse(Call<List<GrammarTopic>> call, Response<List<GrammarTopic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    grammarTopics = response.body();
                    grammarTopicAdapter = new GrammarTopicAdapter(GrammarTopicReview.this, grammarTopics, v -> {
                        if (v.getGrammarLesson() != null) {
                            Long idG = v.getGrammarLesson().getId();
                            Intent intent = new Intent(GrammarTopicReview.this, GrammarExerciseActivity.class);
                            intent.putExtra("grammarTopicId",v.getId());
                            intent.putExtra("grammarLessonId", idG);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(grammarTopicAdapter);
                } else {
                    Toast.makeText(GrammarTopicReview.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<GrammarTopic>> call, Throwable t) {
                Toast.makeText(GrammarTopicReview.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
