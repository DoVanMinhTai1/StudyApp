package nlu.fit.studyappr.api.learningpath;

import java.util.List;

import nlu.fit.studyappr.model.learningProgress.DailyProgressEntry;
import nlu.fit.studyappr.model.learningProgress.ProgressOverviewViewModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProgressApiService {
    @GET("/progress/getDailyProgressDetail") // Replace with your actual endpoint
    Call<List<DailyProgressEntry>> getDailyProgressDetails(@Query("userId") String userId);

    @GET("/progress/getProgressOverView")
    Call<ProgressOverviewViewModel> getProgressOverView(@Query("userId") String userId);
}
