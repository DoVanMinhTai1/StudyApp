package com.nlu.fit.controller;

import com.nlu.fit.model.vocabulary.VocabularyTopic;
import com.nlu.fit.service.vocabulary.VocabularyService;
import com.nlu.fit.viewmodel.vocabulary.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class VocabularyController {
    private VocabularyService vocabularyService;


    /*
    * [
  { "id": 1, "name": "Du lịch" },
  { "id": 2, "name": "Giao tiếp" }
]

    * */
    @GetMapping("/vocabulary/topics")
    public ResponseEntity<List<VocabularyTopic>> getAllVocabularyTopics() {
        return ResponseEntity.ok(vocabularyService.getAllVocabularyTopics());
    }

    /*
        [
  { "id": "beginner", "name": "Cơ bản" },
  { "id": "intermediate", "name": "Trung cấp" },
  { "id": "advanced", "name": "Nâng cao" }
]

     */
    @GetMapping("/vocabulary/levels")
    public ResponseEntity<List<Map<String, String>>> getLevels() {
        return ResponseEntity.ok(vocabularyService.getLevels());
    }

    /*
{
  "topicId": 1,
  "level": "beginner",
  "method": "flashcard"
}

{
  "sessionId": "abc123",
  "words": [
    {
      "id": 101,
      "word": "travel",
      "meaning": "du lịch",
      "pronunciation": "/ˈtræv.əl/",
      "imageUrl": "https://...",
      "audioUrl": "https://..."
    }
  ]
}

 */
    @PostMapping("/vocabulary/start-learning")
    public ResponseEntity<StartLearningResponse> startLearning(@RequestBody StartLearningRequest request) {
        return ResponseEntity.ok(vocabularyService.startLearningRequest(request));
    }

    /*
    {
  "sessionId": "abc123",
  "results": [
    { "wordId": 101, "status": "remembered" }
  ]
}


{ "message": "Learning progress saved." }

     */
    @PostMapping("/vocabulary/complete-learning")
    public ResponseEntity<VocabularyEndResponse> completeLearning(@RequestBody VocabularyEndRequest request) {
        return ResponseEntity.ok(vocabularyService.endLearningSessionEarly(request));
    }


}
