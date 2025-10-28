package com.chineselearning.controller;

import com.chineselearning.domain.Textbook;
import com.chineselearning.repository.TextbookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/textbooks")
@RequiredArgsConstructor
@Tag(name = "Textbooks", description = "Giáo trình API")
public class TextbookController {

    private final TextbookRepository textbookRepository;

    @Operation(summary = "Lấy danh sách giáo trình", description = "Lấy danh sách tất cả giáo trình")
    @GetMapping
    public ResponseEntity<List<Textbook>> getTextbooks() {
        List<Textbook> textbooks = textbookRepository.findAll();
        return ResponseEntity.ok(textbooks);
    }
}

