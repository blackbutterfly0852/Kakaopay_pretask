package kakaopay.moneyDistribute.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
/**
 * Users : 사용자 테이블
 * PK : x_user_id
 **/
public class User {
    @Id
    @GeneratedValue
    @Column(name = "x_user_id")
    private Long id; // 사용자 ID

    @Column(name = "x_user_name")
    private String userName; // 사용자 이름

    @Column(name = "x_created_time", nullable = false)
    private LocalDateTime createdTime; // 사용자 생성 시각

    @Column(name = "x_ended_time")
    private LocalDateTime endedTime; // 사용자 삭제 시각

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRoom> userRoomList = new ArrayList<>();

    // 생성자 접근 제한 -> 생성메소드로 객체 생성
    protected User() {
    }

    // 1. 생성 메소드
    public static User createUser(String userName) {
        User user = new User();
        user.setUserName(userName);
        user.setCreatedTime(LocalDateTime.now());
        return user;
    }

    // 2. 연관관계 메소드(User <-> UserRoom)
    public void addUserRoom(UserRoom userRoom) {
        userRoom.setUser(this);
        userRoom.getRoom().setCurrInt(userRoom.getRoom().getCurrInt() + 1);
        this.userRoomList.add(userRoom);
    }



}
