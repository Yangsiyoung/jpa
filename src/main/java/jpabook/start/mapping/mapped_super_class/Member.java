package jpabook.start.mapping.mapped_super_class;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
/**
 * 혹시나 @MappedSuperclass 클래스에 선언된 속성 중
 * 특정 Entity 와 연관된 테이블에서의 명이 다른 경우
 * @AttributeOverrides - @AttributeOverride 를 사용해서 재정의 할 수 있다.
 *
 * 내 생각엔 등록일자, 수정일자, 등록자 같이 여러 Entity 에서 공통으로 사용되는 속성을 @MappedSuperclass 클래스에 정의해두고
 * 사용하려하는데 실제 몇몇 Table 에 등록일자, 수정일자, 등록자 관련 컬럼명이 Table 마다 다를 경우에 사용하면 좋을 것 같다.
 * (ex. 다른 Table 에서는 수정일자를 UPDATE_DATE 로 사용하고 있어서 @MappedSuperclass 의 속성에도
 * 수정일자 관련 컬럼을 UPDATE_DATE 로 설정해두었으나, 어떤 TABLE 에서 수정일자를 MODIFY_DATE 로 사용해서
 * MODIFY_DATE 로 사용하고 있는 테이블과 연관된 Entity 에 아래와 같이 설정하는 것처럼...
 *
 *      @AttributeOverrides({
 *              // name : @MappedSuperclass 에 정의된 프로퍼티 명, column : 현재 Entity 와 관련된 Table 의 어떤 컬럼에 매칭할 지
 *              @AttributeOverride(name = "updateDate", column = @Column(name = "MODIFY_DATE"))
 *      })
 *
 * )
 *
 *
 *
 */
@AttributeOverrides({
        // name : @MappedSuperclass 에 정의된 프로퍼티 명, column : 현재 Entity 와 관련된 Table 의 어떤 컬럼에 매칭할 지
        @AttributeOverride(name = "id", column = @Column(name = "member_id"))
})
@Table(name = "mapped_super_class_member")
@Entity(name = "MappedSuperClassMember")
public class Member extends BaseEntity {

    private String email;

    @Override
    public String toString() {
        return "Member{" +
                "email='" + email + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
