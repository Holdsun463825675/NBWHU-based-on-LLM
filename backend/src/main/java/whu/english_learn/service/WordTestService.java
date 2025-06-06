package whu.english_learn.service;

import whu.english_learn.dto.WordTestDTO;
import whu.english_learn.dto.WordTestReportDTO;
import whu.english_learn.entity.DailyWordStatus;
import java.util.Date;
import java.util.List;

public interface WordTestService {
    /**
     * 获取用户每日单词列表
     * @param userId 用户ID
     * @param date 日期
     * @return 每日单词列表
     */
    List<DailyWordStatus> getDailyWordsByUserIdAndDate(String userId, Date date);

    /**
     * 获取单词测试信息
     * @param wordId 单词ID
     * @return 单词测试信息
     */
    WordTestDTO getWordTestInfo(String wordId);

    /**
     * 获取每日单词测试
     * @param userId 用户ID
     * @return 单词测试信息
     */
    WordTestDTO getDailyWordTest(String userId);

    /**
     * 保存测试报告
     * @param report 测试报告
     * @return 是否保存成功
     */
    boolean saveTestReport(WordTestReportDTO report);
} 