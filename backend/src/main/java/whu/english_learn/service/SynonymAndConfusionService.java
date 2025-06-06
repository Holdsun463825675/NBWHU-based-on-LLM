package whu.english_learn.service;

import whu.english_learn.entity.SynonymAndConfusion;

public interface SynonymAndConfusionService {
    /**
     * 根据单词获取同义词和易混词
     * @param word 单词
     * @return 同义词和易混词信息
     */
    SynonymAndConfusion getByWord(String word);

    /**
     * 根据单词查询同义词/易混淆词信息
     * @param word 单词
     * @return 同义词/易混淆词信息
     */
    SynonymAndConfusion getSynonymByWord(String word);
} 