package jpabook.start.value_type.collection;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "value_type_collection_member")
@Entity(name = "ValueTypeCollectionMember")
public class Member {

    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;

    /**
     * @CollectionTable 을 통해 Member 하나에 여러 값을 저장하기 위한(일 대 다) Table 과
     * Table 의 어떤 컬럼과 Member Table 의 식별자와 조인할 지 설정을 한다.
     */
    @CollectionTable(
            name = "value_type_collection_member_favorite_food"
            , joinColumns = @JoinColumn(name = "member_favorite_food_member_id")
    )
    // Entity 연관관계(@OneToMany 처럼)가 아니며 값을 일 대 다로 저장하기 때문에 Value Collection 임을 명시해 줌
    @ElementCollection
    /**
     * Set<String> 타입은 컬럼 하나를 사용하기 때문에 value_type_collection_member_favorite_food Table 에
     * 어떤 컬럼명으로 생성할지 직접 지정했다.
     */
    @Column(name = "favorite_food_name")
    private Set<String> favoriteFoods = new HashSet<>();

    /**
     * @CollectionTable 을 통해 Member 하나에 여러 값을 저장하기 위한(일 대 다) Table 과
     * Table 의 어떤 컬럼과 Member Table 의 식별자와 조인할 지 설정을 한다.
     */
    @CollectionTable(
            name = "value_type_collection_member_address_history"
            , joinColumns = @JoinColumn(name = "member_address_history_member_id")
    )
    // Entity 연관관계(@OneToMany 처럼)가 아니며 값을 일 대 다로 저장하기 때문에 Value Collection 임을 명시해 줌
    @ElementCollection
    // favoriteFoods 와는 달리 Address 라는 Value Class (@Embeddable 로 생성한)를 사용
    private List<Address> addressHistory = new ArrayList<>();

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", favoriteFoods=" + favoriteFoods +
                ", addressHistory=" + addressHistory +
                '}';
    }
}
