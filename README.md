# SSDA팀의 Dassda.
*소중한 일상을 공유해보세요 /released 240225*

![dassda-og-image](https://github.com/SSDA-Side/SSDA-Front/assets/48979587/ab6673f3-8f7c-4f9a-8d2d-a7aaceb03ad1)

# 서비스 소개

> #### 당신의 일상을 공유하고, 소중한 순간을 함께 나누는 '공유 일기장' 어플!

- **다양한 일기 작성 탬플릿**과 특별한 기능으로, 더 심플하게 일기를 쓸 수 있어요.
- 회원 등급에 따라 차별된 서비스를 제공하며, **공유 일기장을 통해 친구들과 소통**하세요.
- **기분 스티커, 댓글, 좋아요 기능**을 통해 더 풍성한 일기 작성 경험을 제공해요.
- **모바일웹**을 통해 언제 어디서든 편리하게 이용 가능합니다.

***함께 쓰는 일기장으로 더 특별한 순간을 기록해보세요🙌***

# 서비스 정보 개요

#### 개발 인원
- 총 6명.

| 역할 | 이름 | 연락처 | 비고 |
| ------------- | ------------- | ------------- | ------------- |
| 📝 PM | 이세은 | wntkfkd95@naver.com | - |
| 🎨 DE | 유수 | - | - |
| ⚙ BE | 권동휘 | hocci0222@kakao.com | 팀 리드 |
| ⚙ BE | 김범준 | gkgk9753@gmail.com | - |
| 📺 FE | 김주현 | sang.pok.e@gmail.com | FE 리드 |
| 📺 FE | 이어진 | - | - |

#### 개발 기간
- 총 4달, 2023년 11월 02일 ~ 2023년 02월 25일
- 기존 팀원: 이세은, 유수, 권동휘, 김범준
- 24년 01월 01일 이후 합류 팀원: 김주현, 이어진

#### 리팩토링
- 총 3일,  2024년 03월 01일 ~ 3일
- 총 2명

| 역할 | 이름 | 연락처 | 비고 |
| --- | --- | --- | --- |
| ⚙ BE | 권동휘 | hocci0222@kakao.com | - |
| 📺 FE | 김주현 | sang.pok.e@gmail.com | - |

#### 배포 주소
- https://dassda.today/

#### 화면설계 URL
- [쓰다 Prototyping UI / Figma URL](https://www.figma.com/file/nx8EkCrbjOGxo22KYulTcP/%EA%B3%B5%EC%9C%A0-%EC%9D%BC%EA%B8%B0%EC%9E%A5?type=design&node-id=0%3A1&mode=design&t=dibWmhUdCesJXOy9-1)

## 기술 스택 /Back-End
`Spring Boot`, `Spring Security`
- 쉬운 설정과 배포를 위한 Spring boot를 사용하고 인증과 권한 부여를 통해 보안을 강화하기 위해 시큐리티를 선택하였습니다.

`Jenkins`, `Docker`, `Ubuntu(Compact, Micro)`, `DNS`, `SSL 인증서`
- 수동 배포의 불편함 해소와 우분투 서버의 경량화를 위해 젠킨스와 도커를 사용하였습니다.
- 사용자 친화적인 도메인 이름을 위해 DNS를 웹 사이트 간 정보가 암호화하고 보안을 강화하기 위해 SSL인증서를 사용하여 HTTPS 구축하였습니다.

`JPA`, `MySQL`, `Redis`
- 데이터 중심의 로직보다는 비즈니스 로직에 더 집중하기 위해 JPA를 사용하였습니다.
- 안정적이고 확장가능한 관계형 데이터베이스 MySQL을 사용하였습니다.
- 빠른 읽기 및 쓰기 속도를 이용하여 자동 로그인(RefreshToken)과 알림 기능에서 Redis를 사용하였습니다.

`oauth(REST API)`, `JWT`
- 편리한 로그인을 위해 카카오 로그인을 사용하였고 스케일러블하고 유연한 인증 및 정보 교환을 위해 JWT를 선택하였습니다.

## 협업 /팀 전체
`Jira`: 팀 전체 진도 현황을 공유하였습니다.

`Figma`: 서비스 화면 및 기능에 대한 의견을 주고 받았습니다.

`Slack`: 주 소통 채널로서 팀원 또는 팀끼리의 협업에 대한 이야기를 나누거나 자료를 공유하였습니다.

## 협업 /BE팀

**`Github Webhook`을 통한 CI/CD, 티켓을 통한 Jira 이슈 관리**
- Github Main 브렌치에 대한 PR, Merge 발생 시 Docker, Jenkins 빌드 후 Ubuntu에 자동 배포
- 생성한 Jira 이슈의 티켓 넘버를 받아와 Push 네임에 티켓으로 발행
- 모든 API를 동시 구현 후 코드 리뷰하는 방식으로 진행

# 서비스 특징

## Oauth(Kakao) 로그인
- Oauth를 통한 간편한 로그인을 제공하였습니다.
- 서비스 내에 로그인을 필요로 하는 Entry Point가 두 군데 존재하였습니다.
- Access Token을 받아오는 콜백 주소는 고정하되, localStorage를 통해 다음 서비스 경로를 지정해주는 흐름으로 구현하였습니다.
- 사용자 인증 방식은 JWT 방식을 사용하였습니다.
- RestTemplate을 이용하여 카카오 서버에서 사용자 정보를 가져와 DB에 저장하였습니다.
- DB를 조회하여 사용자가 없으면 AccessToken과 RefreshToken을 생성하며 저장하였습니다.

| 쓰다 로그인 흐름 순서도 |
| -------- |
| ![쓰다 로그인 흐름](https://github.com/SSDA-Side/SSDA-Front/assets/48979587/7f7c6086-fb49-43a5-8c8c-2711c6efcfa5) |


## 알림
- 서비스를 이용하면서 놓칠 수 있는 부분을 알림으로 알려주고 있습니다.
- 총 5종류로 알림을 구분하였고, 각 종류별 마다 적절한 경로로 따라가게 했습니다.
- Pageable을 이용하여 무한 스크롤을 구현하였습니다.(마지막 id값으로 다음 페이지 응답)
- 기획의도에 따라 알림의 만료 기간을 30일로 지정하고 빠른 읽기와 쓰기 속도를 위해 Redis를 사용하였습니다.

| 쓰다 알림 화면 | 알림 종류 명세 |
| -------- | ------- |
| ![image](https://github.com/SSDA-Side/SSDA-Front/assets/48979587/54324464-f0f9-4a32-8d85-052d07760d80) | ![image 57](https://github.com/SSDA-Side/SSDA-Front/assets/48979587/83578685-f47a-485c-afc5-34d58f7e5542) |


## 공유 일기장 초대
- 상대방을 초대하기 위해선 공유 링크를 생성해야 합니다.
- 서버와 API 통신을 통해 해당 일기장 초대에 대한 해시값을 받아 링크를 생성합니다.
- `Clipboard` API를 통해 복사하거나, 카카오톡 SDK에서 제공하는 공유하기 기능을 통해 초대를 보냅니다.
- 초대 링크 화면에서는 해시값을 조회하고 유효한 해시인지 판별합니다.
- 사용자가 참여하기 액션을 하면 일기장 참여 핸들러 경로로 이동하여 참여를 시도합니다.
  - location에 state를 담아 핸들러로 보냅니다. 직접 경로로 들어오는 경우 state가 없으므로 유효하지 않은 접근입니다.

| 쓰다 초대하기 모달 | 카카오톡 공유하기 | 공유 링크 접속 화면 |
| -------- | ------- | --------- |
| ![image](https://github.com/SSDA-Side/SSDA-Front/assets/48979587/fcb9af26-c0f7-476a-a580-fc1eada330ba) | ![image](https://github.com/SSDA-Side/SSDA-Front/assets/48979587/a5d4bdea-447b-46d9-aa8a-1840f272bcaf) | ![GIF 2024-03-02 오후 11-07-55](https://github.com/SSDA-Side/SSDA-Front/assets/48979587/7b8bbcf5-9fdb-4c67-87c9-9ecbbcb16440) |
|  |  | * Network Throttle이 적용된 화면입니다 |

| 쓰다 초대하기 흐름 순서도 |
| -------- |
| ![쓰다 초대 흐름](https://github.com/SSDA-Side/SSDA-Front/assets/48979587/8e5d0fe8-8ce3-4485-ae0c-4011c21dae54) |


## 클린 코드
- 중복 코드 제거와 가독성을 위해
-  Double Brace Initialization를 사용하였습니다.(알림 리스너에서)
-  모던 자바를 사용하였습니다.(서비스 로직, Stream api)
-  @Component를 사용하여 자주 접근하는 객체를 클래스화 시켰습니다.(SecurityContextHolder)

**| NotificationEventListener**
```java
@EventListener
    public void onCommentCreated(CommentCreatedEvent event) {
        Comment comment = event.getComment();
        Diary diary = diary(comment.getDiary().getId());

        if(!diary.getMember().getId().equals(member().getId())) {
            Map<String, Object> notificationData = new HashMap<>() {
                {
                    put("notificationTypeId", 1);
                    put("isRead", false);
                    put("regDate", LocalDateTime.now());
                    put("writerId", comment.getMember().getId());
                    put("boardId", diary.getBoard().getId());
                    put("boardTitle", diary.getBoard().getTitle());
                    put("diaryId", diary.getId());
                    put("commentId", comment.getId());
                    put("commentContent", comment.getComment());
                    put("commentWriterNickname", comment.getMember().getNickname());
                }
            };
            parseJson(notificationData, diary.getMember().getId());
        }
    }
```

**| Service 코드**
```java
@Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException("삭제된 일기장입니다."));
        if(!board.getMember().getId().equals(currentMember().getId())) {
            throw new IllegalStateException("삭제 권한이 없다.");
        }
        board.setBackUp(true);
        List<Diary> diaryList = diaryRepository.findByBoardId(board.getId());
        if(!(shareRepository.existsByBoardId(board.getId()) && shareRepository.existsByMemberId(currentMember().getId()))) {
            diaryList.stream()
                    .filter(diary -> !diary.isBackUp())
                    .forEach(diary -> diary.setBackUp(true));
            diaryRepository.saveAll(diaryList);
        }
        boardRepository.save(board);
    }
```

**| GetMember**
```java
@Component
public class GetMember {

    private static MemberRepository memberRepository;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        GetMember.memberRepository = memberRepository;
    }
    public static Member getCurrentMember() {
        return memberRepository
                .findByEmail(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()
                )
                .orElseThrow(() -> new IllegalStateException("다시 로그인 해주세요"));
    }
}
```
