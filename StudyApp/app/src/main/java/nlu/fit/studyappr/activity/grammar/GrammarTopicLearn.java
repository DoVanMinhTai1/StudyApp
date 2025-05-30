package nlu.fit.studyappr.activity.grammar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.adapter.GrammarTopicAdapter;
import nlu.fit.studyappr.api.grammar.GrammarApiService;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.model.GrammarTopic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrammarTopicLearn extends AppCompatActivity {
    GrammarApiService grammarApiService;
    List<GrammarTopic> grammarTopics;

    GrammarTopicAdapter grammarTopicAdapter;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_grammar_learn_topic_list);

        listView = findViewById(R.id.listview);

        grammarApiService = InitializeRetrofit.getInstance().create(GrammarApiService.class);
        Call<List<GrammarTopic>> call = grammarApiService.getAllGrammarTopics();
        call.enqueue(new Callback<List<GrammarTopic>>() {
            @Override
            public void onResponse(Call<List<GrammarTopic>> call, Response<List<GrammarTopic>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    grammarTopics = response.body();
                    grammarTopicAdapter = new GrammarTopicAdapter(GrammarTopicLearn.this, grammarTopics);
                    listView.setAdapter(grammarTopicAdapter);
                } else {
                    Toast.makeText(GrammarTopicLearn.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<GrammarTopic>> call, Throwable t) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GrammarTopic clickedTopic = grammarTopics.get(position);
                System.out.println(clickedTopic);
                Toast.makeText(GrammarTopicLearn.this, "Bạn đã chọn: " + clickedTopic.getTitle(), Toast.LENGTH_SHORT).show();
                if (clickedTopic.getGrammarLesson() != null) {
                    Long idG = clickedTopic.getGrammarLesson().getId();
                    Intent intent = new Intent(GrammarTopicLearn.this, GrammarDetailActivity.class);
                    intent.putExtra("grammarLessonId", idG);
                    startActivity(intent);
                } else {
                    Toast.makeText(GrammarTopicLearn.this, "No lesson linked to this topic", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
