package com.chineselearning.dto;

import com.chineselearning.domain.Vocabulary.BienTheType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyResponse {

    private Long id;
    private String hanzi;
    private String pinyin;
    private String nghia;
    private String viDu;
    private BienTheType bienThe;
    private Integer hskLevel;
    private Integer frequencyRank;
    private Set<String> tags;
}

