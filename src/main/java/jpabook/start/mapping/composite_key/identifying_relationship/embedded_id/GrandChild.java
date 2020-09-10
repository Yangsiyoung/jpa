package jpabook.start.mapping.composite_key.identifying_relationship.embedded_id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "identifying_relationship_embedded_id_grand_child")
@Entity(name = "IdentifyingRelationshipEmbeddedIdGrandChild")
public class GrandChild {

    @EmbeddedId
    private GrandChildId grandChildId;

    @JoinColumns({
            @JoinColumn(name = "grand_child_grand_parent_id", referencedColumnName = "child_parent_id")
            , @JoinColumn(name = "grand_child_parent_id", referencedColumnName = "child_id")
    })
    // 외래 키와 매핑한 연관관계를 기본 키에도 매핑하겠다는 뜻
    @MapsId(value = "grandChildParentId")
    @ManyToOne
    private Child child;

    private String name;

    @Override
    public String toString() {
        return "GrandChild{" +
                "grandChildId=" + grandChildId +
                ", child=" + child +
                ", name='" + name + '\'' +
                '}';
    }
}
