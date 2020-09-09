package jpabook.start.mapping.composite_key.embedded_id;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @IdClass 로 설정해 사용하던 식별자 클래스와는 다르게
 * @Embeddable 어노테이션이 붙어있으며
 *
 * 이 식별자 클래스에 있는 컬럼을 바로 사용한다.
 * (@IdClass 에서는 부모 Entity 에 있는 변수 명과 @IdClass 로 사용 할 식별자 클래스의 변수 명이
 * 일치해야하던 것돠 대비된다.)
 */
@Setter
@Getter
@Embeddable
public class ParentId implements Serializable {

    @Column(name = "parent_id_1")
    private String parentId1;

    @Column(name = "parent_id_2")
    private String parentId2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParentId parentId = (ParentId) o;
        return Objects.equals(parentId1, parentId.parentId1) &&
                Objects.equals(parentId2, parentId.parentId2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId1, parentId2);
    }

    /**
     * @IdClass 를 사용할 때엔 부모 Entity 에 복합 키 관련 변수가 있어서 부모 Entity 에 toString 을 구현하면 되었지만
     * @EmbeddedId 를 사용하면 부모 Entity 에서 복합 키로 설정한 부모 Entity 변수에 @EmbeddedId 로 설정한
     * @Embeddable 로 설정하고 객체를 가져다 복합 키로 사용하기 때문에
     *
     * 복합 키에 해당하는  @Embeddable 클래스에 이와 같이 toString() 을 정의해줬다.
     */
    @Override
    public String toString() {
        return "ParentId{" +
                "parentId1='" + parentId1 + '\'' +
                ", parentId2='" + parentId2 + '\'' +
                '}';
    }
}
