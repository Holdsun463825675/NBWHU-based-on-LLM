package whu.english_learn.service;

import whu.english_learn.dto.WordDTO;

import java.util.Date;
import java.util.List;

public interface WordStudyService {
     /**获取每日单词学习列表并将plannedCount插入到daily_study_record表中,
     /*同时将单词列表插入到daily_word_statu表中
     */
    List<WordDTO> getStudyWordsFirst(String wordBook, int plannedCount, String username);
    /**获取每日单词学习列表,
     /*将单词列表插入到daily_word_statu表中
     */
    List<WordDTO> getStudyWords(String username);

    void knownWord(String username, String word, Date date);

    void unKnownWord(String username, String word, Date date);

    void tooSimpleWord(String username, String word, Date date);

    WordDTO getNextWord(String username, String word, Date date);

    void changeBook(String username, String wordBook,Date date);
}
