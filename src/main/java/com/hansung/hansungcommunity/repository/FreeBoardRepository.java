package com.hansung.hansungcommunity.repository;

import com.hansung.hansungcommunity.entity.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long> {

    @Query("SELECT f FROM FreeBoard f WHERE f.title LIKE %:search% OR f.content LIKE %:search%")
    Page<FreeBoard> findAllWithSearchParam(@Param("search") String search, Pageable pageable);

    @Query("SELECT f " +
            "FROM FreeBoard f LEFT OUTER JOIN f.bookmarks fb " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(fb) DESC")
    Page<FreeBoard> findAllSortByBookmarks(Pageable pageable);

    @Query("SELECT f " +
            "FROM FreeBoard f LEFT OUTER JOIN f.bookmarks fb " +
            "WHERE f.title LIKE %:search% OR f.content LIKE %:search% " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(fb) DESC")
    Page<FreeBoard> findAllSortByBookmarksWithSearchParam(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(f)" +
            "FROM FreeBoard f " +
            "WHERE f.title LIKE %:search% OR f.content LIKE %:search% ")
    long countWithSearch(@Param("search") String search);

    @Modifying
    @Query("UPDATE Board b SET b.views = b.views + 1 WHERE b.id = :id")
    int increaseViewsById(@Param("id") Long id);

}
