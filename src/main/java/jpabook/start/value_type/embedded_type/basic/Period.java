package jpabook.start.value_type.embedded_type.basic;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;

/**
 * 값 으로 사용할 객체이기 때문에
 * Setter 를 없애고 값 변경이 필요할 경우
 * 새로 만들어서 사용하도록 유도
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 값 타입을 정의
@Embeddable
public class Period {

    private LocalDate startDate;

    private LocalDate endDate;

    @Override
    public String toString() {
        return "Period{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
