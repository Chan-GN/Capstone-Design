package com.hansung.hansungcommunity.service;

import com.hansung.hansungcommunity.dto.user.UserInfoDto;
import com.hansung.hansungcommunity.dto.user.UserRequestDto;
import com.hansung.hansungcommunity.entity.User;
import com.hansung.hansungcommunity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor // 생성자 주입 (final 키워드)
@Transactional(readOnly = true) // 읽기 전용
public class UserService {

    private final UserRepository userRepository;
    private final QnaReplyRepository qnaReplyRepository;
    private final FreeReplyRepository freeReplyRepository;
    private final FreeBoardBookmarkRepository freeBoardBookmarkRepository;
    private final QnaBoardBookmarkRepository qnaBoardBookmarkRepository;

    /**
     * 회원가입
     */
    @Transactional // 필요 시 쓰기 전용
    public Long join(UserRequestDto dto) {
        validateDuplicateUser(dto);
        User user = userRepository.save(User.of(dto));

        return user.getId();
    }

    private void validateDuplicateUser(UserRequestDto dto) {
        if (checkUser(dto.getStudentId())) {
            throw new IllegalStateException("이미 존재하는 학생입니다.");
        }
    }


    public boolean checkUser(String stuId) {
        return userRepository.existsUserByStudentId(stuId);
    }

    public Optional<User> getByStudentId(String studentId) {
        return userRepository.findByStudentId(studentId);
    }

    public UserInfoDto getUserInfo(Long stuId) {
        User user = userRepository.findById(stuId).orElseThrow(()->new RuntimeException("유저가 존재하지 않습니다."));
        int replyCount =
                freeReplyRepository.findAllByUserId(stuId)
                    .orElseThrow(()->new IllegalArgumentException("해당 댓글이 없습니다.")).size()
                + qnaReplyRepository.findAllByUserId(stuId)
                    .orElseThrow(()->new IllegalArgumentException("해당 댓글이 없습니다.")).size();
        int bookmarkCount =
                freeBoardBookmarkRepository.findAllByUserId(stuId)
                        .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다.")).size()
                + qnaBoardBookmarkRepository.findAllByUserId(stuId)
                        .orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다.")).size();

        UserInfoDto userInfoDto = UserInfoDto.from(user);
        userInfoDto.setReply(replyCount);
        userInfoDto.setBookmark(bookmarkCount);

        return userInfoDto;
    }


}
