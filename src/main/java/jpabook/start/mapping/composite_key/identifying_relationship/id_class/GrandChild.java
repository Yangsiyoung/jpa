package jpabook.start.mapping.composite_key.identifying_relationship.id_class;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@IdClass(GrandChildId.class)
@Table(name = "identifying_relationship_id_class_grand_child")
@Entity(name = "IdentifyingRelationshipIdClassGrandChild")
public class GrandChild {

    @Column(name = "grand_child_id")
    @Id
    private String grandChildId;

    @JoinColumns(
            {
                    @JoinColumn(name = "grand_child_grand_parent_id", referencedColumnName = "child_parent_id")
                    , @JoinColumn(name = "grand_child_parent_id", referencedColumnName = "child_id")
            }
    )
    @ManyToOne
    @Id
    private Child child;

    private String name;

    @Override
    public String toString() {
        return "GrandChild{" +
                "grandChildId='" + grandChildId + '\'' +
                ", child=" + child +
                ", name='" + name + '\'' +
                '}';
    }
}
