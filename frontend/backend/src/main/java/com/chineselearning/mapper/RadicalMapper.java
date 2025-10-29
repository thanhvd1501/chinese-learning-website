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

    RadicalResponse toResponse(Radical radical);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Radical toEntity(RadicalRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(RadicalRequest request, @MappingTarget Radical radical);
}

