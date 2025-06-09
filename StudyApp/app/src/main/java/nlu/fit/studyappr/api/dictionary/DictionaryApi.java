package nlu.fit.studyappr.api.dictionary;

import java.util.List;

import nlu.fit.studyappr.model.dictionary.DictionaryResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApi {
    @GET("api/v2/entries/en/{word}")
    Call<List<DictionaryResponse>> lookupWord(@Path("word") String word);
}
