package nlu.fit.studyappr.api.grammar;

import java.util.List;

import nlu.fit.studyappr.model.AnswerSubmissionRequest;
import nlu.fit.studyappr.model.ExerciseResult;
import nlu.fit.studyappr.model.GrammarLesson;
import nlu.fit.studyappr.model.GrammarReviewResult;
import nlu.fit.studyappr.model.GrammarTopic;
import nlu.fit.studyappr.model.UserAnswer;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GrammarApiService {
    @GET("/getAllGrammarTopics")
    Call<List<GrammarTopic>> getAllGrammarTopics();

    @GET("/getGrammarLessonById")
    Call<GrammarLesson> getGrammarLessonById(@Query("id") Long id);

    @POST("/checkAnswer")
    Call<ExerciseResult> checkAnswer(@Body List<AnswerSubmissionRequest> answerSubmissionRequest);

    @POST("/saveUserAnswer")
    Call<Boolean> saveUserAnswer(@Body List<UserAnswer> userAnswers);

    @POST("/saveGrammarReviewResult")
    Call<GrammarReviewResult> saveGrammarReviewResult(@Body GrammarReviewResult grammarReviewResult);
}
