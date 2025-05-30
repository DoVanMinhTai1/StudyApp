package nlu.fit.studyappr.activity.login;

import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import nlu.fit.studyappr.R;


public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_forgot_password);

        // Ánh xạ view
        tilEmail = findViewById(R.id.tilEmail);
        etEmail = findViewById(R.id.etEmail);
        progressBar = findViewById(R.id.progressBar);

        // Xử lý sự kiện gửi yêu cầu
        findViewById(R.id.btnSubmit).setOnClickListener(v -> validateEmail());

        // Quay lại đăng nhập
        findViewById(R.id.tvBackToLogin).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void validateEmail() {
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Vui lòng nhập email");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Email không hợp lệ");
            return;
        }

        sendResetPasswordEmail(email);
    }

    private void sendResetPasswordEmail(String email) {
        progressBar.setVisibility(View.VISIBLE);

        // TODO: Thay thế bằng logic gửi email thực tế (Firebase, API...)
        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);

            // Giả lập thành công
            Toast.makeText(this, R.string.reset_link_sent, Toast.LENGTH_LONG).show();

            // Chuyển về màn hình đăng nhập sau 2 giây
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }, 2000);
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}