package jpabook.start.mapping.join_strategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
// 어떤 컬럼명으로 자식 상품들을 구별할지, name = "item_type" 생략 시 기본 값 "DTYPE"
@DiscriminatorColumn(name = "item_type")
// 부모 클래스에 이 어노테이션을 사용해줘야 함, 매핑 전략을 JOINED 로 설정해 전략을 조인 전략으로 설정
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "join_strategy_item")
@Entity(name = "JoinStrategyItem")
/**
 *  abstract 를 빼고 일반 class 로 해도 작동하지만, Item 그 자체로 상품이 아닌, 상세 정보를 나타낼 자식 상품 Entity 가 있어야 하기에
 *  abstract 를 명시적으로 선언해 Item Entity 단일로는 상품의 가치가 없음을 알려주고있다.
 */
public abstract class Item {

    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;

    private int price;

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
