package whu.english_learn.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.english_learn.entity.UserWordRecord;
import whu.english_learn.entity.Word;

import java.util.List;

@Mapper
public interface UserWordRecordMapper {
    void insert(UserWordRecord userWordRecord);

    void delete(String userId,String wordId);

    List<String> getAllWordsInWordRecord(String userId);
}
