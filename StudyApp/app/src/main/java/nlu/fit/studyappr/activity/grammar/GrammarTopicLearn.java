package nlu.fit.studyappr.activity.grammar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class GrammarTopicLearn extends AppCompatActivity {
    GrammarApiService grammarApiService;
    List<GrammarTopic> grammarTopics;

    GrammarTopicAdapter grammarTopicAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_learn_topic_list);

        recyclerView = findViewById(R.id.topicsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        grammarApiService = InitializeRetrofit.getInstance().create(GrammarApiService.class);
        Call<List<GrammarTopic>> call = grammarApiService.getAllGrammarTopics();
        call.enqueue(new Callback<List<GrammarTopic>>() {
            @Override
            public void onResponse(Call<List<GrammarTopic>> call, Response<List<GrammarTopic>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    grammarTopics = response.body();
                    GrammarTopicAdapter grammarTopicAdapter = new GrammarTopicAdapter(GrammarTopicLearn.this, grammarTopics, topic -> {
                        Toast.makeText(GrammarTopicLearn.this, "Bạn đã chọn: " + topic.getTitle(), Toast.LENGTH_SHORT).show();
                        if (topic.getGrammarLesson() != null) {
                            Long idG = topic.getGrammarLesson().getId();
                            Intent intent = new Intent(GrammarTopicLearn.this, GrammarLearnActivity.class);
                            intent.putExtra("grammarLessonId", idG);
                            startActivity(intent);
                        } else {
                            Toast.makeText(GrammarTopicLearn.this, "No lesson linked to this topic", Toast.LENGTH_SHORT).show();
                        }
                    });
                    recyclerView.setAdapter(grammarTopicAdapter);
                } else {
                    Toast.makeText(GrammarTopicLearn.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<GrammarTopic>> call, Throwable t) {
                Toast.makeText(GrammarTopicLearn.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}
