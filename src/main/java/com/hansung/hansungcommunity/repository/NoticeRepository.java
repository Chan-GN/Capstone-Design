package com.hansung.hansungcommunity.repository;

import com.hansung.hansungcommunity.entity.NoticeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<NoticeBoard, Long> {

    List<NoticeBoard> findAllById(Long id);

    @Query("SELECT n FROM NoticeBoard n WHERE n.title LIKE %:search% OR n.content LIKE %:search%")
    Page<NoticeBoard> findAllWithSearchParam(@Param("search") String search, Pageable pageable);

    @Query("SELECT n " +
            "FROM NoticeBoard n LEFT OUTER JOIN n.bookmarks nb " +
            "GROUP BY n.id " +
            "ORDER BY COUNT(nb) DESC")
    Page<NoticeBoard> findAllSortByBookmarks(Pageable pageable);


    @Query("SELECT n " +
            "FROM NoticeBoard n LEFT OUTER JOIN n.bookmarks nb " +
            "WHERE n.title LIKE %:search% OR n.content LIKE %:search% " +
            "GROUP BY n.id " +
            "ORDER BY COUNT(nb) DESC")
    Page<NoticeBoard> findAllSortByBookmarksWithSearchParam(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(n)" +
            "FROM NoticeBoard n " +
            "WHERE n.title LIKE %:search% OR n.content LIKE %:search% ")
    long countWithSearch(@Param("search") String search);

    @Modifying
    @Query("UPDATE Board b SET b.views = b.views + 1 WHERE b.id = :id")
    int increaseViewsById(@Param("id") Long id);

}
