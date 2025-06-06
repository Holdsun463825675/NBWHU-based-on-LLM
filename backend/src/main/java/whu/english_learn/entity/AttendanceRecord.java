package whu.english_learn.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AttendanceRecord {
    private String id;
    private User user;
    private AttendanceStatus status;
    private Date date;
    public enum AttendanceStatus {
        打卡, 未打卡
    }

}
