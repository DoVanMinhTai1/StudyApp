package nlu.fit.studyappr.model.login;

public class LoginResponse {
    private String token;


    public LoginResponse(String token) {
        this.token = token;
    }

    public LoginResponse() {
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
