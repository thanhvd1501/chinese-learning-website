package com.chineselearning.repository;

import com.chineselearning.domain.Textbook;
import com.chineselearning.domain.Textbook.VersionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Textbook entity
 * 
 * @author Senior Backend Architect
 */
@Repository
public interface TextbookRepository extends JpaRepository<Textbook, Long> {

    List<Textbook> findByVersion(VersionType version);

    List<Textbook> findByPublicationYear(Integer publicationYear);
}

