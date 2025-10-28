package com.chineselearning.repository;

import com.chineselearning.domain.Course;
import com.chineselearning.domain.Course.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTextbookId(Long textbookId);

    List<Course> findByDifficulty(Difficulty difficulty);

    Optional<Course> findByLevelAndTextbookId(String level, Long textbookId);
}

