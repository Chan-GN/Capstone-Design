package com.hansung.hansungcommunity.dto.user;

import com.hansung.hansungcommunity.entity.User;
import lombok.Getter;

/**
 * 단순 유저 정보를 담는 DTO
 */
@Getter
public class SimpleUserInfoDto {

    private Long id;
    private String studentId;
    private String name;
    private String nickname;
    private String profileImg;
    private String introduce;
    private String track1;
    private String track2;

    private SimpleUserInfoDto(User user) {
        this.id = user.getId();
        this.studentId = user.getStudentId();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
        this.introduce = user.getIntroduce();
        this.track1 = user.getTrack1();
        this.track2 = user.getTrack2();
    }

    public static SimpleUserInfoDto from(User user) {
        return new SimpleUserInfoDto(user);
    }

}
