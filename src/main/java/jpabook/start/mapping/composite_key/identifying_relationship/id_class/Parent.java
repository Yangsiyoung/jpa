package jpabook.start.mapping.composite_key.identifying_relationship.id_class;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "identifying_relationship_id_class_parent")
@Entity(name = "IdentifyingRelationshipIdClassParent")
public class Parent {

    @Column(name = "parent_id")
    @Id
    private String parentId;

    private String name;

    @Override
    public String toString() {
        return "Parent{" +
                "parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
