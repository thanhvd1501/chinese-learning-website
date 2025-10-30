package com.chineselearning.mapper;

import com.chineselearning.domain.Vocabulary;
import com.chineselearning.dto.response.VocabularyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for Vocabulary entity
 * 
 * @author Senior Backend Architect
 */
@Mapper(componentModel = "spring")
public interface VocabularyMapper {

    VocabularyMapper INSTANCE = Mappers.getMapper(VocabularyMapper.class);

    @Mapping(target = "variant", expression = "java(vocabulary.getVariant() != null ? vocabulary.getVariant().name() : null)")
    VocabularyResponse toResponse(Vocabulary vocabulary);
}

