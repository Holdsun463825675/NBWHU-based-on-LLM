package whu.english_learn.entity;

import lombok.Data;

@Data
public class WordAllocation {
    private String id;
    private Word word;
    private WordBook wordBook;

}
