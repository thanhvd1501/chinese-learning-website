package com.chineselearning.repository;

import com.chineselearning.domain.Radical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Radical entity
 * 
 * @author Senior Backend Architect
 */
@Repository
public interface RadicalRepository extends JpaRepository<Radical, Long> {

    Optional<Radical> findByHanzi(String hanzi);

    List<Radical> findByStrokes(Integer strokes);

    Radical findTopByOrderByFrequencyRankAsc();
}

