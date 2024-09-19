# Ping Pong Resource Server

## 프로젝트 소개

이 프로젝트는 한성대학교 캡스톤 디자인 작품의 일환으로, **OIDC(OpenID Connect) 프로토콜을 지원하는 리소스 서버**를 구현한 것입니다. **Spring Resource Server** 프레임워크를 기반으로 개발되었으며, 한성대학교 학생들을 위한 커뮤니티 플랫폼의 백엔드 시스템 역할을 수행합니다.

이 프로젝트의 주요 목적은 **한성대학교 학생들을 위한 안전하고 효율적인 커뮤니티 플랫폼을 구축하는 것**입니다. 이를 통해 학생들 간의 소통을 활성화하고, 다양한 정보 공유 및 팀 프로젝트 구인 등의 기능을 제공하고자 합니다.

## 개발 기간

- **2023/03/20 ~ 2023/06/01**

## 개발 환경

- 개발 언어: **`Java 11`**
- IDE: `IntelliJ IDEA`
- 프레임워크: **`Spring Boot 2`**, **`Spring Security`**, **`Spring Data JPA`**
- 데이터베이스: `MySQL`
- 빌드 도구: `Gradle`
- 파일 저장소: `Firebase Storage`

## 프로젝트 주요 기능

1. **사용자 인증 및 권한 관리**
    - OIDC 프로토콜 기반 인증
    - 역할 기반 접근 제어 (ROLE_STUDENT, ROLE_USER, ROLE_ADMIN)
2. **게시판 시스템**
    - 자유 게시판
    - Q&A 게시판
    - 구인 게시판
    - 공지사항
3. **구인 게시판 특화 기능**
    - 팀 소속 신청 및 승인 시스템
    - 모집 완료 처리
4. **사용자 프로필 관리**
5. **북마크 기능**
6. **댓글 시스템**
7. **파일 업로드**
8. **검색 기능**
9. **인기 게시글 조회**
10. **사용자 랭킹 시스템**
11. **일일 학습 요약 기능**

## 개인 역할 및 주요 기여 사항

