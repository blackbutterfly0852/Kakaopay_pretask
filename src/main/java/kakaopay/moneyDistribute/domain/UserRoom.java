package kakaopay.moneyDistribute.domain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
/**
 * UsersRoom : 다 : 다  위한 테이블
 * PK : x_user_room_id
 * FK : x_user_id, x_room_id
 **/
@Table(name = "USERROOM")
public class UserRoom {
    @Id
    @GeneratedValue
    @Column(name = "x_user_room_id")
    private Long id; // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "x_user_id")
    private User user; // 사용자 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "x_room_id")
    private Room room; // 방 ID

    @Column(name ="x_join_time", nullable = false)
    private LocalDateTime joinTime; // 방 입장 시각

    @Column(name = "x_out_time")
    private LocalDateTime outTime; // 방 나간 시각

    // 생성자 접근 제한 -> 생성메소드로 객체 생성
    protected UserRoom(){

    }

    // 1. 생성 메소드
    public static UserRoom createUserRoom(User user, Room room){
        UserRoom userRoom = new UserRoom();
        userRoom.setUser(user);
        userRoom.setRoom(room);
        userRoom.setJoinTime(LocalDateTime.now());
        return userRoom;
    }
}
