package nlu.fit.studyappr.adapter.exam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.activity.exam.ExamDetailActivity;
import nlu.fit.studyappr.model.exam.Exam;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {
    private List<Exam> examList;

    private Context context;

    public ExamAdapter(Context context, List<Exam> examList) {
        this.context = context;
        this.examList = examList;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam, parent, false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        Exam exam = examList.get(position);
        holder.titleTextView.setText(exam.getTitle());
        holder.statusTextView.setText(exam.getStatus());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ExamDetailActivity.class);
            intent.putExtra("examId", exam.getExamId());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return examList.size();
    }

    public static class ExamViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, statusTextView;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.examTitleTextView);
            statusTextView = itemView.findViewById(R.id.examStatusTextView);
        }
    }
}
