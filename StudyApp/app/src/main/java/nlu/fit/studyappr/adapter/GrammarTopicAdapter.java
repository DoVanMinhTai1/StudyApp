package nlu.fit.studyappr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.model.GrammarTopic;

public class GrammarTopicAdapter extends ArrayAdapter<GrammarTopic> {

    private final LayoutInflater layoutInflater;

    public GrammarTopicAdapter(@NonNull Context context, List<GrammarTopic> grammarTopics) {
        super(context, 0, grammarTopics);
        this.layoutInflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        TextView textView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GrammarTopic grammarTopic = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_grammar_topic, parent, false);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.tvTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (grammarTopic != null) {
            holder.textView.setText(grammarTopic.getTitle());
        }

        return convertView;
    }
}
