package com.owori.domain.keyword.service;

import com.owori.domain.keyword.dto.response.FindKeywordsResponse;
import com.owori.domain.keyword.entity.Keyword;
import com.owori.domain.keyword.repository.KeywordRepository;
import com.owori.domain.member.entity.Member;
import com.owori.domain.member.service.AuthService;
import com.owori.global.exception.EntityNotFoundException;
import com.owori.global.exception.NoAuthorityException;
import com.owori.global.service.EntityLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordService implements EntityLoader<Keyword, UUID> {

    private final KeywordRepository keywordRepository;
    private final AuthService authService;

    public void addKeyword(String keyword, Member member) {
        Optional<Keyword> findKeyword = keywordRepository.findByContents(keyword);
        findKeyword.ifPresentOrElse(word -> word.getBaseTime().setCreatedAt(LocalDateTime.now()), // 같은 검색어가 이미 존재하면 createdAt만 update
                () -> keywordRepository.save(new Keyword(keyword, member)));
    }

    @Transactional(readOnly = true)
    public List<FindKeywordsResponse> findSearchWords() {
        Member loginUser = authService.getLoginUser();
        List<Keyword> keywordList = keywordRepository.findByMember(loginUser);

        return Optional.ofNullable(keywordList)
                .map(
                        keywords -> keywords.stream()
                                .map(keyword -> new FindKeywordsResponse(keyword.getId(), keyword.getContents())).toList())
                .orElseGet(List::of);
    }

    @Transactional
    public void deleteSearchWords() {
        Member loginUser = authService.getLoginUser();
        keywordRepository.findByMember(loginUser).forEach(Keyword::delete);
    }

    @Transactional
    public void deleteSearchWord(UUID keywordId) {
        Keyword keyword = loadEntity(keywordId);
        if (!keyword.getMember().getId().equals(authService.getLoginUser().getId())) {
            throw new NoAuthorityException();
        }
        keyword.delete();
    }

    @Override
    public Keyword loadEntity(UUID id) {
        return keywordRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
