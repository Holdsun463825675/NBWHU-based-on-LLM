package whu.english_learn.service;

import whu.english_learn.entity.DailyWordStatus;
import java.util.Date;
import java.util.List;

public interface DailyWordStatusService {
    /**
     * 根据用户ID和日期获取每日单词列表
     * @param userId 用户ID
     * @param date 日期
     * @return 每日单词列表
     */
    List<DailyWordStatus> getDailyWordsByUserIdAndDate(String userId, Date date);
} 