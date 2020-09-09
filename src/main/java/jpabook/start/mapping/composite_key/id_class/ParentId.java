package jpabook.start.mapping.composite_key.id_class;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 식별자 클래스로 사용할 클래스는
 * 1. 식별자 클래스의 변수명과 Entity 에서 사용하는 변수명이 일치해야한다.
 * 2. Serializable 인터페이스를 implements 해야한다.
 * 3. equals(), hashCode() 를 구현해야 한다.
 * 4. 기본 생성자가 있어야 한다.
 * 5. public class 여야 한다.
 */
@NoArgsConstructor
@Setter
@Getter
public class ParentId implements Serializable {

    private String parentId1;
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
}
