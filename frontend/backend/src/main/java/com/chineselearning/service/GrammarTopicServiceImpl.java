package com.chineselearning.service;

import com.chineselearning.domain.GrammarTopic;
import com.chineselearning.dto.PageResponse;
import com.chineselearning.dto.request.GrammarTopicRequest;
import com.chineselearning.dto.response.GrammarTopicResponse;
import com.chineselearning.exception.custom.DuplicateResourceException;
import com.chineselearning.exception.custom.ResourceNotFoundException;
import com.chineselearning.mapper.GrammarTopicMapper;
import com.chineselearning.repository.GrammarTopicRepository;
import com.chineselearning.service.interfaces.GrammarTopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Grammar Topic Service Implementation
 * Implements business logic for grammar topic management
 * 
 * @author Senior Backend Architect
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GrammarTopicServiceImpl implements GrammarTopicService {

    private final GrammarTopicRepository grammarTopicRepository;
    private final GrammarTopicMapper grammarTopicMapper;

    @Override
    @Transactional
    @CacheEvict(value = "grammarTopics", allEntries = true)
    public GrammarTopicResponse create(GrammarTopicRequest request) {
        log.info("Creating new grammar topic: {}", request.getTitle());

        // Check for duplicate title
        boolean exists = grammarTopicRepository.findAll().stream()
                .anyMatch(g -> g.getTitle().equalsIgnoreCase(request.getTitle()));

        if (exists) {
            throw new DuplicateResourceException("GrammarTopic", "title", request.getTitle());
        }

        GrammarTopic grammarTopic = grammarTopicMapper.toEntity(request);
        GrammarTopic saved = grammarTopicRepository.save(grammarTopic);

        log.info("Grammar topic created successfully with ID: {}", saved.getId());
        return grammarTopicMapper.toResponse(saved);
    }

    @Override
    @Cacheable(value = "grammarTopics", key = "#id")
    public GrammarTopicResponse getById(Long id) {
        log.debug("Fetching grammar topic by ID: {}", id);

        GrammarTopic grammarTopic = grammarTopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrammarTopic", "id", id));

        return grammarTopicMapper.toResponse(grammarTopic);
    }

    @Override
    @Cacheable(value = "grammarTopics", key = "'all'")
    public List<GrammarTopicResponse> getAll() {
        log.debug("Fetching all grammar topics");

        return grammarTopicRepository.findAll(Sort.by(Sort.Direction.ASC, "title")).stream()
                .map(grammarTopicMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "grammarTopics", key = "'page-' + #page + '-' + #size")
    public PageResponse<GrammarTopicResponse> getPage(int page, int size) {
        log.debug("Fetching grammar topics page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "title"));
        Page<GrammarTopic> grammarPage = grammarTopicRepository.findAll(pageable);

        List<GrammarTopicResponse> content = grammarPage.getContent().stream()
                .map(grammarTopicMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.of(
                content,
                grammarPage.getTotalElements(),
                grammarPage.getTotalPages(),
                page,
                size
        );
    }

    @Override
    @Transactional
    @CacheEvict(value = "grammarTopics", allEntries = true)
    public GrammarTopicResponse update(Long id, GrammarTopicRequest request) {
        log.info("Updating grammar topic ID: {}", id);

        GrammarTopic grammarTopic = grammarTopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrammarTopic", "id", id));

        grammarTopicMapper.updateEntityFromRequest(request, grammarTopic);

        GrammarTopic updated = grammarTopicRepository.save(grammarTopic);

        log.info("Grammar topic updated successfully: {}", updated.getId());
        return grammarTopicMapper.toResponse(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = "grammarTopics", allEntries = true)
    public void delete(Long id) {
        log.info("Deleting grammar topic ID: {}", id);

        if (!grammarTopicRepository.existsById(id)) {
            throw new ResourceNotFoundException("GrammarTopic", "id", id);
        }

        grammarTopicRepository.deleteById(id);
        log.info("Grammar topic deleted successfully: {}", id);
    }

    @Override
    public boolean exists(Long id) {
        return grammarTopicRepository.existsById(id);
    }

    @Override
    public List<GrammarTopicResponse> search(String keyword) {
        log.debug("Searching grammar topics by keyword: {}", keyword);

        return grammarTopicRepository.findAll().stream()
                .filter(g -> g.getTitle().toLowerCase().contains(keyword.toLowerCase())
                        || g.getStructure().toLowerCase().contains(keyword.toLowerCase()))
                .map(grammarTopicMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "grammarTopics", key = "'tag-' + #tag")
    public List<GrammarTopicResponse> findByTag(String tag) {
        log.debug("Finding grammar topics by tag: {}", tag);

        return grammarTopicRepository.findAll().stream()
                .filter(g -> g.getTags() != null && g.getTags().contains(tag))
                .map(grammarTopicMapper::toResponse)
                .collect(Collectors.toList());
    }
}

