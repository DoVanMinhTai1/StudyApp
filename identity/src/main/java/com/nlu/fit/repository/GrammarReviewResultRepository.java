package com.nlu.fit.repository;

import com.nlu.fit.model.GrammarReviewResult;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrammarReviewResultRepository extends JpaRepository<GrammarReviewResult, Long> {

    List<GrammarReviewResult> findAllByUserId(String userId);
}
