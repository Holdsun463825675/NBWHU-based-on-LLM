package whu.english_learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whu.english_learn.dto.WordTestDTO;
import whu.english_learn.dto.WordTestReportDTO;
import whu.english_learn.entity.DailyWordStatus;
import whu.english_learn.entity.SynonymAndConfusion;
import whu.english_learn.service.DailyWordStatusService;
import whu.english_learn.service.SynonymAndConfusionService;
import whu.english_learn.service.WordTestService;
import whu.english_learn.result.Result;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/wordtest")
public class WordTestController {

    @Autowired
    private WordTestService wordTestService;

    @Autowired
    private SynonymAndConfusionService synonymAndConfusionService;

    @GetMapping("/daily")
    public Result getDailyWords(@RequestParam(required = false) String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            
            List<DailyWordStatus> dailyWords = wordTestService.getDailyWordsByUserIdAndDate(userId, new Date());
            if (dailyWords == null || dailyWords.isEmpty()) {
                return Result.error("今天没有需要学习的单词");
            }
            return Result.success(dailyWords);
        } catch (Exception e) {
            e.printStackTrace(); // 添加日志打印
            return Result.error("获取每日单词失败：" + e.getMessage());
        }
    }

    @GetMapping("/synonym")
    public Result getSynonymAndConfusion(@RequestParam(required = false) String word) {
        try {
            if (word == null || word.trim().isEmpty()) {
                return Result.error("单词不能为空");
            }
            
            SynonymAndConfusion synonymAndConfusion = synonymAndConfusionService.getByWord(word);
            if (synonymAndConfusion == null) {
                return Result.error("未找到该单词的同义词和易混词");
            }
            return Result.success(synonymAndConfusion);
        } catch (Exception e) {
            e.printStackTrace(); // 添加日志打印
            return Result.error("获取同义词和易混词失败：" + e.getMessage());
        }
    }

    @PostMapping("/report")
    public Result saveTestReport(@RequestBody WordTestReportDTO report) {
        try {
            if (report == null || report.getUserId() == null || 
                report.getWordTests() == null || report.getWordTests().isEmpty()) {
                return Result.error("测试报告数据不完整");
            }

            // 验证每个单词测试项的数据完整性
            for (WordTestReportDTO.WordTestItem item : report.getWordTests()) {
                if (item.getWordId() == null || item.getWordId().trim().isEmpty()) {
                    return Result.error("单词ID不能为空");
                }
            }

            if (report.getTestDate() == null) {
                report.setTestDate(new Date());
            }

            boolean success = wordTestService.saveTestReport(report);
            if (success) {
                return Result.success("测试报告保存成功");
            } else {
                return Result.error("测试报告保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("保存测试报告失败：" + e.getMessage());
        }
    }
} 