package com.chineselearning.controller;

import com.chineselearning.domain.Radical;
import com.chineselearning.repository.RadicalRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/radicals")
@RequiredArgsConstructor
@Tag(name = "Radicals", description = "Bộ thủ API")
public class RadicalController {

    private final RadicalRepository radicalRepository;

    @Operation(summary = "Lấy danh sách bộ thủ", description = "Lấy danh sách tất cả bộ thủ")
    @GetMapping
    public ResponseEntity<List<Radical>> getRadicals() {
        List<Radical> radicals = radicalRepository.findAll();
        return ResponseEntity.ok(radicals);
    }

    @Operation(summary = "Lấy bộ thủ theo ID", description = "Lấy chi tiết một bộ thủ")
    @GetMapping("/{id}")
    public ResponseEntity<Radical> getRadicalById(@PathVariable Long id) {
        Optional<Radical> radical = radicalRepository.findById(id);
        return radical.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lấy bộ thủ phổ biến", description = "Lấy bộ thủ có tần suất cao nhất")
    @GetMapping("/popular")
    public ResponseEntity<Radical> getPopularRadical() {
        Radical radical = radicalRepository.findTopByOrderByFrequencyRankAsc();
        return ResponseEntity.ok(radical);
    }
}

