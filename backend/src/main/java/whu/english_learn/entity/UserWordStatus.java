package whu.english_learn.entity;

import lombok.Data;

@Data
public class UserWordStatus {
    private String id;
    private String userId;
    private String wordId;
    private Status status = Status.正在学习;
    private int importantIndex;
    public enum Status {
        已掌握, 正在学习
    }
}
