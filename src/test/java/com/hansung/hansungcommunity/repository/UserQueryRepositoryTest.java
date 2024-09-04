package com.hansung.hansungcommunity.repository;

import com.hansung.hansungcommunity.dto.free.FreeBoardRequestDto;
import com.hansung.hansungcommunity.dto.user.UserInfoDto;
import com.hansung.hansungcommunity.dto.user.UserRequestDto;
import com.hansung.hansungcommunity.entity.*;
import com.hansung.hansungcommunity.repository.student.UserQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserQueryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserQueryRepository userQueryRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        Skill javaSkill = entityManager.persist(Skill.of("Java"));
        Skill springSkill = entityManager.persist(Skill.of("Spring"));

        UserRequestDto dto = UserRequestDto.builder()
                .studentId("1891239")
                .name("ChanGN")
                .nickname("Coherent")
                .track1("모바일소프트웨어트랙")
                .track2("웹공학트랙")
                .introduce("안녕하세요")
                .picture("example-uri/picture.jpg")
                .build();

        testUser = entityManager.persist(User.from(dto));
        entityManager.persist(UserSkill.of(testUser, javaSkill));
        entityManager.persist(UserSkill.of(testUser, springSkill));

        FreeBoard board1 = entityManager.persist(FreeBoard.createBoard(testUser, FreeBoardRequestDto.of("Test title 1", "Test content 1")));
        entityManager.persist(FreeBoard.createBoard(testUser, FreeBoardRequestDto.of("Test title 2", "Test content 2")));
        entityManager.persist(Bookmark.of(testUser, board1));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("getUserInfoById(), getUserSkillNames() 정상 동작 테스트")
    void getUserInfoById_shouldReturnCorrectUserInfo() {
        UserInfoDto result = userQueryRepository.getUserInfoById(testUser.getId());
        Set<String> userSkills = userQueryRepository.getUserSkillNames(testUser.getId());
        userSkills.forEach(result::addSkills);

        assertThat(result)
                .isNotNull()
                .satisfies(userInfo -> {
                    assertThat(userInfo.getId()).isEqualTo(testUser.getId());
                    assertThat(userInfo.getStudentId()).isEqualTo("1891239");
                    assertThat(userInfo.getName()).isEqualTo("ChanGN");
                    assertThat(userInfo.getNickname()).isEqualTo("Coherent");
                    assertThat(userInfo.getTrack1()).isEqualTo("모바일소프트웨어트랙");
                    assertThat(userInfo.getTrack2()).isEqualTo("웹공학트랙");
                    assertThat(userInfo.getPostBoardCount()).isEqualTo(2);
                    assertThat(userInfo.getBookmarkCount()).isEqualTo(1);
                    assertThat(userInfo.getApplicationCount()).isEqualTo(0);
                    assertThat(userInfo.getIntroduce()).isEqualTo("안녕하세요");
                    assertThat(userInfo.getProfileImg()).isEqualTo("example-uri/picture.jpg");
                    assertThat(userInfo.getSkills()).containsExactlyInAnyOrder("Java", "Spring");
                });
    }

}