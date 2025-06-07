package nlu.fit.studyappr.activity.exam;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nlu.fit.studyappr.R;

public class ExamResultActivity extends AppCompatActivity {
    private TextView scoreTextView, correctAnswersTextView;
    private Button other_exam_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock_exam_results);

        scoreTextView = findViewById(R.id.scoreTextView);
        correctAnswersTextView = findViewById(R.id.correctAnswersTextView);
        other_exam_btn=findViewById(R.id.chooseAnotherExamButton);

        other_exam_btn.setOnClickListener(v->{
            finish();
        });



        TextView timeTakenTextView = findViewById(R.id.timeTakenTextView);

        double score = getIntent().getDoubleExtra("score", 0);
        int correct = getIntent().getIntExtra("correct", 0);
        int incorrect = getIntent().getIntExtra("incorrect", 0);
        int total = getIntent().getIntExtra("total", 0);

        scoreTextView.setText("Điểm: " + score);
        correctAnswersTextView.setText("Số câu đúng: " + correct + "/" + total);

        long timeTaken = getIntent().getLongExtra("time", 0);

        String formattedTime = String.format("%02d:%02d", timeTaken / 60, timeTaken % 60);
        timeTakenTextView.setText("Thời gian làm: " + formattedTime);
    }
}
