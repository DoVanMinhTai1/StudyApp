package com.nlu.fit.service.vocabulary;

import com.nlu.fit.enumeration.LearningStatusVocabulary;
import com.nlu.fit.model.vocabulary.*;
import com.nlu.fit.repository.vocabulary.*;
import com.nlu.fit.viewmodel.vocabulary.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
    public VocabularyReviewListResponse getReviewWords(String userId) {
        List<LearningSessionWord> allVocabularyByUser = learningSessionWordRepository.findAllByUserId(userId);
        Map<Long, List<LearningSessionWord>> vocabularyByWordId = allVocabularyByUser.stream()
                .collect(Collectors.groupingBy(word -> word.getVocabularyWord().getId()));

        List<VocabularyWordsReviewList> vocabularyReviews = vocabularyByWordId.values().stream()
                .map(words -> words.stream().max(Comparator.comparing(LearningSessionWord::getAttemptAt)).orElse(null))
                .filter(Objects::nonNull)
                .map(sessionWord -> new VocabularyWordsReviewList(
                        sessionWord.getId(),
                        sessionWord.getLearningSession().getId(),
                        sessionWord.getVocabularyWord().getId(),
                        sessionWord.getStatus(),
                        sessionWord.getVocabularyWord().getWord(),
                        sessionWord.getVocabularyWord().getMeaning()
                ))
                .filter(r -> r.vocabularyStatus() == LearningStatusVocabulary.FORGOT || r.vocabularyStatus() == LearningStatusVocabulary.REMEMBERED)
                .sorted(Comparator.comparing(r -> getStatusPriority(r.vocabularyStatus())))
                .collect(Collectors.toList());

        return new VocabularyReviewListResponse(null, vocabularyReviews.size(), vocabularyReviews);
    }

    private int getStatusPriority(LearningStatusVocabulary status) {
        return switch (status) {
            case FORGOT -> 0;
            case REMEMBERED -> 1;
            default -> 2;
        };
    }

    private String getCurrentUser() {
//        return SecurityContextHolder.getContext().getAuthentication().getName();
        return "2";
    }

    public VocabularyReviewEndResponse completeLearning(VocabularyReviewEndRequest request) {
        Long reviewScheduleId = request.reviewScheduleId();
        List<WordAnswer> userAnswers = request.answers();
        int wordsMarkedKnownByClient = 0;
        List<LearningSessionWord> updatedSessionWords = new ArrayList<>();
        List<LearningSessionWord> learningSessionWords = new ArrayList<>();
        List<NextReviewTimeInfo> nextReviewTimeInfos = new ArrayList<>();

        ReviewSchedule reviewSchedule = reviewScheduleRepository.findById(reviewScheduleId).orElseThrow(() -> new RuntimeException("Session not found"));

        int remembered = 0;
        for (WordAnswer userAnswer : userAnswers) {
            Boolean status = userAnswer.status();
            if (status.equals(Boolean.TRUE)) {
                remembered++;
            }
            Long globalWordId = userAnswer.wordId(); // Assuming wordId is sent as String

            LearningSessionWord learningSessionWord = learningSessionWordRepository.findByReviewScheduleAndVocabularyWord_Id(reviewSchedule, globalWordId);
            LearningStatusVocabulary newStatus;
            if (userAnswer.status()) {
                newStatus = LearningStatusVocabulary.REMEMBERED;
                wordsMarkedKnownByClient++; // Count known words
            } else {
                newStatus = LearningStatusVocabulary.FORGOT; // Or NEEDS_REVIEW
            }
            learningSessionWord.setStatus(newStatus);
            learningSessionWord.setAttemptAt(OffsetDateTime.now()); // Set current time
            updatedSessionWords.add(learningSessionWord);
            String wordText = learningSessionWord.getVocabularyWord() != null ?
                    learningSessionWord.getVocabularyWord().getWord() : "Unknown Word";
            nextReviewTimeInfos.add(new NextReviewTimeInfo(wordText, suggestNextReview(newStatus)));
        }
        learningSessionWordRepository.saveAll(learningSessionWords);


        List<VocabularyReviewResult> vocabularyReviewResults = learningSessionWords.stream().map(item -> new VocabularyReviewResult(item.getVocabularyWord().getWord(), suggestNextReview(item.getStatus()))).collect(Collectors.toList());

        String overallSessionStatus;
        if (userAnswers != null &&
                request.lastWordIndexSeen() < (userAnswers.size() - 1)) {
            overallSessionStatus = "ENDED_EARLY";
        } else {
            overallSessionStatus = "COMPLETED";
        }


        reviewSchedule.setStatus(overallSessionStatus);
        reviewSchedule.setLastReviewAt(LocalDateTime.now());
        reviewScheduleRepository.save(reviewSchedule);

        int totalWordsActuallyReviewed = userAnswers != null ? userAnswers.size() : 0;
        int wordsToReviewAgain = totalWordsActuallyReviewed - wordsMarkedKnownByClient;

        String suggestion;
        if (wordsToReviewAgain > 0) {
            suggestion = "Gợi ý: Lịch ôn tập của bạn đã được cập nhật. " + wordsToReviewAgain +
                    " từ cần ôn lại sẽ được ưu tiên trong lần tới. Hãy tiếp tục ôn tập đều đặn!";
        } else if (totalWordsActuallyReviewed > 0) {
            suggestion = "Tuyệt vời! Bạn đã ghi nhớ tốt tất cả các từ đã ôn.";
        } else {
            suggestion = "Hãy bắt đầu ôn tập để cải thiện vốn từ vựng của bạn!";
        }
        String responseSessionId = String.valueOf(reviewScheduleId);
        String reviewMethod = "Flashcard"; // Get this from request.getTopicType() or ReviewSchedule if stored

        return new VocabularyReviewEndResponse(
                responseSessionId,
                reviewMethod,
                totalWordsActuallyReviewed,
                wordsMarkedKnownByClient,
                wordsToReviewAgain,
                suggestion,
                nextReviewTimeInfos
        );

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
        String userId = request.userId();
        List<LearningSessionWord> allVocabularyByUser = learningSessionWordRepository.findAllByUserId(userId);
        Map<Long, List<LearningSessionWord>> vocabularyByWordId = allVocabularyByUser.stream()
                .collect(Collectors.groupingBy(word -> word.getVocabularyWord().getId()));

        List<VocabularyWordsReviewList> reviewInputList = vocabularyByWordId.values().stream()
                .map(words -> words.stream().max(Comparator.comparing(LearningSessionWord::getAttemptAt)).orElse(null))
                .filter(Objects::nonNull)
                .map(sessionWord -> new VocabularyWordsReviewList(
                        sessionWord.getId(),
                        sessionWord.getLearningSession().getId(),
                        sessionWord.getVocabularyWord().getId(),
                        sessionWord.getStatus(),
                        sessionWord.getVocabularyWord().getWord(),
                        sessionWord.getVocabularyWord().getMeaning()
                ))
                .filter(r -> r.vocabularyStatus() == LearningStatusVocabulary.FORGOT || r.vocabularyStatus() == LearningStatusVocabulary.REMEMBERED)
                .sorted(Comparator.comparing(r -> getStatusPriority(r.vocabularyStatus())))
                .collect(Collectors.toList());


        List<LearningSessionWord> learningSessionWords = learningSessionWordRepository.findAllByLearningSession_IdIn(reviewInputList.stream().map(item ->
                item.sessionLearningColumnId()).collect(Collectors.toList()));

        List<VocabularyWord> vocabularyWordList = learningSessionWords.stream().map(item ->
                item.getVocabularyWord()).collect(Collectors.toList());

        ReviewSchedule reviewSchedule = new ReviewSchedule();
        reviewSchedule.setUserId(getCurrentUser());
        reviewSchedule.setWord_id(vocabularyWordList);
        reviewSchedule.setStatus("LEARNING");
        ReviewSchedule savedReviewSchedule = reviewScheduleRepository.save(reviewSchedule);

        for (LearningSessionWord word : learningSessionWords) {
            word.setReviewSchedule(savedReviewSchedule); // make sure this setter exists
        }

// 5. Save the updated session words
        learningSessionWordRepository.saveAll(learningSessionWords);

        List<VocabularyWordViewModel> vocabularyWordViewModels = learningSessionWords.stream().map(item -> {

            return new VocabularyWordViewModel(
                    item.getId(),
                    item.getStatus(),
                    item.getVocabularyWord().getId(),
                    item.getVocabularyWord().getWord(),
                    item.getVocabularyWord().getMeaning(),
                    String.valueOf(item.getVocabularyWord().getLevelTypeVocabulary()),
                    item.getVocabularyWord().getImageUrl(),
                    item.getVocabularyWord().getAudioUrl(),
                    item.getVocabularyWord().getQuizQuestionPrompt(),
                    item.getVocabularyWord().getOptionA(),
                    item.getVocabularyWord().getOptionB(),
                    item.getVocabularyWord().getOptionC(),
                    item.getVocabularyWord().getOptionD(),
                    item.getVocabularyWord().getCorrectOption());
        }).toList();
        return new StartLearningReviewResponse(
                reviewSchedule.getId(),
                method,
                vocabularyWordViewModels
        );
    }
}
