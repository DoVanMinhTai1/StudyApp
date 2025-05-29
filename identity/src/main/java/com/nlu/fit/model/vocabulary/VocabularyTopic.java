package com.nlu.fit.model.vocabulary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "vocabulary_topic")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "vocabularyTopic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<VocabularyWord> vocabularyWord;
}
