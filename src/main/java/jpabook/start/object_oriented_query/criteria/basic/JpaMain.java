package jpabook.start.object_oriented_query.criteria.basic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
/**
 * JPQL 과 다르게 Criteria 는 직접 쿼리를 문자열로 작성하는 것이 아니라 코드를 통해 작성한다.
 * 문자열로 쿼리를 작성하면 휴먼에러(오타) 의 높은 가능성 때문에 컴파일은 성공하지만 런타임시에 쿼리를 사용할 때 오류가 발생하게 된다.
 *
 * 따라서 코드를 통해 쿼리가 작성되는 Criteria 는 컴파일 시점에 오류를 발견할 수 있고, 동적 쿼리를 작성하기 편하다
 *
 * 하지만... Criteria 는 복잡하고... 코드가 눈에 익지않는다...
 */

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

            // Criteria 사용 준비
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);

            // 조회를 시작할 Class(조회의 기준이 되는 Class 이자 ROOT Class)
            Root<Member> member = query.from(Member.class);

            // 쿼리 생성
            CriteriaQuery<Member> criteriaQuery = query.select(member).where(criteriaBuilder.equal(member.get("age"), 28));

            // 쿼리 기반 조회(조회하기까지 준비과정이 너무 복잡하네...)
            List<Member> findMembers = entityManager.createQuery(criteriaQuery).getResultList();

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
