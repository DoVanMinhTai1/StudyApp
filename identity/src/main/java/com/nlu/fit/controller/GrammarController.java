package com.nlu.fit.controller;

import com.nlu.fit.model.GrammarExerciseQuestion;
import com.nlu.fit.model.GrammarExplanation;
import com.nlu.fit.model.GrammarLesson;
import com.nlu.fit.model.GrammarTopic;
import com.nlu.fit.service.GrammarExerciseService;
import com.nlu.fit.service.GrammarExplanationService;
import com.nlu.fit.service.GrammarLessonService;
import com.nlu.fit.service.GrammarTopicService;
import com.nlu.fit.viewmodel.grammarexercise.AnswerSubmissionRequest;
import com.nlu.fit.viewmodel.grammarexercise.ExerciseResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class GrammarController {
    private final GrammarExerciseService grammarExerciseService;
    private final GrammarExplanationService grammarExplanationService;
    private final GrammarTopicService grammarTopicService;
    private final GrammarLessonService grammarLessonService;

    @GetMapping("/getAllGrammarTopics")
    public ResponseEntity<List<GrammarTopic>> getAllGrammarTopics() {
        return ResponseEntity.ok(grammarTopicService.getAll());
    }

    @GetMapping("/getGrammarLessonById")
    public ResponseEntity<GrammarLesson> getGrammarLessonById(@RequestParam Long id) {
        return ResponseEntity.ok(grammarLessonService.getGrammarLessonById(id));
    }

    @PostMapping("/checkAnswer")
    public ResponseEntity<ExerciseResult> checkAnswer(@RequestBody List<AnswerSubmissionRequest> answerSubmissionRequest) {
        return ResponseEntity.ok(grammarExerciseService.checkAnswer(answerSubmissionRequest));
    }


}
