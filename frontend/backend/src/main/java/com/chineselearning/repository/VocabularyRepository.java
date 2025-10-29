package com.chineselearning.repository;

import com.chineselearning.domain.Vocabulary;
import com.chineselearning.domain.Vocabulary.VariantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

    Page<Vocabulary> findByHanziContainingIgnoreCaseOrPinyinContainingIgnoreCaseOrNghiaContainingIgnoreCase(
            String hanzi, String pinyin, String nghia, Pageable pageable
    );

    Page<Vocabulary> findByBienThe(VariantType bienThe, Pageable pageable);

    Page<Vocabulary> findByBienTheAndHanziContainingIgnoreCase(
            VariantType bienThe, String search, Pageable pageable
    );

    @Query("SELECT v FROM Vocabulary v WHERE " +
            "v.bienThe = :bienThe AND " +
            "(v.hanzi LIKE %:search% OR v.pinyin LIKE %:search% OR v.nghia LIKE %:search%)")
    Page<Vocabulary> findByBienTheAndSearch(
            @Param("bienThe") VariantType bienThe,
            @Param("search") String search,
            Pageable pageable
    );

    Page<Vocabulary> findByHskLevel(Integer hskLevel, Pageable pageable);

    Optional<Vocabulary> findByHanzi(String hanzi);
}

