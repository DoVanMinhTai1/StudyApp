package com.nlu.fit.controller;

import com.nlu.fit.service.vocabulary.VocabularyReviewService;
import com.nlu.fit.viewmodel.vocabulary.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("")
@AllArgsConstructor
public class VocabularyReviewController {
    private VocabularyReviewService vocabularyReviewService;

    /*
     Use Case 2: Ôn tập từ vựng
     2.1. Lấy danh sách từ cần ôn

       GET /api/review/words
        Request: Query Parameters: (tùy chọn) method=flashcard, limit=10

Response:
{
  "wordsToReview": [
    {
      "id": 201,
      "word": "departure",
      "meaning": "sự khởi hành",
      "example": "The departure was delayed.",
      "audioUrl": "https://..."
    }
  ]
}
 */
    @GetMapping("/vocabularyReview/reviewWords")
    public ResponseEntity<VocabularyWordReview> getReviewWords() {
        return ResponseEntity.ok(vocabularyReviewService.getReviewWords());

    }


    @PostMapping("/vocabularyReview/startReview")
    public ResponseEntity<StartLearningReviewResponse> getStartReview(@RequestBody StartLearningRevewRequest request) {
        return ResponseEntity.ok(vocabularyReviewService.getStartReview(request));
    }

    /*
2.2. Ghi nhận kết quả ôn tập
http
POST /api/review/submit
Request Body:

{
  "results": [
    {
      "wordId": 201,
      "status": "needs_review" // or "remembered"
    }
  ]
}
Response:

json
Sao chép
Chỉnh sửa
{ "message": "Review progress saved." }

*/
    @PostMapping("/vocabularyReview/submitReviewResult")
    public ResponseEntity<?> submitReviewResult(@RequestBody CompleteReviewRequest request) {
        return ResponseEntity.ok(vocabularyReviewService.completeLearning(request));
    }
/*

2.3. Gợi ý thời gian ôn tập kế tiếp
http
Sao chép
Chỉnh sửa
GET /api/review/schedule
Response:

{
  "nextReview": "2025-05-25T10:00:00Z"
}
     */

}
