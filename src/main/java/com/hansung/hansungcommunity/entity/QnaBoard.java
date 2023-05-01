package com.hansung.hansungcommunity.entity;


import com.hansung.hansungcommunity.dto.qna.QnaBoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Getter
@ToString(callSuper = true)
@Entity
@NoArgsConstructor
public class QnaBoard extends Board {

    @Id
    private Long id;
    @NotNull
    private String title;
    @NotNull
    @Lob
    private String content;  //TODO: length 설정하기
    private int point;
    @Column
    private String tag;
    @Column
    private String language;

    @OneToMany(fetch = FetchType.LAZY)
    private List<FileEntity> fileEntity = new ArrayList<>();

    @ToString.Exclude
    @JoinColumn(name = "stu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 조회 편의성을 위해 댓글 Entity 와 연관관계 매핑
    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<QnaReply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "qnaBoard")
    private Set<QnaBoardBookmark> bookmarks = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "adopt_id")
    private Adopt adopt;

    public QnaBoard(User user, String title, String content, String tag, int point, String language) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.point = point;
        this.language = language;
    }

    public QnaBoard(String title, String content, int point, String language) {
        this.title = title;
        this.content = content;
        this.point = point;
        this.language = language;
    }

    //추후 다른 곳에서(EX.. Test)에서 편하게 만들기위해 Factory method 사용
    public static QnaBoard of(User user, String title, String content, String tag, int point, String language) {
        return new QnaBoard(user, title, content, tag, point, language);
    }

    public static QnaBoard of(String title, String content, int point, String language) {
        return new QnaBoard(title, content, point, language);
    }

    public void updateBoard(QnaBoardRequestDto dto) {
        if (dto.getTitle() != null) this.title = dto.getTitle();
        if (dto.getContent() != null) this.content = dto.getContent();
        this.point = dto.getPoint();
        this.language = dto.getLanguage();
        modified();
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QnaBoard)) return false;
        QnaBoard that = (QnaBoard) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    // 조회수 증가 메소드
    public void increaseHits() {
        increaseViews();
    }

}
