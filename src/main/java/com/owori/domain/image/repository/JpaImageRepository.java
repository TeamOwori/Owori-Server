package com.owori.domain.image.repository;

import com.owori.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface JpaImageRepository extends JpaRepository<Image, UUID>, ImageRepository {

    @Query("select i from Image i where i.url in :urls")
    List<Image> findAllByUrls(List<String> urls);
}
