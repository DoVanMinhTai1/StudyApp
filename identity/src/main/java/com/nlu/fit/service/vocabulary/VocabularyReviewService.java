package com.nlu.fit.service.vocabulary;

import com.nlu.fit.enumeration.LearningStatusVocabulary;
import com.nlu.fit.model.vocabulary.*;
import com.nlu.fit.repository.vocabulary.*;
import com.nlu.fit.viewmodel.vocabulary.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VocabularyReviewService {
    private final VocabularyTopicRepository vocabularyTopicRepository;
    private final LearningSessionRepository learningSessionRepository;
    private final LearningSessionWordRepository learningSessionWordRepository;
    private final VocabularyWordRepository vocabularyWordRepository;
    private final ReviewScheduleRepository reviewScheduleRepository;

    /*
    Thời gian học gần nhất
       attemptAt
    Mức độ ghi nhớ
        status
    ? Tần suất ôn tập
        number of items in the list
     */
    public VocabularyWordReview getReviewWords() {
//  1.Get ALL record
        List<LearningSessionWord> allVocabularyByUser = learningSessionWordRepository.findAllByUserId(getCurrentUser());
        List<VocabularyWordsReviewList> vocabularyReviews = new ArrayList<>();
//  2. Group By VocabularyWord
        Map<Long, List<LearningSessionWord>> vocabularyByWordId = allVocabularyByUser.stream().collect(
                Collectors.groupingBy(item -> item.getVocabularyWord().getId())
        );
//  3. get Latest attempAt
        for (Map.Entry<Long, List<LearningSessionWord>> entry : vocabularyByWordId.entrySet()) {
            List<LearningSessionWord> learningSessionWords = entry.getValue();

            LearningSessionWord sessionWord = learningSessionWords.stream().max(Comparator.comparing(LearningSessionWord::getAttemptAt)).orElse(null);

            if (sessionWord != null) {
                vocabularyReviews.add(new VocabularyWordsReviewList(
                        sessionWord.getLearningSession().getId(),
                        sessionWord.getVocabularyWord().getId(),
                        sessionWord.getStatus(),
                        sessionWord.getVocabularyWord().getWord(),
                        sessionWord.getVocabularyWord().getMeaning()
                ));
            }
        }

        vocabularyReviews = vocabularyReviews.stream().filter(r -> r.vocabularyStatus() == LearningStatusVocabulary.FORGOT
                        || r.vocabularyStatus() == LearningStatusVocabulary.REMEMBERED)
                .sorted(Comparator.comparing((VocabularyWordsReviewList r) -> {
                    if (r.vocabularyStatus() == LearningStatusVocabulary.FORGOT) return 0;
                    if (r.vocabularyStatus() == LearningStatusVocabulary.REMEMBERED) return 1;
                    return 2;
                })).collect(Collectors.toList());


        return new VocabularyWordReview(vocabularyReviews.size(), vocabularyReviews);
    }

    private String getCurrentUser() {
//        return SecurityContextHolder.getContext().getAuthentication().getName();
        return "2";
    }

    public CompleteLearningResponse completeLearning(CompleteReviewRequest request) {
        Long reviewScheduleId = Long.valueOf(request.reviewScheduleId());
        List<ReviewResults> list = request.reviewResults();
        List<LearningSessionWord> learningSessionWords = new ArrayList<>();

        ReviewSchedule reviewSchedule = reviewScheduleRepository.findById(reviewScheduleId).orElseThrow(() -> new RuntimeException("Session not found"));

        int remembered = 0;
        for (ReviewResults reviewResult : list) {
            String status = reviewResult.status();
            if (status.equals(LearningStatusVocabulary.REMEMBERED.toString())) {
                remembered++;
            }
            LearningSessionWord learningSessionWord = learningSessionWordRepository.findById(reviewResult.sessionVocabularyWordId()).orElseThrow(() -> new RuntimeException("Session not found"));
            learningSessionWord.setStatus(LearningStatusVocabulary.valueOf(reviewResult.status()));
            learningSessionWords.add(learningSessionWord);
        }
        learningSessionWordRepository.saveAll(learningSessionWords);

        reviewSchedule.setStatus(request.status());
        reviewSchedule.setLastReviewAt(LocalDateTime.now());
        reviewScheduleRepository.save(reviewSchedule);

        List<VocabularyReviewResult> vocabularyReviewResults = learningSessionWords.stream().map(item -> new VocabularyReviewResult(item.getVocabularyWord().getWord(), suggestNextReview(item.getStatus()))).collect(Collectors.toList());

        return new CompleteLearningResponse(
                learningSessionWords.size(), remembered, vocabularyReviewResults);
    }

    private String suggestNextReview(LearningStatusVocabulary status) {
        return switch (status) {
            case REMEMBERED -> "Tốt lắm! Bạn có thể ôn lại sau 3 ngày.";
            case LEARNING -> "Gợi ý ôn lại vào ngày mai.";
            case FORGOT -> "Hãy ôn lại từ này sau 5 phút.";
            case NEW -> "Ôn lại sau 1 giờ.";
        };
    }


    public StartLearningReviewResponse getStartReview(StartLearningRevewRequest request) {
        String method = request.method();
        List<VocabularyWordsReviewList> reviewInputList = request.vocabularyWordsReviewLists();

        List<LearningSessionWord> learningSessionWords = learningSessionWordRepository.findAllByLearningSession_IdIn(reviewInputList.stream().map(item ->
                item.sessionLearningColumnId()).collect(Collectors.toList()));

        List<VocabularyWord> vocabularyWordList = learningSessionWords.stream().map(item ->
                item.getVocabularyWord()).collect(Collectors.toList());

        ReviewSchedule reviewSchedule = new ReviewSchedule();
        reviewSchedule.setUserId(getCurrentUser());
        reviewSchedule.setWord_id(vocabularyWordList);
        reviewSchedule.setStatus("LEARNING");
        reviewScheduleRepository.save(reviewSchedule);

        List<VocabularyWordViewModel> vocabularyWordViewModels = learningSessionWords.stream().map(item -> {

            return new VocabularyWordViewModel(
                    item.getId(),
                    item.getStatus(),
                    item.getVocabularyWord().getId(),
                    item.getVocabularyWord().getWord(),
                    item.getVocabularyWord().getMeaning(),
                    String.valueOf(item.getVocabularyWord().getLevelTypeVocabulary()),
                    item.getVocabularyWord().getImageUrl(),
                    item.getVocabularyWord().getAudioUrl());
        }).toList();
        return new StartLearningReviewResponse(
                reviewSchedule.getId(),
                method,
                vocabularyWordViewModels
        );
    }
}
