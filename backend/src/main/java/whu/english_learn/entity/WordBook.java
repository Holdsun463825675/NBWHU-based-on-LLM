package whu.english_learn.entity;

import lombok.Data;

@Data
public class WordBook {
    private String id;
    private String tag;
    private String name;
    private int wordCount;
}
