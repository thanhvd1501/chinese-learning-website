package com.chineselearning.service;

import com.chineselearning.domain.Vocabulary;
import com.chineselearning.dto.PageResponse;
import com.chineselearning.dto.response.VocabularyResponse;
import com.chineselearning.exception.custom.ResourceNotFoundException;
import com.chineselearning.mapper.VocabularyMapper;
import com.chineselearning.repository.VocabularyRepository;
import com.chineselearning.service.interfaces.VocabularyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vocabulary Service Implementation
 * Implements business logic for vocabulary management
 * 
 * @author Senior Backend Architect
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class VocabularyServiceImpl implements VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyMapper vocabularyMapper;

    @Override
    @Cacheable(value = "vocabularies", key = "'page-' + #page + '-' + #size + '-' + #search + '-' + #variant")
    public PageResponse<VocabularyResponse> getVocabulary(int page, int size, String search, String variant) {
        log.debug("Fetching vocabulary: page={}, size={}, search={}, variant={}", page, size, search, variant);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        // Build specification for filtering
        Specification<Vocabulary> spec = Specification.where(null);

        if (search != null && !search.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("hanzi")), "%" + search.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("pinyin")), "%" + search.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("meaning")), "%" + search.toLowerCase() + "%")
                    )
            );
        }

        if (variant != null && !variant.isEmpty()) {
            try {
                Vocabulary.VariantType variantType = Vocabulary.VariantType.valueOf(variant.toUpperCase());
                spec = spec.and((root, query, cb) ->
                        cb.or(
                                cb.equal(root.get("variant"), variantType),
                                cb.equal(root.get("variant"), Vocabulary.VariantType.BOTH)
                        )
                );
            } catch (IllegalArgumentException e) {
                log.warn("Invalid variant type: {}", variant);
            }
        }

        Page<Vocabulary> vocabPage = vocabularyRepository.findAll(spec, pageable);

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

    @Override
    @Cacheable(value = "vocabularies", key = "#id")
    public VocabularyResponse getById(Long id) {
        log.debug("Fetching vocabulary by ID: {}", id);
        Vocabulary vocabulary = vocabularyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vocabulary", "id", id));
        return vocabularyMapper.toResponse(vocabulary);
    }

    @Override
    @Cacheable(value = "vocabularies", key = "'all'")
    public List<VocabularyResponse> getAll() {
        log.debug("Fetching all vocabulary");
        return vocabularyRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(vocabularyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<VocabularyResponse> search(String keyword) {
        log.debug("Searching vocabulary by keyword: {}", keyword);
        return vocabularyRepository.findAll().stream()
                .filter(v -> v.getHanzi().contains(keyword)
                        || v.getPinyin().toLowerCase().contains(keyword.toLowerCase())
                        || v.getMeaning().toLowerCase().contains(keyword.toLowerCase()))
                .map(vocabularyMapper::toResponse)
                .collect(Collectors.toList());
    }
}

