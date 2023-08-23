package com.owori.domain.story.repository;

import com.owori.domain.family.entity.Family;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StoryRepository extends JpaRepository<Story, Long>, StoryRepositoryCustom {
    Story save(Story story);
    Optional<Story> findById(UUID id);
    Long countByMember(Member member);

    @Query("select s from Story s where s.family = :family order by s.baseTime.createdAt DESC ")
    List<Story> findAllByFamilyOrderByCreatedAt(Family family);

    @Query("select s from Story s where s.family = :family order by s.startDate DESC, s.endDate DESC, s.baseTime.createdAt DESC")
    List<Story> findAllByFamilyOrderByStartDate(Family family);

    @Query("select s from Story s where s.member = :member order by s.startDate DESC, s.endDate DESC, s.baseTime.createdAt DESC")
    List<Story> findAllByMember(Member member);
}
