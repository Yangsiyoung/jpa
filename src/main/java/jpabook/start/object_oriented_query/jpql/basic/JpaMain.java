package jpabook.start.object_oriented_query.jpql.basic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("query");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try{

            entityTransaction.begin();

            Member member1 = new Member(null, "member1", 28);
            entityManager.persist(member1);

            Member member2 = new Member(null, "member2", 28);
            entityManager.persist(member2);

            entityManager.clear();

            /**
             * jpql 은 table 이 아니라 entity 를 대상으로 하며, entity 는 @Entity(name = "Entity 이름") 으로
             * 설정한 Entity Name 을 사용해야한다.
             */
            String jpql = "SELECT m FROM JpqlBasicMember as m WHERE m.age = 28";
            List<Member> findMembers = entityManager.createQuery(jpql, Member.class).getResultList();
            System.out.println("findMembers info are " + findMembers);

            entityTransaction.commit();

        } catch(Exception e) {
            entityTransaction.rollback();
            System.out.println(e.toString());
        } finally {
            entityManager.close();
        }
        entityManagerFactory.close();
    }
}
