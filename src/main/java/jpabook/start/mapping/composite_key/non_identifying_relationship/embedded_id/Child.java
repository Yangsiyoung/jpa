package jpabook.start.mapping.composite_key.non_identifying_relationship.embedded_id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "embedded_id_child")
@Entity(name = "EmbeddedIdChild")
public class Child {

    @Column(name = "child_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @JoinColumns({
            /**
             * name : Child Table 에 있는 컬럼 명, referencedColumnName : Parent Table 에 있는 컬럼 명
             * 부모 테이블에 있는 복합 키로 사용되는 컬럼명들과 자식 테이블에 외래 키로 사용될 키들의 컬럼명이 다를 경우
             * referencedColumnName 로 따로 부모 테이블의 복합 키 관련 컬럼명들을 지정해준다.
             */
            @JoinColumn(name = "CHILD_PARENT_ID_1", referencedColumnName = "PARENT_ID_1")
            , @JoinColumn(name = "CHILD_PARENT_ID_2", referencedColumnName = "PARENT_ID_2")
    })
    @ManyToOne
    private Parent parent;

    private String name;

    @Override
    public String toString() {
        return "Child{" +
                "id=" + id +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                '}';
    }
}
