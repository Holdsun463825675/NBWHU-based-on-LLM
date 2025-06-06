package whu.english_learn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import whu.english_learn.entity.User;
import whu.english_learn.entity.VerificationCode;
import whu.english_learn.mapper.VerificationCodeMapper;
import whu.english_learn.service.SendMailService;
import jakarta.mail.internet.MimeMessage;
import whu.english_learn.service.VerificationCodeService;

import java.util.Random;

/**
 * 发送邮箱验证码
 */
@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private VerificationCodeService verificationCodeMapper;

    private VerificationCode verificationCode = new VerificationCode();
    @Override
    public void sendMail(User user) {
        //生成验证码
        Random random= new Random();
        int code = 100001 + random.nextInt(900000);
        //封装数据库对象
        String code1 = String.valueOf(code);
        verificationCode.setCode(code1);
        verificationCode.setEmail(user.getEmail());
        //保存验证码到数据库
        verificationCodeMapper.addCode(verificationCode);
        //调用javamail方法发送邮箱
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setTo(user.getEmail());
                helper.setFrom("1094212332@qq.com");
                helper.setSubject("Whu智能英语学习平台验证码");
                //设置邮件文本
                helper.setText(
                        "<p>尊敬的" + user.getUsername() + ",您好:</p>" +
                                "<p>您正在Whu智能英语学习平台进行注册，本次请求的邮件验证码是：<span style='color: blue; font-weight: bold;'>" +
                                code + "</span>(为了保证您账号的安全性，请您在5分钟内完成验证).</p>" +
                                "<p>本验证码5分钟内有效，请及时输入。</p>" +
                                "<p>为保证账号安全，请勿泄漏此验证码。</p>",
                        true // true 表示邮件内容支持 HTML 格式
                );


            }
        };

        try {
            mailSender.send(preparator);
            System.out.println("邮件发送成功！");
        } catch (MailException e) {
            // 处理邮件发送失败的情况
            System.err.println("邮件发送失败，错误信息：" + e.getMessage());
            // 如果邮件地址不可达（例如目标邮箱不存在），可以捕获错误并提供反馈
            if (e.getMessage().contains("550")) {
                // 如果是目标邮箱不存在的错误，返回具体的错误信息
                throw new RuntimeException("邮件发送失败，目标邮箱不存在或不可达，请检查邮箱地址！");
            } else {
                // 其他邮件发送错误处理
                throw new RuntimeException("邮件发送失败，请稍后重试！");
            }
        }
    }
}
