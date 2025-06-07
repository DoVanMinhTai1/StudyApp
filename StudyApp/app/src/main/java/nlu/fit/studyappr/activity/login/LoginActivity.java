package nlu.fit.studyappr.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.activity.home.HomeActivity;
import nlu.fit.studyappr.api.auth.AuthApiService;
import nlu.fit.studyappr.api.initRetrofit.InitializeRetrofit;
import nlu.fit.studyappr.model.login.LoginRequest;
import nlu.fit.studyappr.model.login.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister;
    private TextView tvforgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login); // Layout của bạn là login.xml

        // Ánh xạ view
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvforgotpassword = findViewById(R.id.tvForgotPassword);
        FirebaseApp.initializeApp(this);

        // Bắt sự kiện đăng nhập
        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Thực hiện đăng nhập (gọi API hoặc kiểm tra giả)
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                AuthApiService authApiService = InitializeRetrofit.getInstance().create(AuthApiService.class);
                LoginRequest loginRequest = new LoginRequest(email, password);

                Call<LoginResponse> call = authApiService.login(loginRequest);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            String customToken = loginResponse.getToken();
                            FirebaseAuth.getInstance().signInWithCustomToken(customToken).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String userId = user.getUid();
                                    user.getIdToken(true).addOnCompleteListener(tokenTask -> {
                                        String idToken = tokenTask.getResult().getToken();
                                        saveTokenAndLogin(idToken, userId);
                                    });

                                } else {
                                    Toast.makeText(LoginActivity.this, "Lấy ID Token thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(LoginActivity.this, "Firebase login thất bại", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

            }
        });

        // Bắt sự kiện chuyển sang đăng ký
        tvRegister.setOnClickListener(view -> {
            // TODO: Mở màn hình đăng ký (nếu có)
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
        tvforgotpassword.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }

    private void saveTokenAndLogin(String idToken, String userId) {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);

        preferences.edit()
                .putString("id_token", idToken)
                .putString("user_id", userId)
                .apply();
        // Chuyển sang màn hình chính (nếu có)
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }


}