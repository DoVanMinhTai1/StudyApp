package nlu.fit.studyappr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.model.GrammarLesson;

public class GrammarExplanationFragment extends Fragment {

    public static GrammarExplanationFragment newInstance(GrammarLesson grammarLesson) {
        GrammarExplanationFragment fragment = new GrammarExplanationFragment();
        Bundle args = new Bundle();
        args.putSerializable("lesson", grammarLesson);
        fragment.setArguments(args);
        return fragment;
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_grammar_learn_explanation, container, false);
//        GrammarLesson grammarLesson = (GrammarLesson) getArguments().getSerializable("lesson");
//        TextView textView = view.findViewById(R.id.textViewOk);
//        textView.setText(grammarLesson.toString());
//        return view;
//    }
}
