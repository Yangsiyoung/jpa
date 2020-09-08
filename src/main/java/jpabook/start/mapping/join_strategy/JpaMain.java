package jpabook.start.mapping.join_strategy;

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
            movie1.setActor("actor radi");
            movie1.setDirector("director radi");
            movie1.setPrice(12000);

            entityManager.persist(movie1);

            Book book1 = new Book();
            book1.setName("book1");
            book1.setAuthor("radi");
            book1.setIsbn("book1radi");
            book1.setPrice(30000);

            entityManager.persist(book1);

            entityManager.clear();

            Album findAlbum1 = entityManager.find(Album.class, album1.getId());
            Movie findMovie1 = entityManager.find(Movie.class, movie1.getId());
            Book findBook1 = entityManager.find(Book.class, book1.getId());

            System.out.println("findAlbum1 info is " + findAlbum1);
            System.out.println("findMovie1 info is " + findMovie1);
            System.out.println("findBook1 info is " + findBook1);

            entityTransaction.commit();

        }catch (Exception e){
            entityTransaction.rollback();
            System.out.println(e.toString());
        }finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
