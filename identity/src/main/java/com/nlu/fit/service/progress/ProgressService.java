package com.nlu.fit.service.progress;

import com.nlu.fit.model.progress.*;
import com.nlu.fit.repository.progress.ChartDataPointRepository;
import com.nlu.fit.repository.progress.DailyProgressEntryRepository;
import com.nlu.fit.repository.progress.LearningPathProposalRepository;
import com.nlu.fit.repository.progress.UserLearningPathRepository;
import com.nlu.fit.viewmodel.learningPath.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProgressService {
    private final LearningPathProposalRepository learningPathProposalRepository;
    private final UserLearningPathRepository userLearningPathRepository;
    private final DailyProgressEntryRepository dailyProgressEntryRepository;
    private final ChartDataPointRepository chartDataPointRepository;


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

    public List<DailyProgressEntry> getDailyProgressDetails(String userId) {
        List<DailyProgressEntry> dailyProgressEntry = dailyProgressEntryRepository.findByUserIdOrderByDate(userId);
        return dailyProgressEntry;
    }

    public ProgressOverviewViewModel getProgressOverView(String userId) {
        ProgressStats progressStats = userLearningPathRepository.findByUserIdAndIsActive(userId, true).getProgressStats();

        int vocabularyProgress = caculatePercentage(progressStats.getLearnedVocab(), 40);
        int grammarProgress = caculatePercentage(progressStats.getLessonsCompleted(), progressStats.getTotalLessons());
        int learningPathProgress = (int) progressStats.getCompletionPercentage();

        return ProgressOverviewViewModel.builder().grammarProgress(grammarProgress)
                .learningPathProgress(learningPathProgress).vocabularyProgress(vocabularyProgress).build();
    }

    private int caculatePercentage(int lessonsCompleted, int totalLessons) {
        if(totalLessons == 0) return 0;
        return lessonsCompleted * 100 / totalLessons;
    }



    /*
    *
 {
  "userId": "2",
  "topicId": 1,
  "topicType": "VOCABULARY",
  "topicTitle": "Ôn Tập Từ Vựng Gia Đình",
  "timeSpentSeconds": 24
}
    *
    * */
    @Transactional
    public void recordTopicCompletion(UpdateProgress updateProgress) {
        String userId = updateProgress.userId();
        String topicType = updateProgress.topicType();
        String topicTitle = updateProgress.topicTitle();
        int timeSpentSeconds = updateProgress.timeSpentSeconds();

        // --- BƯỚC 1: TÌM LỘ TRÌNH HỌC ĐANG HOẠT ĐỘNG ---
        UserLearningPath userPath = userLearningPathRepository.findByUserIdAndIsActive(userId, true);
        // --- BƯỚC 2: CẬP NHẬT THỐNG KÊ TỔNG QUAN (ProgressStats) ---
        updateOverallStats(userPath, topicType);

        // --- BƯỚC 3: GHI LẠI HOẠT ĐỘNG HÀNG NGÀY (DailyProgressEntry) ---
        logDailyActivity(userId, topicTitle, topicType, timeSpentSeconds);

        // --- BƯỚC 4: CẬP NHẬT DỮ LIỆU BIỂU ĐỒ (ChartDataPoint) ---
        updateChartData(userPath, timeSpentSeconds);

    }

    private void updateOverallStats(UserLearningPath userPath, String topicType) {
        ProgressStats stats = userPath.getProgressStats();
        if (stats == null) {
            // Trường hợp dữ liệu khởi tạo bị lỗi, tạo mới để tránh NullPointerException
            stats = new ProgressStats();
            userPath.setProgressStats(stats);
            stats.setUserLearningPath(userPath);
        }

        // Tăng số bài học đã hoàn thành
        stats.setLessonsCompleted(stats.getLessonsCompleted() + 1);

        // Tăng số từ vựng nếu là topic từ vựng
        if ("VOCABULARY".equalsIgnoreCase(topicType)) {
            stats.setLearnedVocab(stats.getLearnedVocab() + 1);
        }
        if ("GRAMMAR".equalsIgnoreCase(topicType)) {
            stats.setLessonsCompleted(stats.getLessonsCompleted() + 1);
        }

        // Tính toán lại phần trăm hoàn thành
        int totalLessons = userPath.getLearningPathProposal().getGrammarTopics().size() +
                userPath.getLearningPathProposal().getVocabularyTopics().size();
        stats.setTotalLessons(totalLessons);

        if (totalLessons > 0) {
            double percentage = ((double) stats.getLessonsCompleted() / totalLessons) * 100.0;
            stats.setCompletionPercentage(percentage);
        }
    }

    private void logDailyActivity(String userId, String topicTitle, String topicType, int timeSpentSeconds) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay(); // 00:00 hôm nay
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);   // 23:59:59... hôm nay

        // Tìm xem đã có entry cho ngày hôm nay chưa
        DailyProgressEntry dailyEntry = dailyProgressEntryRepository
                .findByUserIdAndDateBetween(userId, startOfDay, endOfDay)
                .orElseGet(() -> {
                    // Nếu chưa có, tạo mới
                    DailyProgressEntry newEntry = new DailyProgressEntry();
                    newEntry.setUserId(userId);
                    newEntry.setDate(LocalDateTime.now());
                    newEntry.setActivities(new ArrayList<>());
                    return newEntry;
                });

        // Tạo một item hoạt động mới
        DailyActivityItem newActivity = new DailyActivityItem();
        newActivity.setTitle(topicTitle);
        newActivity.setType(topicType);
        newActivity.setTimeSpentSecondsToday(timeSpentSeconds);

        // Thêm hoạt động mới vào danh sách của ngày
        dailyEntry.getActivities().add(newActivity);

        // Lưu lại (cả trường hợp tạo mới và cập nhật)
        dailyProgressEntryRepository.save(dailyEntry);
    }

    private void updateChartData(UserLearningPath userPath, int timeSpentSeconds) {
        LocalDate today = LocalDate.now();

        // Tìm điểm dữ liệu cho ngày hôm nay
        ChartDataPoint dataPoint = chartDataPointRepository
                .findByUserLearningPathAndDate(userPath, today)
                .orElseGet(() -> {
                    // Nếu chưa có, tạo mới
                    ChartDataPoint newPoint = new ChartDataPoint();
                    newPoint.setDate(today);
                    newPoint.setUserLearningPath(userPath);
                    return newPoint;
                });

        // Cập nhật các giá trị
        dataPoint.setItemsCompletedToday(dataPoint.getItemsCompletedToday() + 1);
        dataPoint.setTimeSpentSecondsToday(dataPoint.getTimeSpentSecondsToday() + timeSpentSeconds);

        // Lưu lại (cả trường hợp tạo mới và cập nhật)
        chartDataPointRepository.save(dataPoint);
    }

}
