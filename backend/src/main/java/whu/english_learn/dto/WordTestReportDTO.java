package whu.english_learn.dto;

import lombok.Data;
import whu.english_learn.entity.WordTestStatus.Status;
import java.util.Date;
import java.util.List;

@Data
public class WordTestReportDTO {
    private String userId;
    private List<WordTestItem> wordTests;
    private Date testDate;

    @Data
    public static class WordTestItem {
        private String wordId;
        private String testType;  // 测试类型，默认为"看英选中"
        private Status status;  // 正确 或 错误
    }
} 