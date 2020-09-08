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

* 조인 전략(각각의 테이블로 변환)
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
  
* 단일 테이블 전략(통합 테이블로 변환)
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

* 구현 클래스마다 테이블 전략(서브타입 테이블로 변환)
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

