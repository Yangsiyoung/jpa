package jpabook.start.relational_mapping.bidirectional.many_to_one;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "one_to_many_member")
@Entity(name = "OneToManyMember")
public class Member {

    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    // 양방향 연관관계에서 외래 키가 있는 쪽이 연관관계의 주인
    @JoinColumn(name = "team_id")
    @ManyToOne
    private Team team;


}
