package nlu.fit.studyappr.api.auth;

import nlu.fit.studyappr.model.login.LoginRequest;
import nlu.fit.studyappr.model.login.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {

    @POST("/login")
    public Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
