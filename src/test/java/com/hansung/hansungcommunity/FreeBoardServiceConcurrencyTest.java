package com.hansung.hansungcommunity;

import com.hansung.hansungcommunity.dto.free.FreeBoardRequestDto;
import com.hansung.hansungcommunity.dto.user.UserRequestDto;
import com.hansung.hansungcommunity.entity.FreeBoard;
import com.hansung.hansungcommunity.entity.User;
import com.hansung.hansungcommunity.repository.FreeBoardRepository;
import com.hansung.hansungcommunity.repository.UserRepository;
import com.hansung.hansungcommunity.service.FreeBoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FreeBoardServiceConcurrencyTest {

    @Autowired
    private FreeBoardService freeBoardService;

    @Autowired
    private FreeBoardRepository freeBoardRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testConcurrentHitsIncrease() throws InterruptedException {
        // 테스트용 사용자와 게시글 생성
        User user = userRepository.save(User.from(UserRequestDto.builder().build()));
        FreeBoard board = freeBoardRepository.save(FreeBoard.createBoard(
                user,
                FreeBoardRequestDto.of("title", "content")
        ));

        // 동시성 테스트를 위한 스레드 풀 및 CountDownLatch 설정
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // 여러 스레드에서 동시에 조회수 증가 메소드 호출
        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    freeBoardService.increaseHits(board.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 스레드의 작업이 완료될 때까지 대기 (최대 30초)
        latch.await(30, TimeUnit.SECONDS);

        // 스레드 풀 종료 및 대기
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        // 데이터베이스에서 최신 상태의 게시글 조회
        FreeBoard updatedBoard = freeBoardRepository.findById(board.getId()).orElseThrow();

        // 동시성 문제가 없다면 조회수는 스레드 수와 동일해야 함
        assertEquals(numberOfThreads, updatedBoard.getViews(),
                "동시성 문제로 인해 예상 조회수와 실제 조회수가 일치하지 않습니다.");
    }

}