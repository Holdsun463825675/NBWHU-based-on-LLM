package whu.english_learn.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import whu.english_learn.entity.WordTestStatus;
import java.util.Date;
import java.util.List;

@Mapper
public interface WordTestStatusMapper {
    /**
     * 批量插入测试状态记录
     * @param statusList 测试状态列表
     * @return 影响的行数
     */
    int batchInsert(@Param("statusList") List<WordTestStatus> statusList);

    /**
     * 获取用户已测试的单词ID列表
     * @param userId 用户ID
     * @param date 日期
     * @return 已测试的单词ID列表
     */
    List<String> getTestedWordIds(@Param("userId") String userId, @Param("date") Date date);
} 