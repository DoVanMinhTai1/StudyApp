package com.nlu.fit.controller;

import com.nlu.fit.model.progress.UserLearningPath;
import com.nlu.fit.service.progress.ProgressService;
import com.nlu.fit.viewmodel.learningPath.ConfirmPathRequest;
import com.nlu.fit.viewmodel.learningPath.LearningPathProposalResponse;
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
    public ResponseEntity<LearningPathProposalResponse> getLearningPathProposal(@RequestBody LearningPathRequest request) {
        return ResponseEntity.ok(progressService.getLearningPathProposal(request));
    }

    @PostMapping("/learning-path/confirm") // Your actual endpoint for confirming
    public ResponseEntity<UserLearningPath> saveCurrentPathInfoLocally(@RequestBody ConfirmPathRequest request) {
        return ResponseEntity.ok(progressService.saveCurrentPathInfoLocally(request));
    }

    @GetMapping("/learning-path/userActive")
    public ResponseEntity<UserLearningPath> getUserLearningPathActive(@RequestParam String userId) {
        return ResponseEntity.ok(progressService.getUserLearningPathActive(userId));
    }



    // Your ProgressController or LearningPathController
//    @RestController
//    @RequestMapping("/api/progress") // Or "/api/learning-paths"
//    public class ProgressUpdateController {
//
//        @Autowired
//        private ProgressUpdateService progressUpdateService; // Your new service
//
//        @PostMapping("/update")
//        public ResponseEntity<UserLearningPath> updateUserProgress(@RequestBody ProgressUpdateRequestDto requestDto) {
//            try {
//                UserLearningPath updatedPath = progressUpdateService.recordLessonProgress(requestDto);
//                return ResponseEntity.ok(updatedPath);
//            } catch (IllegalArgumentException e) {
//                return ResponseEntity.badRequest().build(); // Or a more specific error response
//            } catch (Exception e) {
//                // Log the exception
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//        }
//    }


//    // com.nlu.fit.dto.ProgressUpdateRequestDto.java (Server)
//    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
//    public class ProgressUpdateRequestDto {
//        private String userId;
//        private Long userLearningPathId;
//        private String lessonId;
//        private String lessonType; // "VOCABULARY_TOPIC", "GRAMMAR_EXERCISE", etc.
//        private int itemsCompletedInSession; // e.g., 5 new words learned in this specific lesson attempt
//        private long completionTimestamp; // Milliseconds since epoch
//        // Add score if applicable
//    }

//    @Service
//    public class ProgressUpdateService {
//
//        @Autowired
//        private UserLearningPathRepository userLearningPathRepository;
//        @Autowired
//        private ProgressStatsRepository progressStatsRepository;
//        @Autowired
//        private ChartDataPointRepository chartDataPointRepository;
//        // Repositories for your VocabularyTopic, GrammarTopic etc. if you need to fetch lesson details
//
//        @Transactional // Ensure atomicity
//        public UserLearningPath recordLessonProgress(ProgressUpdateRequestDto dto) {
//            UserLearningPath ulp = userLearningPathRepository.findByIdAndUserId(dto.getUserLearningPathId(), dto.getUserId())
//                    .orElseThrow(() -> new IllegalArgumentException("UserLearningPath not found or does not belong to user."));
//
//            if (!ulp.isActive()) {
//                throw new IllegalStateException("Cannot update progress for an inactive path.");
//            }
//
//            ProgressStats stats = ulp.getProgressStats();
//            if (stats == null) { // Should be created when UserLearningPath is confirmed
//                stats = new ProgressStats();
//                // stats.setTotalLessonsInPath(calculateTotalLessons(ulp.getLearningPathProposal())); // Set this based on proposal
//                ulp.setProgressStats(stats);
//            }
//
//            // --- Update ProgressStats based on lessonType and itemsCompleted ---
//            if ("VOCABULARY_TOPIC".equalsIgnoreCase(dto.getLessonType())) {
//                stats.setLearnedVocab(stats.getLearnedVocab() + dto.getItemsCompletedInSession());
//                stats.setExercisesDone(stats.getExercisesDone() + 1); // Increment for each lesson "attempt"
//                // Determine if "completed" based on some criteria (e.g., all words in topic learned)
//                // For simplicity, let's say completing any part of a vocab topic counts as an exercise completed
//                stats.setExercisesCompleted(stats.getExercisesCompleted() + 1);
//            } else if ("GRAMMAR_EXERCISE".equalsIgnoreCase(dto.getLessonType())) {
//                stats.setExercisesDone(stats.getExercisesDone() + 1);
//                // Assume itemsCompletedInSession is number of correct answers, or 1 if exercise passed
//                if (dto.getItemsCompletedInSession() > 0) { // Or some score threshold
//                    stats.setExercisesCompleted(stats.getExercisesCompleted() + 1);
//                }
//            }
//            // ... Add more lesson types ...
//
//            // --- Update ChartDataPoint ---
//            LocalDate today = Instant.ofEpochMilli(dto.getCompletionTimestamp()).atZone(ZoneId.systemDefault()).toLocalDate();
//            ChartDataPoint todayData = chartDataPointRepository
//                    .findByUserLearningPathAndDate(ulp, today)
//                    .orElseGet(() -> {
//                        ChartDataPoint newDataPoint = new ChartDataPoint();
//                        newDataPoint.setUserLearningPath(ulp);
//                        newDataPoint.setDate(today);
//                        return newDataPoint;
//                    });
//
//            todayData.setItemsCompletedToday(todayData.getItemsCompletedToday() + 1); // e.g., number of distinct lessons/sessions
//            // todayData.setTimeSpentSecondsToday(todayData.getTimeSpentSecondsToday() + calculateTimeSpentForThisSession());
//            chartDataPointRepository.save(todayData);
//
//            // --- Check if path is completed ---
//            if (stats.getTotalLessonsInPath() > 0 && stats.getExercisesCompleted() >= stats.getTotalLessonsInPath()) {
//                ulp.setActive(false); // Path completed
//            }
//
//            // Save changes (ProgressStats might be saved by cascade if UserLearningPath owns it)
//            progressStatsRepository.save(stats); // Explicit save can be safer
//            return userLearningPathRepository.save(ulp);
//        }
//
//        // Helper to set total lessons when path is created
//        // public int calculateTotalLessons(LearningPathProposal proposal) {
//        //    int total = 0;
//        //    if (proposal.getGrammarTopics() != null) total += proposal.getGrammarTopics().size();
//        //    if (proposal.getVocabularyTopics() != null) total += proposal.getVocabularyTopics().size();
//        //    return total;
//        // }
//    }
}
