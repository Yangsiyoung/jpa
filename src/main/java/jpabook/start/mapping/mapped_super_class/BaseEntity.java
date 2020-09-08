package jpabook.start.mapping.mapped_super_class;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
/**
 * 상속 관계 매핑은 아니며, 등록일자, 수정일자, 등록자 같이 여러 Entity 에서 공통으로 사용되는 속성을
 * 정의해두고 상속만 받아 사용하는 방식이다.
 *
 * 즉, 매핑 정보를 상속할 목적으로 사용하지 상속 관계 매핑처럼 부모 Entity 를 만들어 DB 의 Table 화를 하는 등
 * Entity 로서 사용하지 않는다.
 * (@Entity 어노테이션이 없는 것을 확인할 수 있다.)
 *
 */
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "base_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    protected Integer id;

    protected String name;
}
