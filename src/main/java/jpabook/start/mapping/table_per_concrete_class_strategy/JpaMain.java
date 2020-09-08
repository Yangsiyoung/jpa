package jpabook.start.mapping.table_per_concrete_class_strategy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("study");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try{

            entityTransaction.begin();

            Album album1 = new Album();
            album1.setName("album1");
            album1.setArtist("radi");
            album1.setPrice(20000);

            entityManager.persist(album1);

            Movie movie1 = new Movie();
            movie1.setName("movie1");
            movie1.setDirector("director radi");
            movie1.setActor("actor radi");
            movie1.setPrice(12000);
            entityManager.persist(movie1);

            Book book1 = new Book();
            book1.setName("book1");
            book1.setAuthor("author radi");
            book1.setIsbn("radibook1");
            book1.setPrice(30000);

            entityManager.persist(book1);

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
             * @GeneratedValue(strategy = GenerationType.TABLE)을 사용한다는 의미는
             * persist 호출 시 기본 키(식별자)를 관련 Table 에서 가져와 Persistence Context 에 넣는다는 것이고,
             * 이는 entityManager.persist(book1); 호출 시 바로 INSERT 쿼리가 나가는 것이 아님을 의미한다.
             *
             * 따라서 Persistence Context Cache 와 Snapshot 을 비우고 find 를 통해
             * DB 에 정상적으로 INSERT 되었는지 확인하기 위해선
             *
             * entityManager.clear(); 이전에 entityManager.flush(); 를 호출해
             * INSERT 쿼리를 날려줘야 한다.
             */
            entityManager.flush();
            entityManager.clear();

            Album findAlbum1 = entityManager.find(Album.class, album1.getId());
            System.out.println("findAlbum1 info is " + findAlbum1);

            Movie findMovie1 = entityManager.find(Movie.class, movie1.getId());
            System.out.println("findMovie1 info is " + findMovie1);

            Book findBook1 = entityManager.find(Book.class, book1.getId());
            System.out.println("findBook1 info is " + findBook1);

            entityTransaction.commit();

        }catch (Exception e) {
            entityTransaction.rollback();
            System.out.println(e.toString());
        }finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
