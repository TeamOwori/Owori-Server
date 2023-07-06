package com.owori.domain.record.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRecordRepository extends JpaRepository<Record, Long>, RecordRepository {}
