package nlu.fit.studyappr.activity.exam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.adapter.exam.ExamAdapter;
import nlu.fit.studyappr.api.exam.ExamApiService;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.model.exam.Exam;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExamAdapter examAdapter;

    private ImageView exit_btn;

    private FloatingActionButton btn_add_exam;
    private List<Exam> examList = new ArrayList<>();
    private static final String TAG = "ExamActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mock_exam);

        recyclerView = findViewById(R.id.mockExamRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        examAdapter = new ExamAdapter(this, examList);
        recyclerView.setAdapter(examAdapter);
        exit_btn=findViewById(R.id.ExitButton);

        btn_add_exam=findViewById(R.id.fab_add);

        btn_add_exam.setOnClickListener(v->{
            Intent intent=new Intent(this, CreateExamActivity.class);
            startActivity(intent);
        });

        exit_btn.setOnClickListener(v->{
            finish();
        });

        fetchExamsFromApi();
    }

    private void fetchExamsFromApi() {
        ExamApiService apiService = InitializeRetrofit.getNodeApiInstance().create(ExamApiService.class);
        Call<List<Exam>> call = apiService.getAllExam();

        call.enqueue(new Callback<List<Exam>>() {
            @Override
            public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    examList.clear();
                    examList.addAll(response.body());
                    examAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Exam>> call, Throwable t) {
                Log.e(TAG, "API call failed", t);
            }
        });
    }
}
