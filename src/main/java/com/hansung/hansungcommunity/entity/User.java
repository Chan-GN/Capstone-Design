package com.hansung.hansungcommunity.entity;


import com.hansung.hansungcommunity.dto.user.UserRequestDto;
import com.hansung.hansungcommunity.dto.user.UserUpdateDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "student")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stu_id")
    private Long id;
    private String studentId;
    private String name; // 이름
    private String nickname; // 닉네임
    private String introduce; // 소개글
    private String track1;
    private String track2;
    private String profileImg; // 프로필 사진, 추후 개발

    private User(String studentId, String name, String nickname, String introduce, String track1, String track2, String profileImg) {
        this.studentId = studentId;
        this.name = name;
        this.nickname = nickname;
        this.introduce = introduce;
        this.track1 = track1;
        this.track2 = track2;
        this.profileImg = profileImg;
    }

    public static User from(UserRequestDto dto) {
        return new User(
                dto.getStudentId(),
                dto.getName(),
                dto.getNickname(),
                dto.getIntroduce(),
                dto.getTrack1(),
                dto.getTrack2(),
                dto.getPicture()
        );
    }

    public void updateUserInfo(UserUpdateDto dto) {
        if (dto.getNickname() != null) this.nickname = dto.getNickname();
        if (dto.getIntroduce() != null) this.introduce = dto.getIntroduce();
    }

}
