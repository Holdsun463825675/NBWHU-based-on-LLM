package whu.english_learn.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.english_learn.entity.User;
import whu.english_learn.entity.VerificationCode;
import whu.english_learn.mapper.DailyStudyRecordMapper;
import whu.english_learn.mapper.UserMapper;
import whu.english_learn.mapper.VerificationCodeMapper;
import whu.english_learn.service.UserService;
import whu.english_learn.service.VerificationCodeService;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private VerificationCodeService v;
    @Autowired
    private DailyStudyRecordMapper dailyStudyRecordMapper;

    @Override
    public User login(User user) {
        return userMapper.getByUsernameAndPassword(user);
    }
    @Override
    public int register(User user) {
        //获取用户验证码
        String code = v.getCode(user.getEmail());
        //如果用户名已存在，返回0；如果验证码不正确，返回1，如果邮箱已经被注册，返回2，如果不出现上述情况，注册成功返回3；
        if (userMapper.isUsernameEquals(user) > 0) {
            return 0;  // 用户名已存在
        } else if (!Objects.equals(code, user.getVerificationCode())) {
            return 1;  // 验证码不正确
        }
        else if (userMapper.isEmailEquals(user) > 0){
            return 2; //邮箱已被注册
        } else {
            user.setId(UUID.randomUUID().toString());  // 设置用户ID
            userMapper.addUser(user);  // 添加用户
            //注册成功后删除验证码
            VerificationCode verificationCode = new VerificationCode();
            //封装验证码信息
            verificationCode.setEmail(user.getEmail());
            verificationCode.setCode(code);
            //删除验证码
            v.deleteCode(verificationCode);
            return 3;  // 注册成功
        }

    }

    @Override
    public User getUserInfo(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public Integer getLearnedNumber(String userId, Date date) {
        return dailyStudyRecordMapper.getLearnedNumber(userId,date);
    }
}

