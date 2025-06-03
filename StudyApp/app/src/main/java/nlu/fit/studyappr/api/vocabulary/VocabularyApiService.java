package nlu.fit.studyappr.api.vocabulary;

import java.util.List;
import java.util.Map;

import nlu.fit.studyappr.model.vocabulary.StartLearningRequest;
import nlu.fit.studyappr.model.vocabulary.StartLearningResponse;
import nlu.fit.studyappr.model.vocabulary.StartLearningReviewRequest;
import nlu.fit.studyappr.model.vocabulary.StartLearningReviewResponse;
import nlu.fit.studyappr.model.vocabulary.VocabularyEndRequest;
import nlu.fit.studyappr.model.vocabulary.VocabularyEndResponse;
import nlu.fit.studyappr.model.vocabulary.VocabularyReviewEndRequest;
import nlu.fit.studyappr.model.vocabulary.VocabularyReviewEndResponse;
import nlu.fit.studyappr.model.vocabulary.VocabularyReviewListResponse;
import nlu.fit.studyappr.model.vocabulary.VocabularyTopic;
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
