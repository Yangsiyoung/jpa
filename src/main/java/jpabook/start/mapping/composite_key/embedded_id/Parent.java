package jpabook.start.mapping.composite_key.embedded_id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "embedded_id_parent")
@Entity(name = "EmbeddedIdParent")
public class Parent {

    // @Embeddable 로 설정된 식별자 클래스임을 나타냄
    @EmbeddedId
    private ParentId id;

    private String name;

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
