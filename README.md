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

# 2. 연관관계
## 1. 방향
  회원과 팀이라는 개념이 있고, 회원과 팀이 관계가 있을 때,
  회원 -> 팀, 팀 -> 회원으로 참조하는 방향이 있다.
  
  회원 -> 팀으로만 참조할 수 있거나, 팀 -> 회원으로만 참조하는 것을 단방향 관계라고 한다.
  (양쪽으로 참조 가능한 것을 양방향 관계라고 함.)
  
## 2. 다중성
  '다대일', '일대다', '일대일', '다대다' 라는 다중성이 있다.

  회원과 팀의 경우 팀 하나에 여러명의 회원이 소속이 되고,
  관계는 '회원 여러명 - 팀' 으로 정리가 된다.
  
  이를 회원을 기준으로 생각한다면 다대일(회원 여러명 - 팀 1개) 관계가 되고,
  반대로 팀을 기준으로 생각한다면 일대다(팀 1개 - 회원 여러명) 관계가 된다.
  
## 3. 객체 - 테이블
  회원이라는 객체(Member.java)는 MEMBER 라는 이름의 table 로 관리하고 있고,
  팀이라는 객체(Team.java)는 Team 이라는 이름의 Table 로 관리하고 있다.
  
  회원과 팀의 객체끼리는 변수를 통한 참조로 연관관계를 맺고,
  회원과 팀의 Table 끼리는 외래 키를 통해 연관관계를 맺는다.
  
  객체끼리의 참조는 회원 -> 팀, 팀 -> 회원 등 참조하는 방향을 가지며,
  이는 단방향 관계이다.(단방향 관계를 회원 -> 팀, 팀 -> 회원 2개를 만든게
  양방향 관계, 즉, 단방향 관계를 양쪽에서 만든 것)
  
  Table 끼리의 Join 은 방향이 없는, 굳이 방향을 따지자면 양방향으로
  외래 키만 있다면 해당 Table 들의 정보를 가져올 수 있다.
  
   
## 4. 단방향 연관관계
### 1.  다대일(ManyToOne) 단방향 연관관계
  * 회원과 팀의 경우, 회원의 입장에서 보면 '회원 여러명 - 팀 1개' 로 관계가 생성되므로,
  회원의 입장에서 다대일(ManyToOne)관계이다.
  
  * @ManyToOne  
  다대일임을 나타내는 어노테이션  
  회원(다) 객체에서 Team team 이라는 필드가 있고, 이 필드가 회원(Member) 과 팀(Team)의
  다대일 연관관계를 나타내는 것 임을 나타내는 역할을 함.
  
  * @JoinColumn(name="Foreign key")
  @ManyToOne 어노테이션을 통해 어떤 엔티티와 다대일 관계인지 나타냈다면,
  해당 엔티티와 매핑해 줄  외래 키 이름을 지정
  
  Member.java
  <pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Table(name = "many_to_one_member")
    @Entity(name = "ManyToOneMember")
    public class Member {
    
        @Column(name = "member_id")
        // IDENTITY 는 DB의 AUTO INCREMENT 를 사용하므로, persist 시 INSERT 쿼리가 바로 나간다.
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Integer id;
    
        @Column(name = "user_name")
        private String userName;
    
        /**
         * 처음 생각
         * @JoinColumn 을 생략할 수 있다는데, 아마도 변수 타입으로 Team 이라는 Entity 를 들고있고,
         * 해당 Entity 의 @Id 컬럼을 알 수 있기 때문이 아닐까??
         *
         * 자료 내용
         * @JoinColumn 을 생략하면 외래 키를 찾을 때 기본 전략을 사용한다고 한다,
         * 기본 전략은 : 필드명 + _ + 참조하는 테이블의 컬럼명
         * 지금 이 변수에서 기본전략을 적용하면,
         * 변수명은 team 이며, 테이블의 컬럼명은 Team entity 에 @Id 에 해당하는 column 명은, team_id
         * 따라서 team_team_id 외래 키를 참조하게 된다고 한다.
         *
         * 결국 Team 이라는 Entity 를 들고있으니 어떤 Entity 혹은 Table 을 참조해야하는 지 알 수 있기에,
         * 기본 전략으로 team_team_id 라는 외래키를 사용할 수 있는 것 같다.
         */
        @JoinColumn(name = "team_id")
        @ManyToOne
        private Team team;
    
        @Override
        public String toString() {
            return "Member{" +
                    "id=" + id +
                    ", userName='" + userName + '\'' +
                    ", team=" + team +
                    '}';
        }
    }
    </code>
  </pre>
  
  Team.java
  <pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Table(name = "many_to_one_team")
    @Entity(name = "ManyToOneTeam")
    public class Team {
    
        @Column(name = "team_id")
        // IDENTITY 는 DB의 AUTO INCREMENT 를 사용하므로, persist 시 INSERT 쿼리가 바로 나간다.
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Integer id;
    
        private String name;
    
        @Override
        public String toString() {
            return "Team{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
  </pre>
  
  


