package com.chineselearning.mapper;

import com.chineselearning.domain.Course;
import com.chineselearning.dto.request.CreateCourseRequest;
import com.chineselearning.dto.response.CourseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "textbookId", source = "textbook.id")
    @Mapping(target = "textbookName", source = "textbook.name")
    CourseResponse toResponse(Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "textbook", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Course toEntity(CreateCourseRequest request);
}

