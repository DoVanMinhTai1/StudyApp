package com.nlu.fit.repository;

import com.nlu.fit.model.GrammarTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrammarTopicRepository extends JpaRepository<GrammarTopic, Long> {
}
