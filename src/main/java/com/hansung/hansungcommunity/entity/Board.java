package com.hansung.hansungcommunity.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Board extends ModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;
    private int views;
    @ManyToOne(fetch = FetchType.LAZY) // JPA 활용 시, XToOne 인 경우 fetch 타입을 LAZY 로 설정 !!!
    @JoinColumn(name = "stu_id")
    private User user;
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private Set<Bookmark> bookmarks = new HashSet<>();
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<FileEntity> fileEntity = new ArrayList<>();

    protected Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBoardType() {
        return this.getClass().getSimpleName(); // 게시글 타입은 엔티티 클래스의 이름(기본값)
    }

    public void updateTitleAndContent(String title, String content) {
        this.title = title;
        this.content = content;
        modified();
    }

}