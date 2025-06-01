package com.nlu.fit.controller;

import com.nlu.fit.service.progress.ProgressService;
import com.nlu.fit.viewmodel.learningPath.LearningPathProposal;
import com.nlu.fit.viewmodel.learningPath.LearningPathRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@AllArgsConstructor
public class LearningPathController {

    private ProgressService progressService;


    @PostMapping("/learning-path/propose")
    public ResponseEntity<LearningPathProposal> getLearningPathProposal(@RequestBody LearningPathRequest request) {
        return ResponseEntity.ok(progressService.getLearningPathProposal(request));
    }

    // @PostMapping("/learning-path/confirm") // Your actual endpoint for confirming
    // public ResponseEntity<Void> confirmLearningPath(@RequestBody ConfirmPathRequest request) {
    //     return ResponseEntity.ok(progressService.confirmLearningPath(request)).build();
    // }

}
