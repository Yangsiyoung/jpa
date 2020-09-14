package jpabook.start.value_type.embedded_type.advanced;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Period {

    // 해당 프로퍼티에 대한 컬럼 명 지정(사용하는 곳에서 @AttributeOverride 속성을 사용해 재정의 가능)
    @Column(name = "start_date")
    private LocalDate startDate;

    // 해당 프로퍼티에 대한 컬럼 명 지정(사용하는 곳에서 @AttributeOverride 속성을 사용해 재정의 가능)
    @Column(name = "end_date")
    private LocalDate endDate;

    @Override
    public String toString() {
        return "Period{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
