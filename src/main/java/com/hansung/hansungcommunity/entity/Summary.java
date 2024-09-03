package com.hansung.hansungcommunity.entity;

import com.hansung.hansungcommunity.dto.user.UserSummaryDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Summary extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String language;
    private boolean isFixed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Summary(Long summaryId, String content, boolean isFixed, String language) {
        this.id = summaryId;
        this.content = content;
        this.language = language;
        this.isFixed = isFixed;
    }

    public static Summary of(UserSummaryDto dto) {
        return new Summary(
                dto.getSummaryId(),
                dto.getContent(),
                false,
                dto.getLanguage()
        );
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    // 고정
    public void fix() {
        this.isFixed = true;
    }

    // 고정 해제
    public void release() {
        this.isFixed = false;
    }

}
