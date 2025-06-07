package com.nlu.fit.repository.vocabulary;

import com.nlu.fit.model.vocabulary.VocabularyTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabularyTopicRepository extends JpaRepository<VocabularyTopic, Long> {
}
