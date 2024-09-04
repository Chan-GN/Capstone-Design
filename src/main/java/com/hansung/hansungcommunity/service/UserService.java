package com.hansung.hansungcommunity.service;

import com.hansung.hansungcommunity.dto.user.UserCheckNicknameDto;
import com.hansung.hansungcommunity.dto.user.UserInfoDto;
import com.hansung.hansungcommunity.dto.user.UserRankDto;
import com.hansung.hansungcommunity.dto.user.UserRequestDto;
import com.hansung.hansungcommunity.entity.Skill;
import com.hansung.hansungcommunity.entity.User;
import com.hansung.hansungcommunity.entity.UserSkill;
import com.hansung.hansungcommunity.exception.DuplicateStudentException;
import com.hansung.hansungcommunity.exception.SkillNotFoundException;
import com.hansung.hansungcommunity.repository.*;
import com.hansung.hansungcommunity.repository.student.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    private final UserSkillRepository userSkillRepository;

    /**
     * 회원가입
     */
    @Transactional // 필요 시 쓰기 전용
    public Long join(UserRequestDto dto) {
        validateDuplicateUser(dto);
        User user = userRepository.save(User.from(dto));

        // 모든 필요한 스킬을 한 번에 조회
        List<Skill> skills = skillRepository.findByNameIn(dto.getSkills());

        // 존재하지 않는 스킬이 있는지 확인
        if (skills.size() != dto.getSkills().size()) {
            throw new SkillNotFoundException("회원가입 실패, 존재하지 않는 기술이 있습니다.");
        }

        // UserSkill 객체들을 생성
        List<UserSkill> userSkills = skills.stream()
                .map(skill -> UserSkill.of(user, skill))
                .collect(Collectors.toList());

        // 한 번의 호출로 모든 UserSkill 저장
        userSkillRepository.saveAll(userSkills);

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
