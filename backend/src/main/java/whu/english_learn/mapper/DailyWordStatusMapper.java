package whu.english_learn.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import whu.english_learn.entity.DailyWordStatus;
import java.util.Date;
import java.util.List;

@Mapper
public interface DailyWordStatusMapper {
    /**
     * 根据用户ID和日期获取每日单词列表
     * @param userId 用户ID
     * @param date 日期
     * @return 每日单词列表
     */
    List<DailyWordStatus> getDailyWordsByUserIdAndDate(String userId, Date date);

    void insert(DailyWordStatus dailyWordStatus);

    void updateKnownStatus(String username, String word,Date date);

    void updateUnknownStatus(String username, String word, Date date);

    void updateEasyStatus(String username, String word, Date date);

    List<DailyWordStatus> getLearningWords(
            @Param("userId") String userId,
            @Param("date") Date date,
            @Param("planNumber") int planNumber,
            @Param("wordId") String wordId
    );

    DailyWordStatus getCurrentStatus(String userId, String wordId, java.sql.Date date);

    void decrementSkipCounts(@Param("userId") String userId, @Param("date") Date date);

    void deleteByUserId(String userId, Date date);
}