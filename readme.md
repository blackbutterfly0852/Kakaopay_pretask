# 뿌리기 기능 구현하기 by 카카오페이 서버 개발 지원자 김동우

## 1. 요구 사항
* 뿌리기, 받기, 조회 기능을 수행하는 REST API 를 구현합니다.
    * 요청한 사용자의 식별값은 숫자 형태이며 "X-USER-ID" 라는 HTTP Header로 전달됩니다.
    * 요청한 사용자가 속한 대화방의 식별값은 문자 형태이며 "X-ROOM-ID" 라는 HTTP Header로 전달됩니다. 
    * 모든 사용자는 뿌리기에 충분한 잔액을 보유하고 있다고 가정하여 별도로 잔액에 관련된 체크는 하지 않습니다.
    * 작성하신 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계되어야 합니다.
    * 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성합니다.

## 2. 상세 구현 요건 및 제약사항

### 1) 뿌리기 API → `POST` /v1?initAmt=?&initCnt=?

* 다음 조건을 만족하는 뿌리기 API를 만들어 주세요.
    * 뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.
    * 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다. 
    * 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게 구현해 주세요.)
    * token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.

### 2) 받기 API → `PUT` /v2?token=?

* 다음 조건을 만족하는 받기 API를 만들어 주세요.
    * 뿌리기 시 발급된 token을 요청값으로 받습니다.
    * token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를 API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다. → `org.antop.kakao.PickupApiTest#test001`
    * 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.  
    * 자신이 뿌리기한 건은 자신이 받을 수 없습니다. 
    * 뿌린이가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다. 
    * 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기 실패 응답이 내려가야 합니다. 

### 3) 조회 API → `GET` /v3?token=?

* 다음 조건을 만족하는 조회 API를 만들어 주세요.
    * 뿌리기 시 발급된 token을 요청값으로 받습니다.
    * token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재 상태는 다음의 정보를 포함합니다. 
    * 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은 사용자 아이디] 리스트)
    * 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지 않은 token에 대해서는 조회 실패 응답이 내려가야 합니다. 

## 2. Problem Solving    
### 1) Development environment
* Language : Java 11
* FrameWork : Spring Boot 2.3.6.RELEASE, Spring JPA
* Database : H2 1.4.199.RELEASE


### 2) Database Modeling
* 응집도를 높이기 위해 각 도메인 별로 __생성 메소드__ 및 __연관관계 메소드__ 활용
* 핵심 TABLE 및 DDL
   * Share : 뿌리기 테이블
   * SharedAmount : 받기 테이블
``` H2 Database
create table share (
    x_share_id bigint not null, -- 뿌리기 PK 
    x_token varchar(255), -- 고유 토큰, unique 제약 조건
    x_user_id bigint, -- 뿌린 사용자
    x_room_id bigint, -- 뿌려진 방
    x_init_amt bigint not null, -- 최초 뿌린 금액
    x_curr_amt bigint not null, -- 현재 남은 금액
    x_init_cnt integer not null,-- 최초 뿌린 수
    x_curr_cnt integer not null, -- 현재 받은 사람
    x_req_created_time timestamp not null, -- 뿌리기 생성 시간
    x_req_ended_time timestamp, -- 뿌리기 종료 시간
    primary key (x_share_id));

alter table share add constraint UK_lgtx6v5oll9i8uhgno0ady7ud unique (x_token);    

create table sharedamount (
    x_shared_amt_id bigint not null, -- 받기 PK
    x_share_id bigint, -- Share 테이블 FK
    x_seq integer not null, -- 토큰 당 분배 시퀀스
    x_rcv_amt bigint not null, -- 분배 금액
    x_rcv_id bigint, -- 분배 받은 사용자
    x_rcv_time timestamp, -- 분배 받은 시각
    primary key (x_shared_amt_id))
```          
* 관리 TABLE 및 DDL
    * User : 사용자 관리 테이블
    * UserRoom : 대화방에 소속된 사용자 관리 테이블
    * Room : 대화방 관리 테이블
    
* ERD

* Dummy Data
  * User, UserRoom, Room 테이블의 Dummy Data  
  * 실행 시 InitDB.java의 @PostConstruct로 인해 Dummy Data가 Insert
  * Dummy Data의 구조 (UserRoom 테이블 참고)
  
    |   |roomA  |roomB  |roomC  |roomD  |roomE  |
    |---|:-----:|:-----:|:-----:|:-----:|:-----:|
    |U  |1      |4      |1      |4      |7      |
    |S  |4      |10     |7      |7      |13     |
    |R  |7      |       |10     |10     |       |
    |I  |10     |       |       |13     |       |
    |D  |13     |       |       |       |       |
      
 
