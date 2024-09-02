package com.hansung.hansungcommunity.service;


import com.hansung.hansungcommunity.dto.media.ImageDto;
import com.hansung.hansungcommunity.dto.qna.*;
import com.hansung.hansungcommunity.entity.QnaBoard;
import com.hansung.hansungcommunity.entity.User;
import com.hansung.hansungcommunity.exception.BoardNotFoundException;
import com.hansung.hansungcommunity.repository.QnaBoardRepository;
import com.hansung.hansungcommunity.repository.UserRepository;
import com.hansung.hansungcommunity.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class QnaBoardService {

    private final UserRepository userRepository;
    private final QnaBoardRepository qnaBoardRepository;

    /**
     * 특정 게시글 조회
     */
    public QnaBoardDetailsDto findOne(Long boardId) {
        QnaBoard board = qnaBoardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시글 조회 실패, 해당하는 게시글이 없습니다."));

        return QnaBoardDetailsDto.from(board);
    }

    /**
     * 정렬 된 4개 Qna 게시글 반환
     */
    public List<QnaBoardMainDto> findAll() {
        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.DESC, "createdAt");

        return qnaBoardRepository.findAll(pageable).getContent()
                .stream()
                .map(QnaBoardMainDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 저장
     */
    @Transactional
    public Long createPost(Long userId, QnaBoardRequestDto dto) {
        User user = userRepository.getReferenceById(userId);
        QnaBoard board = QnaBoard.of(user, dto.getTitle(), dto.getContent(), dto.getLanguage());

        QnaBoard savedBoard = qnaBoardRepository.save(board);

        return savedBoard.getId();
    }

    /**
     * user 컬럼만 비어있는 qnaBoard 엔티티에 유저 매핑
     */
    @Transactional
    public Long getMappingUser(Long userId, QnaBoard entity) {
        User user = userRepository.getReferenceById(userId);

        entity.setUser(user);
        qnaBoardRepository.save(entity);
        return entity.getId();
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public Long update(Long boardId, QnaBoardRequestDto dto) {
        QnaBoard target = qnaBoardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시글 수정 실패, 해당하는 게시글이 없습니다."));

        target.patch(dto);

        return boardId;
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void delete(Long boardId) {
        qnaBoardRepository.deleteById(boardId);
    }

    /**
     * 조회수 증가 로직
     * Auditing 수정 시간 업데이트, 논의 후 해결 요망
     */
    @Transactional
    public void increaseHits(Long boardId) {
        int updatedRows = qnaBoardRepository.increaseViewsById(boardId);
        if (updatedRows == 0) {
            throw new BoardNotFoundException("조회수 증가 실패, 해당하는 게시글이 없습니다.");
        }
    }

    /**
     * 게시글 리스트 조회
     * 프론트에서 요청한 페이지 정보에 맞게 게시글 반환
     */
    public List<QnaBoardListDto> findByPage(Pageable pageable, String search) {
        Sort fallbackSort = Sort.by(Sort.Direction.DESC, "createdAt");
        Page<QnaBoard> page;

        if (pageable.getSort().stream().anyMatch(order -> order.getProperty().equals("bookmarks"))) {
            Pageable setPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

            if (search == null) {
                page = qnaBoardRepository.findAllSortByBookmarks(setPage);
            } else {
                page = qnaBoardRepository.findAllSortByBookmarksWithSearchParam(search, setPage);
            }
        } else {
            Pageable setPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(fallbackSort));

            if (search == null) {
                page = qnaBoardRepository.findAll(setPage);
            } else {
                page = qnaBoardRepository.findAllWithSearchParam(search, setPage);
            }
        }

        return page.getContent()
                .stream()
                .map(board -> {
                    QnaBoardListDto dto = QnaBoardListDto.from(board);
                    dto.setImage(extractImagesFromContent(board.getContent()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Long getUserId(Long boardId) {
        QnaBoard qnaBoard = qnaBoardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("해당하는 게시글이 없습니다."));

        return qnaBoard.getUser().getId();
    }

    /**
     * 게시글 수정 시, 기존 게시글 정보 반환
     */
    public QnaBoardUpdateDto findOneForUpdate(Long boardId) {
        QnaBoard board = qnaBoardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시글 조회 실패, 해당하는 게시글이 없음"));

        return QnaBoardUpdateDto.from(board);
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
        return qnaBoardRepository.count();
    }

    /**
     * 검색된 게시글 수
     */
    private long getTotalWithSearch(String search) {
        return qnaBoardRepository.countWithSearch(search);
    }

    /**
     * 이미지 추출
     */
    private List<ImageDto> extractImagesFromContent(String content) {
        List<ImageDto> images = ImageUtils.extractImagesFromContent(content);

        return images;
    }

    public QnaBoard get(Long boardId) {
        return qnaBoardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시글 조회 실패, 해당하는 게시글이 없습니다."));
    }

}
