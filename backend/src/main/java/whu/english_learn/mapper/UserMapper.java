package whu.english_learn.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.english_learn.entity.User;

@Mapper
public interface UserMapper {

    //根据用户名和密码查询用户，用来实现基础登录功能
    User getByUsernameAndPassword(User user);

    void addUser(User user);

    Integer isUsernameEquals(User user);

    Integer isEmailEquals(User user);

    //根据用户名获取用户，从而获取到用户id；
    User getUserByUsername(String username);

    void addWordBook(String username, String wordBook);

}

