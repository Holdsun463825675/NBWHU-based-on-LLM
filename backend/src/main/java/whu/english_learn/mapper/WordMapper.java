package whu.english_learn.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import whu.english_learn.entity.Word;
import java.util.List;

@Mapper
public interface WordMapper {
    
    @Select("SELECT * FROM word WHERE word LIKE CONCAT(#{keyword}, '%') LIMIT #{offset}, #{limit}")
    List<Word> searchWords(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM word WHERE word LIKE CONCAT(#{keyword}, '%')")
    int countWords(@Param("keyword") String keyword);
    
    @Select("SELECT * FROM word WHERE id = #{id}")
    Word getWordById(@Param("id") String id);

    List<Word> getWordsByTags(@Param("tags") String tags);

    String getWordIdByWord(String word);

    List<Word> getWordsByIds(List<String> wordIds);

    @Select("SELECT * FROM word WHERE word = #{word}")
    Word selectByWord(String word);
}