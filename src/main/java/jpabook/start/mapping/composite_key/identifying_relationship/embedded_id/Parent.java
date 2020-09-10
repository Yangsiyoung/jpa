package jpabook.start.mapping.composite_key.identifying_relationship.embedded_id;

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
@Table(name = "identifying_relationship_embedded_id_parent")
@Entity(name = "IndentifyingRelationshipEmbeddedIdParent")
public class Parent {

    @Column(name = "parent_id")
    @Id
    private String id;

    private String name;

    @Override
    public String toString() {
        return "Parent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
