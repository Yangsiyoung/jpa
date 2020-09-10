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

