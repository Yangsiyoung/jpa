package jpabook.start;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Table(name = "MEMBER", uniqueConstraints = {@UniqueConstraint( name = "NAME_UNIQ", columnNames = {"NAME"})})
@Entity
public class Member {

    @Id
    @Column(name = "ID")
    private String id;

    // Runtime 시 nullable, length Check 가 되는 것이 아니라 DDL 에 관련된 조건
    // null check, length check 를 하기 위해서는 Validator 를 사용해야 할까
    @Column(name = "NAME", nullable = false, length = 10)
    private String userName;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;


}
