package whu.english_learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import whu.english_learn.dto.WordDTO;
import whu.english_learn.result.Result;
import whu.english_learn.service.WordStudyService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/wordStudy")
public class WordStudyController {
    @Autowired
    private WordStudyService wordStudyService;

    /**获取每日单词学习列表并将plannedCount插入到daily_study_record表中,
    /*同时将单词列表插入到daily_word_statu表和user_word_statu表中
     */
    @PostMapping("/getStudyWordsFirst")
    public Result getStudyWordsFirst(@RequestParam String wordBook,
                                @RequestParam(defaultValue = "50") int plannedCount,
                                @RequestParam String username) {
        List<WordDTO> result = wordStudyService.getStudyWordsFirst(wordBook, plannedCount,username);
        return Result.success(result);
    }

    /**获取每日单词学习列表,
     /*将单词列表插入到daily_word_statu表和user_word_statu表中
     */
    @PostMapping("/getStudyWords")
    public Result getStudyWords(@RequestParam String username) {
        List<WordDTO> result = wordStudyService.getStudyWords(username);
        return Result.success(result);
    }

    /**获取下一单词,
     * 判断用户点击下一个后后续的学习单词
     */
    @PutMapping("/getNextWord")
    public Result getNextWord(@RequestParam String username,
                              @RequestParam String word,
                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                              @RequestParam String action) {
        switch(action) {
            //用户点击认识，则对应单词的status-1
            case "认识":
                wordStudyService.knownWord(username, word, date);
                return Result.success("单词status已减1");
            //用户点击不认识，则对应单词的status重置为3，promptTimes加1
            case "不认识":
                wordStudyService.unKnownWord(username, word, date);
                return Result.success("单词status已重置为3");
            //用户点击太简单，则对应单词的status直接置为0，同时更新user_word_statu表中对应单词为已掌握
            case "太简单":
                wordStudyService.tooSimpleWord(username, word, date);
                return Result.success("单词已经掌握");
            //用户点击下一个，则根据一定算法返回相应的一个单词
            case "下一个":
                try {
                    WordDTO result = wordStudyService.getNextWord(username, word, date);
                    if (result == null) {
                        return Result.success("已完成今日学习计划");
                    }
                    return Result.success(result);
                } catch (IllegalStateException e) {
                    return Result.error(e.getMessage());
                }
            default:
                return Result.error("无效操作");
        }
    }
    //执行一个删除一个插入
    @DeleteMapping("/changeBook")
    public Result changeBook(@RequestParam String username,
                             @RequestParam String wordBook,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        wordStudyService.changeBook(username, wordBook, date);
        return Result.success("单词本已切换");
    }
}
