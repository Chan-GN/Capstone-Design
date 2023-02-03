package com.hansung.hansungcommunity.entity;

import com.hansung.hansungcommunity.dto.FreeArticleDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Table(name = "free_article")
@Getter
@Setter
@Entity
public class FreeArticle {
    @Id
    @GeneratedValue
    @Column(name = "free_article_id")
    private Long id;
    private String title; // 제목
    private String content; // 내용
    private LocalDateTime postDate; // 작성 시간
    private int hits; // 조회수
    private int bookmarkHits; // 북마크 횟수
    private int report; // 신고 횟수

    @ManyToOne(fetch = FetchType.LAZY) // JPA 활용 시, XToOne 인 경우 fetch 타입을 LAZY 로 설정 !!!
    @JoinColumn(name = "user_id")
    private User user;

    // private Image image; // 게시글 내부 이미지, 추후 개발

    // 생성 메소드
    public static FreeArticle createArticle(User user, FreeArticleDto dto) {
        FreeArticle article = new FreeArticle();

        article.setUser(user); // 연관관계 설정

        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setHits(0);
        article.setBookmarkHits(0);
        article.setReport(0);

        article.setPostDate(LocalDateTime.now()); // 작성 일자, @CreationTimestamp 활용할지 ?

        return article;
    }

    // 연관관계 메소드
    public void setUser(User user) {
        this.user = user;
        user.getPostFreeArticles().add(this); // 필요한가?
    }

    // 비즈니스 메소드
    public void patch(FreeArticleDto dto) {
        if (dto.getTitle() != null)
            this.title = dto.getTitle();

        if (dto.getContent() != null)
            this.content = dto.getContent();
    }
}
