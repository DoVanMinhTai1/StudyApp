package com.nlu.fit.controller;

import com.nlu.fit.model.progress.DailyProgressEntry;
import com.nlu.fit.service.progress.ProgressService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}

