package whu.english_learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.web.bind.annotation.*;
import whu.english_learn.entity.User;
import whu.english_learn.result.Result;
import whu.english_learn.service.SendMailService;

import java.util.Random;

@RestController
@RequestMapping
public class SendMailController {
    @Autowired
    private SendMailService sendMailService;

    @PostMapping("/sendmail")
    public Result sendMail(@RequestBody User user) {
        //返回信息
        try {
            sendMailService.sendMail(user);
            return Result.success("验证码发送成功");
        } catch (RuntimeException e) {
            // 捕获异常并返回具体错误信息
            return Result.error(e.getMessage());
        }
    }
}
