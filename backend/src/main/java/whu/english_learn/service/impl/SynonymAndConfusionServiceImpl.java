package whu.english_learn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.english_learn.entity.SynonymAndConfusion;
import whu.english_learn.mapper.SynonymAndConfusionMapper;
import whu.english_learn.service.SynonymAndConfusionService;

@Service
public class SynonymAndConfusionServiceImpl implements SynonymAndConfusionService {

    @Autowired
    private SynonymAndConfusionMapper synonymAndConfusionMapper;

    @Override
    public SynonymAndConfusion getByWord(String word) {
        return synonymAndConfusionMapper.getByWord(word);
    }

    @Override
    public SynonymAndConfusion getSynonymByWord(String word) {
        return synonymAndConfusionMapper.selectByWord(word);
    }
} 