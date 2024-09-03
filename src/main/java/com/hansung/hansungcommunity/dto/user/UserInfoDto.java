package com.hansung.hansungcommunity.dto.user;

import com.hansung.hansungcommunity.entity.Skill;
import com.hansung.hansungcommunity.entity.User;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class UserInfoDto {

    private final Long id;
    private final String studentId;
    private final String nickname;
    private final String name;
    private final String track1;
    private final String track2;
    private final int postBoardCount;
    private final int bookmarkCount;
    private final String introduce;
    private final String profileImg;
    private final Set<String> skills; // 관심 기술
    private int application;

    private UserInfoDto(Long id, String studentId, String name, String nickname, String track1, String track2, int postBoardCount, int bookmarkCount, String introduce, String profileImg, Set<Skill> skills) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.nickname = nickname;
        this.track1 = track1;
        this.track2 = track2;
        this.postBoardCount = postBoardCount;
        this.bookmarkCount = bookmarkCount;
        this.introduce = introduce;
        this.profileImg = profileImg;
        this.skills = skills.stream().map(Skill::getName).collect(Collectors.toSet());
    }

    public static UserInfoDto from(User user) {
        return new UserInfoDto(
                user.getId(),
                user.getStudentId(),
                user.getName(),
                user.getNickname(),
                user.getTrack1(),
                user.getTrack2(),
                user.getPostBoards().size(),
                user.getBookmarks().size(),
                user.getIntroduce(),
                user.getProfileImg(),
                user.getSkills()
        );
    }

    public void setApplication(int application) {
        this.application = application;
    }

}
