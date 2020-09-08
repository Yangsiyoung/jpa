package jpabook.start.relational_mapping.one_way.one_to_one;

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
            Locker locker1 = new Locker();
            locker1.setName("locker1");

            entityManager.persist(locker1);

            Member member1 = new Member();
            member1.setUserName("member1");
            member1.setLocker(locker1);
            entityManager.persist(member1);

            entityManager.clear();

            Member findMember1 = entityManager.find(Member.class, member1.getId());
            System.out.println("findMember1 is " + findMember1);

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
