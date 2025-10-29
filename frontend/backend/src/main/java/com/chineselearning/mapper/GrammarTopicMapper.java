package com.chineselearning.mapper;

import com.chineselearning.domain.GrammarTopic;
import com.chineselearning.dto.request.GrammarTopicRequest;
import com.chineselearning.dto.response.GrammarTopicResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for GrammarTopic entity
 * 
 * @author Senior Backend Architect
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface GrammarTopicMapper {

    GrammarTopicResponse toResponse(GrammarTopic grammarTopic);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GrammarTopic toEntity(GrammarTopicRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(GrammarTopicRequest request, @MappingTarget GrammarTopic grammarTopic);
}

