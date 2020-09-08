package jpabook.start.mapping.table_per_concrete_class_strategy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
// 부모 클래스에 이 어노테이션을 사용해줘야 함, 매핑 전략을 TABLE_PER_CLASS 로 설정해 전략을 구현 클래스마다 테이블 전략으로 설정
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "table_per_concrete_class_strategy_item")
@Entity(name = "TablePerConcreteClassStrategyItem")
/**
 *  abstract 를 빼고 일반 class 로 해도 작동하지만, Item 그 자체로 상품이 아닌, 상세 정보를 나타낼 자식 상품 Entity 가 있어야 하기에
 *  abstract 를 명시적으로 선언해 Item Entity 단일로는 상품의 가치가 없음을 알려주고있다.
 */
public abstract class Item {

    @Column(name = "item_id")
    /**
     * Table Per Concrete Class Strategy 의 경우 각각의 테이블이 생기며, Item table 이 생성되지 않는다.
     * 이 뜻은 @GeneratedValue(strategy = GenerationType.IDENTITY) 을 사용하여 Item table 의 기본 키(식별자)의
     * 자동으로 증가하는 유일한 값을 사용할 수 없음을 의미한다.
     *
     * Album, Movie, Book 3개의 테이블이 생성되고
     * 각각의 테이블의 식별자 값은 테이블 간 동일한 기본 키(식별자)를 생성하지 않기 위해서
     * @GeneratedValue(strategy = GenerationType.TABLE) 혹은  @GeneratedValue(strategy = GenerationType.SEQUENCE)을
     * 사용해야한다는 것을 의미하고,
     *
     * @GeneratedValue 에 strategy 를 설정하지 않으면 기본으로 @GeneratedValue(strategy = GenerationType.AUTO) 가 할당되는데
     * 이렇게 되면 자동적으로 @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) 를 사용하기 위해
     * 설정된 방언(현재 : H2)에 알맞은 @GeneratedValue(strategy = GenerationType.SEQUENCE) 을 설정한다.
     *
     * 기본적으로 Hibernate 가 제공하는 TABLE 을 통한 기본 키(식별자) 획득 관련 TABLE 혹은 SEQUENCE 이외
     * DB 에 설정된 별도의 기본 키(식별자) 획득 관련 TABLE 혹은 SEQUENCE 를 사용하려면,
     * 기본 키(식별자) 할당 전략을 참고하여 알맞게 설정해야한다.
     */
    @GeneratedValue
    @Id
    protected Integer id;

    protected String name;

    protected int price;
}
