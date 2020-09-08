package jpabook.start.relational_mapping.one_way.one_to_one;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "one_way_one_to_one_member")
@Entity(name = "OneWayOneToOneMember")
public class Member {

    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @JoinColumn(name = "locker_id")
    @OneToOne
    private Locker locker;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", locker=" + locker +
                '}';
    }
}
