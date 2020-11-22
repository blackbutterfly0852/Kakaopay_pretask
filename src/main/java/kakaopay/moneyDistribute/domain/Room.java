package kakaopay.moneyDistribute.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.*;

@Entity
@Getter
@Setter
/**
 * Room : 채팅방 테이블
 * PK : x_room_id
 **/
public class Room {
    @Id
    @GeneratedValue
    @Column(name = "x_room_id")
    private Long id; // PK

    @Column(name ="x_room_name", unique = true)
    private String name; // 대화방 이름

    @Column(name = "x_created_user_id", nullable = false)
    private Long userId ; // 대화방 개설자

    @Column(name = "x_init_cnt", nullable = false)
    private int initCnt; // 대화방 개설시 최초 인원

    @Column(name = "x_curr_cnt", nullable = false)
    private int currInt; // 대화방 현재 남은 인원(default = x_init_cnt)

    @Column(name ="x_created_time", nullable = false)
    private LocalDateTime createdTime; // 대화방 개설 시각

    @Column(name ="x_endedTime")
    private LocalDateTime endedTime; // 대화방 종료 시각

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<UserRoom> userRoomList = new ArrayList<>();

    // 생성자 접근 제한 -> 생성메소드로 객체 생성
    protected Room(){
    }

    // 1. 생성 메소드
    public static Room createRoom(Long userId, String roomName){
        Room room = new Room();
        room.setName(roomName);
        room.setUserId(userId);
        room.setInitCnt(1);
        room.setCurrInt(1);
        room.setCreatedTime(LocalDateTime.now());
        return room;
    }

    // 2. 연관관계 메소드
    public void addUserRoom(UserRoom userRoom) {
        userRoom.setRoom(this);
        this.userRoomList.add(userRoom);
    }


    // 3. 비즈니스 로직
    // 1) 뿌리기 시 해당 방의 인원 제한 여부  (1 <= 뿌리기 최초 인원 < 방의 내부 인원 - 1)
    public boolean isOverNumber(int initCnt) {
        if (this.currInt <= initCnt || initCnt < 1) {
            return false;
        }
        return true;
    }


}
