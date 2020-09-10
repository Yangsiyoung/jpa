package jpabook.start.mapping.composite_key.identifying_relationship.embedded_id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Columns;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class GrandChildId implements Serializable {

    private ChildId grandChildParentId;

    @Column(name = "grand_child_id")
    private String grandChildId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrandChildId that = (GrandChildId) o;
        return Objects.equals(grandChildParentId, that.grandChildParentId) &&
                Objects.equals(grandChildId, that.grandChildId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(grandChildParentId, grandChildId);
    }

    @Override
    public String toString() {
        return "GrandChildId{" +
                "grandChildParentId=" + grandChildParentId +
                ", grandChildId='" + grandChildId + '\'' +
                '}';
    }
}
