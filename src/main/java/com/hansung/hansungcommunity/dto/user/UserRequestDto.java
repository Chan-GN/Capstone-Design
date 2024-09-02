package com.hansung.hansungcommunity.dto.user;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequestDto {

    private String studentId;
    private String name; // 이름
    @Size(min = 2, max = 8)
    private String nickname; // 닉네임
    @Size(max = 100)
    private String introduce; // 소개글
    private String track1;
    private String track2;
    @Size(max = 7)
    private List<String> skills; // 관심 기술
    private String picture;

}

