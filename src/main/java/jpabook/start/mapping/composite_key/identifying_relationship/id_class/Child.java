package jpabook.start.mapping.composite_key.identifying_relationship.id_class;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@IdClass(ChildId.class)
@Table(name = "identifying_relationship_id_class_child")
@Entity(name = "IdentifyingRelationshipIdClassChild")
public class Child {

    @Column(name = "child_id")
    @Id
    private String childId;

    @JoinColumn(name = "child_parent_id")
    @ManyToOne
    @Id
    private Parent parent;

    private String name;

    @Override
    public String toString() {
        return "Child{" +
                "childId='" + childId + '\'' +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                '}';
    }
}
