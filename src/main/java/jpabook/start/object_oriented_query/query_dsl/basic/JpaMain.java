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

            Posts posts1 = new Posts(null, "title1", "content1", member1.getId());
            entityManager.persist(posts1);

            Member member2 = new Member(null, "member2", 28);
            entityManager.persist(member2);

            Posts posts2 = new Posts(null, "title2", "content2", member2.getId());
            entityManager.persist(posts2);

            entityManager.clear();
            JPAQuery<Member> query = new JPAQuery(entityManager);
            QMember member = new QMember("myQMember");

            List<Member> findMembers = query
                                        .from(member)
                                        .where(member.age.eq(28))
                                        .fetch();

            System.out.println("findMembers info are " + findMembers);

            JPAQuery<Posts> queryForPosts = new JPAQuery(entityManager);
            QPosts posts = new QPosts("myQPosts");
            List<Posts> findPosts = queryForPosts
                                        .from(posts)
                                        .where(posts.memberId.eq(findMembers.get(0).getId()))
                                        .fetch();

            System.out.println("findPosts info are " + findPosts);

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
