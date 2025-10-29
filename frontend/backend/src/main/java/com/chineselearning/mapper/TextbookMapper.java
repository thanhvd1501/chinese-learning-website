package com.chineselearning.mapper;

import com.chineselearning.domain.Textbook;
import com.chineselearning.dto.request.TextbookRequest;
import com.chineselearning.dto.response.TextbookResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for Textbook entity
 * 
 * @author Senior Backend Architect
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TextbookMapper {

    /**
     * Convert entity to response DTO
     */
    @Mapping(target = "courseCount", expression = "java(textbook.getCourses() != null ? textbook.getCourses().size() : 0)")
    TextbookResponse toResponse(Textbook textbook);

    /**
     * Convert request DTO to entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Textbook toEntity(TextbookRequest request);

    /**
     * Update entity from request DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courses", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(TextbookRequest request, @MappingTarget Textbook textbook);
}

