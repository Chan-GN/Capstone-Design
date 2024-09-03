package com.hansung.hansungcommunity.service;

import com.hansung.hansungcommunity.dto.free.FreeBoardRequestDto;
import com.hansung.hansungcommunity.dto.user.UserInfoDto;
import com.hansung.hansungcommunity.dto.user.UserRequestDto;
import com.hansung.hansungcommunity.entity.Bookmark;
import com.hansung.hansungcommunity.entity.FreeBoard;
import com.hansung.hansungcommunity.entity.Skill;
import com.hansung.hansungcommunity.entity.User;
import com.hansung.hansungcommunity.repository.BookmarkRepository;
import com.hansung.hansungcommunity.repository.FreeBoardRepository;
import com.hansung.hansungcommunity.repository.SkillRepository;
import com.hansung.hansungcommunity.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private FreeBoardRepository freeBoardRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private EntityManager entityManager;

    private User testUser;

    @BeforeEach
    void setup() {
        List<String> skillNames = List.of("Java", "Spring");
        Set<Skill> skills = skillNames.stream()
                .map(name -> skillRepository.save(Skill.of(name)))
                .collect(Collectors.toSet());

        UserRequestDto dto = UserRequestDto.builder()
                .studentId("1891239")
                .name("ChanGN")
                .nickname("Coherent")
                .track1("모바일소프트웨어트랙")
                .track2("웹공학트랙")
                .introduce("안녕하세요")
                .picture("example-uri/picture.jpg")
                .skills(skillNames)
                .build();

        testUser = userRepository.save(User.of(dto, skills));

        FreeBoard board1 = freeBoardRepository.save(FreeBoard.createBoard(testUser, FreeBoardRequestDto.of("Test title 1", "Test content 1")));
        freeBoardRepository.save(FreeBoard.createBoard(testUser, FreeBoardRequestDto.of("Test title 2", "Test content 2")));
        bookmarkRepository.save(Bookmark.of(testUser, board1));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("getUserInfo() 정상 동작 테스트")
    void getUserInfo_shouldReturnCorrectUserInfo() {
        UserInfoDto result = userService.getUserInfo(testUser.getId());

        assertThat(result).isNotNull()
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