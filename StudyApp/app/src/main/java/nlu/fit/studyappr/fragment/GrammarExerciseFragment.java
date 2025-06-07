package nlu.fit.studyappr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.model.grammar.GrammarExcerciseQuestion;
import nlu.fit.studyappr.model.grammar.GrammarLesson;

public class GrammarExerciseFragment extends Fragment {

    public static GrammarExerciseFragment newInstance(GrammarLesson grammarLesson) {
        GrammarExerciseFragment grammarExerciseFragment = new GrammarExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("lesson", grammarLesson);
        grammarExerciseFragment.setArguments(bundle);
        return grammarExerciseFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_grammar_learn_detail, container, false);
        GrammarLesson grammarLesson = (GrammarLesson) getArguments().getSerializable("lesson");
        List<GrammarExcerciseQuestion> grammarExcerciseQuestionList = grammarLesson.getGrammarExcerciseQuestionList();
//        RecyclerView recyclerView = view.findViewById(R.id.grammarRecyclerView);
//        GrammarQuestionAdapter adapter = new GrammarQuestionAdapter(grammarExcerciseQuestionList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
        return view;
    }
}
