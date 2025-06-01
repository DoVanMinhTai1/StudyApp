package nlu.fit.studyappr.api.learningpath;

import java.util.List;

import nlu.fit.studyappr.model.ConfirmPathRequest;
import nlu.fit.studyappr.model.DailyProgressEntry;
import nlu.fit.studyappr.model.LearningPathProposal;
import nlu.fit.studyappr.model.LearningPathRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProgressApiService {
    @GET("api/progress/daily-details") // Replace with your actual endpoint
    Call<List<DailyProgressEntry>> getDailyProgressDetails(@Query("userId") String userId);
}
