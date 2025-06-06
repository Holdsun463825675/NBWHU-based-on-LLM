package whu.english_learn.dto;

import lombok.Data;
import java.util.Date;

@Data
public class DialogueDTO {
    private String id;
    private String userId;
    private String name;
    private String theme;
    private Date buildTime;
} 