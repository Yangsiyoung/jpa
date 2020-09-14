package jpabook.start.value_type.collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("study");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        try{

            entityTransaction.begin();

            Address address1 = new Address("성남시", "서현로", "1234");

            Address address2 = new Address("용인시", "포은대로", "12345");

            List<Address> addressHistory = new ArrayList<>();

            addressHistory.add(address1);
            addressHistory.add(address2);

            Set<String> favoriteFoods = new HashSet<>();
            favoriteFoods.add("치킨");
            favoriteFoods.add("만두");

            Member member1 = new Member(null, "member1", favoriteFoods, addressHistory);

            entityManager.persist(member1);

            /**
             * entityManager.flush() 를 생략하고 entityManager.clear() 하면
             * entityManager.persist() 시에 Member Table 에 식별자를 획득하기 위해 쿼리가 나가지만
             * 컬렉션으로 사용된 Table 에는 쿼리가 나가지 않기때문에 entityManager.flush() 를 해줘야한다.
             */
            entityManager.flush();
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
