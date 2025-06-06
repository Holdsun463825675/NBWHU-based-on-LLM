package whu.english_learn.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Dialogue {
    private String id;
    private String userId;
    private String name;
    private String theme;
    private Date buildTime;
    private Date updateTime;
}
