package whu.english_learn.dto;

import lombok.Data;
import java.util.Date;

@Data
public class DialogueRecordDTO {
    private String dialogueId;
    private String content;
    private Date timestamp;
    private String role;
} 