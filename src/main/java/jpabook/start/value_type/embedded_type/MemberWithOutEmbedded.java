package jpabook.start.value_type.embedded_type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "member_with_out_embedded")
@Entity(name = "MemberWithOutEmbedded")
public class MemberWithOutEmbedded {

    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;

    @Column(name = "member_city")
    private String city;

    private String street;

    @Column(name = "zip_code")
    private String zipcode;

    private LocalDate startDate;

    private LocalDate endDate;

    @Override
    public String toString() {
        return "MemberWithOutEmbedded{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
