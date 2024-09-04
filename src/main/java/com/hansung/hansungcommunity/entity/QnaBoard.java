package com.hansung.hansungcommunity.entity;


import com.hansung.hansungcommunity.dto.qna.QnaBoardRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaBoard extends Board {

    private String language;

    private QnaBoard(User user, String title, String content, String language) {
        super(title, content);
        super.setUser(user);
        this.language = language;
    }

    public static QnaBoard of(User user, String title, String content, String language) {
        return new QnaBoard(user, title, content, language);
    }

    public void patch(QnaBoardRequestDto dto) {
        updateTitleAndContent(dto.getTitle(), dto.getContent());
        this.language = dto.getLanguage();
    }

}
