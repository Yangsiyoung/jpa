package jpabook.start.relational_mapping.one_way.many_to_one;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "many_to_one_member")
@Entity(name = "ManyToOneMember")
public class Member {

    @Column(name = "member_id")
    // IDENTITY 는 DB의 AUTO INCREMENT 를 사용하므로, persist 시 INSERT 쿼리가 바로 나간다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    /**
     * 처음 생각
     * @JoinColumn 을 생략할 수 있다는데, 아마도 변수 타입으로 Team 이라는 Entity 를 들고있고,
     * 해당 Entity 의 @Id 컬럼을 알 수 있기 때문이 아닐까??
     *
     * 자료 내용
     * @JoinColumn 을 생략하면 외래 키를 찾을 때 기본 전략을 사용한다고 한다,
     * 기본 전략은 : 필드명 + _ + 참조하는 테이블의 컬럼명
     * 지금 이 변수에서 기본전략을 적용하면,
     * 변수명은 team 이며, 테이블의 컬럼명은 Team entity 에 @Id 에 해당하는 column 명은, team_id
     * 따라서 team_team_id 외래 키를 참조하게 된다고 한다.
     *
     * 결국 Team 이라는 Entity 를 들고있으니 어떤 Entity 혹은 Table 을 참조해야하는 지 알 수 있기에,
     * 기본 전략으로 team_team_id 라는 외래키를 사용할 수 있는 것 같다.
     */
    @JoinColumn(name = "team_id")
    @ManyToOne
    private Team team;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", team=" + team +
                '}';
    }
}
