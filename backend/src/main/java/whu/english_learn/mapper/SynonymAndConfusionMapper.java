package whu.english_learn.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import whu.english_learn.entity.SynonymAndConfusion;

@Mapper
public interface SynonymAndConfusionMapper {
    /**
     * 根据单词获取同义词和易混词
     * @param word 单词
     * @return 同义词和易混词信息
     */
    SynonymAndConfusion getByWord(String word);

    @Select("SELECT * FROM synonym_and_confusion WHERE word = #{word}")
    SynonymAndConfusion selectByWord(String word);
} 