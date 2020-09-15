package jpabook.start.object_oriented_query.query_dsl.basic;

import com.querydsl.jpa.impl.JPAQuery;

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

        try {

            entityTransaction.begin();

            Member member1 = new Member(null, "member1", 28);
            entityManager.persist(member1);

            Member member2 = new Member(null, "member2", 28);
            entityManager.persist(member2);

            entityManager.clear();
            JPAQuery<Member> query = new JPAQuery(entityManager);
            QMember member = QMember.member;

            List<Member> findMembers = query.from(member).where(member.age.eq(28)).fetch();
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
