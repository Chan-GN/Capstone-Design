package com.hansung.hansungcommunity.repository;

import com.hansung.hansungcommunity.entity.QnaBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long> {

    @Query("SELECT q FROM QnaBoard q WHERE q.title LIKE %:search% OR q.content LIKE %:search%")
    Page<QnaBoard> findAllWithSearchParam(@Param("search") String search, Pageable pageable);

    @Query("SELECT q " +
            "FROM QnaBoard q LEFT OUTER JOIN q.bookmarks qb " +
            "GROUP BY q.id " +
            "ORDER BY COUNT(qb) DESC")
    Page<QnaBoard> findAllSortByBookmarks(Pageable setPage);

    @Query("SELECT q " +
            "FROM QnaBoard q LEFT OUTER JOIN q.bookmarks qb " +
            "WHERE q.title LIKE %:search% OR q.content LIKE %:search% " +
            "GROUP BY q.id " +
            "ORDER BY COUNT(qb) DESC")
    Page<QnaBoard> findAllSortByBookmarksWithSearchParam(@Param("search") String search, Pageable setPage);

    @Query("SELECT COUNT(q)" +
            "FROM QnaBoard q " +
            "WHERE q.title LIKE %:search% OR q.content LIKE %:search% ")
    long countWithSearch(@Param("search") String search);

    @Modifying
    @Query("UPDATE QnaBoard b SET b.views = b.views + 1 WHERE b.id = :id")
    int increaseViewsById(@Param("id") Long id);

}
