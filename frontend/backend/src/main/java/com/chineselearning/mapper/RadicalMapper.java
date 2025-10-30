package com.chineselearning.mapper;

import com.chineselearning.domain.Radical;
import com.chineselearning.dto.request.RadicalRequest;
import com.chineselearning.dto.response.RadicalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for Radical entity
 * 
 * @author Senior Backend Architect
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RadicalMapper {

    @Mapping(source = "hanzi", target = "radical")
    @Mapping(source = "strokes", target = "strokeCount")
    @Mapping(source = "pronunciation", target = "pinyin")
    RadicalResponse toResponse(Radical radical);

    @Mapping(source = "radical", target = "hanzi")
    @Mapping(source = "strokeCount", target = "strokes")
    @Mapping(source = "pinyin", target = "pronunciation")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "frequencyRank", ignore = true)
    Radical toEntity(RadicalRequest request);

    @Mapping(source = "radical", target = "hanzi")
    @Mapping(source = "strokeCount", target = "strokes")
    @Mapping(source = "pinyin", target = "pronunciation")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "frequencyRank", ignore = true)
    void updateEntityFromRequest(RadicalRequest request, @MappingTarget Radical radical);
}

