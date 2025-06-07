package com.nlu.fit.controller;

import com.nlu.fit.model.progress.DailyProgressEntry;
import com.nlu.fit.service.progress.ProgressService;
import com.nlu.fit.viewmodel.learningPath.ProgressOverviewViewModel;
import com.nlu.fit.viewmodel.learningPath.UpdateProgress;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class ProgressController {
    private final ProgressService progressService;


    @GetMapping("/progress/getDailyProgressDetail")
    public ResponseEntity<List<DailyProgressEntry>> getDailyProgressDetails(@RequestParam String userId) {
        return ResponseEntity.ok(progressService.getDailyProgressDetails(userId));
    }

    @GetMapping("/progress/getProgressOverView")
    public ResponseEntity<ProgressOverviewViewModel> getProgressOverView(@RequestParam String userId) {
        return ResponseEntity.ok(progressService.getProgressOverView(userId));
    }

    @PostMapping("/progress/updateProgress")
    public ResponseEntity<Void> updateProgress(@RequestBody UpdateProgress updateProgress ) {
        progressService.recordTopicCompletion(updateProgress);
        return ResponseEntity.ok().build();
    }
}

