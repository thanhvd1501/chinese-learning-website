package com.chineselearning.repository;

import com.chineselearning.domain.Textbook;
import com.chineselearning.domain.Textbook.VersionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextbookRepository extends JpaRepository<Textbook, Long> {

    List<Textbook> findByPhienBan(VersionType phienBan);

    List<Textbook> findByNamXuatBan(Integer namXuatBan);
}

