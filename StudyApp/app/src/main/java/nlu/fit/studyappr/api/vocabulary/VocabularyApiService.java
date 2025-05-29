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
import nlu.fit.studyappr.model.UserAnswer;
import nlu.fit.studyappr.model.VocabularyTopic;
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


}
