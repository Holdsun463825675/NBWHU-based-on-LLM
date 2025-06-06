package whu.english_learn.dto;

import lombok.Data;

@Data
public class WordTestDTO {
    private String id;
    private String word;
    private String phonetic;
    private String chineseDefinition;
    private String confusion1;
    private String confusion2;
    private String confusion3;
    private String confusion1Chinese;
    private String confusion2Chinese;
    private String confusion3Chinese;
} 