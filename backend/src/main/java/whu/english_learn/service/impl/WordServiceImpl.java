package whu.english_learn.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import whu.english_learn.dto.WordDTO;
import whu.english_learn.dto.WordListDTO;
import whu.english_learn.entity.Word;
import whu.english_learn.entity.Word.DifficultyLevel;
import whu.english_learn.mapper.WordMapper;
import whu.english_learn.service.WordService;

@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Override
    public WordListDTO searchWords(String keyword, int page, int size) {
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 查询单词列表
        List<Word> words = wordMapper.searchWords(keyword, offset, size);
        int total = wordMapper.countWords(keyword);
        
        // 转换为 DTO
        List<WordDTO> wordDTOs = words.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 构建返回结果
        WordListDTO result = new WordListDTO();
        result.setWords(wordDTOs);
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        
        return result;
    }

    @Override
    public WordDTO getWordDetail(String id) {
        Word word = wordMapper.getWordById(id);
        if (word == null) {
            return null;
        }
        return convertToDTO(word);
    }

    @Override
    public Word getWordByWord(String word) {
        return wordMapper.selectByWord(word);
    }

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