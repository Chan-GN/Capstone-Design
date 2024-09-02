package com.hansung.hansungcommunity.dto.free;

import com.hansung.hansungcommunity.entity.FreeBoard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FreeBoardRequestDto {

    private Long id;

    @NotEmpty
    @Size(min = 3, max = 30)
    private String title;

    @NotEmpty
    private String content;

    private FreeBoardRequestDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public FreeBoardRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static FreeBoardRequestDto from(FreeBoard freeBoard) {
        return new FreeBoardRequestDto(
                freeBoard.getId(),
                freeBoard.getTitle(),
                freeBoard.getContent()
        );
    }

    public static FreeBoardRequestDto of(String title, String content) {
        return new FreeBoardRequestDto(title, content);
    }

}
