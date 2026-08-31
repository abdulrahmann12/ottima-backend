package com.ottima.finishing_tracking.standard_item.repository;

import com.ottima.finishing_tracking.standard_item.entity.StandardItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StandardItemRepository extends JpaRepository<StandardItem, UUID> {

    @Query("SELECT s FROM StandardItem s WHERE " +
            "(:keyword IS NULL OR LOWER(s.nameAr) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.nameEn) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<StandardItem> searchItems(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByNameArOrNameEn(String nameAr, String nameEn);

    @Query("SELECT COUNT(i) > 0 FROM StandardItem i WHERE (i.nameAr = :nameAr OR i.nameEn = :nameEn) AND i.itemId  != :itemId ")
    boolean existsByNameAndIdNot(@Param("nameAr") String nameAr, @Param("nameEn") String nameEn, @Param("itemId") UUID itemId );
}