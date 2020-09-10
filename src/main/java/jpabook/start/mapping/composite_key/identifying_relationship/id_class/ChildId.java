package jpabook.start.mapping.composite_key.identifying_relationship.id_class;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
public class ChildId implements Serializable {

    // Parent 의 기본 키(식별자) 타입이 String 이고, ChildId 를 식별자 클래스로 사용할 Child Entity 의 Parent 연관관계 변수명이 parent
    private String parent;
    private String childId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChildId childId1 = (ChildId) o;
        return Objects.equals(parent, childId1.parent) &&
                Objects.equals(childId, childId1.childId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, childId);
    }

    @Override
    public String toString() {
        return "ChildId{" +
                "parent='" + parent + '\'' +
                ", childId='" + childId + '\'' +
                '}';
    }
}
