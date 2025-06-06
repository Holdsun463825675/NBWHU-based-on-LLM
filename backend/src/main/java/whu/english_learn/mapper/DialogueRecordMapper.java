package whu.english_learn.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.english_learn.entity.DialogueRecord;
import java.util.List;

@Mapper
public interface DialogueRecordMapper {
    int insert(DialogueRecord record);
    List<DialogueRecord> getRecordsByDialogueId(String dialogueId);
} 