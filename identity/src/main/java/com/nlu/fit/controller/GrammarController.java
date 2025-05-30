package com.nlu.fit.controller;

import com.nlu.fit.model.*;
import com.nlu.fit.service.*;
import com.nlu.fit.viewmodel.grammarexercise.AnswerSubmissionRequest;
import com.nlu.fit.viewmodel.grammarexercise.ExerciseResult;
import com.nlu.fit.viewmodel.grammarexercise.GrammarLessonVm;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping
@AllArgsConstructor
public class GrammarController {
    private  GrammarExerciseService grammarExerciseService;
    private  GrammarExplanationService grammarExplanationService;
    private  GrammarTopicService grammarTopicService;
    private  GrammarLessonService grammarLessonService;
    private  UserAnswerService userAnswerService;
    
    @GetMapping("/getAllGrammarTopics")
    public ResponseEntity<List<GrammarTopic>> getAllGrammarTopics() {
        return ResponseEntity.ok(grammarTopicService.getAll());
    }

    @GetMapping("/getGrammarLessonById")
    public ResponseEntity<GrammarLessonVm> getGrammarLessonById(@RequestParam Long id) {
        return ResponseEntity.ok(grammarLessonService.getGrammarLessonById(id));
    }

    @PostMapping("/checkAnswer")
    public ResponseEntity<ExerciseResult> checkAnswer(@RequestBody List<AnswerSubmissionRequest> answerSubmissionRequest) {
        return ResponseEntity.ok(grammarExerciseService.checkAnswer(answerSubmissionRequest));
    }
    
    @GetMapping("/getAllGrammarReview")
    public ResponseEntity<List<GrammarLesson>> getGrammarLessonByIdAndTypeExercise(@RequestParam Long id) {
        return ResponseEntity.ok(grammarLessonService.getGrammarLessonByIdAndTypeExercise(id));
    }

    @PostMapping("/saveUserAnswer")
    public ResponseEntity<Boolean> saveUserAnswer(@RequestBody List<UserAnswer> userAnswers) {
        return ResponseEntity.ok(userAnswerService.saveUserAnswer(userAnswers));
    }

    @PostMapping("/saveGrammarReviewResult")
    public ResponseEntity<GrammarReviewResult> saveGrammarReviewResult(@RequestBody GrammarReviewResult grammarReviewResult) {
        return ResponseEntity.ok(grammarExerciseService.saveGrammarReviewResult(grammarReviewResult));
    }
    

    


    
}
