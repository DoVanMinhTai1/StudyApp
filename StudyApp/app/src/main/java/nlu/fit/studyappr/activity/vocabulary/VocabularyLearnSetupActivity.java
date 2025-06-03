package nlu.fit.studyappr.activity.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.api.vocabulary.VocabularyApiService;
import nlu.fit.studyappr.model.vocabulary.StartLearningRequest;
import nlu.fit.studyappr.model.vocabulary.StartLearningResponse;
import nlu.fit.studyappr.model.vocabulary.VocabularyTopic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabularyLearnSetupActivity extends AppCompatActivity {

    private VocabularyApiService vocabularyApiService;
    private List<VocabularyTopic> list;

    ArrayAdapter<VocabularyTopic> arrayAdapter;

    ArrayAdapter<String> arrayAdapterLevel;
    RadioGroup radioGroup;

    Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_learn_setup);

        spinner = findViewById(R.id.spinner_topic);
        Button button = findViewById(R.id.button_start);

        VocabularyApiService vocabularyCall = InitializeRetrofit.getInstance().create(VocabularyApiService.class);
        Call<List<VocabularyTopic>> call = vocabularyCall.getAllVocabularyTopics();
        call.enqueue(new Callback<List<VocabularyTopic>>() {
            @Override
            public void onResponse(Call<List<VocabularyTopic>> call, Response<List<VocabularyTopic>> response) {
                if (response.isSuccessful()) {
                    list = response.body();

                    list = list.stream()
                            .filter(vt -> vt != null && vt.getTitle() != null)
                            .collect(Collectors.toList());

                    List<String> nameTopics = new ArrayList<String>();
                    for (VocabularyTopic vt : list) {
                        nameTopics.add(vt.getTitle());
                    }

                    arrayAdapter = new ArrayAdapter<VocabularyTopic>(VocabularyLearnSetupActivity.this,
                            android.R.layout.simple_spinner_item, list);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(arrayAdapter);
                } else {
                    Toast.makeText(VocabularyLearnSetupActivity.this, "Failed to load topics", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<List<VocabularyTopic>> call, Throwable t) {
                Toast.makeText(VocabularyLearnSetupActivity.this, "Failed to load topics", Toast.LENGTH_SHORT).show();
            }
        });

        final Spinner spinnerLevels = findViewById(R.id.spinner_level);

        vocabularyApiService = InitializeRetrofit.getInstance().create(VocabularyApiService.class);
        Call<List<Map<String, String>>> callLevels = vocabularyApiService.getLevels();
        callLevels.enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (response.isSuccessful()) {
                    List<Map<String, String>> levels = response.body();
                    List<String> levelNames = levels.stream()
                            .map(map -> map.get("level"))
                            .filter(level -> level != null && !level.trim().isEmpty())
                            .collect(Collectors.toList());

                    arrayAdapterLevel = new ArrayAdapter<String>(VocabularyLearnSetupActivity.this,
                            android.R.layout.simple_spinner_item, levelNames);
                    arrayAdapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerLevels.setAdapter(arrayAdapterLevel);

                } else {
                    Toast.makeText(VocabularyLearnSetupActivity.this, "Failed to load levels", Toast.LENGTH_SHORT)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                Toast.makeText(VocabularyLearnSetupActivity.this, "Failed to load levels", Toast.LENGTH_SHORT).show();

            }
        });

        radioGroup = findViewById(R.id.radio_group_method);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItemPosition = spinner.getSelectedItemPosition();
                VocabularyTopic vocabularyTopic = arrayAdapter.getItem(selectedItemPosition);
                Long topicId;
                if (vocabularyTopic != null) {
                    topicId = vocabularyTopic.getId();
                } else {
                    return;
                }

                int methodId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(methodId);
                String method = radioButton.getText().toString();

                int selectedItemLevelPosition = spinnerLevels.getSelectedItemPosition();
                String level = arrayAdapterLevel.getItem(selectedItemLevelPosition);

                StartLearningRequest request = new StartLearningRequest(topicId, level, method);

                Call<StartLearningResponse> callStartLearning = vocabularyApiService.startLearning(request);
                callStartLearning.enqueue(new Callback<StartLearningResponse>() {
                    @Override
                    public void onResponse(Call<StartLearningResponse> call, Response<StartLearningResponse> response) {
                        if (response.isSuccessful()) {
                            StartLearningResponse startLearningResponse = response.body();
                            String method = startLearningResponse.getMethod();

                            Intent intent = null;

                            switch (method) {
                                case "FLASHCARD":
                                    intent = new Intent(VocabularyLearnSetupActivity.this,
                                            VocabularyLearnFlashCardActivity.class);
                                    break;
                                case "REWRITE":
                                    intent = new Intent(VocabularyLearnSetupActivity.this,
                                            VocabularyLearnRewriteActivity.class);
                                    break;
                                case "QUIZ":
                                    intent = new Intent(VocabularyLearnSetupActivity.this,
                                            VocabularyLearnQuizActivity.class);
                                    break;
                            }

                            if (intent != null) {
                                intent.putExtra("startLearning", startLearningResponse);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(VocabularyLearnSetupActivity.this, "Failed to start learning",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<StartLearningResponse> call, Throwable t) {
                        Toast.makeText(VocabularyLearnSetupActivity.this, "Failed to start learning: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }
}
