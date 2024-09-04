package com.hansung.hansungcommunity.entity;


import com.hansung.hansungcommunity.dto.notice.NoticeBoardDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeBoard extends Board {

    private NoticeBoard(String title, String content, User user) {
        super(title, content);
        super.setUser(user);
    }

    public static NoticeBoard of(NoticeBoardDto dto, User user) {
        return new NoticeBoard(
                dto.getTitle(),
                dto.getContent(),
                user
        );
    }

}
