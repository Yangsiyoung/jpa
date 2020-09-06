package jpabook.start.relational_mapping.one_way.many_to_one;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "many_to_one_team")
@Entity(name = "ManyToOneTeam")
public class Team {

    @Column(name = "team_id")
    // IDENTITY 는 DB의 AUTO INCREMENT 를 사용하므로, persist 시 INSERT 쿼리가 바로 나간다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
