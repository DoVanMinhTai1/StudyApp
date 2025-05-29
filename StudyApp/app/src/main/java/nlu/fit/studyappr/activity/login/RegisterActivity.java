package nlu.fit.studyappr.activity.login;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import nlu.fit.studyappr.R;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilFullName, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etFullName, etEmail, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_register);


        // Ánh xạ view
        tilFullName = findViewById(R.id.tilFullName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        // Xử lý sự kiện đăng ký
        findViewById(R.id.btnRegister).setOnClickListener(v -> attemptRegister());

        // Xử lý chuyển sang màn hình đăng nhập
        findViewById(R.id.tvLogin).setOnClickListener(v -> {
            onBackPressed(); // Hoặc startActivity(new Intent(this, LoginActivity.class));
            finish();

        });

    }

    private void attemptRegister() {
        // Reset errors
        tilFullName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        // Lấy giá trị từ các trường nhập liệu
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        boolean hasError = false;

        // Validate họ tên
        if (TextUtils.isEmpty(fullName)) {
            tilFullName.setError("Vui lòng nhập họ tên");
            hasError = true;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Vui lòng nhập email");
            hasError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Email không hợp lệ");
            hasError = true;
        }

        // Validate mật khẩu
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Vui lòng nhập mật khẩu");
            hasError = true;
        } else if (password.length() < 6) {
            tilPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            hasError = true;
        }

        // Validate xác nhận mật khẩu
        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            hasError = true;
        } else if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Mật khẩu không khớp");
            hasError = true;
        }

        if (!hasError) {
            // Thực hiện đăng ký
            registerUser(fullName, email, password);
        }

    }

    private void registerUser(String fullName, String email, String password) {
        // Hiển thị progress bar
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

        // TODO: Thực hiện logic đăng ký (API call, Firebase, SQLite...)
        // Ví dụ mẫu:
        new android.os.Handler().postDelayed(() -> {
//            findViewById(R.id.progressBar).setVisibility(View.GONE);

            // Giả lập đăng ký thành công
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

            // Chuyển về màn hình đăng nhập
            finish();
        }, 1500);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}