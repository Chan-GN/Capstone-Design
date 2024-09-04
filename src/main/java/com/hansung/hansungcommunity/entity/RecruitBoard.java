package com.hansung.hansungcommunity.entity;

import com.hansung.hansungcommunity.dto.recruit.RecruitBoardRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitBoard extends Board {

    private String required;
    private String optional;
    private int party; // 모집할 인원 수
    private int gathered; // 모집된 인원 수
    private boolean isCompleted; // 모집 완료 여부

    private RecruitBoard(String title, String content, String required, String optional, int party, int gathered, User user) {
        super(title, content);
        super.setUser(user);
        this.required = required;
        this.optional = optional;
        this.party = party;
        this.gathered = gathered;
        this.isCompleted = party <= gathered;
    }

    public static RecruitBoard createBoard(RecruitBoardRequestDto dto, User user) {
        return new RecruitBoard(
                dto.getTitle(),
                dto.getContent(),
                dto.getRequired(),
                dto.getOptional(),
                dto.getParty(),
                dto.getGathered(),
                user
        );
    }

    // 자동 모집 완료 처리
    public void updateIsCompleted(Long count) {
        this.isCompleted = this.gathered + count >= this.party;
    }

    public void patch(RecruitBoardRequestDto dto) {
        updateTitleAndContent(dto.getTitle(), dto.getContent());
        updatePartyAndOptional(dto);
    }

    private void updatePartyAndOptional(RecruitBoardRequestDto dto) {
        if (dto.getOptional() != null)
            this.optional = dto.getOptional();
        this.party = dto.getParty();
    }

    // 작성자에 의한 모집 완료 처리
    public void complete() {
        this.isCompleted = true;
    }

}
