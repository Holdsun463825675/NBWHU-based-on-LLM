package whu.english_learn.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.english_learn.entity.VerificationCode;

@Mapper
public interface VerificationCodeMapper {

    void deleteCode(VerificationCode verificationCode);

    void addCode(VerificationCode verificationCode);

    String getCode(String email);
}
