package com.hansung.hansungcommunity.dto.recruit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hansung.hansungcommunity.entity.User;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * 신청자 정보를 담는 DTO
 */
@Getter
public class ApplicantDto {

    private final Long id;
    private final String nickname;
    private final String studentId;
    private final String introduce;
    private final String profileImg;
    private final String track1;
    private final Set<String> skills = new HashSet<>();
    @JsonProperty("isMeetRequired")
    private boolean isMeetRequired;
    @JsonProperty("isMeetOptional")
    private Boolean isMeetOptional;
    @JsonProperty("isApproved")
    private boolean isApproved;

    private ApplicantDto(User user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.studentId = user.getStudentId();
        this.introduce = user.getIntroduce();
        this.profileImg = user.getProfileImg();
        this.track1 = user.getTrack1();
    }

    public static ApplicantDto from(User user) {
        return new ApplicantDto(user);
    }

    public void addSkillName(String skillName) {
        this.skills.add(skillName);
    }

    public void setMeetRequired(boolean meetRequired) {
        isMeetRequired = meetRequired;
    }

    public void setIsMeetOptional(Boolean meetOptional) {
        isMeetOptional = meetOptional;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

}
