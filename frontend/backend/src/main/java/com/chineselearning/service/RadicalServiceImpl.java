package com.chineselearning.service;

import com.chineselearning.domain.Radical;
import com.chineselearning.dto.PageResponse;
import com.chineselearning.dto.request.RadicalRequest;
import com.chineselearning.dto.response.RadicalResponse;
import com.chineselearning.exception.custom.DuplicateResourceException;
import com.chineselearning.exception.custom.ResourceNotFoundException;
import com.chineselearning.mapper.RadicalMapper;
import com.chineselearning.repository.RadicalRepository;
import com.chineselearning.service.interfaces.RadicalService;
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
 * Radical Service Implementation
 * Implements business logic for radical management
 * 
 * @author Senior Backend Architect
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RadicalServiceImpl implements RadicalService {

    private final RadicalRepository radicalRepository;
    private final RadicalMapper radicalMapper;

    @Override
    @Transactional
    @CacheEvict(value = "radicals", allEntries = true)
    public RadicalResponse create(RadicalRequest request) {
        log.info("Creating new radical: {}", request.getRadical());

        // Check for duplicate radical
        radicalRepository.findByRadical(request.getRadical())
                .ifPresent(r -> {
                    throw new DuplicateResourceException("Radical", "radical", request.getRadical());
                });

        Radical radical = radicalMapper.toEntity(request);
        Radical saved = radicalRepository.save(radical);

        log.info("Radical created successfully with ID: {}", saved.getId());
        return radicalMapper.toResponse(saved);
    }

    @Override
    @Cacheable(value = "radicals", key = "#id")
    public RadicalResponse getById(Long id) {
        log.debug("Fetching radical by ID: {}", id);

        Radical radical = radicalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Radical", "id", id));

        return radicalMapper.toResponse(radical);
    }

    @Override
    @Cacheable(value = "radicals", key = "'all'")
    public List<RadicalResponse> getAll() {
        log.debug("Fetching all radicals");

        return radicalRepository.findAll(Sort.by(Sort.Direction.ASC, "strokeCount")).stream()
                .map(radicalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "radicals", key = "'page-' + #page + '-' + #size")
    public PageResponse<RadicalResponse> getPage(int page, int size) {
        log.debug("Fetching radicals page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "strokeCount"));
        Page<Radical> radicalPage = radicalRepository.findAll(pageable);

        List<RadicalResponse> content = radicalPage.getContent().stream()
                .map(radicalMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.of(
                content,
                radicalPage.getTotalElements(),
                radicalPage.getTotalPages(),
                page,
                size
        );
    }

    @Override
    @Transactional
    @CacheEvict(value = "radicals", allEntries = true)
    public RadicalResponse update(Long id, RadicalRequest request) {
        log.info("Updating radical ID: {}", id);

        Radical radical = radicalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Radical", "id", id));

        radicalMapper.updateEntityFromRequest(request, radical);

        Radical updated = radicalRepository.save(radical);

        log.info("Radical updated successfully: {}", updated.getId());
        return radicalMapper.toResponse(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = "radicals", allEntries = true)
    public void delete(Long id) {
        log.info("Deleting radical ID: {}", id);

        if (!radicalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Radical", "id", id);
        }

        radicalRepository.deleteById(id);
        log.info("Radical deleted successfully: {}", id);
    }

    @Override
    public boolean exists(Long id) {
        return radicalRepository.existsById(id);
    }

    @Override
    @Cacheable(value = "radicals", key = "'stroke-' + #strokeCount")
    public List<RadicalResponse> findByStrokeCount(Integer strokeCount) {
        log.debug("Finding radicals by stroke count: {}", strokeCount);

        return radicalRepository.findByStrokeCount(strokeCount).stream()
                .map(radicalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "radicals", key = "'radical-' + #radical")
    public RadicalResponse findByRadical(String radical) {
        log.debug("Finding radical by character: {}", radical);

        Radical found = radicalRepository.findByRadical(radical)
                .orElseThrow(() -> new ResourceNotFoundException("Radical", "radical", radical));

        return radicalMapper.toResponse(found);
    }

    @Override
    public List<RadicalResponse> searchByMeaning(String keyword) {
        log.debug("Searching radicals by meaning: {}", keyword);

        return radicalRepository.findAll().stream()
                .filter(r -> r.getMeaning().toLowerCase().contains(keyword.toLowerCase()))
                .map(radicalMapper::toResponse)
                .collect(Collectors.toList());
    }
}

