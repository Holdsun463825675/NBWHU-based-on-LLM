package whu.english_learn.service;

import whu.english_learn.dto.WordDTO;
import whu.english_learn.dto.WordListDTO;
import whu.english_learn.entity.Word;
import java.util.List;

public interface WordService {
    WordListDTO searchWords(String keyword, int page, int size);
    WordDTO getWordDetail(String id);

    /**
     * 根据单词查询单词信息
     * @param word 单词
     * @return 单词信息
     */
    Word getWordByWord(String word);
}