package nlu.fit.studyappr.adapter.grammar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.HashMap; // For storing user answers

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.model.GrammarExcerciseQuestion;

public class GrammarQuestionReviewAdapter extends RecyclerView.Adapter<GrammarQuestionReviewAdapter.QuizQuestionViewHolder>  {

    private List<GrammarExcerciseQuestion> questionList;
    private LayoutInflater inflater;
    private Context context;
    // To store user's selected answer for each question (position -> selectedOption e.g., "A", "B")
    private HashMap<Integer, String> userAnswers;
    private OnAnswerSelectedListener answerSelectedListener;


    // Interface to notify when an answer is selected
    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int questionPosition, String selectedOption);
    }

    public GrammarQuestionReviewAdapter(Context context, List<GrammarExcerciseQuestion> questionList, OnAnswerSelectedListener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.questionList = questionList;
        this.userAnswers = new HashMap<>();
        this.answerSelectedListener = listener;
    }

    @NonNull
    @Override
    public QuizQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activity_grammar_list_item_grammar_question_review_card, parent, false); // Use the new layout
        return new QuizQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizQuestionViewHolder holder, int position) {
        GrammarExcerciseQuestion currentQuestion = questionList.get(position);

        holder.questionText.setText((position + 1) + ". " + currentQuestion.getQuestionText());

        holder.optionA.setText(currentQuestion.getOptionA());
        holder.optionB.setText(currentQuestion.getOptionB());
        holder.optionC.setText(currentQuestion.getOptionC());
        holder.optionD.setText(currentQuestion.getOptionD());

        // Clear previous listener to avoid multiple triggers
        holder.optionsGroup.setOnCheckedChangeListener(null);
        // Clear checked state before binding new data or restoring
        holder.optionsGroup.clearCheck();


        // Restore selected answer if exists for this question position
        String previousAnswer = userAnswers.get(position); // Get answer by position
        if (previousAnswer != null) {
            switch (previousAnswer) {
                case "A":
                    holder.optionA.setChecked(true);
                    break;
                case "B":
                    holder.optionB.setChecked(true);
                    break;
                case "C":
                    holder.optionC.setChecked(true);
                    break;
                case "D":
                    holder.optionD.setChecked(true);
                    break;
            }
        }


        // Listener for RadioGroup selection changes
        holder.optionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String selectedOption = "";
                if (checkedId == R.id.optionA) {
                    selectedOption = "A";
                } else if (checkedId == R.id.optionB) {
                    selectedOption = "B";
                } else if (checkedId == R.id.optionC) {
                    selectedOption = "C";
                } else if (checkedId == R.id.optionD) {
                    selectedOption = "D";
                }

                if (!selectedOption.isEmpty()) {
                    userAnswers.put(holder.getAdapterPosition(), selectedOption); // Store answer by adapter position
                    if (answerSelectedListener != null) {
                        answerSelectedListener.onAnswerSelected(holder.getAdapterPosition(), selectedOption);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList != null ? questionList.size() : 0;
    }

    // Method to get all user answers (e.g., when submitting the quiz)
    public HashMap<Integer, String> getUserAnswers() {
        return userAnswers;
    }

    // Method to update a specific question's user answer (if loaded from saved state)
    public void setUserAnswerForQuestion(int questionPosition, String answer) {
        userAnswers.put(questionPosition, answer);
        // No need to notifyItemChanged here unless you want to visually update
        // the radio button immediately, which onBindViewHolder will handle
        // when the view is recycled or bound.
    }


    static class QuizQuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionText;
        RadioGroup optionsGroup;
        RadioButton optionA, optionB, optionC, optionD;

        public QuizQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionTextView);
            optionsGroup = itemView.findViewById(R.id.optionsRadioGroup);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
        }
    }
}