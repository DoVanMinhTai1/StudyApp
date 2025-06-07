package nlu.fit.studyappr.api.learningpath;

import nlu.fit.studyappr.model.learningProgress.ConfirmPathRequest;
import nlu.fit.studyappr.model.learningProgress.LearningPathProposal;
import nlu.fit.studyappr.model.learningProgress.LearningPathRequest;
import nlu.fit.studyappr.model.learningProgress.UserLearningPath;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LearningPathApiService {
    @POST("/learning-path/propose") // Replace with your actual endpoint
    Call<LearningPathProposal> getLearningPathProposal(@Body LearningPathRequest request);

    @POST("/learning-path/confirm") // Your actual endpoint for confirming
    Call<UserLearningPath> confirmLearningPath(@Body ConfirmPathRequest request);

    @GET("/learning-path/userActive")
    Call<UserLearningPath> getUserLearningPathActive(@Query("userId") String userId);


}
