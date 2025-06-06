package whu.english_learn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.english_learn.dto.WordTestDTO;
import whu.english_learn.dto.WordTestReportDTO;
import whu.english_learn.entity.DailyWordStatus;
import whu.english_learn.entity.SynonymAndConfusion;
import whu.english_learn.entity.WordTestStatus;
import whu.english_learn.mapper.DailyWordStatusMapper;
import whu.english_learn.mapper.SynonymAndConfusionMapper;
import whu.english_learn.mapper.WordTestStatusMapper;
import whu.english_learn.service.WordTestService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WordTestServiceImpl implements WordTestService {

    @Autowired
    private DailyWordStatusMapper dailyWordStatusMapper;

    @Autowired
    private SynonymAndConfusionMapper synonymAndConfusionMapper;

    @Autowired
    private WordTestStatusMapper wordTestStatusMapper;

    @Override
    public List<DailyWordStatus> getDailyWordsByUserIdAndDate(String userId, Date date) {
        return dailyWordStatusMapper.getDailyWordsByUserIdAndDate(userId, date);
    }

    @Override
    public WordTestDTO getWordTestInfo(String wordId) {
        // 这里需要实现获取单词测试信息的逻辑
        return null;
    }

    @Override
    public WordTestDTO getDailyWordTest(String userId) {
        try {
            // 获取当天的日期
            Date today = new Date();
            
            // 从 daily_word_status 表获取当天学习的单词
            List<DailyWordStatus> dailyWords = dailyWordStatusMapper.getDailyWordsByUserIdAndDate(userId, today);
            
            if (dailyWords.isEmpty()) {
                return null;
            }
            
            // 随机选择一个单词
            int randomIndex = (int) (Math.random() * dailyWords.size());
            DailyWordStatus selectedWord = dailyWords.get(randomIndex);
            
            // 获取单词详情
            WordTestDTO word = selectedWord.getWord();
            
            // 获取易混词的中文释义
            if (word.getConfusion1() != null) {
                SynonymAndConfusion confusion = synonymAndConfusionMapper.getByWord(word.getConfusion1());
                if (confusion != null) {
                    word.setConfusion1Chinese(confusion.getChineseDefinition1());
                }
            }
            
            if (word.getConfusion2() != null) {
                SynonymAndConfusion confusion = synonymAndConfusionMapper.getByWord(word.getConfusion2());
                if (confusion != null) {
                    word.setConfusion2Chinese(confusion.getChineseDefinition1());
                }
            }
            
            if (word.getConfusion3() != null) {
                SynonymAndConfusion confusion = synonymAndConfusionMapper.getByWord(word.getConfusion3());
                if (confusion != null) {
                    word.setConfusion3Chinese(confusion.getChineseDefinition1());
                }
            }
            
            return word;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean saveTestReport(WordTestReportDTO report) {
        try {
            List<WordTestStatus> statusList = report.getWordTests().stream()
                .map(item -> {
                    WordTestStatus status = new WordTestStatus();
                    status.setId(UUID.randomUUID().toString());
                    status.setUserId(report.getUserId());
                    status.setWordId(item.getWordId());
                    status.setTestType(item.getTestType() != null ? item.getTestType() : "看英选中");
                    status.setStatus(item.getStatus());
                    status.setDate(report.getTestDate());
                    return status;
                })
                .collect(Collectors.toList());

            return wordTestStatusMapper.batchInsert(statusList) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 