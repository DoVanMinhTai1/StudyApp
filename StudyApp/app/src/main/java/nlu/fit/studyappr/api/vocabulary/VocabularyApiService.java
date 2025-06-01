package nlu.fit.studyappr.api.vocabulary;

import java.util.List;
import java.util.Map;

import nlu.fit.studyappr.model.AnswerSubmissionRequest;
import nlu.fit.studyappr.model.ExerciseResult;
import nlu.fit.studyappr.model.GrammarLesson;
import nlu.fit.studyappr.model.GrammarReviewResult;
import nlu.fit.studyappr.model.GrammarTopic;
import nlu.fit.studyappr.model.StartLearningRequest;
import nlu.fit.studyappr.model.StartLearningResponse;
import nlu.fit.studyappr.model.StartLearningReviewRequest;
import nlu.fit.studyappr.model.StartLearningReviewResponse;
import nlu.fit.studyappr.model.UserAnswer;
import nlu.fit.studyappr.model.VocabularyEndRequest;
import nlu.fit.studyappr.model.VocabularyEndResponse;
import nlu.fit.studyappr.model.VocabularyReviewEndRequest;
import nlu.fit.studyappr.model.VocabularyReviewEndResponse;
import nlu.fit.studyappr.model.VocabularyReviewListResponse;
import nlu.fit.studyappr.model.VocabularyTopic;
import nlu.fit.studyappr.model.VocabularyWordReview;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface VocabularyApiService {
    @GET("/vocabulary/topics")
    Call<List<VocabularyTopic>> getAllVocabularyTopics();

    @GET("/vocabulary/levels")
    Call<List<Map<String,String>>> getLevels();

    @POST("/vocabulary/start-learning")
    Call<StartLearningResponse> startLearning(@Body StartLearningRequest request);

    @POST("/vocabulary/complete-learning")
    Call<VocabularyEndResponse> endLearningSessionEarly(@Body VocabularyEndRequest request);

    @GET("/vocabularyReview/reviewWords") // Your actual endpoint
    Call<VocabularyReviewListResponse> getVocabularyReviewList(
            @Query("userId") String userId,
            @Query("currentTime") String isoCurrentTime // Send current time as ISO 8601 String
    );

    @POST("/vocabularyReview/startReview")
    Call<StartLearningReviewResponse> getStartReview(@Body StartLearningReviewRequest request);

    @POST("/vocabularyReview/submitReviewResult")
    Call<VocabularyReviewEndResponse> endReviewSessionEarly(@Body VocabularyReviewEndRequest request);
}
