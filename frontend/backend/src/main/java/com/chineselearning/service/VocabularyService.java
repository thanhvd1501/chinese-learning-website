package com.chineselearning.service;

import com.chineselearning.domain.Vocabulary;
import com.chineselearning.domain.Vocabulary.BienTheType;
import com.chineselearning.dto.PageResponse;
import com.chineselearning.dto.VocabularyResponse;
import com.chineselearning.mapper.VocabularyMapper;
import com.chineselearning.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyMapper vocabularyMapper;

    @Cacheable(value = "vocabularies", key = "#page + '-' + #size + '-' + #bienThe + '-' + #search")
    @Transactional(readOnly = true)
    public PageResponse<VocabularyResponse> getVocabularies(
            int page, int size, BienTheType bienThe, String search
    ) {
        log.debug("Fetching vocabularies - page: {}, size: {}, bienThe: {}, search: {}", page, size, bienThe, search);

        Pageable pageable = PageRequest.of(page, size);

        Page<Vocabulary> vocabPage;
        if (search != null && !search.isEmpty() && bienThe != null) {
            vocabPage = vocabularyRepository.findByBienTheAndSearch(bienThe, search, pageable);
        } else if (search != null && !search.isEmpty()) {
            vocabPage = vocabularyRepository.findByHanziContainingIgnoreCaseOrPinyinContainingIgnoreCaseOrNghiaContainingIgnoreCase(
                    search, search, search, pageable
            );
        } else if (bienThe != null) {
            vocabPage = vocabularyRepository.findByBienThe(bienThe, pageable);
        } else {
            vocabPage = vocabularyRepository.findAll(pageable);
        }

        List<VocabularyResponse> content = vocabPage.getContent().stream()
                .map(vocabularyMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.of(
                content,
                vocabPage.getTotalElements(),
                vocabPage.getTotalPages(),
                page,
                size
        );
    }

    @Transactional(readOnly = true)
    public VocabularyResponse getVocabularyById(Long id) {
        Vocabulary vocab = vocabularyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vocabulary not found with id: " + id));
        return vocabularyMapper.toResponse(vocab);
    }
}

