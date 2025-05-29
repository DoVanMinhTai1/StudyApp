package nlu.fit.studyappr.activity.grammar;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nlu.fit.studyappr.fragment.GrammarExerciseFragment;
import nlu.fit.studyappr.fragment.GrammarExplanationFragment;
import nlu.fit.studyappr.R;
import nlu.fit.studyappr.api.grammar.GrammarApiService;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.model.GrammarLesson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrammarDetailActivity extends AppCompatActivity {

    GrammarApiService grammarApiService;
    GrammarLesson grammarLesson;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_grammar_detail);

            
        
        ListView listView = findViewById(R.id.listViewGrammarDetail);
        TextView textViewExercise = findViewById(R.id.textViewGrammarExercise);
        TextView textViewExplanation = findViewById(R.id.textViewGrammarExplanation);
        ProgressBar loading = findViewById(R.id.loadingSpinner);

        Long id = getIntent().getLongExtra("grammarLessonId", -1);
        loading.setVisibility(View.VISIBLE);
        grammarApiService = InitializeRetrofit.getInstance("123").create(GrammarApiService.class);
        Call<GrammarLesson> call = grammarApiService.getGrammarLessonById(id);
        call.enqueue(new Callback<GrammarLesson>() {
            @Override
            public void onResponse(Call<GrammarLesson> call, Response<GrammarLesson> response) {
                loading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    grammarLesson = response.body();

                    textViewExplanation.setEnabled(true);
                    textViewExercise.setEnabled(true);

                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                                    GrammarExplanationFragment.newInstance(grammarLesson))
                            .commitAllowingStateLoss();


                } else {
                    Toast.makeText(GrammarDetailActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GrammarLesson> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Toast.makeText(GrammarDetailActivity.this, "Lỗi khi tải dữ liệu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

        textViewExplanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grammarLesson != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                            GrammarExplanationFragment.newInstance(grammarLesson)).commit();
                }
            }
        });

        textViewExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (grammarLesson != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,
                                    GrammarExerciseFragment.newInstance(grammarLesson))
                            .commit();
                }
            }
        });


    }
}
