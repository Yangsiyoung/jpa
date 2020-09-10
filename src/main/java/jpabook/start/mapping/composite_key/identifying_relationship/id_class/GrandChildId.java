package jpabook.start.mapping.composite_key.identifying_relationship.id_class;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
public class GrandChildId implements Serializable {

    private ChildId child;
    private String grandChildId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrandChildId that = (GrandChildId) o;
        return Objects.equals(child, that.child) &&
                Objects.equals(grandChildId, that.grandChildId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(child, grandChildId);
    }

    @Override
    public String toString() {
        return "GrandChildId{" +
                "child=" + child +
                ", grandChildId='" + grandChildId + '\'' +
                '}';
    }
}
