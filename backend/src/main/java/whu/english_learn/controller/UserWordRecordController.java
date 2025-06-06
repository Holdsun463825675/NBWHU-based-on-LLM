package whu.english_learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whu.english_learn.dto.WordDTO;
import whu.english_learn.entity.Word;
import whu.english_learn.result.Result;
import whu.english_learn.service.UserWordRecordService;

import java.util.List;

@RestController
@RequestMapping("/wordRecord")
public class UserWordRecordController {
    @Autowired
    private UserWordRecordService userWordRecordService;
    //添加单词到单词记录本
    @PostMapping("/add")
    public Result addWordToWordRecord(@RequestParam String userId,
                                      @RequestParam String wordId) {
        //TODO:添加单词到单词记录本
        userWordRecordService.addWordToWordRecord(userId, wordId);
        return Result.success("添加单词成功");
    }

    //删除单词记录本的单词,根据单词ID
    @DeleteMapping("/delete")
    public Result deleteWordFromWordRecord(@RequestParam String userId,
                                           @RequestParam String wordId) {
        //TODO:删除单词记录本的单词,根据单词ID
        userWordRecordService.deleteWordFromWordRecord(userId,wordId);
        return Result.success("删除单词成功");
    }

    //根据用户id获取单词记录本中的全部单词
    @GetMapping("/get/{id}")
    public Result getAllWordsInWordRecord(@PathVariable("id") String userId) {
        //TODO:根据用户id获取单词记录本中的全部单词
        List<WordDTO> result = userWordRecordService.getAllWordsInWordRecord(userId);
        return Result.success(result);
    }
}
