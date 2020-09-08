package jpabook.start.relational_mapping.bidirectional.many_to_one;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "one_to_many_team")
@Entity(name = "OneToManyTeam")
public class Team {

    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;

    // 양방향 연관관계의 주인이 아니기 때문에 mappedBy 로 연관관계 주인 필드 이름을 설정해줘야 함
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

}
