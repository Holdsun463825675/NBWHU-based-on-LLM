package whu.english_learn.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import whu.english_learn.entity.UserWordStatus;

@Mapper
public interface UserWordStatusMapper {
    // 新增用户单词状态记录
    int insert(UserWordStatus status);

    // 更新用户单词状态
    int updateStatus(String userId, String wordId, UserWordStatus.Status status);

    // 检查是否存在记录
    Integer exists(@Param("userId") String userId, @Param("wordId") String wordId);
    //更新表示用户已掌握单词
    void updateWordStatus(String username, String word, UserWordStatus.Status status);
}
