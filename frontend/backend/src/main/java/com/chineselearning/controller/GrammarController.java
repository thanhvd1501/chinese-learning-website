package com.chineselearning.controller;

import com.chineselearning.domain.GrammarTopic;
import com.chineselearning.repository.GrammarTopicRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grammar")
@RequiredArgsConstructor
@Tag(name = "Grammar", description = "Ngữ pháp API")
public class GrammarController {

    private final GrammarTopicRepository grammarTopicRepository;

    @Operation(summary = "Lấy danh sách chủ đề ngữ pháp", description = "Lấy danh sách tất cả chủ đề ngữ pháp")
    @GetMapping
    public ResponseEntity<List<GrammarTopic>> getGrammarTopics(
            @RequestParam(required = false) String level
    ) {
        List<GrammarTopic> topics = grammarTopicRepository.findAll();
        return ResponseEntity.ok(topics);
    }

    @Operation(summary = "Lấy chủ đề ngữ pháp theo ID", description = "Lấy chi tiết một chủ đề ngữ pháp")
    @GetMapping("/{id}")
    public ResponseEntity<GrammarTopic> getGrammarTopicById(@PathVariable Long id) {
        Optional<GrammarTopic> topic = grammarTopicRepository.findById(id);
        return topic.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

