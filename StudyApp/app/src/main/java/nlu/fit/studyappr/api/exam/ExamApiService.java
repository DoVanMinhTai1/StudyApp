package nlu.fit.studyappr.api.exam;

import java.util.List;

import nlu.fit.studyappr.model.exam.Exam;
import nlu.fit.studyappr.model.exam.ExamResult;
import nlu.fit.studyappr.model.exam.ExamSubmited;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ExamApiService {

    @GET("exam")
    Call<List<Exam>> getAllExam();

    //api get Exam by param examId
    @GET("exam/{examId}")
    Call<Exam> getExamById(@retrofit2.http.Path("examId") String examId);

    @POST("exam/submit")
    Call<ExamResult> submitExam(@Body ExamSubmited request);



}
