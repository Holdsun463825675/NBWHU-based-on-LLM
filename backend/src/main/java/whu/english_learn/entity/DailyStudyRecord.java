package whu.english_learn.entity;

import lombok.Data;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.Date;

@Data
public class DailyStudyRecord {
    private String id;
    private String userId; // 直接映射字段
    private int planNumber;
    private int learnedNumber;
    private LocalDate date;

    private User user; // 仅用于业务逻辑

    public void setUser(User user) {
        this.user = user;
        this.userId = user.getId();
    }
}
