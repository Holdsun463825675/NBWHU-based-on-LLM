package whu.english_learn.entity;

import lombok.Data;
import java.util.Date;

@Data
public class DialogueRecord {
    private String id;
    private String dialogueId;
    private String content;
    private Date timestamp;
    private String role;  // user 或 assistant
}
