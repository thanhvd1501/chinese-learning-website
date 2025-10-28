package com.chineselearning.repository;

import com.chineselearning.domain.Radical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RadicalRepository extends JpaRepository<Radical, Long> {

    Optional<Radical> findByHanzi(String hanzi);

    Radical findTopByOrderByFrequencyRankAsc();
}

