package jpabook.start.mapping.composite_key.identifying_relationship.embedded_id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "identifying_relationship_embedded_id_child")
@Entity(name = "IdentifyingRelationshipEmbeddedIdChild")
public class Child {

    @EmbeddedId
    private ChildId childId;

    // 외래 키와 매핑한 연관관계를 기본 키에도 매핑하겠다는 뜻
    @MapsId(value = "parentId")
    @JoinColumn(name = "child_parent_id", referencedColumnName = "parent_id")
    @ManyToOne
    private Parent parent;

    private String name;

    @Override
    public String toString() {
        return "Child{" +
                "childId=" + childId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                '}';
    }
}
