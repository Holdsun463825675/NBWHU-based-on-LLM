package whu.english_learn.dto;

import java.util.List;

import lombok.Data;

@Data
public class WordListDTO {
    private List<WordDTO> words;
    private int total;
    private int page;
    private int size;
} 