package jpabook.start.value_type.embedded_type;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Date;
import java.time.LocalDate;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("study");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try {

            entityTransaction.begin();

            Period period = new Period(LocalDate.now(), LocalDate.now().plusDays(3));
            Address address = new Address("용인시", "포은대로", "1234");

            Member member1 = new Member(null, "member1", period, address);
            entityManager.persist(member1);

            entityManager.clear();

            Member findMember1 = entityManager.find(Member.class, member1.getId());
            System.out.println("findMember1 info is " + findMember1);

            entityTransaction.commit();

        } catch (Exception e) {
            entityTransaction.rollback();
            System.out.println(e.toString());
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
