package com.chineselearning.service;

import com.chineselearning.domain.Textbook;
import com.chineselearning.domain.Textbook.PhienBanType;
import com.chineselearning.dto.PageResponse;
import com.chineselearning.dto.request.TextbookRequest;
import com.chineselearning.dto.response.TextbookResponse;
import com.chineselearning.exception.custom.DuplicateResourceException;
import com.chineselearning.exception.custom.ResourceNotFoundException;
import com.chineselearning.mapper.TextbookMapper;
import com.chineselearning.repository.TextbookRepository;
import com.chineselearning.service.interfaces.TextbookService;
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
 * Textbook Service Implementation
 * Implements business logic for textbook management
 * 
 * @author Senior Backend Architect
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TextbookServiceImpl implements TextbookService {

    private final TextbookRepository textbookRepository;
    private final TextbookMapper textbookMapper;

    @Override
    @Transactional
    @CacheEvict(value = "textbooks", allEntries = true)
    public TextbookResponse create(TextbookRequest request) {
        log.info("Creating new textbook: {}", request.getName());

        // Validate no duplicate name in same year
        boolean exists = textbookRepository.findByPhienBan(request.getPhienBan()).stream()
                .anyMatch(t -> t.getName().equalsIgnoreCase(request.getName()) 
                        && t.getNamXuatBan().equals(request.getNamXuatBan()));

        if (exists) {
            throw new DuplicateResourceException(
                    "Textbook", 
                    "name and year", 
                    request.getName() + " (" + request.getNamXuatBan() + ")"
            );
        }

        Textbook textbook = textbookMapper.toEntity(request);
        Textbook saved = textbookRepository.save(textbook);

        log.info("Textbook created successfully with ID: {}", saved.getId());
        return textbookMapper.toResponse(saved);
    }

    @Override
    @Cacheable(value = "textbooks", key = "#id")
    public TextbookResponse getById(Long id) {
        log.debug("Fetching textbook by ID: {}", id);
        
        Textbook textbook = textbookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Textbook", "id", id));

        return textbookMapper.toResponse(textbook);
    }

    @Override
    @Cacheable(value = "textbooks", key = "'all'")
    public List<TextbookResponse> getAll() {
        log.debug("Fetching all textbooks");
        
        return textbookRepository.findAll(Sort.by(Sort.Direction.DESC, "namXuatBan")).stream()
                .map(textbookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "textbooks", key = "'page-' + #page + '-' + #size")
    public PageResponse<TextbookResponse> getPage(int page, int size) {
        log.debug("Fetching textbooks page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "namXuatBan"));
        Page<Textbook> textbookPage = textbookRepository.findAll(pageable);

        List<TextbookResponse> content = textbookPage.getContent().stream()
                .map(textbookMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.of(
                content,
                textbookPage.getTotalElements(),
                textbookPage.getTotalPages(),
                page,
                size
        );
    }

    @Override
    @Transactional
    @CacheEvict(value = "textbooks", allEntries = true)
    public TextbookResponse update(Long id, TextbookRequest request) {
        log.info("Updating textbook ID: {}", id);

        Textbook textbook = textbookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Textbook", "id", id));

        // Update fields from request
        textbookMapper.updateEntityFromRequest(request, textbook);

        Textbook updated = textbookRepository.save(textbook);

        log.info("Textbook updated successfully: {}", updated.getId());
        return textbookMapper.toResponse(updated);
    }

    @Override
    @Transactional
    @CacheEvict(value = "textbooks", allEntries = true)
    public void delete(Long id) {
        log.info("Deleting textbook ID: {}", id);

        if (!textbookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Textbook", "id", id);
        }

        textbookRepository.deleteById(id);
        log.info("Textbook deleted successfully: {}", id);
    }

    @Override
    public boolean exists(Long id) {
        return textbookRepository.existsById(id);
    }

    @Override
    @Cacheable(value = "textbooks", key = "'phienban-' + #phienBan")
    public List<TextbookResponse> findByPhienBan(PhienBanType phienBan) {
        log.debug("Finding textbooks by phien ban: {}", phienBan);

        return textbookRepository.findByPhienBan(phienBan).stream()
                .map(textbookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "textbooks", key = "'year-' + #year")
    public List<TextbookResponse> findByNamXuatBan(Integer year) {
        log.debug("Finding textbooks by year: {}", year);

        return textbookRepository.findByNamXuatBan(year).stream()
                .map(textbookMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TextbookResponse> searchByName(String name) {
        log.debug("Searching textbooks by name: {}", name);

        return textbookRepository.findAll().stream()
                .filter(t -> t.getName().toLowerCase().contains(name.toLowerCase()))
                .map(textbookMapper::toResponse)
                .collect(Collectors.toList());
    }
}

