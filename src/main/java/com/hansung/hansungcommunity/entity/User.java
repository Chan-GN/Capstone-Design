package com.hansung.hansungcommunity.entity;


import com.hansung.hansungcommunity.dto.user.UserRequestDto;
import com.hansung.hansungcommunity.dto.user.UserUpdateDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "user")
    private List<Board> postBoards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Adopt> qnaAdopt = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_skill",
            joinColumns = {@JoinColumn(name = "stu_id")},
            inverseJoinColumns = {@JoinColumn(name = "skill_id")}
    )
    private Set<Skill> skills;

    @OneToMany(mappedBy = "user")
    private List<Summary> summaries = new ArrayList<>();


    private User(String studentId, String name, String nickname, String introduce, String track1, String track2, String profileImg, Set<Skill> skills) {
        this.studentId = studentId;
        this.name = name;
        this.nickname = nickname;
        this.introduce = introduce;
        this.track1 = track1;
        this.track2 = track2;
        this.profileImg = profileImg;
        this.skills = skills;
    }

    public static User of(UserRequestDto dto, Set<Skill> skills) {
        return new User(
                dto.getStudentId(),
                dto.getName(),
                dto.getNickname(),
                dto.getIntroduce(),
                dto.getTrack1(),
                dto.getTrack2(),
                dto.getPicture(),
                skills
        );
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public void updateUserInfo(UserUpdateDto dto) {
        if (dto.getNickname() != null) this.nickname = dto.getNickname();
        if (dto.getIntroduce() != null) this.introduce = dto.getIntroduce();
    }

}
