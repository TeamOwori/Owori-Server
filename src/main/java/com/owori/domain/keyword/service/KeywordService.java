package com.owori.domain.keyword.service;

import com.owori.domain.keyword.entity.Keyword;
import com.owori.domain.keyword.entity.dto.response.FindKeywordsResponse;
import com.owori.domain.keyword.repository.KeywordRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeywordService implements EntityLoader<Keyword, UUID> {

    private final KeywordRepository keywordRepository;
    private final AuthService authService;

    public void addKeyword(String keyword, Member member) {
        Optional<Keyword> findKeyword = keywordRepository.findByContents(keyword);
        findKeyword.ifPresent(word -> word.getBaseTime().setCreatedAt(LocalDateTime.now())); // 같은 검색어가 이미 존재하면 createdAt만 update
        findKeyword.orElseGet(() -> keywordRepository.save(new Keyword(keyword, member)));
    }

    public List<FindKeywordsResponse> findSearchWords() {
        Member loginUser = authService.getLoginUser();
        List<Keyword> keywordList = keywordRepository.findByMember(loginUser);

        return Optional.ofNullable(keywordList)
                .map(
                        keywords -> keywords.stream()
                                .map( keyword -> new FindKeywordsResponse(keyword.getId(), keyword.getContents())).toList())
                .orElse(null);
    }

    public void deleteSearchWords() {
        keywordRepository.deleteAll();
    }

    public void deleteSearchWord(UUID keywordId) {
        Keyword keyword = loadEntity(keywordId);
        keywordRepository.delete(keyword);
    }

    @Override
    public Keyword loadEntity(UUID id) {
        return keywordRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

}
