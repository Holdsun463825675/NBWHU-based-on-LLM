package whu.english_learn.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.english_learn.dto.WordDTO;
import whu.english_learn.entity.UserWordRecord;
import whu.english_learn.entity.Word;
import whu.english_learn.mapper.UserWordRecordMapper;
import whu.english_learn.mapper.WordMapper;
import whu.english_learn.service.UserWordRecordService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserWordRecordServiceImpl implements UserWordRecordService {
    @Autowired
    private UserWordRecordMapper userWordRecordMapper;
    @Autowired
    private WordMapper wordMapper;

    @Override
    public void addWordToWordRecord(String userId,String wordId) {
        UserWordRecord userWordRecord = new UserWordRecord();
        //设置对象信息
        userWordRecord.setUserId(userId);
        userWordRecord.setWordId(wordId);
        userWordRecord.setId(UUID.randomUUID().toString());
        //添加信息到数据库
        userWordRecordMapper.insert(userWordRecord);
    }

    @Override
    public void deleteWordFromWordRecord(String userId,String wordId) {
        userWordRecordMapper.delete(userId,wordId);
    }

    @Override
    public List<WordDTO> getAllWordsInWordRecord(String userId) {
        //首先根据用户id获取该用户的所有记录的单词
        List<String> wordIds = userWordRecordMapper.getAllWordsInWordRecord(userId);
        //根据得到的id获取对应的word，封装成List<WordDTO>形式
        List<Word> words = wordMapper.getWordsByIds(wordIds);
        List<WordDTO> result= words.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return result;
    }

    //方法函数 作用为将单词转换为DTO形式
    private WordDTO convertToDTO(Word word) {
        WordDTO dto = new WordDTO();
        dto.setId(word.getId());
        dto.setWord(word.getWord());
        dto.setPhonetic(word.getPhonetic());
        dto.setDifficultyLevel(word.getDifficultyLevel());

        // 转换标签
        List<String> tags = new ArrayList<>();
        if (word.getTag1() != null) tags.add(word.getTag1());
        if (word.getTag2() != null) tags.add(word.getTag2());
        if (word.getTag3() != null) tags.add(word.getTag3());
        dto.setTags(tags);

        // 转换中文释义
        List<String> definitions = Arrays.asList(
                        word.getChineseDefinition1(),
                        word.getChineseDefinition2(),
                        word.getChineseDefinition3(),
                        word.getChineseDefinition4(),
                        word.getChineseDefinition5()
                ).stream()
                .filter(def -> def != null && !def.trim().isEmpty())
                .collect(Collectors.toList());
        dto.setChineseDefinitions(definitions);

        // 转换例句
        List<String> examples = Arrays.asList(
                        word.getExampleSentence1(),
                        word.getExampleSentence2(),
                        word.getExampleSentence3(),
                        word.getExampleSentence4(),
                        word.getExampleSentence5()
                ).stream()
                .filter(ex -> ex != null && !ex.trim().isEmpty())
                .collect(Collectors.toList());
        dto.setExampleSentences(examples);

        // 转换近义词
        List<String> synonyms = new ArrayList<>();
        if (word.getSynonym1() != null) synonyms.add(word.getSynonym1());
        if (word.getSynonym2() != null) synonyms.add(word.getSynonym2());
        if (word.getSynonym3() != null) synonyms.add(word.getSynonym3());
        dto.setSynonyms(synonyms);

        // 转换易混淆词
        List<String> confusions = new ArrayList<>();
        if (word.getConfusion1() != null) confusions.add(word.getConfusion1());
        if (word.getConfusion2() != null) confusions.add(word.getConfusion2());
        if (word.getConfusion3() != null) confusions.add(word.getConfusion3());
        dto.setConfusions(confusions);

        return dto;
    }
}
