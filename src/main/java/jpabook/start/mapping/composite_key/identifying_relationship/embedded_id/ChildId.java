package jpabook.start.mapping.composite_key.identifying_relationship.embedded_id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class ChildId implements Serializable {

    // Child Entity @MapsId("parentId") 와 매핑됨
    @Column(name = "child_parent_id")
    private String parentId;

    @Column(name = "child_id")
    private String childId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChildId childId1 = (ChildId) o;
        return Objects.equals(parentId, childId1.parentId) &&
                Objects.equals(childId, childId1.childId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentId, childId);
    }

    @Override
    public String toString() {
        return "ChildId{" +
                "parentId='" + parentId + '\'' +
                ", childId='" + childId + '\'' +
                '}';
    }
}
