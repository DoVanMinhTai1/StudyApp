package nlu.fit.studyappr.activity.vocabulary;



import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import nlu.fit.studyappr.R;
import nlu.fit.studyappr.activity.home.HomeActivity;
import nlu.fit.studyappr.api.dictionary.DictionaryApi;
import nlu.fit.studyappr.model.dictionary.DictionaryResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DictionaryLookupActivity extends AppCompatActivity {

    private EditText edtWord;
    private TextView tvResult,tv_back_text;
    private Button btnLookup;

    private DictionaryApi dictionaryApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_lookup);

        edtWord = findViewById(R.id.edtWord);
        tvResult = findViewById(R.id.tvResult);
        btnLookup = findViewById(R.id.btnLookup);
tv_back_text= findViewById(R.id.tv_back_text);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.dictionaryapi.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dictionaryApi = retrofit.create(DictionaryApi.class);

        btnLookup.setOnClickListener(view -> {
            String word = edtWord.getText().toString().trim();
            if (TextUtils.isEmpty(word)) {
                Toast.makeText(this, "Vui lòng nhập từ cần tra", Toast.LENGTH_SHORT).show();
                return;
            }

            lookupWord(word);
        });
        tv_back_text.setOnClickListener(v -> {
            Intent intent = new Intent(DictionaryLookupActivity.this, HomeActivity.class); // hoặc HomeActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

    }

    private void lookupWord(String word) {
        tvResult.setText("Đang tra cứu...");

        dictionaryApi.lookupWord(word).enqueue(new Callback<List<DictionaryResponse>>() {
            @Override
            public void onResponse(Call<List<DictionaryResponse>> call, Response<List<DictionaryResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    DictionaryResponse result = response.body().get(0);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Từ: ").append(result.word).append("\n\n");

                    for (DictionaryResponse.Meaning meaning : result.meanings) {
                        sb.append("Loại từ: ").append(meaning.partOfSpeech).append("\n");
                        for (DictionaryResponse.Definition def : meaning.definitions) {
                            sb.append("- ").append(def.definition).append("\n");
                            if (def.example != null) {
                                sb.append("   Ví dụ: ").append(def.example).append("\n");
                            }
                        }
                        sb.append("\n");
                    }

                    tvResult.setText(sb.toString());
                } else {
                    tvResult.setText("Không tìm thấy nghĩa của từ này.");
                }
            }

            @Override
            public void onFailure(Call<List<DictionaryResponse>> call, Throwable t) {
                tvResult.setText("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}

