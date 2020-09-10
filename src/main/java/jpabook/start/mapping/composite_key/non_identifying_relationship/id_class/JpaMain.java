package jpabook.start.mapping.composite_key.non_identifying_relationship.id_class;

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

            Parent parent1 = new Parent();
            parent1.setName("parent1");
            parent1.setParentId1("parentId1");
            parent1.setParentId2("parentId2");

            entityManager.persist(parent1);

            // 식별자를 직접 지정했기때문에 persist 시 INSERT 쿼리가 바로 나가지 않으므로, 바로 보내기위해 flush
            entityManager.flush();
            entityManager.clear();

            ParentId parentId = new ParentId();
            parentId.setParentId1(parent1.getParentId1());
            parentId.setParentId2(parent1.getParentId2());

            Parent findParent1 = entityManager.find(Parent.class, parentId);

            System.out.println("findParent1 info is " + findParent1);

            entityManager.clear();
            /**
             * Parent findParent2 = entityManager.find(Parent.class, parent1.getParentId1());
             * 위와 같이 ParentId 식별자 클래스를 사용하지 않고 복합 키 컬럼 중 하나로 조회를 시도하면 아래와 같은 에러가 발생한다.
             * java.lang.IllegalArgumentException: Provided id of the wrong type for class jpabook.start.mapping.composite_key.non_identifying_relationship.id_class.Parent. Expected: class jpabook.start.mapping.composite_key.non_identifying_relationship.id_class.ParentId, got class java.lang.String
             */

            Child child1 = new Child();
            child1.setName("child1");
            child1.setParent(parent1);

            entityManager.persist(child1);

            Child child2 = new Child();
            child2.setName("child2");
            child2.setParent(parent1);

            entityManager.persist(child2);

            entityManager.clear();

            Child findChild1 = entityManager.find(Child.class, child1.getId());
            System.out.println("findChild1 info is " + findChild1);

            Child findChild2 = entityManager.find(Child.class, child2.getId());
            System.out.println("findChild2 info is " + findChild2);

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
