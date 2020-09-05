JPA 정리
==========

# 1. 기본 키(식별자) 할당 전략
## 1. 직접 할당 전략
<pre>
    <code>
    // Member.java
    @Id
    @Column(name = "ID")
    private Long id;
    
    // JpaMain.java
    Member member = new Member();
    member.setId(id); // 기본 키(식별자) 직접 할당
    member.setUserName(name);
    member.setAge(age);
    member.setRoleType(RoleType.USER);
    member.setCreatedDate(new Date());
    member.setLastModifiedDate(new Date());

    entityManager.persist(member);
    </code>
</pre>

## 2. IDENTITY 전략
기본 키(식별자) 생성을 DB에 위임(EX. MySQL 의 AUTO_INCREMENT 값 사용)

Entity 를 EntityManager 를 통해 관리(Persistence Context 내 관리)하기 위해선
Entity 가 기본 키(식별자)를 가지고 있어야 하기 때문에, DB 에 값이 INSERT 될 때 기본 키(식별자)가 생성되는 이 전략을 사용하면,
entityManager.persist() 를 호출하는 순간 쿼리가 DB에 전달된다.
(쓰기 지연이 동작하지 않는다는 뜻)

<pre>
    <code>
    // Member.java
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
        
     // JpaMain.java
     Member member = new Member();
     //member.setId(id); // 기본 키 직접 할당
     member.setUserName(name);
     member.setAge(age);
     member.setRoleType(RoleType.USER);
     member.setCreatedDate(new Date());
     member.setLastModifiedDate(new Date());
     
     entityManager.persist(member);
    </code>
</pre> 

 
## 3. SEQUENCE 전략
기본 키(식별자) 생성을 DB의 시퀀스를 활용해 생성
entityManager.persist() 호출시 시퀀스를 통해 기본 키(식별자)를 조회해
Entity 에 할당하고 Entity 에 기본 키(식별자)가 할당 되었으므로
Persistence Context 에서 관리 됨

<pre>
    <code>
    // Member.java
    @Entity
    @SequenceGenerator(name = "MEMBER_SEQUENCE_GENERATOR"
            , sequenceName = "MEMBER_SEQ_IN_DB"
            , initialValue = 1
            , allocationSize = 1)
    public class Member {
        @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE
                            , generator =  "MEMBER_SEQUENCE_GENERATOR")
            @Column(name = "ID")
            private Long id;
    
    // JpaMain.java
    Member member = new Member();
    //member.setId(id); // 기본 키 직접 할당
    member.setUserName(name);
    member.setAge(age);
    member.setRoleType(RoleType.USER);
    member.setCreatedDate(new Date());
    member.setLastModifiedDate(new Date());
     
    entityManager.persist(member);
    </code>
</pre>
## 4. TABLE 전략
기본 키(식별자) 생성 전용 Table을 사용하여 Sequence 처럼 사용하는 전략
entityManager.persist() 호출시 기본 키(식별자) 생성 전용 Table 을 조회해 기본 키(식별자)를
Entity 에 할당하고 Entity 에 기본 키(식별자)가 할당 되었으므로
Persistence Context 에서 관리 됨
<pre>
    <code>
    // Member.java
    @Entity
    @TableGenerator(name = "MEMBER_SEQUENCE_GENERATOR"
            , table = "MEMBER_SEQUENCES"
            , pkColumnValue = "MEMBER_SEQ"
            , allocationSize = 1)
    public class Member {
        @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE
                            , generator =  "MEMBER_SEQUENCE_GENERATOR")
            @Column(name = "ID")
            private Long id;
    
    // JpaMain.java
    Member member = new Member();
    //member.setId(id); // 기본 키 직접 할당
    member.setUserName(name);
    member.setAge(age);
    member.setRoleType(RoleType.USER);
    member.setCreatedDate(new Date());
    member.setLastModifiedDate(new Date());
     
    entityManager.persist(member);
    </code>
</pre>
## 5. AUTO 전략
설정한 Database 방언(Dialect)에 따라 적합한 전략(IDENTITY, SEQUENCE, TABLE 중 하나)을 자동으로 선택
@GeneratedValue.strategy 기본 값이며, 개발 초기 단계에 적합한 것 같다.
(property name="hibernate.hbm2ddl.auto" value="create" 설정이 되어있으면
어느정도 자동으로 만들어주지만 실제 SEQUENCE 나 TABLE 전략을 사용하려면 DB 에 해당 시퀀스나,
Table을 만들어줘야하기 때문)



