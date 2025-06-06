package whu.english_learn.entity;

import lombok.Data;

@Data
public class User {
    private String id;
    private String email;
    private String telephone;
    private String username;
    private String password;
    private Identity identity = Identity.未选择;
    private String verificationCode;
    private String wordBook;
    public enum Identity { 未选择, 学生, 职场人士, 儿童 }

}
