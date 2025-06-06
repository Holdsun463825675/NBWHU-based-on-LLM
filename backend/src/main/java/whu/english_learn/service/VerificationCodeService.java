package whu.english_learn.service;

import whu.english_learn.entity.VerificationCode;

public interface VerificationCodeService {
    public void addCode(VerificationCode verificationCode);
    public void deleteCode(VerificationCode verificationCode);
    public String getCode(String email);
}
