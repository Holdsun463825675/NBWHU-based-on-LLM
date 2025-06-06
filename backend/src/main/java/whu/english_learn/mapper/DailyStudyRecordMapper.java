package whu.english_learn.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import whu.english_learn.entity.DailyStudyRecord;

import java.util.Date;

@Mapper
public interface DailyStudyRecordMapper {
    void insert(DailyStudyRecord dailyStudyRecord);

    int getplanNumberByUserId(String id);

    DailyStudyRecord getTodayRecord(
            @Param("userId") String userId,
            @Param("date") Date date
    );

    void updateLearnedNumber(DailyStudyRecord record);

    Integer getLearnedNumber(String userId, Date date);

    void incrementLearnedNumber(String id, int learnedNumber);
}
