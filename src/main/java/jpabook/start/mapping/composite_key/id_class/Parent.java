package jpabook.start.mapping.composite_key.id_class;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
// 복합 키를 지정한 식별자 클래스를 알려 줌
@IdClass(ParentId.class)
@Table(name = "id_class_parent")
@Entity(name = "IdClassParent")
public class Parent {

    @Column(name = "parent_id_1")
    @Id
    private String parentId1;

    @Column(name = "parent_id_2")
    @Id
    private String parentId2;

    private String name;

    @Override
    public String toString() {
        return "Parent{" +
                "parentId1='" + parentId1 + '\'' +
                ", parentId2='" + parentId2 + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
