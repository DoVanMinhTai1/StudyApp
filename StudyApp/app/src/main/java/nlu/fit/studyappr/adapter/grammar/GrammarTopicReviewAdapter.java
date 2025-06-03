package nlu.fit.studyappr.adapter.grammar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.model.grammar.GrammarTopic;

public class GrammarTopicReviewAdapter extends RecyclerView.Adapter<GrammarTopicReviewAdapter.RecyclerView> {
    List<GrammarTopic> grammarTopics;
    LayoutInflater layoutInflater;

    OnTopicReviewClickListener onTopicReviewClickListener;

    public GrammarTopicReviewAdapter(@NonNull Context context, List<GrammarTopic> grammarTopics, OnTopicReviewClickListener onTopicReviewClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.onTopicReviewClickListener = onTopicReviewClickListener;
    }

    public interface OnTopicReviewClickListener {
        void onTopicReviewClickListener(GrammarTopic grammarTopic);
    }

    @NonNull
    @Override
    public GrammarTopicReviewAdapter.RecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.activity_grammar_list_item_review_topic, parent,false);
            return new GrammarTopicReviewAdapter.RecyclerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrammarTopicReviewAdapter.RecyclerView holder, int position) {
        GrammarTopic grammarTopic = grammarTopics.get(position);
        holder.nameTopics.setText(grammarTopic.getTitle());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

     class RecyclerView extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TextView nameTopics;

        public RecyclerView(@NonNull View itemView) {
            super(itemView);

            nameTopics = itemView.findViewById(R.id.reviewTopicNameTextView);
            nameTopics.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != androidx.recyclerview.widget.RecyclerView.NO_POSITION && onTopicReviewClickListener != null) {
                    GrammarTopic clickedTopic = grammarTopics.get(position);
                    onTopicReviewClickListener.onTopicReviewClickListener(clickedTopic);
                }
            });
        }
    }

}
