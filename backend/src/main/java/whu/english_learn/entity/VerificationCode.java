package whu.english_learn.entity;

import lombok.Data;

@Data
public class VerificationCode {
    private String code;
    private String email;
}
