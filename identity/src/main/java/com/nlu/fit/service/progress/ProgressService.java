package com.nlu.fit.service.progress;

import com.nlu.fit.model.progress.ChartDataPoint;
import com.nlu.fit.model.progress.LearningPathProposal;
import com.nlu.fit.model.progress.ProgressStats;
import com.nlu.fit.model.progress.UserLearningPath;
import com.nlu.fit.repository.progress.LearningPathProposalRepository;
import com.nlu.fit.repository.progress.UserLearningPathRepository;
import com.nlu.fit.viewmodel.learningPath.ConfirmPathRequest;
import com.nlu.fit.viewmodel.learningPath.LearningPathProposalResponse;
import com.nlu.fit.viewmodel.learningPath.LearningPathRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProgressService {
    private final LearningPathProposalRepository learningPathProposalRepository;
    private final UserLearningPathRepository userLearningPathRepository;


    public LearningPathProposalResponse getLearningPathProposal(LearningPathRequest request) {
        double hoursPerDay = request.hoursPerWeek() / 7;

        LearningPathProposal learningPathProposal = learningPathProposalRepository.findMatchingProposalApproximate(
                request.targetScore(),
                request.studyDuration(),
                hoursPerDay
        ).orElseThrow();

        return LearningPathProposalResponse.builder()
                .id(learningPathProposal.getId())
                .targetAchieved(learningPathProposal.getTargetAchieved())
                .durationForTarget(learningPathProposal.getDurationForTarget())
                .lessonsPerDay(learningPathProposal.getLessonsPerDay())
                .studyTimePerDay(learningPathProposal.getStudyTimePerDay())
                .grammarTopics(learningPathProposal.getGrammarTopics())
                .vocabularyTopics(learningPathProposal.getVocabularyTopics())
                .pathId(String.valueOf(learningPathProposal.getId()))
                .build();
    }


    public UserLearningPath saveCurrentPathInfoLocally(ConfirmPathRequest request) {
        String userId = request.userId();
        Long pathId = request.pathId();

        LearningPathProposal learningPathProposal = learningPathProposalRepository.
                findById(pathId).orElseThrow();
        UserLearningPath userLearningPath = new UserLearningPath();
        ChartDataPoint chartDataPoint = new ChartDataPoint();
        chartDataPoint.setItemsCompletedToday(0);
        chartDataPoint.setDate(LocalDate.now());
        chartDataPoint.setTimeSpentSecondsToday(0);
        List<ChartDataPoint> chartDataPoints = new ArrayList<>();
        chartDataPoints.add(chartDataPoint);
        ProgressStats progressStats = new ProgressStats();
        progressStats.setLessonsCompleted(0);
        progressStats.setTotalLessons(0);
        progressStats.setCompletionPercentage(0);
        progressStats.setLearnedVocab(0);
        List<UserLearningPath> userLearningPaths = userLearningPathRepository.findAll()
                .stream().peek(item -> item.setActive(false)).collect(Collectors.toList());
        userLearningPathRepository.saveAll(userLearningPaths);
        if (learningPathProposal != null) {
            userLearningPath.setUserId(userId);
            userLearningPath.setLearningPathProposal(learningPathProposal);
            userLearningPath.setConfirmed_at(LocalDateTime.now());
            userLearningPath.setActive(true);
            userLearningPath.setChartData(chartDataPoints);
            userLearningPath.setProgressStats(progressStats);
        }

        UserLearningPath userLearnSaved = userLearningPathRepository.
                save(userLearningPath);

        return userLearnSaved;

    }

    public UserLearningPath getUserLearningPathActive(String userId) {
        UserLearningPath learningPathProposal = userLearningPathRepository.findByUserIdAndIsActive(userId, true);
        if (learningPathProposal == null) {
            return new UserLearningPath();
        }
        return learningPathProposal;
    }
}
