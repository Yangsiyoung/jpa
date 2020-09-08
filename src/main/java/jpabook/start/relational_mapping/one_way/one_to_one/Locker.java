package jpabook.start.relational_mapping.one_way.one_to_one;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "one_way_one_to_one_locker")
@Entity(name = "OneWayOneToOneLocker")
public class Locker {

    @Column(name = "locker_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;

    @Override
    public String toString() {
        return "Locker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
