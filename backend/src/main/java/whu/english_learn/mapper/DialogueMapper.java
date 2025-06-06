package whu.english_learn.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.english_learn.entity.Dialogue;
import java.util.List;

@Mapper
public interface DialogueMapper {
    int insert(Dialogue dialogue);
    int updateUpdateTime(String id);
    List<String> getDialogueIdsByUserId(String userId);
    List<Dialogue> getDialoguesByUserId(String userId);
} 