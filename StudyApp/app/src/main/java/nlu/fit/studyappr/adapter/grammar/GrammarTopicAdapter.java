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
import nlu.fit.studyappr.model.GrammarTopic;

public class GrammarTopicAdapter extends RecyclerView.Adapter<GrammarTopicAdapter.GrammarTopicViewHolder> {

    private List<GrammarTopic> nameTopic;
    private LayoutInflater layoutInflater;

    private  OnTopicClickListener onTopicClickListener;



    @NonNull
    @Override
    public GrammarTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.activity_grammar_list_item_grammar_topic,parent, false);
        return new GrammarTopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrammarTopicViewHolder holder, int position) {
        GrammarTopic name = nameTopic.get(position);
        holder.topicNameTextView.setText(name.getTitle());
    }

    @Override
    public int getItemCount() {
        return nameTopic != null ? nameTopic.size() : 0;
    }

    public interface OnTopicClickListener {
        void onTopicClick(GrammarTopic grammarTopic);
    }

    public GrammarTopicAdapter(Context context, List<GrammarTopic> nameTopic, OnTopicClickListener onTopicClickListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.nameTopic = nameTopic;
        this.onTopicClickListener = onTopicClickListener;
    }

    // 2. Create your ViewHolder class
     class GrammarTopicViewHolder extends RecyclerView.ViewHolder {
        TextView topicNameTextView; // Assuming your list item XML has a TextView with this ID

        public GrammarTopicViewHolder(@NonNull View itemView) {
            super(itemView);

            topicNameTextView = itemView.findViewById(R.id.topicNameTextView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onTopicClickListener != null) {
                    GrammarTopic clickedTopic = nameTopic.get(position);
                    onTopicClickListener.onTopicClick(clickedTopic);
                }
            });
        }
    }
}
