package com.chineselearning.repository;

import com.chineselearning.domain.GrammarTopic;
import com.chineselearning.domain.GrammarTopic.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarTopicRepository extends JpaRepository<GrammarTopic, Long> {

    List<GrammarTopic> findByLevel(Level level);

    List<GrammarTopic> findByTitleContainingIgnoreCase(String title);
}