이 [프로젝트](https://github.com/HansungCapstoneDesign)는 4인 팀 프로젝트로 진행되었으며, 제가 담당한 주요 기여 사항은 다음과 같습니다.

1. **OIDC Resource Server 구현**
    - Spring Resource Server를 활용한 JWT 기반 토큰 검증 시스템 구축
    - `SecurityConfig` 클래스에서 리소스 서버 관련 설정 구성

2. **사용자 역할 기반 접근 제어 시스템 구현**
    - ROLE_STUDENT, ROLE_USER, ROLE_ADMIN 권한 관리

3. **RESTful API 설계 및 구현**
    - 자유 게시판, 구인 게시판 CRUD 및 검색 기능 개발
    - 사용자 프로필 관리 및 마이페이지 기능 구현

4. **구인 게시판 특화 기능 개발**
    - 팀 소속 신청 및 승인 시스템 구현
    - 모집 완료 처리 로직 개발

5. **JPA를 활용한 데이터베이스 설계 및 구현**
    - 게시판, 사용자, 북마크 등 주요 엔티티 설계
    - 상속 구조를 활용한 다양한 게시판 타입 구현

6. **글로벌 예외 핸들러 구축**
    - 일관된 에러 처리 시스템 개발

7. **부가 기능 구현**
    - 인기 게시글 조회 로직
    - 사용자 랭킹 시스템
    - 일일 학습 요약 기능

## 프로젝트 구조

```
com.hansung.hansungcommunity
├── auth
│   └── CustomAuthentication.java
├── config
│   ├── CustomJwtAuthenticationTokenConverter.java
│   ├── FireBaseInitializer.java
│   ├── JpaAuditingConfig.java
│   ├── SecurityConfig.java
│   └── WebMvcConfig.java
├── controller
│   ├── AdminController.java
│   ├── BoardController.java
│   ├── FirebaseController.java
│   ├── FreeBoardApiController.java
│   ├── FreeBoardBookmarkController.java
│   ├── FreeReplyController.java
│   ├── GlobalExceptionHandler.java
│   ├── MyPageController.java
│   ├── NoticeBoardBookmarkController.java
│   ├── NoticeController.java
│   ├── NoticeReplyController.java
│   ├── QnaBoardApiController.java
│   ├── QnaBoardBookmarkController.java
│   ├── QnaReplyController.java
│   ├── RecruitBoardBookmarkController.java
│   ├── RecruitBoardController.java
│   ├── RecruitReplyController.java
│   ├── SummaryController.java
│   └── UserApiController.java
├── dto
│   ├── admin
│   │   ├── AdminBoardDto.java
│   │   └── AdminUserDto.java
│   ├── common
│   │   ├── BoardMainDto.java
│   │   └── ReplyDto.java
│   ├── free
│   │   ├── FreeBoardDetailsDto.java
│   │   ├── FreeBoardListDto.java
│   │   ├── FreeBoardMainDto.java
│   │   ├── FreeBoardRequestDto.java
│   │   └── FreeBoardUpdateDto.java
│   ├── media
│   │   ├── FileDto.java
│   │   ├── FileRequestDto.java
│   │   └── ImageDto.java
│   ├── notice
│   │   ├── NoticeBoardDetailsDto.java
│   │   ├── NoticeBoardDto.java
│   │   ├── NoticeBoardListDto.java
│   │   └── NoticeBoardMainDto.java
│   ├── qna
│   │   ├── QnaBoardDetailsDto.java
│   │   ├── QnaBoardListDto.java
│   │   ├── QnaBoardMainDto.java
│   │   ├── QnaBoardRequestDto.java
│   │   ├── QnaBoardUpdateDto.java
│   │   ├── QnaReplyAdoptCheckDto.java
│   │   └── QnaReplyDto.java
│   ├── recruit
│   │   ├── ApplicantDto.java
│   │   ├── ApplicationStatus.java
│   │   ├── RecruitBoardApplyRequestDto.java
│   │   ├── RecruitBoardDetailDto.java
│   │   ├── RecruitBoardListDto.java
│   │   ├── RecruitBoardMainDto.java
│   │   ├── RecruitBoardRequestDto.java
│   │   └── RecruitBoardUpdateDto.java
│   └── user
│       ├── SimpleUserInfoDto.java
│       ├── UserActivityDto.java
│       ├── UserCheckNicknameDto.java
│       ├── UserInfoDto.java
│       ├── UserRankDto.java
│       ├── UserReplyDto.java
│       ├── UserRequestDto.java
│       ├── UserSummaryDto.java
│       └── UserUpdateDto.java
├── entity
│   ├── Adopt.java
│   ├── AuditingFields.java
│   ├── Board.java
│   ├── Bookmark.java
│   ├── FileEntity.java
│   ├── FreeBoard.java
│   ├── ModifiedEntity.java
│   ├── NoticeBoard.java
│   ├── Party.java
│   ├── QnaBoard.java
│   ├── QnaReply.java
│   ├── RecruitBoard.java
│   ├── Reply.java
│   ├── Skill.java
│   ├── Summary.java
│   ├── User.java
│   └── UserSkill.java
├── exception
│   ├── AdoptNotFoundException.java
│   ├── AttachedFileNotFoundException.java
│   ├── BoardNotFoundException.java
│   ├── BookmarkNotFoundException.java
│   ├── DuplicateNicknameException.java
│   ├── DuplicateStudentException.java
│   ├── InvalidAccessException.java
│   ├── ParentReplyMismatchException.java
│   ├── PartyNotFoundException.java
│   ├── RecruitmentCompletedException.java
│   ├── ReplyNotFoundException.java
│   ├── SkillNotFoundException.java
│   ├── SummaryNotFoundException.java
│   └── UserNotFoundException.java
├── repository
│   ├── student
│   │   └── UserQueryRepository.java
│   ├── AdoptRepository.java
│   ├── BoardRepository.java
│   ├── BookmarkRepository.java
│   ├── FileRepository.java
│   ├── FreeBoardRepository.java
│   ├── NoticeRepository.java
│   ├── PartyRepository.java
│   ├── QnaBoardRepository.java
│   ├── QnaReplyRepository.java
│   ├── RecruitBoardRepository.java
│   ├── ReplyRepository.java
│   ├── SkillRepository.java
│   ├── SummaryRepository.java
│   ├── UserRepository.java
│   └── UserSkillRepository.java
├── service
│   ├── AdminService.java
│   ├── BoardService.java
│   ├── BookmarkService.java
│   ├── FileService.java
│   ├── FireBaseService.java
│   ├── FreeBoardService.java
│   ├── MyPageService.java
│   ├── NoticeService.java
│   ├── QnaBoardService.java
│   ├── QnaReplyService.java
│   ├── RecruitBoardService.java
│   ├── ReplyService.java
│   ├── SummaryService.java
│   └── UserService.java
├── util
│   └── ImageUtils.java
└── HansungCommunityApplication.java
```

## 느낀점 및 배운점

이 프로젝트를 통해 다음과 같은 점을 배우고 성장할 수 있었습니다:

- **OIDC와 OAuth2 프로토콜**을 실제 서비스에 적용해보며, 보안과 인증의 중요성을 깊이 이해하게 되었습니다.
- **Spring Resource Server**를 활용하여 실제 운영 환경에서 사용할 수 있는 백엔드 시스템을 구축하는 경험을 쌓았습니다.
- **JPA**를 활용한 데이터베이스 설계와 구현을 통해 객체 지향적인 데이터 모델링 능력을 향상시켰습니다.
- **RESTful API** 설계 원칙을 적용하며 효율적이고 확장 가능한 API 구조를 만드는 방법을 학습했습니다.
- 팀 프로젝트를 통해 협업 능력과 의사소통 능력을 키울 수 있었습니다.

이 프로젝트를 통해 단순히 기능 구현을 넘어서 보안, 성능, 확장성 등을 고려한 실제 서비스 개발의 복잡성과 중요성을 체감할 수 있었습니다.
## 원본 프로젝트 링크

[Organization 리포지토리 링크](https://github.com/HansungCapstoneDesign/Capstone-Design)