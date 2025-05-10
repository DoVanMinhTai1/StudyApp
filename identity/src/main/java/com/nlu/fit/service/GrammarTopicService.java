package com.nlu.fit.service;

import com.nlu.fit.model.GrammarTopic;
import com.nlu.fit.repository.GrammarTopicRepository;
import com.nlu.fit.viewmodel.grammarexercise.ExerciseResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GrammarTopicService {
    private final GrammarTopicRepository grammarTopicRepository;

    public List<GrammarTopic> getAll() {
        return grammarTopicRepository.findAll();
    }


}
