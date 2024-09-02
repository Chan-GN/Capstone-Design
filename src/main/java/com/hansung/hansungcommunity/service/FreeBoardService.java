package com.hansung.hansungcommunity.service;

import com.hansung.hansungcommunity.dto.free.*;
import com.hansung.hansungcommunity.dto.media.ImageDto;
import com.hansung.hansungcommunity.entity.FreeBoard;
import com.hansung.hansungcommunity.entity.User;
import com.hansung.hansungcommunity.exception.BoardNotFoundException;
import com.hansung.hansungcommunity.repository.FreeBoardRepository;
import com.hansung.hansungcommunity.repository.UserRepository;
import com.hansung.hansungcommunity.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 생성자 주입 (final 키워드)
@Transactional(readOnly = true) // 읽기 전용
public class FreeBoardService {

    private final FreeBoardRepository freeBoardRepository;
    private final UserRepository userRepository;

    /**
     * 자유 게시글 게시
     */
    @Transactional // 필요 시 쓰기 전용
    public Long createPost(Long userId, FreeBoardRequestDto boardDto) {
        User user = userRepository.getReferenceById(userId);
        FreeBoard board = FreeBoard.createBoard(user, boardDto); // 게시글 생성

        FreeBoard savedBoard = freeBoardRepository.save(board); // DB에 저장

        return savedBoard.getId();
    }

    /**
     * 자유 게시글 수정
     */
    @Transactional
    public FreeBoardRequestDto updatePost(Long boardId, FreeBoardRequestDto dto) {
        FreeBoard target = freeBoardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시글 수정 실패, 해당하는 게시글이 없습니다."));

        // 게시글 수정
        target.patch(dto);

        return dto;
    }

    /**
     * 자유 게시글 삭제
     */
    @Transactional
    public void deletePost(Long boardId) {
        FreeBoard target = freeBoardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시글 삭제 실패, 해당하는 게시글이 없습니다."));

        // 게시글 삭제
        freeBoardRepository.delete(target);
    }

    /**
     * 게시글 리스트 조회
     * 정렬 후, 4개의 게시글만 반환
     */
    public List<FreeBoardMainDto> findAll() {
        Pageable pageable = PageRequest.of(0, 4, Sort.Direction.DESC, "createdAt");

        return freeBoardRepository.findAll(pageable).getContent()
                .stream()
                .map(FreeBoardMainDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 게시글 조회
     */
    public FreeBoardDetailsDto findOne(Long boardId) {
        FreeBoard board = freeBoardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시글 조회 실패, 해당하는 게시글이 없습니다."));

        return FreeBoardDetailsDto.from(board);
    }

    /**
     * 특정 게시글 조회
     */
    public FreeBoard getDetailedPost(Long boardId) {
        return freeBoardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시글 조회 실패, 해당하는 게시글이 없습니다."));
    }

    /**
     * 조회수 증가 로직
     * Auditing 수정 시간 업데이트, 논의 후 해결 요망
     */
    @Transactional
    public void increaseHits(Long boardId) {
        int updatedRows = freeBoardRepository.increaseViewsById(boardId);
        if (updatedRows == 0) {
            throw new BoardNotFoundException("조회수 증가 실패, 해당하는 게시글이 없습니다.");
        }
    }

    /**
     * 게시글 리스트 조회
     * 프론트에서 요청한 페이지 정보에 맞게 게시글 반환
     */
    public List<FreeBoardListDto> findByPage(Pageable pageable, String search) {
        Sort fallbackSort = Sort.by(Sort.Direction.DESC, "createdAt");
        Page<FreeBoard> page;

        if (pageable.getSort().stream().anyMatch(order -> order.getProperty().equals("bookmarks"))) {
            Pageable setPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

            if (search == null) {
                page = freeBoardRepository.findAllSortByBookmarks(setPage);
            } else {
                page = freeBoardRepository.findAllSortByBookmarksWithSearchParam(search, setPage);
            }
        } else {
            Pageable setPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(fallbackSort));

            if (search == null) {
                page = freeBoardRepository.findAll(setPage);
            } else {
                page = freeBoardRepository.findAllWithSearchParam(search, setPage);
            }
        }

        return page.getContent().stream()
                .map(board -> {
                    FreeBoardListDto dto = FreeBoardListDto.from(board);

                    dto.setImage(extractImagesFromContent(board.getContent()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 게시글 수정 시, 기존 게시글 정보 반환
     */
    public FreeBoardUpdateDto getUpdatingData(Long boardId) {
        FreeBoard board = freeBoardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("게시글 조회 실패, 해당하는 게시글이 없습니다."));

        return FreeBoardUpdateDto.from(board);
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
        return freeBoardRepository.count();
    }

    /**
     * 검색된 게시글 수
     */
    private long getTotalWithSearch(String search) {
        return freeBoardRepository.countWithSearch(search);
    }

    /**
     * 이미지 추출
     */
    private List<ImageDto> extractImagesFromContent(String content) {
        List<ImageDto> images = ImageUtils.extractImagesFromContent(content);
        return images;
    }

    @Transactional
    public Long getMappingUser(Long id, FreeBoard freeBoard) {
        User user = userRepository.getReferenceById(id);
        freeBoard.setUser(user);
        freeBoardRepository.save(freeBoard);
        return freeBoard.getId();
    }

}

