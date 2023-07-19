package com.owori.domain.keyword.controller;

import com.owori.domain.keyword.entity.dto.response.FindKeywordsResponse;
import com.owori.domain.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/keywords")
public class KeywordController {
    private final KeywordService keywordService;

    /**
     * 최근 검색어를 조회 컨트롤러입니다.
     * @return 검색어 정보가 담긴 dto list를 반환합니다.
     */
    @GetMapping
    public ResponseEntity<List<FindKeywordsResponse>> findSearchWords(){
        return ResponseEntity.ok(keywordService.findSearchWords());
    }


    /**
     * 최근 검색어를 전체 삭제하는 컨트롤러입니다.
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteSearchWords(){
        keywordService.deleteSearchWords();
        return ResponseEntity.ok().build();
    }

    /**
     * 최근 검색어를 단일 삭제하는 컨트롤러입니다.
     * @param keywordId 삭제할 검색어의 id 값
     */
    @DeleteMapping("/{keywordId}")
    public ResponseEntity<Void> deleteSearchWords(@PathVariable UUID keywordId){
        keywordService.deleteSearchWord(keywordId);
        return ResponseEntity.ok().build();
    }
}
