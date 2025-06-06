package whu.english_learn.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.english_learn.entity.VerificationCode;
import whu.english_learn.mapper.VerificationCodeMapper;
import whu.english_learn.service.VerificationCodeService;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Autowired
    VerificationCodeMapper verificationCodeMapper;

    @Override
    public void addCode(VerificationCode verificationCode) {
        verificationCodeMapper.addCode(verificationCode);
    }

    @Override
    public void deleteCode(VerificationCode verificationCode) {
        verificationCodeMapper.deleteCode(verificationCode);
    }

    @Override
    public String getCode(String email) {return verificationCodeMapper.getCode(email);}
}
