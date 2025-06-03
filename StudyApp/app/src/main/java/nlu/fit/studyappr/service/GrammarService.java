package nlu.fit.studyappr.service;

import android.util.Log;

import java.util.List;

import nlu.fit.studyappr.api.grammar.GrammarApiService;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.model.grammar.GrammarTopic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrammarService {

    private final GrammarApiService api;

    public GrammarService() {
        this.api = InitializeRetrofit.getInstance("213").create(GrammarApiService.class);
    }

    public List<GrammarTopic> getAllGrammar() {
        Call<List<GrammarTopic>> call = api.getAllGrammarTopics();
        call.enqueue(new Callback<List<GrammarTopic>>() {
            @Override
            public void onResponse(Call<List<GrammarTopic>> call, Response<List<GrammarTopic>> response) {
                if (response.isSuccessful()) {
                    List<GrammarTopic> topics = response.body();
                    Log.d("GrammarService", "Fetched " + topics.size() + " topics");
                }
            }

            @Override
            public void onFailure(Call<List<GrammarTopic>> call, Throwable t) {
                Log.e("GrammarService", "API call failed: " + t.getMessage());

            }
        });
        return null;

    }
}
