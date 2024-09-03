package com.hansung.hansungcommunity.dto.user;

import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class UserInfoDto {

    private final Long id;
    private final String studentId;
    private final String nickname;
    private final String name;
    private final String track1;
    private final String track2;
    private final long postBoardCount;
    private final long bookmarkCount;
    private final String introduce;
    private final String profileImg;
    private final long applicationCount;
    private final Set<String> skills = new HashSet<>(); // 관심 기술

    public UserInfoDto(Long id, String studentId, String nickname, String name, String track1, String track2, long postBoardCount, long bookmarkCount, long applicationCount, String introduce, String profileImg) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.nickname = nickname;
        this.track1 = track1;
        this.track2 = track2;
        this.postBoardCount = postBoardCount;
        this.bookmarkCount = bookmarkCount;
        this.applicationCount = applicationCount;
        this.introduce = introduce;
        this.profileImg = profileImg;
    }

    public void addSkills(String skillName) {
        this.skills.add(skillName);
    }

}
