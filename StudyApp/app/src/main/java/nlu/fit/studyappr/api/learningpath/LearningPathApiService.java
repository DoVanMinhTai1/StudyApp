package nlu.fit.studyappr.api.learningpath;

import nlu.fit.studyappr.model.ConfirmPathRequest;
import nlu.fit.studyappr.model.LearningPathProposal;
import nlu.fit.studyappr.model.LearningPathRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LearningPathApiService {
    @POST("/learning-path/propose") // Replace with your actual endpoint
    Call<LearningPathProposal> getLearningPathProposal(@Body LearningPathRequest request);

    @POST("/learning-path/confirm") // Your actual endpoint for confirming
    Call<Void> confirmLearningPath(@Body ConfirmPathRequest request);
}
