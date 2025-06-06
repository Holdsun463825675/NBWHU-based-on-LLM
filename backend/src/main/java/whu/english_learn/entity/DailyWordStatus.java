package whu.english_learn.entity;

import lombok.Data;
import whu.english_learn.dto.WordTestDTO;
import java.util.Date;

@Data
public class DailyWordStatus {
    private String id;
    private String userId;
    private String wordId;
    private int status;
    private int promptTimes;
    private Date date;
    private int skipCount; // 新增跳过计数器
    private WordTestDTO word;
}
