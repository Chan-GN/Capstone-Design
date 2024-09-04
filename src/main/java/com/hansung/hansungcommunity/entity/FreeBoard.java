package com.hansung.hansungcommunity.entity;

import com.hansung.hansungcommunity.dto.free.FreeBoardRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FreeBoard extends Board {

    private FreeBoard(String title, String content, User user) {
        super(title, content);
        super.setUser(user);
    }

    // 생성 메소드
    public static FreeBoard createBoard(User user, FreeBoardRequestDto dto) {
        return new FreeBoard(
                dto.getTitle(),
                dto.getContent(),
                user
        );
    }

}
