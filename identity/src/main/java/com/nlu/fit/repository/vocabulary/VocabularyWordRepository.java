package com.nlu.fit.repository.vocabulary;

import com.nlu.fit.enumeration.LevelTypeVocabulary;
import com.nlu.fit.model.vocabulary.VocabularyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyWordRepository extends JpaRepository<VocabularyWord, Long> {
    List<VocabularyWord> findAllByVocabularyTopic_Id(Long vocabularyTopicId);

    List<VocabularyWord> findByVocabularyTopic_Id(Long vocabularyTopicId);
}
