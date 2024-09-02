package com.hansung.hansungcommunity.service;

import com.hansung.hansungcommunity.dto.media.ImageDto;
import com.hansung.hansungcommunity.dto.notice.NoticeBoardDetailsDto;
import com.hansung.hansungcommunity.dto.notice.NoticeBoardDto;
import com.hansung.hansungcommunity.dto.notice.NoticeBoardListDto;
import com.hansung.hansungcommunity.dto.notice.NoticeBoardMainDto;
import com.hansung.hansungcommunity.entity.NoticeBoard;
import com.hansung.hansungcommunity.entity.User;
import com.hansung.hansungcommunity.exception.BoardNotFoundException;
import com.hansung.hansungcommunity.exception.UserNotFoundException;
import com.hansung.hansungcommunity.repository.BoardRepository;
import com.hansung.hansungcommunity.repository.NoticeRepository;
import com.hansung.hansungcommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<NoticeBoardDto> getList(Long id) {
        return boardRepository.findAllByUserIdOrderByCreatedAtDesc(id)
                .stream()
                .flatMap(board -> noticeRepository.findAllById(board.getId()).stream())
                .map(NoticeBoardDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 메인 페이지 공지사항 조회
     * 정렬 후, 4개의 게시글만 반환
     */
    public List<NoticeBoardMainDto> findAll() {
        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.DESC, "createdAt");

        return noticeRepository.findAll(pageable).getContent()
                .stream()
                .map(NoticeBoardMainDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 리스트 조회
     * 프론트에서 요청한 페이지 정보에 맞게 게시글 반환
     */
    public List<NoticeBoardListDto> findByPage(Pageable pageable, String search) {
        Sort fallbackSort = Sort.by(Sort.Direction.DESC, "createdAt");
        Page<NoticeBoard> page;

        if (pageable.getSort().stream().anyMatch(order -> order.getProperty().equals("bookmarks"))) {
            Pageable setPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

            if (search == null) {
                page = noticeRepository.findAllSortByBookmarks(setPage);
            } else {
                page = noticeRepository.findAllSortByBookmarksWithSearchParam(search, setPage);
            }
        } else {
            Pageable setPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(fallbackSort));

            if (search == null) {
                page = noticeRepository.findAll(setPage);
            } else {
                page = noticeRepository.findAllWithSearchParam(search, setPage);
            }
        }

        return page.getContent().stream()
                .map(board -> {
                    NoticeBoardListDto dto = NoticeBoardListDto.from(board);

                    dto.setImage(extractImagesFromContent(board.getContent()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public NoticeBoardDetailsDto getDetailedPost(Long boardId) {
        return NoticeBoardDetailsDto.from(noticeRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("공지사항 조회 실패, 해당하는 공지사항이 없습니다.")));
    }


    @Transactional
    public Long createPost(NoticeBoardDto dto, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("공지사항 작성 실패, 해당하는 관리자가 없습니다."));

        return noticeRepository.save(NoticeBoard.of(dto, user)).getId();
    }

    @Transactional
    public void deletePost(Long boardId) {
        noticeRepository.delete(noticeRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("공지사항 삭제 실패, 해당하는 공지사항이 없습니다.")));
    }

    @Transactional
    public NoticeBoardDto updatePost(NoticeBoardDto dto, Long boardId) {
        NoticeBoard noticeBoard = noticeRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("공지사항 수정 실패, 해당하는 게시글이 없습니다."));

        noticeBoard.patch(dto);

        return NoticeBoardDto.from(noticeRepository.save(noticeBoard));
    }

    @Transactional
    public Long getMappingUser(Long id, NoticeBoard noticeBoard) {
        User user = userRepository.getReferenceById(id);
        noticeBoard.setUser(user);
        noticeRepository.save(noticeBoard);
        return noticeBoard.getId();
    }

    /**
     * 이미지 추출
     */
    private List<ImageDto> extractImagesFromContent(String content) {
        List<ImageDto> images = new ArrayList<>();

        // 정규표현식 패턴
        String patternString = "<img\\s+[^>]*src\\s*=\\s*\"([^\"]*)\"[^>]*>";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(content);

        // 매칭된 이미지 URL 추출
        while (matcher.find()) {
            String imageUrl = matcher.group(1);
            String resizeUrl = imageUrl + "/resize";
            ImageDto imageDto = new ImageDto();
            imageDto.setImageUrl(resizeUrl);
            images.add(imageDto);
        }

        return images;
    }

    /**
     * 게시글 수
     * 검색어 유무에 따라 다른 메소드 호출
     */
    public long getCount(String search) {
        return search == null ? getTotal() : getTotalWithSearch(search);
    }

    /**
     * 전체 게시글 수
     */
    public long getTotal() {
        return noticeRepository.count();
    }

    /**
     * 검색된 게시글 수
     */
    private long getTotalWithSearch(String search) {
        return noticeRepository.countWithSearch(search);
    }

    /**
     * 조회수 증가 로직
     * Auditing 수정 시간 업데이트, 논의 후 해결 요망
     */
    @Transactional
    public void increaseHits(Long boardId) {
        int updatedRows = noticeRepository.increaseViewsById(boardId);
        if (updatedRows == 0) {
            throw new BoardNotFoundException("조회수 증가 실패, 해당하는 게시글이 없습니다.");
        }
    }

    public NoticeBoard getPost(Long boardId) {
        return noticeRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시글 조회 실패, 해당하는 게시글이 없습니다."));
    }

}
