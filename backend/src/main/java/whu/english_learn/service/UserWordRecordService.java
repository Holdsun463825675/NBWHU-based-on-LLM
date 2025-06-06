package whu.english_learn.service;

import whu.english_learn.dto.WordDTO;
import whu.english_learn.entity.Word;

import java.util.List;

public interface UserWordRecordService {
    void addWordToWordRecord(String userId, String wordId);

    void deleteWordFromWordRecord(String userId,String wordId);

    List<WordDTO> getAllWordsInWordRecord(String userId);
}
