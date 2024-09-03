package com.hansung.hansungcommunity.service;

import com.hansung.hansungcommunity.dto.user.UserCheckNicknameDto;
import com.hansung.hansungcommunity.dto.user.UserInfoDto;
import com.hansung.hansungcommunity.dto.user.UserRankDto;
import com.hansung.hansungcommunity.dto.user.UserRequestDto;
import com.hansung.hansungcommunity.entity.Skill;
import com.hansung.hansungcommunity.entity.User;
import com.hansung.hansungcommunity.exception.DuplicateStudentException;
import com.hansung.hansungcommunity.exception.SkillNotFoundException;
import com.hansung.hansungcommunity.repository.AdoptRepository;
import com.hansung.hansungcommunity.repository.PartyRepository;
import com.hansung.hansungcommunity.repository.SkillRepository;
import com.hansung.hansungcommunity.repository.UserRepository;
import com.hansung.hansungcommunity.repository.student.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 생성자 주입 (final 키워드)
@Transactional(readOnly = true) // 읽기 전용
public class UserService {

    private final UserRepository userRepository;
    private final AdoptRepository adoptRepository;
    private final SkillRepository skillRepository;
    private final PartyRepository partyRepository;
    private final UserQueryRepository userQueryRepository;

    /**
     * 회원가입
     */
    @Transactional // 필요 시 쓰기 전용
    public Long join(UserRequestDto dto) {
        validateDuplicateUser(dto);
        Set<Skill> skills = dto.getSkills().stream().map(s -> skillRepository.findByName(s)
                        .orElseThrow(() -> new SkillNotFoundException("회원가입 실패, 해당하는 기술이 없습니다.")))
                .collect(Collectors.toSet());

        User user = userRepository.save(User.of(dto, skills));

        return user.getId();
    }

    private void validateDuplicateUser(UserRequestDto dto) {
        if (checkUser(dto.getStudentId())) {
            throw new DuplicateStudentException("이미 존재하는 학생입니다.");
        }
    }

    public boolean checkUser(String stuId) {
        return userRepository.existsUserByStudentId(stuId);
    }

    public Optional<User> getByStudentId(String studentId) {
        return userRepository.findByStudentId(studentId);
    }

    public UserInfoDto getUserInfo(Long stuId) {
        UserInfoDto userInfoDto = userQueryRepository.getUserInfoById(stuId);
        userQueryRepository.getUserSkillNames(stuId)
                .forEach(userInfoDto::addSkills);

        return userInfoDto;
    }

    public List<UserRankDto> getUserRank() {
        return adoptRepository.findTop5UsersByAdoptCount()
                .stream()
                .map(UserRankDto::of)
                .limit(5)
                .collect(Collectors.toList());
    }

    public boolean checkUserNickname(UserCheckNicknameDto dto) {
        User user = userRepository.findByNickname(dto.getNickname());

        return user != null;
    }

}
