package nlu.fit.studyappr.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.model.AnswerSubmissionRequest;
import nlu.fit.studyappr.model.GrammarExcerciseQuestion;

public class GrammarQuestionAdapter extends RecyclerView.Adapter<GrammarQuestionAdapter.ViewHolder> {

    private final List<GrammarExcerciseQuestion> questionList;
    private  List<AnswerSubmissionRequest> answerSubmissionRequest;

    public GrammarQuestionAdapter(List<GrammarExcerciseQuestion> questionList) {
        this.questionList = questionList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView, explanationTextView;
        RadioButton optionA, optionB, optionC, optionD;
        RadioGroup optionsRadioGroup;
        android.widget.Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            explanationTextView = itemView.findViewById(R.id.explanationTextView);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            optionsRadioGroup = itemView.findViewById(R.id.optionsRadioGroup);
            button = itemView.findViewById(R.id.submitButton);
        }
    }

    @NonNull
    @Override
    public GrammarQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grammar_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrammarQuestionAdapter.ViewHolder holder, int position) {
        GrammarExcerciseQuestion q = questionList.get(position);

        holder.questionTextView.setText(q.getQuestionText());
        holder.optionA.setText(q.getOptionA());
        holder.optionB.setText(q.getOptionB());
        holder.optionC.setText(q.getOptionC());
        holder.optionD.setText(q.getOptionD());

        int selectedId = holder.optionsRadioGroup.getCheckedRadioButtonId();
        if(selectedId != -1) {
            RadioButton radioButton = holder.itemView.findViewById(selectedId);
            q.setUserAnswer(radioButton.getText().toString());
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
}
