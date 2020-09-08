package jpabook.start.relational_mapping.bidirectional.one_to_one;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "bidirectional_one_to_one_locker")
@Entity(name = "BidirectionalOneToOneLocker")
public class Locker {

    @Column(name = "locker_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;

    @OneToOne(mappedBy = "locker")
    private Member member;

    @Override
    public String toString() {
        return "Locker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
