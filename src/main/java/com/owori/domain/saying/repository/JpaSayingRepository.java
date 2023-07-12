package com.owori.domain.saying.repository;

import com.owori.domain.saying.entity.Saying;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaSayingRepository extends JpaRepository<Saying, UUID>, SayingRepository{

}
