package whu.english_learn.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import whu.english_learn.entity.User;
import whu.english_learn.result.Result;
import whu.english_learn.service.UserService;
import whu.english_learn.utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        //接受返回的登陆用户数据
        User u = userService.login(user);

        //登录成功后生成并下发JWT令牌
        if(u != null) {
            Map<String, Object> claims = new HashMap<>();

            //设置相关员工信息供JWT令牌保存
            claims.put("id", u.getId());
            claims.put("username", u.getUsername());
            claims.put("identity", u.getIdentity());

            //生成令牌
            String jwt = JwtUtils.generateJwt(claims);

            //返回给前端
            return Result.success(jwt,u);
        }

        //登陆失败返回相关信息
        else {
            return Result.error("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        int ReturnCode = userService.register(user);
        switch (ReturnCode){
            case 0:
                return Result.error("用户名已被注册");
            case 1:
                return Result.error("验证码错误");
            case 2:
                return Result.error("邮箱已被注册");
            default:
                return Result.success("注册成功");
        }
    }
}
