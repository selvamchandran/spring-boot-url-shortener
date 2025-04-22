package com.selvam.urlshortener.domain.repositories;

import com.selvam.urlshortener.domain.entities.ShortUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    //We use join to prevent N+1 problems
    @Query("Select su from ShortUrl su left join fetch su.createdBy where su.isPrivate = false")
    Page<ShortUrl> findPublicShortUrls(Pageable pageable); //JPQL

/*    @Query("Select su from ShortUrl su where su.isPrivate = false order by su.createdAt desc")
    @EntityGraph(attributePaths = "createdBy")
    List<ShortUrl> findPublicShortUrls(); //JPQL*/
//    List<ShortUrl> findByIsPrivateIsFalseOrderByCreatedAtDesc();

    boolean existsByShortKey(String shortKey);
    Optional<ShortUrl> findByShortKey(String shortKey);

    Page<ShortUrl> findByCreatedById(Long userId,Pageable pageable);

    @Modifying
    void deleteByIdInAndCreatedById(List<Long> ids, Long userId);

    @Query("Select su from ShortUrl su left join fetch su.createdBy")
    Page<ShortUrl> findAllShortUrls(Pageable pageable);

}

