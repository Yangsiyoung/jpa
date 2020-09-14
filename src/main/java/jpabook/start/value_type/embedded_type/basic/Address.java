package jpabook.start.value_type.embedded_type.basic;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
public class Address {

    // 이 값 객체를 사용하는 Table 에 어떤 컬럼 명으로 사용할지 설정 가능
    @Column(name = "member_city")
    private String city;

    private String street;

    // 이 값 객체를 사용하는 Table 에 어떤 컬럼 명으로 사용할지 설정 가능
    @Column(name = "zip_code")
    private String zipcode;

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}
