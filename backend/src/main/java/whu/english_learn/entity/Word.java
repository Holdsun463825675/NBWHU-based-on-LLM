package whu.english_learn.entity;

import lombok.Data;

@Data
public class Word {
    private String id;
    private String word;
    private String phonetic;
    private DifficultyLevel difficultyLevel;
    private String tag1;
    private String tag2;
    private String tag3;

    private String chineseDefinition1;
    private String chineseDefinition2;
    private String chineseDefinition3;
    private String chineseDefinition4;
    private String chineseDefinition5;

    private String exampleSentence1;
    private String exampleSentence2;
    private String exampleSentence3;
    private String exampleSentence4;
    private String exampleSentence5;

    private String synonym1;
    private String synonym2;
    private String synonym3;

    private String confusion1;
    private String confusion2;
    private String confusion3;

    private String confusion1Chinese;
    private String confusion2Chinese;
    private String confusion3Chinese;

    public enum DifficultyLevel {L1, L2, L3, L4}
}