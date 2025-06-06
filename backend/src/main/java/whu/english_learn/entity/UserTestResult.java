package whu.english_learn.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserTestResult {
    private String id;
    private User user;
    private TestType testType;
    private int score;
    private Date date;
    public enum TestType {
        看英选中, 看中选英, 拼写, 释义判断
    }
}
