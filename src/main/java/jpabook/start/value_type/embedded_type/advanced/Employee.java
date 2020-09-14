package jpabook.start.value_type.embedded_type.advanced;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "value_type_embedded_type_advanced_employee")
@Entity(name = "ValueTypeEmbeddedTypeAdvancedEmployee")
public class Employee {

    @Column(name = "employee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    private String name;

    /**
     * @Embedded 로 사용한 @Embeddable 로 선언된 Value Class 의 속성을 재정의 함
     * @AttributeOverrides @Embeddable 로 선언된 Value Class 의 여러 속성을 재정의 할 때 사용
     * @AttributeOverride(name = "재정의 할 Value Class 의 프로퍼티", column = @Column(name = "어떤 컬럼명으로 사용하고싶은지")
     */
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "employment_start_date"))
            , @AttributeOverride(name = "endDate", column = @Column(name = "employment_end_date"))
    })
    @Embedded
    private Period employmentPeriod;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employmentPeriod=" + employmentPeriod +
                '}';
    }
}
