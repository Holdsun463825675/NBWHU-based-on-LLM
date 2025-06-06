package whu.english_learn.dto;

import java.util.List;

import lombok.Data;
import whu.english_learn.entity.Word.DifficultyLevel;

@Data
public class WordDTO {
    private String id;
    private String word;
    private String phonetic;
    private DifficultyLevel difficultyLevel;
    private List<String> tags;
    private List<String> chineseDefinitions;
    private List<String> exampleSentences;
    private List<String> synonyms;
    private List<String> confusions;
} 