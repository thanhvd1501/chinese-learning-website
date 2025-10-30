package com.chineselearning.repository;

import com.chineselearning.domain.Vocabulary;
import com.chineselearning.domain.Vocabulary.VariantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Vocabulary entity
 * 
 * @author Senior Backend Architect
 */
@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long>, JpaSpecificationExecutor<Vocabulary> {

    Page<Vocabulary> findByHanziContainingIgnoreCaseOrPinyinContainingIgnoreCaseOrMeaningContainingIgnoreCase(
            String hanzi, String pinyin, String meaning, Pageable pageable
    );

    Page<Vocabulary> findByVariant(VariantType variant, Pageable pageable);

    Page<Vocabulary> findByVariantAndHanziContainingIgnoreCase(
            VariantType variant, String search, Pageable pageable
    );

    @Query("SELECT v FROM Vocabulary v WHERE " +
            "v.variant = :variant AND " +
            "(v.hanzi LIKE %:search% OR v.pinyin LIKE %:search% OR v.meaning LIKE %:search%)")
    Page<Vocabulary> findByVariantAndSearch(
            @Param("variant") VariantType variant,
            @Param("search") String search,
            Pageable pageable
    );

    Page<Vocabulary> findByHskLevel(Integer hskLevel, Pageable pageable);

    Optional<Vocabulary> findByHanzi(String hanzi);
}

