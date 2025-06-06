package whu.english_learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import whu.english_learn.dto.ContextualReadingRequestDTO;
import whu.english_learn.dto.SemanticWordExplorationRequestDTO;
import whu.english_learn.dto.SituationalDialogueRequestDTO;
import whu.english_learn.service.ContextualReadingService;
import whu.english_learn.service.SemanticWordExplorationService;
import whu.english_learn.service.SituationalDialogueService;


@RestController
@RequestMapping("/llm")
public class LLMController {

    @Autowired
    private ContextualReadingService contextualReadingService;
    @Autowired
    private SituationalDialogueService situationalDialogueService;
    @Autowired
    private SemanticWordExplorationService semanticWordExplorationService;

    @PostMapping("/contextual_reading")
    public ResponseEntity<String> ask(@RequestBody ContextualReadingRequestDTO dto) {
        return contextualReadingService.sendRequest(dto);
    }

    @PostMapping("/semantic_word_exploration")
    public ResponseEntity<String> ask(@RequestBody SemanticWordExplorationRequestDTO dto) {
        return semanticWordExplorationService.sendRequest(dto);
    }

    @PostMapping("/situational_dialogue")
    public ResponseEntity<String> ask(@RequestBody SituationalDialogueRequestDTO dto) {
        return situationalDialogueService.sendRequest(dto);
    }
}
