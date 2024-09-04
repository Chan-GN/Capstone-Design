package com.hansung.hansungcommunity.service;

import com.hansung.hansungcommunity.dto.user.UserActivityDto;
import com.hansung.hansungcommunity.dto.user.UserUpdateDto;
import com.hansung.hansungcommunity.entity.*;
import com.hansung.hansungcommunity.exception.DuplicateNicknameException;
import com.hansung.hansungcommunity.exception.SkillNotFoundException;
import com.hansung.hansungcommunity.exception.UserNotFoundException;
import com.hansung.hansungcommunity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final BoardRepository boardRepository;
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final PartyRepository partyRepository;
    private final UserSkillRepository userSkillRepository;

    /**
     * 해당 접속 유저가 작성한 게시글 반환 (최신순서)
     */
    public List<UserActivityDto> getBoardList(Long userId) {
        return boardRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(UserActivityDto::of)
                .collect(Collectors.toList());
    }

    /**
     * 해당 접속 유저가 북마크한 게시글 반환 (최신순서)
     */
    public List<UserActivityDto> getBookmarkList(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(userId);

        return bookmarks.stream()
                .map(bookmark -> UserActivityDto.of(bookmark.getBoard()))
                .sorted(Comparator.comparing(UserActivityDto::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 해당 유저가 신청한 구인 게시글 정보
     */
    public List<UserActivityDto> getApplicationList(Long userId) {
        List<Party> parties = partyRepository.findByUserId(userId);

        return parties.stream()
                .map(party -> UserActivityDto.of(party.getRecruitBoard()))
                .sorted(Comparator.comparing(UserActivityDto::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 유저 자기소개, 관심 기술 수정
     */
    @Transactional
    public void updateUserInfo(UserUpdateDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("유저 정보 수정 실패, 해당 유저가 없습니다."));

        if (!user.getNickname().equals(dto.getNickname())) {
            validateDuplicateNickname(dto.getNickname());
        }

        // 기본 사용자 정보 업데이트
        user.updateUserInfo(dto);

        // 기존 UserSkill 관계를 모두 제거
        userSkillRepository.deleteByUserId(userId);

        // 새로운 UserSkill 관계 생성 및 저장
        List<Skill> skills = skillRepository.findByNameIn(dto.getSkills());
        if (skills.size() != dto.getSkills().size()) {
            throw new SkillNotFoundException("일부 스킬을 찾을 수 없습니다.");
        }

        List<UserSkill> newUserSkills = skills.stream()
                .map(skill -> UserSkill.of(user, skill))
                .collect(Collectors.toList());

        userSkillRepository.saveAll(newUserSkills);
    }

    private void validateDuplicateNickname(String nickname) {
        if (userRepository.existsUserByNickname(nickname)) throw new DuplicateNicknameException("이미 사용 중인 닉네임입니다.");
    }

}
