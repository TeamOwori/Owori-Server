package com.owori.domain.heart.repository;

import com.owori.domain.heart.entity.Heart;
import com.owori.domain.member.entity.Member;
import com.owori.domain.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaHeartRepository extends JpaRepository<Heart, UUID>, HeartRepository {
    @Query("select h.story from Heart h where h.member = :member order by h.story.startDate DESC")
    List<Story> findAllByMember(Member member);

}
