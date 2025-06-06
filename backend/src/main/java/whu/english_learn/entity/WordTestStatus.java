package whu.english_learn.entity;

import lombok.Data;

import java.util.Date;

@Data
public class WordTestStatus {
    private String id;
    private String userId;
    private String wordId;
    private String testType;  // 测试类型，默认为"看英选中"
    private Status status;
    private Date date;

    public enum Status {
        正确,
        错误
    }
}
