package com.hansung.hansungcommunity.repository;

import com.hansung.hansungcommunity.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    @Modifying
    @Query("DELETE FROM UserSkill us WHERE us.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Query("SELECT s.name FROM UserSkill us JOIN us.skill s WHERE us.user.id = :userId")
    List<String> findSkillNameByUserId(@Param("userId") Long userId);

}
