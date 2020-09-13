package jpabook.start.value_type.embedded_type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "value_type_embedded_type_member")
@Entity(name = "ValueTypeEmbeddedTypeMember")
public class Member {

    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;

    // 값 타입을 사용
    @Embedded
    private Period period;

    // 값 타입을 사용
    @Embedded
    private Address address;

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", period=" + period +
                ", address=" + address +
                '}';
    }
}
