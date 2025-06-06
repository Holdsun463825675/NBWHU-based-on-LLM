package whu.english_learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;
import whu.english_learn.dto.DialogueDTO;
import whu.english_learn.dto.DialogueRecordDTO;
import whu.english_learn.entity.Dialogue;
import whu.english_learn.entity.DialogueRecord;
import whu.english_learn.result.Result;
import whu.english_learn.service.DialogueService;

import java.util.List;

@RestController
@RequestMapping("/dialogue")
@ComponentScan(basePackages = "whu.english_learn.service")
public class DialogueController {

    @Autowired
    private DialogueService dialogueService;

    @PostMapping("/create")
    public boolean createDialogue(@RequestBody DialogueDTO dialogueDTO) {
        return dialogueService.createDialogue(dialogueDTO);
    }

    @PostMapping("/record")
    public boolean addDialogueRecord(@RequestBody DialogueRecordDTO recordDTO) {
        return dialogueService.addDialogueRecord(recordDTO);
    }

    @GetMapping("/records/{dialogueId}")
    public Result getDialogueRecords(@PathVariable String dialogueId) {
        List<DialogueRecord> records = dialogueService.getDialogueRecords(dialogueId);
        if (records != null) {
            return Result.success("获取对话记录成功", records);
        } else {
            return Result.error("获取对话记录失败");
        }
    }

    @GetMapping("/user-records/{userId}")
    public Result getUserDialogues(@PathVariable String userId) {
        List<Dialogue> dialogues = dialogueService.getUserDialogues(userId);
        if (dialogues != null) {
            return Result.success("获取用户对话列表成功", dialogues);
        } else {
            return Result.error("获取用户对话列表失败");
        }
    }
} 