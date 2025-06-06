package whu.english_learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import whu.english_learn.dto.WordDTO;
import whu.english_learn.dto.WordListDTO;
import whu.english_learn.entity.SynonymAndConfusion;
import whu.english_learn.entity.Word;
import whu.english_learn.result.Result;
import whu.english_learn.service.WordService;
import whu.english_learn.service.SynonymAndConfusionService;
import whu.english_learn.result.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/word")
public class WordController {

    @Autowired
    private WordService wordService;

    @Autowired
    private SynonymAndConfusionService synonymAndConfusionService;

    @GetMapping("/wordsearch")
    public Result searchWords(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        WordListDTO result = wordService.searchWords(keyword, page, size);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result getWordDetail(@PathVariable String id) {
        WordDTO result = wordService.getWordDetail(id);
        return Result.success(result);
    }

    @PostMapping("/getWordsInfo")
    public Result getWordsInfo(@RequestBody String wordsString) {
        try {
            // 分割单词字符串
            String[] words = wordsString.split("\n");
            List<Map<String, Object>> resultList = new ArrayList<>();

            for (String word : words) {
                word = word.trim();
                if (word.isEmpty()) continue;

                Map<String, Object> wordInfo = new HashMap<>();
                wordInfo.put("word", word);

                // 先在word表中查找
                Word wordEntity = wordService.getWordByWord(word);
                if (wordEntity != null) {
                    wordInfo.put("type", "word");
                    wordInfo.put("info", wordEntity);
                } else {
                    // 在synonym_and_confusion表中查找
                    SynonymAndConfusion synonymEntity = synonymAndConfusionService.getSynonymByWord(word);
                    if (synonymEntity != null) {
                        wordInfo.put("type", "synonym");
                        wordInfo.put("info", synonymEntity);
                    } else {
                        wordInfo.put("type", "not_found");
                        wordInfo.put("info", null);
                    }
                }
                resultList.add(wordInfo);
            }

            return Result.success("获取单词信息成功", resultList);
        } catch (Exception e) {
            return Result.error("获取单词信息失败：" + e.getMessage());
        }
    }

    @GetMapping("/getWordInfo")
    public Result getWordInfo(@RequestParam String word) {
        Word wordEntity = wordService.getWordByWord(word);
        return Result.success("获取单词信息成功", wordEntity);
    }
}