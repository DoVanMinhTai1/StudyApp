package com.nlu.fit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nlu.fit.model.grammar.GrammarExerciseQuestion;
import com.nlu.fit.model.grammar.GrammarTopic;
import com.nlu.fit.service.grammar.GrammarReviewService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;



/*
 * 
 *  2. Các chức năng chính
✅ a. Xem danh sách chủ điểm đã học
Truy vấn từ bảng điểm / lịch sử học để lọc các grammarTopic đã học.

✅ b. Ôn tập từng chủ điểm
Truy vấn danh sách câu hỏi từ GrammarExerciseQuestion theo chủ điểm đã chọn.

Giao diện hiển thị từng câu, người dùng chọn đáp án.

✅ c. Tính điểm và lưu lại
So sánh đáp án người dùng chọn với đáp án đúng.

Tính tổng điểm.

Ghi kết quả vào GrammarReviewResult hoặc bảng tương tự (gồm: userId, topicId, score, time,...)
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class GrammarReviewController {
    private final GrammarReviewService grammarReviewService;
    
    @GetMapping("/getAllTopicLearned")
    public ResponseEntity<List<GrammarTopic>> getAllTopicLearned(@RequestBody String uuId) {
        return ResponseEntity.ok(grammarReviewService.getAllTopicLearned(uuId));
    }
    
    @GetMapping("/getListQuestionLearned")
    public ResponseEntity<List<GrammarExerciseQuestion>> getListQuestionByTopicId(@RequestParam Long topicId) {
        return ResponseEntity.ok(grammarReviewService.getListQuestionByTopicId(topicId));
    }
}
