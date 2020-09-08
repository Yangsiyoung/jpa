package jpabook.start.relational_mapping.one_way.one_to_many;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "one_way_one_to_many_team")
@Entity(name = "OneWayOneToManyTeam")
public class Team {

    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;

    /**
     * 일대다 단방향 연관관계를 이렇게 가지게 되면,
     * Team Entity 를 수정했는데 Member 관련 Table 이 수정되게 되어
     * 혼란을 겪을 수 있을 것 같다...
     *
     * 의미적으로도 Team 을 수정했는데 Member 가 변경되면 이상한 것 같다...
     */
    @JoinColumn(name = "team_id")
    @OneToMany
    private List<Member> members = new ArrayList<>();

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", members=" + members +
                '}';
    }
}
