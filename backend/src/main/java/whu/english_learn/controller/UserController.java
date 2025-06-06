package whu.english_learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import whu.english_learn.entity.User;
import whu.english_learn.result.Result;
import whu.english_learn.service.UserService;
import whu.english_learn.service.WordService;

import java.util.Date;

/**
* 功能：
* 作者：
* 日期：2025/5/19 21:23
*/
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userinfo")
    public Result getUserInfo(@RequestParam String username){
        return Result.success(userService.getUserInfo(username));
    }

    @GetMapping("learnedNumber")
    public Result getLearnedNumber(@RequestParam String userId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date){
        return Result.success(userService.getLearnedNumber(userId,date));
    }
}
