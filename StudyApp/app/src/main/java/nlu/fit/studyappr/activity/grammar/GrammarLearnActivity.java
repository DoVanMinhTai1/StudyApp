package nlu.fit.studyappr.activity.grammar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import nlu.fit.studyappr.activity.home.HomeActivity;
import nlu.fit.studyappr.R;
import nlu.fit.studyappr.api.grammar.GrammarApiService;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.model.GrammarLesson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrammarLearnActivity extends AppCompatActivity {

    GrammarApiService grammarApiService;
    GrammarLesson grammarLesson;

    Button buttonExit;

    Button completeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grammar_learn_detail);


        Long id = getIntent().getLongExtra("grammarLessonId", -1);
        grammarApiService = InitializeRetrofit.getInstance().create(GrammarApiService.class);
        Call<GrammarLesson> call = grammarApiService.getGrammarLessonById(id);
        call.enqueue(new Callback<GrammarLesson>() {
            @Override
            public void onResponse(Call<GrammarLesson> call, Response<GrammarLesson> response) {
                if (response.isSuccessful() && response.body() != null) {
                    grammarLesson = response.body();


                } else {
                    Toast.makeText(GrammarLearnActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GrammarLesson> call, Throwable t) {
                Toast.makeText(GrammarLearnActivity.this, "Lỗi khi tải dữ liệu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

        buttonExit = findViewById(R.id.exitButton);
        completeButton = findViewById(R.id.completeButton);


        buttonExit.setOnClickListener(v -> {
            showExitConfirmationDialog();

        });

//        status Đã Học
        completeButton.setOnClickListener(v -> {
            Intent intent = new Intent(GrammarLearnActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận thoát")
                .setMessage("Bạn có chắc muốn thoát không? Tiến trình học sẽ bị mất.")
                .setPositiveButton("Thoát", (dialog, which) -> {
                    Intent intent = new Intent(GrammarLearnActivity.this, HomeActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
