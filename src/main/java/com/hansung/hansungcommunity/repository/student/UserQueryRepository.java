package com.hansung.hansungcommunity.repository.student;

import com.hansung.hansungcommunity.dto.user.UserInfoDto;
import com.hansung.hansungcommunity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserQueryRepository extends JpaRepository<User, Long> {

    @Query("SELECT new com.hansung.hansungcommunity.dto.user.UserInfoDto(" +
            "u.id, u.studentId, u.nickname, u.name, u.track1, u.track2, " +
            "(SELECT COUNT(b) FROM Board b WHERE b.user = u), " +
            "(SELECT COUNT(bm) FROM Bookmark bm WHERE bm.user = u), " +
            "(SELECT COUNT(*) FROM Party p WHERE p.user = u), " +
            "u.introduce, u.profileImg) " +
            "FROM User u WHERE u.id = :userId")
    UserInfoDto getUserInfoById(@Param("userId") Long id);

    @Query("SELECT s.name FROM User u JOIN u.skills s WHERE u.id = :userId")
    Set<String> getUserSkillNames(@Param("userId") Long userId);

}
