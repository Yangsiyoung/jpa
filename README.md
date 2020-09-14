JPA 정리
==========

참고 자료  
강의 : https://www.inflearn.com/course/ORM-JPA-Basic  
서적 : 자바 ORM 표준 JPA 프로그래밍 - 지은이 : 김영한
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
  팀이라는 객체(Team.java)는 TEAM 이라는 이름의 Table 로 관리하고 있다.
  
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
  
  ### 2. 일대다(OneToMany) 단방향 연관관계
  * 주로 사용할 것 같지 않으므로 필요할 때 공부하도록 하겠다.
  
  ### 3. 일대일(OneToOne) 단방향 연관관계
  * 주 테이블이나 대상 테이블 둘 중 어느 테이블이나 외래 키를 가질 수 있다.  
    (ManyToOne, OneToMany 에서 Many 쪽 테이블이 외래 키를 가지는 특성과 대비된다.)
    
  Member.java
  <pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Table(name = "one_way_one_to_one_member")
    @Entity(name = "OneWayOneToOneMember")
    public class Member {
     
        @Column(name = "member_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Integer id;
     
        @Column(name = "user_name")
        private String userName;
     
        @JoinColumn(name = "locker_id")
        @OneToOne
        private Locker locker;
     
        @Override
        public String toString() {
            return "Member{" +
                    "id=" + id +
                    ", userName='" + userName + '\'' +
                    ", locker=" + locker +
                    '}';
        }
    }    
    </code>
  </pre>
  
  Locker.java
  <pre>
     <code>
     @NoArgsConstructor
     @Setter
     @Getter
     @Table(name = "one_way_one_to_one_locker")
     @Entity(name = "OneWayOneToOneLocker")
     public class Locker {
     
         @Column(name = "locker_id")
         @GeneratedValue(strategy = GenerationType.IDENTITY)
         @Id
         private Integer id;
     
         private String name;
     
         @Override
         public String toString() {
             return "Locker{" +
                     "id=" + id +
                     ", name='" + name + '\'' +
                     '}';
         }
     }
     </code>
  </pre>
    
## 5. 양방향 연관관계
양방향 연관관계의 경우 관리 Point 가 늘어나기 때문에 필요할 때 공부해서 사용하도록 하겠다.

## 6. 다양한 매핑
### 1. 상속 관계 매핑
Item 이라는 부모 타입의 테이블이 있고, Album, Movie, Book 이라는 자식 타입의 상품  
테이블이 있다면 공통 속성을 부모 테이블이 가지고있고 각 상품의 특징에 해당하는 속성은  
테이블 별로 가져야 할 것이다.  
또한 기존에 DB 테이블이 구현된 상태에서 상속 관계를 매핑해야하는 경우에는  
아래에 나오는 전략에 따른 테이블 구조를 보고 참고해서 선택하면 된다.  

이런 상속 관계를 매핑하는 방법에 대해 알아보도록 하자.

* #### 1. 조인 전략(각각의 테이블로 변환)
Entity 별로 테이블을 만들고 자식 테이블이 부모 테이블의 기본 키(식별자)를  
기본 키이자 외래 키로 사용하는 것이다.  

  DB 테이블 구조

  <img width="258" alt="스크린샷 2020-09-08 오전 10 32 05" src="https://user-images.githubusercontent.com/8858991/92424248-cc300200-f1be-11ea-8c9d-debb01e918d9.png">
  
  Item.java
  <pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    // 어떤 컬럼명으로 자식 상품들을 구별할지, name = "item_type" 생략 시 기본 값 "DTYPE"
    @DiscriminatorColumn(name = "item_type")
    // 부모 클래스에 이 어노테이션을 사용해줘야 함, 매핑 전략을 JOINED 로 설정해 전략을 조인 전략으로 설정
    @Inheritance(strategy = InheritanceType.JOINED)
    @Table(name = "join_strategy_item")
    @Entity(name = "JoinStrategyItem")
    /**
     *  abstract 를 빼고 일반 class 로 해도 작동하지만, Item 그 자체로 상품이 아닌, 상세 정보를 나타낼 자식 상품 Entity 가 있어야 하기에
     *  abstract 를 명시적으로 선언해 Item Entity 단일로는 상품의 가치가 없음을 알려주고있다.
     */
    public abstract class Item {
    
        @Column(name = "item_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Integer id;
    
        private String name;
    
        private int price;
    
        @Override
        public String toString() {
            return "Item{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", price=" + price +
                    '}';
        }
    }
    </code>
  </pre>
  
  Album.java
  <pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    /**
     * 부모(Item.java) Entity 관련 테이블의 구분 컬럼(@DiscriminatorColumn 으로 설정한 컬럼)에 어떤 값으로 들어갈지
     * 이 어노테이션 생략 시 Entity Name 이 기본 값으로 들어가게 된다 (@Entity(name = "JoinStrategyBook"))
     */
    @DiscriminatorValue(value = "ALBUM")
    @Table(name = "join_strategy_album")
    @Entity(name = "JoinStrategyAlbum")
    public class Album extends Item{
    
        private String artist;
    
        @Override
        public String toString() {
            return "Album{" +
                    "artist='" + artist + '\'' +
                    '}';
        }
    }
    </code>
  </pre>
  
  Movie.java
  <pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    /**
     * 부모(Item.java) Entity 관련 테이블의 구분 컬럼(@DiscriminatorColumn 으로 설정한 컬럼)에 어떤 값으로 들어갈지
     * 이 어노테이션 생략 시 Entity Name 이 기본 값으로 들어가게 된다 (@Entity(name = "JoinStrategyBook"))
     */
    @DiscriminatorValue(value = "MOVIE")
    @Table(name = "join_strategy_movie")
    @Entity(name = "JoinStrategyMovie")
    public class Movie extends Item{
    
        private String director;
        private String actor;
    
        @Override
        public String toString() {
            return "Movie{" +
                    "director='" + director + '\'' +
                    ", actor='" + actor + '\'' +
                    '}';
        }
    }
    </code>
  </pre>
  
  Book.java
  <pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    /**
     * 부모(Item.java) Entity 관련 테이블의 구분 컬럼(@DiscriminatorColumn 으로 설정한 컬럼)에 어떤 값으로 들어갈지
     * 이 어노테이션 생략 시 Entity Name 이 기본 값으로 들어가게 된다 (@Entity(name = "JoinStrategyBook"))
     */
    @DiscriminatorValue(value = "BOOK")
    @Table(name = "join_strategy_book")
    @Entity(name = "JoinStrategyBook")
    public class Book extends Item{
    
        private String author;
        private String isbn;
    
        @Override
        public String toString() {
            return "Book{" +
                    "author='" + author + '\'' +
                    ", isbn='" + isbn + '\'' +
                    '}';
        }
    }
    </code>
  </pre>
  
* #### 2. 단일 테이블 전략(통합 테이블로 변환)
  부모 자식 테이블을 모두 합쳐 한 테이블로 관리하는 것
  DB 테이블 구조

  <img width="556" alt="스크린샷 2020-09-08 오전 10 32 50" src="https://user-images.githubusercontent.com/8858991/92424340-0dc0ad00-f1bf-11ea-8a28-a149faea6be6.png">
  
  Item.java
  <pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    // 어떤 컬럼명으로 자식 상품들을 구별할지, name = "item_type" 생략 시 기본 값 "DTYPE"
    @DiscriminatorColumn(name = "item_type")
    // 부모 클래스에 이 어노테이션을 사용해줘야 함, 매핑 전략을 SINGLE_TABLE 로 설정해 전략을 단일 테이블 전략으로 설정
    @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
    @Table(name = "single_table_strategy_item")
    @Entity(name = "SingleTableStrategyItem")
    /**
     *  abstract 를 빼고 일반 class 로 해도 작동하지만, Item 그 자체로 상품이 아닌, 상세 정보를 나타낼 자식 상품 Entity 가 있어야 하기에
     *  abstract 를 명시적으로 선언해 Item Entity 단일로는 상품의 가치가 없음을 알려주고있다.
     */
    public abstract class Item {
    
        @Column(name = "item_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        protected Integer id;
    
        protected String name;
    
        protected int price;
    }
    </code>
  </pre>
  
  Album.java
  <pre>
      <code>
      @NoArgsConstructor
      @Setter
      @Getter
      /**
       * 부모(Item.java) Entity 관련 테이블의 구분 컬럼(@DiscriminatorColumn 으로 설정한 컬럼)에 어떤 값으로 들어갈지
       * 이 어노테이션 생략 시 Entity Name 이 기본 값으로 들어가게 된다 (@Entity(name = "JoinStrategyBook"))
       */
      @DiscriminatorValue(value = "ALBUM")
      @Table(name = "single_table_strategy_album")
      @Entity(name = "SingleTableStrategyAlbum")
      public class Album extends Item {
      
          String artist;
      
          @Override
          public String toString() {
              return "Album{" +
                      "artist='" + artist + '\'' +
                      ", id=" + id +
                      ", name='" + name + '\'' +
                      ", price=" + price +
                      '}';
          }
      }
      </code>
  </pre>
    
  Movie.java
  <pre>
      <code>
      @NoArgsConstructor
      @Setter
      @Getter
      /**
       * 부모(Item.java) Entity 관련 테이블의 구분 컬럼(@DiscriminatorColumn 으로 설정한 컬럼)에 어떤 값으로 들어갈지
       * 이 어노테이션 생략 시 Entity Name 이 기본 값으로 들어가게 된다 (@Entity(name = "JoinStrategyBook"))
       */
      @DiscriminatorValue(value = "MOVIE")
      @Table(name = "single_table_strategy_movie")
      @Entity(name = "SingleTableStrategyMovie")
      public class Movie extends Item {
      
          private String director;
          private String actor;
      
          @Override
          public String toString() {
              return "Movie{" +
                      "director='" + director + '\'' +
                      ", actor='" + actor + '\'' +
                      ", id=" + id +
                      ", name='" + name + '\'' +
                      ", price=" + price +
                      '}';
          }
      }
      </code>
  </pre>
    
  Book.java
  <pre>
      <code>
      @NoArgsConstructor
      @Setter
      @Getter
      /**
       * 부모(Item.java) Entity 관련 테이블의 구분 컬럼(@DiscriminatorColumn 으로 설정한 컬럼)에 어떤 값으로 들어갈지
       * 이 어노테이션 생략 시 Entity Name 이 기본 값으로 들어가게 된다 (@Entity(name = "JoinStrategyBook"))
       */
      @DiscriminatorValue(value = "BOOK")
      @Table(name = "single_table_strategy_book")
      @Entity(name = "SingleTableStrategyBook")
      public class Book extends Item {
      
          private String author;
          private String isbn;
      
          @Override
          public String toString() {
              return "Book{" +
                      "author='" + author + '\'' +
                      ", isbn='" + isbn + '\'' +
                      ", id=" + id +
                      ", name='" + name + '\'' +
                      ", price=" + price +
                      '}';
          }
      }
      </code>
  </pre>

* #### 3. 구현 클래스마다 테이블 전략(서브타입 테이블로 변환)
  부모 테이블의 속성을 가진 각각의 자식 테이블로 관리하는 것  
  DB 테이블 구조

  <img width="426" alt="스크린샷 2020-09-08 오전 10 33 41" src="https://user-images.githubusercontent.com/8858991/92424356-1add9c00-f1bf-11ea-857f-5dafa2a5d8df.png">

  Item.java
    <pre>
      <code>
      @NoArgsConstructor
      @Setter
      @Getter
      // 부모 클래스에 이 어노테이션을 사용해줘야 함, 매핑 전략을 TABLE_PER_CLASS 로 설정해 전략을 구현 클래스마다 테이블 전략으로 설정
      @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
      @Table(name = "table_per_concrete_class_strategy_item")
      @Entity(name = "TablePerConcreteClassStrategyItem")
      /**
       *  abstract 를 빼고 일반 class 로 해도 작동하지만, Item 그 자체로 상품이 아닌, 상세 정보를 나타낼 자식 상품 Entity 가 있어야 하기에
       *  abstract 를 명시적으로 선언해 Item Entity 단일로는 상품의 가치가 없음을 알려주고있다.
       */
      public abstract class Item {
      
          @Column(name = "item_id")
          /**
           * Table Per Concrete Class Strategy 의 경우 각각의 테이블이 생기며, Item table 이 생성되지 않는다.
           * 이 뜻은 @GeneratedValue(strategy = GenerationType.IDENTITY) 을 사용하여 Item table 의 기본 키(식별자)의
           * 자동으로 증가하는 유일한 값을 사용할 수 없음을 의미한다.
           *
           * Album, Movie, Book 3개의 테이블이 생성되고
           * 각각의 테이블의 식별자 값은 테이블 간 동일한 기본 키(식별자)를 생성하지 않기 위해서
           * @GeneratedValue(strategy = GenerationType.TABLE) 혹은  @GeneratedValue(strategy = GenerationType.SEQUENCE)을
           * 사용해야한다는 것을 의미하고,
           *
           * @GeneratedValue 에 strategy 를 설정하지 않으면 기본으로 @GeneratedValue(strategy = GenerationType.AUTO) 가 할당되는데
           * 이렇게 되면 자동적으로 @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) 를 사용하기 위해
           * 설정된 방언(현재 : H2)에 알맞은 @GeneratedValue(strategy = GenerationType.SEQUENCE) 을 설정한다.
           *
           * 기본적으로 Hibernate 가 제공하는 TABLE 을 통한 기본 키(식별자) 획득 관련 TABLE 혹은 SEQUENCE 이외
           * DB 에 설정된 별도의 기본 키(식별자) 획득 관련 TABLE 혹은 SEQUENCE 를 사용하려면,
           * 기본 키(식별자) 할당 전략을 참고하여 알맞게 설정해야한다.
           */
          @GeneratedValue
          @Id
          protected Integer id;
      
          protected String name;
      
          protected int price;
      }
      </code>
    </pre>
    
    Album.java
    <pre>
        <code>
        @NoArgsConstructor
        @Setter
        @Getter
        @Table(name = "table_per_concrete_class_strategy_album")
        @Entity(name = "TablePerConcreteClassStrategyAlbum")
        public class Album extends Item {
        
            private String artist;
        
            @Override
            public String toString() {
                return "Album{" +
                        "artist='" + artist + '\'' +
                        ", id=" + id +
                        ", name='" + name + '\'' +
                        ", price=" + price +
                        '}';
            }
        }
        </code>
    </pre>
      
    Movie.java
    <pre>
        <code>
        @NoArgsConstructor
        @Setter
        @Getter
        @Table(name = "table_per_concrete_class_strategy_movie")
        @Entity(name = "TablePerConcreteClassStrategyMovie")
        public class Movie extends Item {
        
            private String director;
            private String actor;
        
            @Override
            public String toString() {
                return "Movie{" +
                        "director='" + director + '\'' +
                        ", actor='" + actor + '\'' +
                        ", id=" + id +
                        ", name='" + name + '\'' +
                        ", price=" + price +
                        '}';
            }
        }
        </code>
    </pre>
      
    Book.java
    <pre>
        <code>
        @NoArgsConstructor
        @Setter
        @Getter
        @Table(name = "table_per_concrete_class_strategy_book")
        @Entity(name = "TablePerConcreteClassStrategyBook")
        public class Book extends Item {
        
            private String author;
            private String isbn;
        
            @Override
            public String toString() {
                return "Book{" +
                        "author='" + author + '\'' +
                        ", isbn='" + isbn + '\'' +
                        ", id=" + id +
                        ", name='" + name + '\'' +
                        ", price=" + price +
                        '}';
            }
        }
        </code>
    </pre>

### 2. 매핑 정보만 상속해서 사용하기(@MappedSuperclass)
부모 클래스와 자식 클래스를 DB Table 과 매핑하여 사용하던 상속관계와는 달리  
공통 속성(등록일자, 수정일자, 등록자 등)을 정의하고 매핑 정보만 상속할 목적으로 사용

BaseEntity.java
<pre>
    <code>
    @Setter
    @Getter
    /**
     * 상속 관계 매핑은 아니며, 등록일자, 수정일자, 등록자 같이 여러 Entity 에서 공통으로 사용되는 속성을
     * 정의해두고 상속만 받아 사용하는 방식이다.
     *
     * 즉, 매핑 정보를 상속할 목적으로 사용하지 상속 관계 매핑처럼 부모 Entity 를 만들어 DB 의 Table 화를 하는 등
     * Entity 로서 사용하지 않는다.
     * (@Entity 어노테이션이 없는 것을 확인할 수 있다.)
     *
     */
    @MappedSuperclass
    public abstract class BaseEntity {
    
        @Column(name = "base_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        protected Integer id;
    
        protected String name;
    }
    </code>
</pre>

Member.java
<pre>
    <code>
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
    </code>
</pre>

Seller.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Table(name = "mapped_super_class_seller")
    @Entity(name = "MappedSuperClassSeller")
    public class Seller extends BaseEntity {
    
        @Column(name = "shop_name")
        private String shopName;
    
        @Override
        public String toString() {
            return "Seller{" +
                    "shopName='" + shopName + '\'' +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
</pre>

### 3. 복합 키와 식별 관계 매핑

* 복합 키  
기존의 기본 키에 해당하는 컬럼 하나를 사용해서 식별자로 사용했다면,  
두개 이상의 컬럼을 합쳐야 기본 키의 역할을 할 수 있을 때가 있다.  
이렇게 두개 이상의 컬럼을 묶어서 식별자로 사용하는 것을  
복합 키 라고 한다.

#### 1. 식별 관계
부모 테이블의 기본 키(식별자)를 자식 테이블의 기본 키(식별자) + 외래 키로 사용하는 관계

#### 2. 비식별 관계
부모 테이블의 기본 키(식별자)를 자식 테이블의 외래 키로만 사용하는 관계  
자식 테이블의 기본 키(식별자)는 자식 테이블에서 직접 관리

* 필수적 비식별 관계
외래 키 NULL 허용 X(모든 데이터가 부모 테이블과 연관관계가 있음)

* 선택적 비식별 관계
외래 키 NULL 허용 O(각각의 데이터들은 부모 테이블과 연관관계가 있을수도 없을수도 있음)

#### 3. 복합 키 - 비식별 관계 매핑
JPA 는 Persistence Context 에 Entity 를 보관할 때 Entity 의 식별자를 키로 사용한다.  
(Map 에서 Key 를 기반으로 Value 를 찾듯)  

식별자(기본 키) 필드가 2개 이상이면 별도의 식별자 클래스를 만들어 해당 클래스의  
equals(), hashCode() 메서드를 구현해야한다.  
(식별자 필드가 2개 이상이기에 식별자 클래스를 만들어 해당 클래스의  
hashCode 를 Persistence Context 의 Entity key 로 사용하고  
equals() 연산으로 같은 식별자인지 확인하기 위해서가 아닐까?)

복합 키 - 비식별 관계 매핑을 위해 아래 2가지 방법 중 자신에게 맞는 방법을 사용하면 된다.

참고로 복합 키에는 @GeneratedValue 를 사용할 수 없으며, 복합 키를 구성하는 컬럼 중 어떤 하나에도  
사용할 수 없다.

* Table  
parent
<img width="401" alt="스크린샷 2020-09-09 오후 4 55 22" src="https://user-images.githubusercontent.com/8858991/92694847-99cf0200-f382-11ea-94b2-d70142663369.png">
child
<img width="397" alt="스크린샷 2020-09-09 오후 4 55 30" src="https://user-images.githubusercontent.com/8858991/92694852-9b98c580-f382-11ea-9981-a1353783eec5.png">
grand_child
<img width="598" alt="스크린샷 2020-09-09 오후 4 55 38" src="https://user-images.githubusercontent.com/8858991/92694857-9c315c00-f382-11ea-81be-06dd0c23fb58.png">

* @IdClass

ParentId.java
<pre>
    <code>
    /**
     * @IdClass 식별자 클래스로 사용할 클래스는
     * 1. 식별자 클래스의 변수명과 Entity 에서 사용하는 변수명이 일치해야한다.
     * 2. Serializable 인터페이스를 implements 해야한다.
     * 3. equals(), hashCode() 를 구현해야 한다.
     * 4. 기본 생성자가 있어야 한다.
     * 5. public class 여야 한다.
     */
    @NoArgsConstructor
    @Setter
    @Getter
    public class ParentId implements Serializable {
    
        private String parentId1;
        private String parentId2;
    
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParentId parentId = (ParentId) o;
            return Objects.equals(parentId1, parentId.parentId1) &&
                    Objects.equals(parentId2, parentId.parentId2);
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(parentId1, parentId2);
        }
    }
    </code>
</pre>

Parent.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    // 복합 키를 지정한 식별자 클래스를 알려 줌
    @IdClass(ParentId.class)
    @Table(name = "id_class_parent")
    @Entity(name = "IdClassParent")
    public class Parent {
    
        @Column(name = "parent_id_1")
        @Id
        private String parentId1;
    
        @Column(name = "parent_id_2")
        @Id
        private String parentId2;
    
        private String name;
    
        @Override
        public String toString() {
            return "Parent{" +
                    "parentId1='" + parentId1 + '\'' +
                    ", parentId2='" + parentId2 + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
</pre>

Child.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Table(name = "id_class_child")
    @Entity(name = "IdClassChild")
    public class Child {
    
        @Column(name = "chlid_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Integer id;
    
        @ManyToOne
        @JoinColumns(
                {
                /**
                 * name : Child Table 에 있는 컬럼 명, referencedColumnName : Parent Table 에 있는 컬럼 명
                 * 부모 테이블에 있는 복합 키로 사용되는 컬럼명들과 자식 테이블에 외래 키로 사용될 키들의 컬럼명이 다를 경우
                 * referencedColumnName 로 따로 부모 테이블의 복합 키 관련 컬럼명들을 지정해준다.
                 */
                @JoinColumn(name = "CHILD_PARENT_ID_1", referencedColumnName = "PARENT_ID_1")
                , @JoinColumn(name = "CHILD_PARENT_ID_2", referencedColumnName = "PARENT_ID_2")
                }
        )
        private Parent parent;
    
        private String name;
    
        @Override
        public String toString() {
            return "Child{" +
                    "id=" + id +
                    ", parent=" + parent +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
</pre>

* @EmbeddedId

ParentId.java
<pre>
    <code>
    /**
     * @IdClass 로 설정해 사용하던 식별자 클래스와는 다르게
     * @Embeddable 어노테이션이 붙어있으며
     *
     * 이 식별자 클래스에 있는 컬럼을 바로 사용한다.
     * (@IdClass 에서는 부모 Entity 에 있는 변수 명과 @IdClass 로 사용 할 식별자 클래스의 변수 명이
     * 일치해야하던 것돠 대비된다.)
     *
     * 1. @Embeddable 어노테이션을 붙여줘야한다.
     * 2. Serializable Interface 를 implements 해야한다.
     * 3. equals(), hashCode() 를 구현해야한다.
     * 4. 기본 생성자가 있어야 한다.
     * 5. public class 여야 한다.
     */
    @Setter
    @Getter
    @Embeddable
    public class ParentId implements Serializable {
    
        @Column(name = "parent_id_1")
        private String parentId1;
    
        @Column(name = "parent_id_2")
        private String parentId2;
    
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParentId parentId = (ParentId) o;
            return Objects.equals(parentId1, parentId.parentId1) &&
                    Objects.equals(parentId2, parentId.parentId2);
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(parentId1, parentId2);
        }
    
        /**
         * @IdClass 를 사용할 때엔 부모 Entity 에 복합 키 관련 변수가 있어서 부모 Entity 에 toString 을 구현하면 되었지만
         * @EmbeddedId 를 사용하면 부모 Entity 에서 복합 키로 설정한 부모 Entity 변수에 @EmbeddedId 로 설정한
         * @Embeddable 로 설정하고 객체를 가져다 복합 키로 사용하기 때문에
         *
         * 복합 키에 해당하는  @Embeddable 클래스에 이와 같이 toString() 을 정의해줬다.
         */
        @Override
        public String toString() {
            return "ParentId{" +
                    "parentId1='" + parentId1 + '\'' +
                    ", parentId2='" + parentId2 + '\'' +
                    '}';
        }
    }
    </code>
</pre>

Parent.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Table(name = "embedded_id_parent")
    @Entity(name = "EmbeddedIdParent")
    public class Parent {
    
        // @Embeddable 로 설정된 식별자 클래스임을 나타냄
        @EmbeddedId
        private ParentId id;
    
        private String name;
    
        @Override
        public String toString() {
            return "Parent{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
</pre>

Child.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Table(name = "embedded_id_child")
    @Entity(name = "EmbeddedIdChild")
    public class Child {
    
        @Column(name = "child_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Integer id;
    
        @JoinColumns({
                /**
                 * name : Child Table 에 있는 컬럼 명, referencedColumnName : Parent Table 에 있는 컬럼 명
                 * 부모 테이블에 있는 복합 키로 사용되는 컬럼명들과 자식 테이블에 외래 키로 사용될 키들의 컬럼명이 다를 경우
                 * referencedColumnName 로 따로 부모 테이블의 복합 키 관련 컬럼명들을 지정해준다.
                 */
                @JoinColumn(name = "CHILD_PARENT_ID_1", referencedColumnName = "PARENT_ID_1")
                , @JoinColumn(name = "CHILD_PARENT_ID_2", referencedColumnName = "PARENT_ID_2")
        })
        @ManyToOne
        private Parent parent;
    
        private String name;
    
        @Override
        public String toString() {
            return "Child{" +
                    "id=" + id +
                    ", parent=" + parent +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
</pre>

#### 4. 복합 키 - 식별 관계 매핑
복합 키 - 식별관계의 경우 부모 - 자식 - 손자 Table 까지 기본 키를 계속해서 전달하는 식별 관계다.  
자식 Table 은 부모 Table 의 기본 키를 포함해서 복합 키를 구성해나가며,  
부모 Table 의 기본 키를 복합 키 이자 외래 키 로 사용하게 된다.

* Table  
parent
<img width="433" alt="스크린샷 2020-09-09 오후 4 55 56" src="https://user-images.githubusercontent.com/8858991/92694862-9cc9f280-f382-11ea-8638-590ff3332a95.png">
child
<img width="419" alt="스크린샷 2020-09-09 오후 4 56 07" src="https://user-images.githubusercontent.com/8858991/92694863-9d628900-f382-11ea-8c1f-0203482c5755.png">
grand_child
<img width="589" alt="스크린샷 2020-09-09 오후 4 56 18" src="https://user-images.githubusercontent.com/8858991/92694866-9dfb1f80-f382-11ea-8a0b-6fc5dc0b66d7.png">

* @ClassId

Parent.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Table(name = "identifying_relationship_id_class_parent")
    @Entity(name = "IdentifyingRelationshipIdClassParent")
    public class Parent {
    
        @Column(name = "parent_id")
        @Id
        private String parentId;
    
        private String name;
    
        @Override
        public String toString() {
            return "Parent{" +
                    "parentId='" + parentId + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
</pre>

ChildId.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    public class ChildId implements Serializable {
    
        // Parent 의 기본 키(식별자) 타입이 String 이고, ChildId 를 식별자 클래스로 사용할 Child Entity 의 Parent 연관관계 변수명이 parent
        private String parent;
        private String childId;
    
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChildId childId1 = (ChildId) o;
            return Objects.equals(parent, childId1.parent) &&
                    Objects.equals(childId, childId1.childId);
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(parent, childId);
        }
    
        @Override
        public String toString() {
            return "ChildId{" +
                    "parent='" + parent + '\'' +
                    ", childId='" + childId + '\'' +
                    '}';
        }
    }
    </code>
</pre>

Child.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @IdClass(ChildId.class)
    @Table(name = "identifying_relationship_id_class_child")
    @Entity(name = "IdentifyingRelationshipIdClassChild")
    public class Child {
    
        @Column(name = "child_id")
        @Id
        private String childId;
    
        @JoinColumn(name = "child_parent_id")
        @ManyToOne
        @Id
        private Parent parent;
    
        private String name;
    
        @Override
        public String toString() {
            return "Child{" +
                    "childId='" + childId + '\'' +
                    ", parent=" + parent +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
</pre>

GrandChildId.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    public class GrandChildId implements Serializable {
    
        private ChildId child;
        private String grandChildId;
    
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GrandChildId that = (GrandChildId) o;
            return Objects.equals(child, that.child) &&
                    Objects.equals(grandChildId, that.grandChildId);
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(child, grandChildId);
        }
    
        @Override
        public String toString() {
            return "GrandChildId{" +
                    "child=" + child +
                    ", grandChildId='" + grandChildId + '\'' +
                    '}';
        }
    }
    </code>
</pre>

GrandChild.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @IdClass(GrandChildId.class)
    @Table(name = "identifying_relationship_id_class_grand_child")
    @Entity(name = "IdentifyingRelationshipIdClassGrandChild")
    public class GrandChild {
    
        @Column(name = "grand_child_id")
        @Id
        private String grandChildId;
    
        @JoinColumns(
                {
                        @JoinColumn(name = "grand_child_grand_parent_id", referencedColumnName = "child_parent_id")
                        , @JoinColumn(name = "grand_child_parent_id", referencedColumnName = "child_id")
                }
        )
        @ManyToOne
        @Id
        private Child child;
    
        private String name;
    
        @Override
        public String toString() {
            return "GrandChild{" +
                    "grandChildId='" + grandChildId + '\'' +
                    ", child=" + child +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
</pre>

* @EmbeddedId
Parent.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Table(name = "identifying_relationship_embedded_id_parent")
    @Entity(name = "IndentifyingRelationshipEmbeddedIdParent")
    public class Parent {
    
        @Column(name = "parent_id")
        @Id
        private String id;
    
        private String name;
    
        @Override
        public String toString() {
            return "Parent{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
</pre>

ChildId.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Embeddable
    public class ChildId implements Serializable {
    
        // Child Entity @MapsId("parentId") 와 매핑됨
        @Column(name = "child_parent_id")
        private String parentId;
    
        @Column(name = "child_id")
        private String childId;
    
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChildId childId1 = (ChildId) o;
            return Objects.equals(parentId, childId1.parentId) &&
                    Objects.equals(childId, childId1.childId);
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(parentId, childId);
        }
    
        @Override
        public String toString() {
            return "ChildId{" +
                    "parentId='" + parentId + '\'' +
                    ", childId='" + childId + '\'' +
                    '}';
        }
    }
    </code>
</pre>

Child.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Table(name = "identifying_relationship_embedded_id_child")
    @Entity(name = "IdentifyingRelationshipEmbeddedIdChild")
    public class Child {
    
        @EmbeddedId
        private ChildId childId;
    
        // 외래 키와 매핑한 연관관계를 기본 키에도 매핑하겠다는 뜻
        @MapsId(value = "parentId")
        @JoinColumn(name = "child_parent_id", referencedColumnName = "parent_id")
        @ManyToOne
        private Parent parent;
    
        private String name;
    
        @Override
        public String toString() {
            return "Child{" +
                    "childId=" + childId +
                    ", parent=" + parent +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
</pre>

GrandChildId.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Embeddable
    public class GrandChildId implements Serializable {
    
        private ChildId grandChildParentId;
    
        @Column(name = "grand_child_id")
        private String grandChildId;
    
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GrandChildId that = (GrandChildId) o;
            return Objects.equals(grandChildParentId, that.grandChildParentId) &&
                    Objects.equals(grandChildId, that.grandChildId);
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(grandChildParentId, grandChildId);
        }
    
        @Override
        public String toString() {
            return "GrandChildId{" +
                    "grandChildParentId=" + grandChildParentId +
                    ", grandChildId='" + grandChildId + '\'' +
                    '}';
        }
    }
    </code>
</pre>

GrandChild.java
<pre>
    <code>
    @NoArgsConstructor
    @Setter
    @Getter
    @Table(name = "identifying_relationship_embedded_id_grand_child")
    @Entity(name = "IdentifyingRelationshipEmbeddedIdGrandChild")
    public class GrandChild {
    
        @EmbeddedId
        private GrandChildId grandChildId;
    
        @JoinColumns({
                @JoinColumn(name = "grand_child_grand_parent_id", referencedColumnName = "child_parent_id")
                , @JoinColumn(name = "grand_child_parent_id", referencedColumnName = "child_id")
        })
        // 외래 키와 매핑한 연관관계를 기본 키에도 매핑하겠다는 뜻
        @MapsId(value = "grandChildParentId")
        @ManyToOne
        private Child child;
    
        private String name;
    
        @Override
        public String toString() {
            return "GrandChild{" +
                    "grandChildId=" + grandChildId +
                    ", child=" + child +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
    </code>
</pre>

# 3. Proxy
연관관계를 맺게되면 Entity 를 불러올 때 해당 Entity 와 연관된 Entity 의 Table 을 같이
조회(Join)한다.  
하지만 연관된 Entity 를 항상 사용하는 것이 아니라면 실제로 사용될 때까지 DB 조회를 지연하는 것이  
좋은데 이를 지연 로딩이라고 한다.  

지연 로딩 기능을 사용하기 위해서는 실제 Entity 객체 대신에 해당 Entity 인 것처럼 행동하다  
실제 사용될 때 까지 DB 조회를 지연하기 위한 가짜 객체가 필요한데 이것은 Proxy 객체라고 한다.

## 1. Entity 조회 시 Proxy 객체로 조회하기
<pre>
    <code>
    /**
     * Entity 조회(즉시 로딩) : entityManager.find();
     * Entity Proxy 객체 조회(지연 로딩) : entityManager.getReference();
     *
     * Proxy Class 의 경우 실제 Entity Class 를 상속받아 만들어지기 때문에
     * Entity 와 겉모습이 같다.
     *
     * 하지만 entityManager.find() 를 통해 조회할 시 해당 Entity 를 즉시 조회하도록
     * Persistence Context 에 요청하고 Persistence Context 에 없으면 DB 를 조회하는데,
     *
     * entityManager.getReference() 를 통해 조회할 시 해당 Entity 를 상속 받은 Proxy 객체가 리턴되고,
     * Proxy 객체는 마치 해당 Entity 처럼 다뤄지는데 빈 값이 들어있는 껍데기에 불과하다.
     *
     * 실제로 referenceMember1.getUserName() 처럼 사용될 때 Persistence Context 에 실제 엔티티 정보를 가져오도록
     * 요청을 한다.(DB 에 SELECT 쿼리가 나가며 Entity 정보를 가져와서 프록시 객체에 세팅한다. => 조회해와도 Type 이 Proxy Class 이다.)
     *
     * 만약에 entityManager.getReference() 를 사용해서 조회하는 Entity 가 Persistence Context 에 존재하는 Entity 라면
     * 바로 해당 Entity 를 리턴하기 때문에 Type 이 Entity Class 이다.
     * (Proxy Class 를 사용하는 이유는 불필요한 쿼리를 발생시키지 않고 정말 필요할 때 쿼리를 발생시키기 위함인데
     *  이미 Persistence Context 에 있는 Entity 일 경우에는 쿼리를 사용하지 않아도 가져올 수 있기 때문이라고 생각한다.)
     *
     * Persistence Context 해당 Entity 정보를 조회 요청을 하고, Persistence Context 해당 Entity 정보가 없으면 빈 껍데기인 Proxy Class 가
     * 실제로 사용될때에 DB 에 SELECT 쿼리를 보내서 Persistence Context 에 해당 Entity 의 정보를 가져와 세팅하거나
     * Persistence Context 에 해당 Entity 정보를 조회 요청을 하고, Persistence Context 해당 Entity 정보가 있으면,
     * Entity 를 그대로 받아서 Proxy Class 가 아닌 해당 Entity 를 리턴하는 등 Persistence Context 에게 의존하고 있다.
     *
     * 따라서 이 Proxy Class 는 준영속 상태가 되면 사용이 불가능 하다.
     *
     */
    Member referenceMember1 = entityManager.getReference(Member.class, member1.getId());
    </code>
</pre>
## 2. Proxy 특징

* Proxy 는 실제 사용될 떄에 Persistence Context 에 실제 Entity 생성을 요청하는데 이것이 Proxy 초기화.

* Persistence Context 는 Proxy 로 부터 Entity 생성을 요청받는데 이 때 DB 를 조회한다.

* Persistence Context 로 부터 Entity 객체를 받으면 이에 대한 참조를 Proxy 객체 내 변수에 저장한다.

*  entityManager.getReference() 메서드를 통해 Proxy 객체를 생성하려할때 해당 Entity 가 Persistence Context 에  
   존재한다면 Proxy 객체가 아닌 Entity 를 리턴한다.

* 처음 사용할 때 한 번만 조회 된다.

* Proxy 객체가 초기화 되더라도 Proxy 객체가 Entity 로 바뀌는 것은 아니다.

* Proxy 객체는 원본 Entity 상속받은 객체이다.

* 따라서 타입 체크시에 주의해야한다.

* Proxy 객체가 실제 사용될 때 초기화를 한다고 했는데 team.getId() 와 같이 식별자를 조회하는  
  행위를 하면 프록시가 초기화 되지 않는다.  
  (entityManager.getReference() 메서드 호출시에 이미 식별자를 넘겨서 Proxy 객체가 들고 있기 때문)


## 3. 즉시 로딩
조회하려는 Entity 와 연관된 Entity 를 즉시 조회한다.  
(Hibernate 는 가능한 SQL Join 을 통해 한 번에 조회)

## 4. 지연 로딩
조회하려는 Entity 와 연관된 Entity 를 Proxy 로 조회한다.  
(Proxy 를 실제로 사용할 때 초기화를 통해 DB를 조회한다.)


## 5. Fetch 전략
개발시에 연관관계에 있는 Entity 들 중 어떤 Entity 들이 자주 함께 사용되고,  
어떤 Entity 는 필요할 때 조회해오는 것이 좋을지 판단하기 힘들다.  

따라서 개발시에는 모든 연관관계에 지연 로딩을 사용하고,  
어느정도 틀이 잡히고 서비스에 대한 도메인 지식이 생기면  
즉시 로딩이 필요하다고 판단 되는 경우 즉시 로딩을 적용하면 될 것 같다.

## 6. 영속성 전이
나중에 필요할 때 공부해보도록 하겠다.

# 4. 값 타입
JPA 에는 Entity 와 Value 두가지 type 이 있다.  
Entity 는 Table 과 매핑되는 Entity 객체이며,  
Value 타입은 Entity 에서 값 처럼 사용되는 객체이다.  

일반적으로 값 타입은 Entity 의 프로퍼티 중 연관있는 프로퍼티를 묶어  
Value Class 로 만들고 이를 @Embeddable 로 선언해 값 객체임을 나타내고  
Entity 에서 @Embedded 를 통해 값 객체를 사용하고 있음을 나타낸다.  

Value Class 를 사용해서 Entity 를 구성하면 어떤 이점이 있는지 코드를 통해 이해해보자.

MemberWithOutEmbedded.java
<pre>
    <code>
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
    </code>
</pre>

Table
<img width="545" alt="스크린샷 2020-09-11 오후 10 39 32" src="https://user-images.githubusercontent.com/8858991/92997109-9cc91e80-f54b-11ea-8fad-9844dd350457.png">


위와 같은 Entity 가 있다고 하자, 해당 Entity 에는 여러 프로퍼티가 있다.  
총 7개의 프로퍼티가 있는데 이름, 나이, 사는 도시, 사는 곳의 도로명, 사는 곳의 우편코드,  
시작일, 종료일이 있다.  

이 Entity 는 어떤 회사의 직원 정보들이라고 할 때, 크게 식별자, 이름, 사는 곳(집 주소), 근무기간  
4가지 정보를 담고 있다.  

이렇게 4가지 정보를 담고있는데 Entity 의 프로퍼티는 7개로 너무 많다고 느껴진다.  
7개의 프로퍼티는 직원 Entity 에 필요한 프로퍼티가 맞지만 객체지향적으로 묶어서 나타낼 수 있다면  
더 좋다고 생각된다.  

아래의 임베디드 타입을 보고 한번 생각해보자.
## 1. 임베디드 타입(복합 값 타입)

Member.java
<pre>
    <code>
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Table(name = "value_type_embedded_type_member")
    @Entity(name = "ValueTypeEmbeddedTypeMember")
    public class Member {
    
        @Column(name = "member_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        private Integer id;
    
        private String name;
    
        // 값 타입을 사용
        @Embedded
        private Period period;
    
        // 값 타입을 사용
        @Embedded
        private Address address;
    
        @Override
        public String toString() {
            return "Member{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", period=" + period +
                    ", address=" + address +
                    '}';
        }
    }
    </code>
</pre>

Period.java
<pre>
    <code>
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
    </code>
</pre>

Address.java
<pre>
    <code>
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
    </code>
</pre>

Table
<img width="545" alt="스크린샷 2020-09-11 오후 10 39 32" src="https://user-images.githubusercontent.com/8858991/92997109-9cc91e80-f54b-11ea-8fad-9844dd350457.png">

만들어지는 Table 의 컬럼 수는 똑같지만 Entity 의 프로퍼티를 보게되면 위와 같이 Embedded type 을 사용해서  
구현하는 것이 관련 Class 수는 많아지지만 프로퍼티 수가 줄어들고 더욱 명확히 의미를 나타낼 수 있다.  

나는 의미를 더 명확하게 나타낼 수 있다면 프로퍼티 1개에 대해서라도 의미를 더욱 명확히 나타낼 수 있다면  
해당 프로퍼티를 위한 값 객체를 생성하는 것이 개인적으로 맞다고 생각한다.  

## 2. 임베디드 타입 응용(복합 값 타입)
아래 Period 라는 Value Class 를 보자, 
<pre>
    <code>
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
    </code>
</pre>

이 Value Class 는 시작과 끝을 나타내는 기간을 표현하므로 여러 군데서 다양한 의미로 사용될 수 있다.  
예를 들어 입사일 - 퇴직일과 같은 근무 기간일 수 도 있고, 입학일 - 졸업일과 같은 재학 기간일 수도 있다.  

따라서 사용하는 곳에 따라 시작일 - 종료일에 대한 컬럼명이 다를 수도 있고 해당 Value Class 에 정의한  
매핑 정보를 재정의해야할 때가 있다. 

아래의 코드를 보며 재정의를 어떻게 하는지, 어떨 때 하는지 깨달아보도록 하겠다. 
 
Table  
employee
<img width="519" alt="스크린샷 2020-09-13 오후 2 22 16" src="https://user-images.githubusercontent.com/8858991/93044711-66e18280-f690-11ea-98c4-61e5ca725b17.png">

student
<img width="492" alt="스크린샷 2020-09-13 오후 2 22 28" src="https://user-images.githubusercontent.com/8858991/93044716-68ab4600-f690-11ea-8eec-d3e393f4d485.png">

Period.java
<pre>
    <code>
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
    </code>
</pre>

Employee.java
<pre>
    <code>
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
    </code>
</pre>


Student.java
<pre>
    <code>
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Table(name = "value_type_embedded_type_advanced_student")
    @Entity(name = "ValueTypeEmbeddedTypeAdvancedStudent")
    public class Student {
    
        @Column(name = "student_id")
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
                @AttributeOverride(name = "startDate", column = @Column(name = "attendance_start_date"))
                , @AttributeOverride(name = "endDate", column = @Column(name = "attendance_end_date"))
        })
        @Embedded
        private Period attendancePeriod;
    
        @Override
        public String toString() {
            return "Student{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", attendancePeriod=" + attendancePeriod +
                    '}';
        }
    }
    </code>
</pre>



  
   

  


