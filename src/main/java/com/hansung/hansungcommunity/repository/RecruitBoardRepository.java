package com.hansung.hansungcommunity.repository;

import com.hansung.hansungcommunity.entity.RecruitBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitBoardRepository extends JpaRepository<RecruitBoard, Long> {

    @Query("SELECT r FROM RecruitBoard r WHERE r.title LIKE %:search% OR r.content LIKE %:search%")
    Page<RecruitBoard> findAllWithSearchParam(@Param("search") String search, Pageable pageable);

    @Query("SELECT r " +
            "FROM RecruitBoard r LEFT OUTER JOIN r.bookmarks rb " +
            "GROUP BY r.id " +
            "ORDER BY r.isCompleted ASC, COUNT(rb) DESC")
    Page<RecruitBoard> findAllSortByBookmarks(Pageable setPage);

    @Query("SELECT r " +
            "FROM RecruitBoard r LEFT OUTER JOIN r.bookmarks rb " +
            "WHERE r.title LIKE %:search% OR r.content LIKE %:search% " +
            "GROUP BY r.id " +
            "ORDER BY r.isCompleted ASC, COUNT(rb) DESC")
    Page<RecruitBoard> findAllSortByBookmarksWithSearchParam(@Param("search") String search, Pageable setPage);

    @Query("SELECT COUNT(r)" +
            "FROM RecruitBoard r " +
            "WHERE r.title LIKE %:search% OR r.content LIKE %:search% ")
    long countWithSearch(@Param("search") String search);

    @Modifying
    @Query("UPDATE RecruitBoard b SET b.views = b.views + 1 WHERE b.id = :id")
    int increaseViewsById(@Param("id") Long id);

}
