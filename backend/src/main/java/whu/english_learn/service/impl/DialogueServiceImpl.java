package whu.english_learn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.english_learn.dto.DialogueDTO;
import whu.english_learn.dto.DialogueRecordDTO;
import whu.english_learn.entity.Dialogue;
import whu.english_learn.entity.DialogueRecord;
import whu.english_learn.mapper.DialogueMapper;
import whu.english_learn.mapper.DialogueRecordMapper;
import whu.english_learn.service.DialogueService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DialogueServiceImpl implements DialogueService {

    @Autowired
    private DialogueMapper dialogueMapper;

    @Autowired
    private DialogueRecordMapper dialogueRecordMapper;

    @Override
    public boolean createDialogue(DialogueDTO dialogueDTO) {
        try {
            Dialogue dialogue = new Dialogue();
            dialogue.setId(dialogueDTO.getId());
            dialogue.setUserId(dialogueDTO.getUserId());
            dialogue.setName(dialogueDTO.getName());
            dialogue.setTheme(dialogueDTO.getTheme());
            dialogue.setBuildTime(dialogueDTO.getBuildTime());
            dialogue.setUpdateTime(new Date());
            
            return dialogueMapper.insert(dialogue) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addDialogueRecord(DialogueRecordDTO recordDTO) {
        try {
            // 插入对话记录
            DialogueRecord record = new DialogueRecord();
            record.setId(UUID.randomUUID().toString());
            record.setDialogueId(recordDTO.getDialogueId());
            record.setContent(recordDTO.getContent());
            record.setTimestamp(recordDTO.getTimestamp());
            record.setRole(recordDTO.getRole());
            
            boolean success = dialogueRecordMapper.insert(record) > 0;
            
            // 更新对话的更新时间
            if (success) {
                dialogueMapper.updateUpdateTime(recordDTO.getDialogueId());
            }
            
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<DialogueRecord> getDialogueRecords(String dialogueId) {
        try {
            return dialogueRecordMapper.getRecordsByDialogueId(dialogueId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<DialogueRecord> getUserDialogueRecords(String userId) {
        try {
            // 获取用户的所有对话ID
            List<String> dialogueIds = dialogueMapper.getDialogueIdsByUserId(userId);
            if (dialogueIds == null || dialogueIds.isEmpty()) {
                return new ArrayList<>();
            }

            // 获取所有对话的记录并按对话ID分组
            Map<String, List<DialogueRecord>> recordsByDialogue = new LinkedHashMap<>();
            for (String dialogueId : dialogueIds) {
                List<DialogueRecord> records = dialogueRecordMapper.getRecordsByDialogueId(dialogueId);
                if (records != null && !records.isEmpty()) {
                    // 对每个对话内的记录按时间排序
                    records.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
                    recordsByDialogue.put(dialogueId, records);
                }
            }

            // 将所有记录按对话ID的顺序合并
            List<DialogueRecord> allRecords = new ArrayList<>();
            for (List<DialogueRecord> records : recordsByDialogue.values()) {
                allRecords.addAll(records);
            }

            return allRecords;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Dialogue> getUserDialogues(String userId) {
        try {
            return dialogueMapper.getDialoguesByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
} 