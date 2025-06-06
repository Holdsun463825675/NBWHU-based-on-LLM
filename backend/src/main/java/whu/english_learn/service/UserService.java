package whu.english_learn.service;

import whu.english_learn.entity.User;
import whu.english_learn.result.Result;

import java.util.Date;

public interface UserService {
    //基础登录功能
    User login(User user);
    int register(User user);

    User getUserInfo(String username);

    Integer getLearnedNumber(String userId, Date date);
}
