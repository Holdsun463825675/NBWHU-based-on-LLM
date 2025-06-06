package whu.english_learn.service;

import whu.english_learn.dto.DialogueDTO;
import whu.english_learn.dto.DialogueRecordDTO;
import whu.english_learn.entity.Dialogue;
import whu.english_learn.entity.DialogueRecord;
import java.util.List;

public interface DialogueService {
    boolean createDialogue(DialogueDTO dialogueDTO);
    boolean addDialogueRecord(DialogueRecordDTO recordDTO);
    List<DialogueRecord> getDialogueRecords(String dialogueId);
    List<DialogueRecord> getUserDialogueRecords(String userId);
    List<Dialogue> getUserDialogues(String userId);
} 